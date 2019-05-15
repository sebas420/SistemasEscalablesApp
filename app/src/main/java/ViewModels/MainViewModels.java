package ViewModels;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Library.MemoryData;
import Library.Networks;
import Models.Collections;

/*public class MainViewModels {
    private Activity _activity;
    private View _headerView;
    private MemoryData _memoryData;
    private NavigationView _navigationView;
    private FirebaseFirestore _db;
    private FirebaseStorage _storage;
    private StorageReference _storageRef;
    private ImageView _imageViewUser;
    private TextView _textViewName,_textViewLastName;
    public MainViewModels(Activity activity, ActivityMainBinding binding){
        _activity = activity;
        _navigationView =  binding.navView;
        _headerView = _navigationView.getHeaderView(0);
        _memoryData = MemoryData.getInstance(activity);
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _textViewName =  _headerView.findViewById(R.id.textViewName);
        _textViewLastName =  _headerView.findViewById(R.id.textViewLastName);
        _imageViewUser =  _headerView.findViewById(R.id.imageViewUser);
        GetUser();
    }
    private void GetUser(){
        final long ONE_MEGABYTE = 1024 * 1024;
        if (new Networks(_activity).verificaConexion()){
            String email = _memoryData.getData("user");
            _db.collection(Collections.Usuarios.USUARIOS).document(email)
                    .get().addOnCompleteListener((snapshots) -> {
                if (snapshots.isSuccessful()){
                    DocumentSnapshot document = snapshots.getResult();
                    if (document.exists()){
                        _textViewName.setText(document.getData().get("Nombre").toString());
                        _textViewLastName.setText(document.getData().get("Apellido").toString());
                        _storageRef.child(Collections.Usuarios.USUARIOS+"/"+email)
                                .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)->{
                            Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            _imageViewUser.setImageBitmap(_selectedImage);
                            _imageViewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        });
                    }
                }
            });
        }else{
            Snackbar.make(_headerView, R.string.networks, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}*/
