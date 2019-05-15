package ViewModels;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.CrearUsuariosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.IonClick;
import Library.LPermissions;
import Library.MemoryData;
import Library.Networks;
import Library.UploadImage;
import Library.Validate;
import Models.Collections;
import Models.Pojo.User;
import Models.UserModels;

public class AddUserViewModels extends UserModels implements IonClick {
    private FirebaseAuth mAuth;
    private Activity _activity;
    private UploadImage _uploadImage;
    private FirebaseFirestore _db;
    private DocumentReference _documentRef;
    private CrearUsuariosBinding _binding;
    private LPermissions _permissions;
    private FirebaseStorage _storage;
    private StorageReference _storageRef;
    private Gson gson = new Gson();
    private MemoryData _memoryData;

    public AddUserViewModels(Activity activity, CrearUsuariosBinding binding) {
        _activity = activity;
        _binding = binding;
        mAuth = FirebaseAuth.getInstance();
        _uploadImage = new UploadImage(activity);
        _permissions = new LPermissions(activity);
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _memoryData = MemoryData.getInstance(activity);
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        if(!_memoryData.getData("User").equals("") ){
            getUser();
        }
    }

    public UploadImage get_uploadImage() {
        return _uploadImage;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddUser:
                registrarUsuarios();
                break;
            case R.id.imageViewUser:
                if (_permissions.STORAGE()){
                    _uploadImage.getImage();
                }
                break;
        }

    }
    private void registrarUsuarios(){

        View focusView ;
        if (TextUtils.isEmpty(nombreUI.getValue())){
            _binding.editTextNombre.setError(_activity.getString(R.string.error_field_required));
            focusView = _binding.editTextNombre;
            focusView.requestFocus();
        }else{
            if (TextUtils.isEmpty(apellidoUI.getValue())){
                _binding.editTextApellido.setError(_activity.getString(R.string.error_field_required));
                focusView = _binding.editTextApellido;
                focusView.requestFocus();
            }else{
                if (TextUtils.isEmpty(emailUI.getValue())){
                    _binding.editTextEmail.setError(_activity.getString(R.string.error_field_required));
                    focusView = _binding.editTextEmail;
                    focusView.requestFocus();

                }else{
                    if (!isEmailValid(emailUI.getValue())){
                        _binding.editTextEmail.setError(_activity.getString(R.string.error_invalid_email));
                        focusView = _binding.editTextEmail;
                        focusView.requestFocus();
                    } else{
                        if (TextUtils.isEmpty(cedulaUI.getValue())){
                            _binding.editTextPassword.setError(_activity.getString(R.string.error_field_required));
                            focusView = _binding.editTextPassword;
                            focusView.requestFocus();
                        }else{
                            if (!isPasswordValid(cedulaUI.getValue())){
                                _binding.editTextPassword.setError(_activity.getString(R.string.error_invalid_password));
                                focusView = _binding.editTextPassword;
                                focusView.requestFocus();
                            }else{
                                if (new Networks(_activity).verificaConexion()){
                                    if(_memoryData.getData("User").equals("") ){
                                        insertUser();
                                    }else{
                                        editUser();
                                    }
                                }else{
                                    focusView = _binding.editTextPassword;
                                    Snackbar.make(focusView, R.string.networks, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private void insertUser(){
        _binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailUI.getValue(),cedulaUI.getValue())
                .addOnCompleteListener(_activity,(task)->{
                    if (task.isSuccessful()){
                        String role = _activity.getResources()
                                .getStringArray(R.array.item_roles)[item
                                .getSelectedItemPosition()];
                        _db = FirebaseFirestore.getInstance();
                        _documentRef = _db.collection(Collections.Usuarios.USUARIOS)
                                .document(emailUI.getValue());
                        Map<String, Object> user = new HashMap<>();
                        user.put(Collections.Usuarios.APELLIDO,apellidoUI.getValue());
                        user.put(Collections.Usuarios.EMAIL,emailUI.getValue());
                        user.put(Collections.Usuarios.NOMBRE,nombreUI.getValue());
                        user.put(Collections.Usuarios.ROLE,role);
                        _documentRef.set(user).addOnCompleteListener((task2)->{
                            if (task2.isSuccessful()){
                                StorageReference imagesRef = _storageRef.
                                        child(Collections.Usuarios.USUARIOS+"/"
                                                +emailUI.getValue());
                                _binding.imageViewUser.setDrawingCacheEnabled(true);
                                _binding.imageViewUser.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) _binding.imageViewUser
                                        .getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = imagesRef.putBytes(data);
                                uploadTask.addOnFailureListener((exception)-> {


                                }).addOnSuccessListener((taskSnapshot)-> {
                                    _memoryData.saveData("User","");
                                    _memoryData.saveData("Data","");
                                    _activity.finish();
                                });
                            }

                        });

                    }else{
                        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
                        View view = _binding.editTextPassword;
                        Snackbar.make(view, R.string.fail_register, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }
    private boolean isEmailValid(String email){
        return Validate.isEmail(email);
    }
    private boolean isPasswordValid(String password){
        return password.length() >= 6;
    }
    public void onSaveInstance(Bundle saveInstanceState, Bitmap selectedImage){
        saveInstanceState.putParcelable("image",selectedImage);
        List<String> data = new ArrayList<>();
        data.add(nombreUI.getValue());
        data.add(apellidoUI.getValue());
        data.add(emailUI.getValue());
        data.add(cedulaUI.getValue());
        _memoryData.saveData("DATA",gson.toJson(data));
    }
    public void onRestoreInstance(Bundle savedInstanceState){
        Bitmap selectedImage = savedInstanceState.getParcelable("image");
        if(selectedImage != null){
            _binding.imageViewUser.setImageBitmap(selectedImage);
            _binding.imageViewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            _binding.imageViewUser.setImageResource(R.drawable.ic_person_white);
        }
        Type typeItem = new TypeToken<List<String>>(){}.getType();
        List<String> data = gson.fromJson(_memoryData.getData("DATA"),typeItem);
        nombreUI.setValue(data.get(0));
        apellidoUI.setValue(data.get(1));
        emailUI.setValue(data.get(2));
        cedulaUI.setValue(data.get(3));
    }
    private static User data;
    private void getUser(){
        final long ONE_MEGABYTE = 1024 * 1024;
        Type typeItem = new TypeToken<User>(){}.getType();
        data = gson.fromJson(_memoryData.getData("User"),typeItem);
        nombreUI.setValue(data.getNombre());
        apellidoUI.setValue(data.getApellido());
        emailUI.setValue(data.getEmail());
        cedulaUI.setValue("********");
        if (data.getRole().equals("Admin")){
            item.setSelectedItemPosition(1);
        }else{
            item.setSelectedItemPosition(0);
        }
        if (new Networks(_activity).verificaConexion()){
            _storageRef.child(Collections.Usuarios.USUARIOS+"/"+data.getEmail())
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)->{
                Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _binding.imageViewUser.setImageBitmap(_selectedImage);
                _binding.imageViewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        }else{
            Snackbar.make(_binding.editTextPassword, R.string.networks, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        _binding.emailLayout.setVisibility(View.GONE);
        _binding.cedulaLayout.setVisibility(View.GONE);
    }
    private void editUser(){
        _binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        String role = _activity.getResources()
                .getStringArray(R.array.item_roles)[item
                .getSelectedItemPosition()];
        _db = FirebaseFirestore.getInstance();
        _documentRef = _db.collection(Collections.Usuarios.USUARIOS)
                .document(data.getEmail());
        Map<String, Object> user = new HashMap<>();
        user.put(Collections.Usuarios.APELLIDO,apellidoUI.getValue());
        user.put(Collections.Usuarios.EMAIL,data.getEmail());
        user.put(Collections.Usuarios.NOMBRE,nombreUI.getValue());
        user.put(Collections.Usuarios.ROLE,role);
        _documentRef.set(user).addOnCompleteListener((task2)-> {
            if (task2.isSuccessful()){
                StorageReference imagesRef = _storageRef.
                        child(Collections.Usuarios.USUARIOS+"/"
                                +data.getEmail());
                _binding.imageViewUser.setDrawingCacheEnabled(true);
                _binding.imageViewUser.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) _binding.imageViewUser
                        .getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = imagesRef.putBytes(data);
                uploadTask.addOnFailureListener((exception)-> {


                }).addOnSuccessListener((taskSnapshot)-> {
                    _memoryData.saveData("DATA","");
                    _memoryData.saveData("User","");
                    _activity.finish();
                });
            }
        });

    }
}
