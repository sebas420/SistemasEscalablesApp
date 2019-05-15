package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apolo.sistemasescalablesapp.CrearCliente;
import com.example.apolo.sistemasescalablesapp.DetailsCliente;
import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.DetailsClienteBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import Interface.IonClick;
import Library.Calendario;
import Library.Codes;
import Library.FormatDecimal;
import Library.MemoryData;
import Library.Networks;
import Models.Collections;
import Models.Pojo.Cliente;

public class DetailsClienteViewModels implements SwipeRefreshLayout.OnRefreshListener, IonClick {
    private Activity _activity;
    private MemoryData _memoryData;
    private FirebaseFirestore _db;
    private FirebaseStorage _storage;
    private StorageReference _storageRef;
    private DocumentReference docRef;
    private DetailsClienteBinding _binding;
    private Gson gson = new Gson();
    private static Cliente data;
    private boolean valor = true;
    private FormatDecimal _formato;
    private Calendario calendar;
    private Codes codes;

    public DetailsClienteViewModels(Activity activity, DetailsClienteBinding binding) {
        _activity = activity;
        _binding = binding;
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _memoryData = MemoryData.getInstance(activity);
        _binding.swipeRefresh.setColorSchemeResources(
                R.color.colorPrimary, R.color.darker_orange, R.color.colorAzul);
        _binding.swipeRefresh.setOnRefreshListener(this);
        _binding.swipeRefresh.setRefreshing(true);
        _formato = new FormatDecimal();
        codes = new Codes();
        CloudFirestore();
    }

    private String deuda, ticket;

    private void CloudFirestore() {
        if (new Networks(_activity).verificaConexion()) {
            valor = false;
            final long ONE_MEGABYTE = 2024 * 2024;
            Type typeItem = new TypeToken<Cliente>() {
            }.getType();
            data = gson.fromJson(_memoryData.getData("Cliente"), typeItem);
            docRef = _db.collection(Collections.Clientes.CLIENTES)
                    .document(data.getNid());
            docRef.addSnapshotListener((snapshot, e) -> {
                if (snapshot.exists()) {
                    String nombre = snapshot.getData()
                            .get(Collections.Clientes.NOMBRE).toString();
                    String apellido = snapshot.getData()
                            .get(Collections.Clientes.APELLIDO).toString();
                    String email = snapshot.getData()
                            .get(Collections.Clientes.EMAIL).toString();
                    String telefono = snapshot.getData()
                            .get(Collections.Clientes.TELEFONO).toString();
                    String direccion = snapshot.getData()
                            .get(Collections.Clientes.DRECCION).toString();
                    _binding.collapser.setTitle(nombre + " " + apellido);
                    _binding.textNid.setText(data.getNid());
                    _binding.textName.setText(nombre);
                    _binding.textLastName.setText(apellido);
                    _binding.textPhone.setText(telefono);
                    _binding.textEmail.setText(email);
                    _binding.textAddress.setText(direccion);
                    _memoryData.saveData("Cliente", gson.toJson(new Cliente(
                            apellido,
                            nombre,
                            email,
                            data.getNid(),
                            telefono,
                            direccion,
                            null
                    )));
                }

            });

            _storageRef.child(Collections.Clientes.CLIENTES + "/" + data.getNid())
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes) -> {
                Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _binding.imageParalax.setImageBitmap(_selectedImage);
                _binding.swipeRefresh.setRefreshing(false);
                valor = true;
            });
            docRef = _db.collection(Collections.Report_clientes.ReportClientes)
                    .document(data.getNid());
            docRef.addSnapshotListener((snapshot, e) -> {
                if (snapshot.exists()) {

                    ticket = snapshot.getData()
                            .get(Collections.Report_clientes.TICKET).toString();
                    _binding.textTicket.setText(ticket);
                    deuda = snapshot.getData()
                            .get(Collections.Report_clientes.DEUDA).toString();
                    _binding.textDebt.setText(deuda);
                    String fecha1 = snapshot.getData()
                            .get(Collections.Report_clientes.FECHADEUDA).toString();
                    _binding.textDate1.setText(fecha1);
                    String pago = snapshot.getData()
                            .get(Collections.Report_clientes.PAGO).toString();
                    _binding.textPayment.setText(pago);
                    String fecha2 = snapshot.getData()
                            .get(Collections.Report_clientes.FECHAPAGO).toString();
                    _binding.textDate2.setText(fecha2);

                }
            });
        } else {
            Snackbar.make(_binding.toolbar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        if (new Networks(_activity).verificaConexion()) {
            CloudFirestore();
        } else {
            Snackbar.make(_binding.toolbar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_Edit:
                if (valor) {
                    _activity.startActivity(new Intent(_activity, CrearCliente.class));
                }
                break;
            case R.id.fab_Payments:
                if (deuda.equals("$0.00")) {
                    Snackbar.make(_binding.toolbar, R.string.debts, Snackbar.LENGTH_LONG).show();
                } else {
                    alertDialog_payments();
                }
                break;
        }
    }

    private void alertDialog_payments() {
        final EditText editText = new EditText(_activity);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setIcon(R.drawable.ic_payment)
                .setTitle(R.string.payments)
                .setMessage("Deuda: " + deuda);
        builder.setView(editText);
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {

        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener((dialog) -> {
            Button boton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            boton.setOnClickListener((v) -> {
                String payment = editText.getText().toString();
                if (payment.equals("")) {
                    editText.requestFocus();
                    editText.setError(_activity.getString(R.string.error_field_required));
                } else {
                    alert.dismiss();

                    payment(payment);
                }
            });
        });
        alert.show();
    }

    private String dataDeuda, dataPago, dataTicket;

    private void payment(String payment) {
        calendar = new Calendario();
        double pago = _formato.reconstruir(payment);
        double deudas = _formato.reconstruir(deuda.replace("$", ""));
        if (pago > deudas) {
            Snackbar.make(_binding.toolbar, R.string.message_payments, Snackbar.LENGTH_LONG).show();
        } else {
            deudas = deudas - pago;
            dataDeuda = "$" + _formato.decimal(deudas);
            dataPago = "$" + _formato.decimal(pago);
            dataTicket = codes.codesTickets(ticket);
            Map<String, Object> report = new HashMap<>();
            report.put(Collections.Report_clientes.DEUDA, dataDeuda);
            report.put(Collections.Report_clientes.FECHADEUDA, calendar.getFecha());
            report.put(Collections.Report_clientes.PAGO, dataPago);
            report.put(Collections.Report_clientes.FECHAPAGO, calendar.getFecha());
            report.put(Collections.Report_clientes.TICKET, dataTicket);
            _db.collection(Collections.Report_clientes.ReportClientes)
                    .document(data.getNid()).set(report).addOnCompleteListener((task1) -> {
                if (task1.isSuccessful()) {
                    Map<String, Object> ticket = new HashMap<>();
                    ticket.put(Collections.Ticket_clientes.DEUDA, dataDeuda);
                    ticket.put(Collections.Ticket_clientes.PAGO, dataPago);
                    ticket.put(Collections.Ticket_clientes.FECHA, calendar.getFecha());
                    ticket.put(Collections.Ticket_clientes.TICKET, dataTicket);
                    String documento = data.getNid()+"_"+dataTicket;
                    _db.collection(Collections.Ticket_clientes.TicketClientes)
                            .document(documento).set(ticket).addOnCompleteListener((task2) -> {
                        _activity.startActivity(new Intent(_activity, DetailsCliente.class));

                    });
                }
            });
        }
    }
}
