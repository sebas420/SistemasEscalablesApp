package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.apolo.sistemasescalablesapp.CrearCliente;
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

import Interface.IonClick;
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
    private boolean valor =  true;
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
        CloudFirestore();
    }
    private String deuda,ticket;
    private void CloudFirestore(){
        if (new Networks(_activity).verificaConexion()){
            valor =  false;
            final long ONE_MEGABYTE = 2024 * 2024;
            Type typeItem = new TypeToken<Cliente>(){}.getType();
            data = gson.fromJson(_memoryData.getData("Cliente"),typeItem);
            docRef = _db.collection(Collections.Clientes.CLIENTES)
                    .document(data.getNid());
            docRef.addSnapshotListener((snapshot,e)->{
                if(snapshot.exists()){
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
                    _binding.collapser.setTitle(nombre+" "+apellido);
                    _binding.textNid.setText(data.getNid());
                    _binding.textName.setText(nombre);
                    _binding.textLastName.setText(apellido);
                    _binding.textPhone.setText(telefono);
                    _binding.textEmail.setText(email);
                    _binding.textAddress.setText(direccion);
                    _memoryData.saveData("Cliente",gson.toJson(new Cliente(
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

            _storageRef.child(Collections.Clientes.CLIENTES+"/"+data.getNid())
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)->{
                Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _binding.imageParalax.setImageBitmap(_selectedImage);
                _binding.swipeRefresh.setRefreshing(false);
                valor =  true;
            });
            docRef = _db.collection(Collections.Report_clientes.ReportClientes)
                    .document(data.getNid());
            docRef.addSnapshotListener((snapshot,e)->{
                if(snapshot.exists()){

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
        }else{
            Snackbar.make(_binding.toolbar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        if (new Networks(_activity).verificaConexion()){
            CloudFirestore();
        }else{
            Snackbar.make(_binding.toolbar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_Edit:
                if (valor) {
                    _activity.startActivity(new Intent(_activity, CrearCliente.class));
                }
                break;
        }
    }
}
