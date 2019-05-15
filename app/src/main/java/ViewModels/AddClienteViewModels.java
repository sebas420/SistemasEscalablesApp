package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apolo.sistemasescalablesapp.DetailsCliente;
import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.CrearClienteBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import Interface.IonClick;
import Library.LPermissions;
import Library.MemoryData;
import Library.Networks;
import Library.UploadImage;
import Library.Validate;
import Models.ClienteModels;
import Models.Collections;
import Models.Pojo.Cliente;

import static android.app.Activity.RESULT_OK;

public class AddClienteViewModels extends ClienteModels implements IonClick, SwipeRefreshLayout.OnRefreshListener {
    private Activity _activity;
    private CrearClienteBinding _binding;
    private FirebaseFirestore _db;
    private DocumentReference _documentRef;
    private FirebaseStorage _storage;
    private StorageReference _storageRef;
    private UploadImage _uploadImage;
    private LPermissions _permissions;
    private Bitmap _selectedImage;
    private static final int RESQUEST_CODE_CROP_IMAGE = 0X3;
    private static final String TEMP_PHOYO_FILE = "temprary_img.png";
    private MemoryData _memoryData;
    private Gson gson = new Gson();
    private boolean valor =  true;

    public AddClienteViewModels(Activity activity, CrearClienteBinding binding){
        _activity = activity;
        _binding = binding;
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _uploadImage = new UploadImage(activity);
        _permissions = new LPermissions(activity);
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        _binding.swipeRefresh.setColorSchemeResources(
                R.color.colorAzul, R.color.darker_orange, R.color.colorPrimaryDark);

        _memoryData = MemoryData.getInstance(activity);
        if(!_memoryData.getData("Cliente").equals("") ){
            _binding.swipeRefresh.setOnRefreshListener(this);
            _binding.swipeRefresh.setRefreshing(true);
            getCliente();
        }else{
            _binding.swipeRefresh.setRefreshing(false);
            _binding.swipeRefresh.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddCliente:
                registrarCliente();
                break;
            case R.id.cardViewCliete:
                if (_permissions.STORAGE()){
                    _uploadImage.getImage();
                }
                break;
        }
    }
    private void registrarCliente(){
        if (TextUtils.isEmpty(nidUI.getValue())) {
            _binding.editTextNID.requestFocus();
            _binding.editTextNID.setError(_activity.getString(R.string.error_field_required));
        }else{
            if (TextUtils.isEmpty(nombreUI.getValue())) {
                _binding.editTextNombre.requestFocus();
                _binding.editTextNombre.setError(_activity.getString(R.string.error_field_required));
            }else{
                if (TextUtils.isEmpty(apellidoUI.getValue())) {
                    _binding.editTextApellido.requestFocus();
                    _binding.editTextApellido.setError(_activity.getString(R.string.error_field_required));
                }else{
                    if (TextUtils.isEmpty(emailUI.getValue())) {
                        _binding.editTextEmail.requestFocus();
                        _binding.editTextEmail.setError(_activity.getString(R.string.error_field_required));
                    }else{
                        if (!isEmailValid(emailUI.getValue())){
                            _binding.editTextEmail.requestFocus();
                            _binding.editTextEmail.setError(_activity.getString(R.string.error_invalid_email));
                        }else{
                            if (TextUtils.isEmpty(telefonoUI.getValue())) {
                                _binding.editTextTelefono.requestFocus();
                                _binding.editTextTelefono.setError(_activity.getString(R.string.error_field_required));
                            }else{
                                if (TextUtils.isEmpty(direccionUI.getValue())) {
                                    _binding.editTextDireccion.requestFocus();
                                    _binding.editTextDireccion.setError(_activity.getString(R.string.error_field_required));
                                }else{
                                    if (new Networks(_activity).verificaConexion()){
                                        if(_memoryData.getData("Cliente").equals("") ){
                                            insertCliente();
                                        }else{
                                            if (valor) {
                                                save();
                                            }
                                        }
                                    }else{
                                        Snackbar.make(_binding.buttonAddCliente, R.string.networks,
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private boolean isEmailValid(String email){
        return Validate.isEmail(email);
    }

    public Bitmap onActivityResult(int requestCode, int resultCode){
        switch (requestCode) {
            case RESQUEST_CODE_CROP_IMAGE:
                if (resultCode == RESULT_OK){
                    String filePath = Environment.getExternalStorageDirectory()+"/"+TEMP_PHOYO_FILE;
                    _selectedImage = BitmapFactory.decodeFile(filePath);
                    _binding.imageViewCliente.setImageBitmap(_selectedImage);
                    _binding.imageViewCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
        }
        return _selectedImage;
    }

    public void  onRequestPermissionsResult(int requestCode,String[] permission,int[]grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    _uploadImage.getImage();
                }else{
                    Snackbar.make(_binding.view2, R.string.permits, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void insertCliente(){
        _binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        _db.collection(Collections.Clientes.CLIENTES).document(nidUI.getValue()).get()
                .addOnCompleteListener((task)->{
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Snackbar.make(_binding.buttonAddCliente, R.string.invalid_nid,
                                    Snackbar.LENGTH_LONG).show();
                        }else{
                            _documentRef = _db.collection(Collections.Clientes.CLIENTES)
                                    .document(nidUI.getValue());
                            Map<String, Object> cliente = new HashMap<>();
                            cliente.put(Collections.Clientes.APELLIDO,apellidoUI.getValue());
                            cliente.put(Collections.Clientes.EMAIL,emailUI.getValue());
                            cliente.put(Collections.Clientes.NOMBRE,nombreUI.getValue());
                            cliente.put(Collections.Clientes.NID,nidUI.getValue());
                            cliente.put(Collections.Clientes.TELEFONO,telefonoUI.getValue());
                            cliente.put(Collections.Clientes.DRECCION,direccionUI.getValue());
                            _documentRef.set(cliente).addOnCompleteListener((task2)->{
                                if (task2.isSuccessful()){
                                    StorageReference imagesRef = _storageRef.
                                            child(Collections.Clientes.CLIENTES+"/"+nidUI.getValue());
                                    byte[] data = _uploadImage.ImgaeByte(_binding.imageViewCliente);
                                    UploadTask uploadTask = imagesRef.putBytes(data);
                                    uploadTask.addOnFailureListener((exception)-> {


                                    }).addOnSuccessListener((taskSnapshot)->{
                                        Map<String, Object> report = Collections.
                                                Report_clientes.getReport();
                                        _db.collection(Collections.Report_clientes.ReportClientes)
                                                .document(nidUI.getValue()).
                                                set(report).addOnCompleteListener((task3)->{
                                            if (task3.isSuccessful()){
                                                _activity.finish();
                                            }
                                        });

                                    });
                                }

                            });
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        if(!_memoryData.getData("Cliente").equals("") ){
            getCliente();
        }
    }
    private static Cliente data;
    private void getCliente(){
        valor =  false;
        final long ONE_MEGABYTE = 2024 * 2024;
        Type typeItem = new TypeToken<Cliente>(){}.getType();
        data = gson.fromJson(_memoryData.getData("Cliente"),typeItem);
        nidUI.setValue(data.getNid());
        nombreUI.setValue(data.getNombre());
        apellidoUI.setValue(data.getApellido());
        emailUI.setValue(data.getEmail());
        telefonoUI.setValue(data.getTelefono());
        direccionUI.setValue(data.getDireccion());
        _binding.editTextNID.setEnabled(false);
        _binding.buttonAddCliente.setText(R.string.edit);
        if (new Networks(_activity).verificaConexion()){
            _storageRef.child(Collections.Clientes.CLIENTES+"/"+data.getNid())
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)-> {
                Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _binding.imageViewCliente.setImageBitmap(_selectedImage);
                _binding.imageViewCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
                _binding.swipeRefresh.setRefreshing(false);
                valor =  true;
            });

        }else{
            Snackbar.make(_binding.progressBar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }
    private void save(){
        _documentRef = _db.collection(Collections.Clientes.CLIENTES).document(nidUI.getValue());
        Map<String, Object> cliente = new HashMap<>();
        cliente.put(Collections.Clientes.APELLIDO,apellidoUI.getValue());
        cliente.put(Collections.Clientes.EMAIL,emailUI.getValue());
        cliente.put(Collections.Clientes.NOMBRE,nombreUI.getValue());
        cliente.put(Collections.Clientes.NID,nidUI.getValue());
        cliente.put(Collections.Clientes.TELEFONO,telefonoUI.getValue());
        cliente.put(Collections.Clientes.DRECCION,direccionUI.getValue());
        _documentRef.set(cliente).addOnCompleteListener((task2)-> {
            if (task2.isSuccessful()){
                if (_selectedImage != null) {
                    StorageReference imagesRef = _storageRef.child(Collections.Clientes.CLIENTES + "/"
                            + nidUI.getValue());
                    byte[] data =_uploadImage.ImgaeByte(_binding.imageViewCliente);
                    UploadTask uploadTask = imagesRef.putBytes(data);
                    uploadTask.addOnFailureListener((exception)-> {

                    }).addOnSuccessListener((taskSnapshot)-> {
                        _activity.startActivity(new Intent(_activity, DetailsCliente.class));
                    });
                }else{
                    _activity.startActivity(new Intent(_activity, DetailsCliente.class));
                }
            }
        });
    }
    public void onSaveInstance(Bundle saveInstanceState, Bitmap selectedImage){
        saveInstanceState.putParcelable("image",selectedImage);
        saveInstanceState.putString(Collections.Clientes.APELLIDO,apellidoUI.getValue());
        saveInstanceState.putString(Collections.Clientes.EMAIL,emailUI.getValue());
        saveInstanceState.putString(Collections.Clientes.NOMBRE,nombreUI.getValue());
        saveInstanceState.putString(Collections.Clientes.NID,nidUI.getValue());
        saveInstanceState.putString(Collections.Clientes.TELEFONO,telefonoUI.getValue());
        saveInstanceState.putString(Collections.Clientes.DRECCION,direccionUI.getValue());
    }
    public void onRestoreInstance(Bundle savedInstanceState){
        Bitmap selectedImage = savedInstanceState.getParcelable("image");
        if(selectedImage != null){
            _binding.imageViewCliente.setImageBitmap(selectedImage);
            _binding.imageViewCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            _binding.imageViewCliente.setImageResource(R.mipmap.person_black);
        }
        nidUI.setValue(savedInstanceState.get(Collections.Clientes.NID).toString());
        nombreUI.setValue(savedInstanceState.get(Collections.Clientes.NOMBRE).toString());
        apellidoUI.setValue(savedInstanceState.get(Collections.Clientes.APELLIDO).toString());
        emailUI.setValue(savedInstanceState.get(Collections.Clientes.EMAIL).toString());
        telefonoUI.setValue(savedInstanceState.get(Collections.Clientes.TELEFONO).toString());
        direccionUI.setValue(savedInstanceState.get(Collections.Clientes.DRECCION).toString());
    }
}
