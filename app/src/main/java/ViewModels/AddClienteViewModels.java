package ViewModels;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.CrearClienteBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import Interface.IonClick;
import Library.LPermissions;
import Library.Networks;
import Library.UploadImage;
import Library.Validate;
import Models.ClienteModels;
import Models.Collections;

import static android.app.Activity.RESULT_OK;

public class AddClienteViewModels extends ClienteModels implements IonClick {
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

    public AddClienteViewModels(Activity activity, CrearClienteBinding binding){
        _activity = activity;
        _binding = binding;
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _uploadImage = new UploadImage(activity);
        _permissions = new LPermissions(activity);
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
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
                                        insertCliente();
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

    public void onActivityResult(int requestCode, int resultCode){
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
                                    _binding.imageViewCliente.setDrawingCacheEnabled(true);
                                    _binding.imageViewCliente.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) _binding.imageViewCliente
                                            .getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] data = baos.toByteArray();
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
}

