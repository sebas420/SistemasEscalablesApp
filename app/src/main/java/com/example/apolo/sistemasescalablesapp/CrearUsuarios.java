package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.apolo.sistemasescalablesapp.databinding.CrearUsuariosBinding;

import Library.MemoryData;
import ViewModels.AddUserViewModels;

public class CrearUsuarios extends AppCompatActivity {
    private static final int RESQUEST_CODE_CROP_IMAGE = 0X3;
    private static final String TEMP_PHOYO_FILE = "temprary_img.png";
    private CrearUsuariosBinding _binding;
    private AddUserViewModels _user;
    private Bitmap _selectedImage;
    private MemoryData _memoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.crear_usuarios);
        _binding = DataBindingUtil.setContentView(this,R.layout.crear_usuarios);
        _user = new AddUserViewModels(this,_binding);
        _binding.setAddUserModel(_user);
        if(savedInstanceState != null){
            _selectedImage = savedInstanceState.getParcelable("image");
            if (_selectedImage != null){
                _binding.imageViewUser.setImageBitmap(_selectedImage);
                _binding.imageViewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                _binding.imageViewUser.setImageResource(R.drawable.ic_person_white);
            }
        }
        _memoryData = MemoryData.getInstance(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        _user.onSaveInstance(saveInstanceState,_selectedImage);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        _user.onRestoreInstance(savedInstanceState);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent imageRetunedIntent){
        super.onActivityResult(requestCode,resultCode,imageRetunedIntent);
        switch (requestCode) {
            case RESQUEST_CODE_CROP_IMAGE:
                if (resultCode == RESULT_OK){
                    String filePath = Environment.getExternalStorageDirectory()+"/"+TEMP_PHOYO_FILE;
                    _selectedImage = BitmapFactory.decodeFile(filePath);
                    _binding.imageViewUser.setImageBitmap(_selectedImage);
                    _binding.imageViewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
        }
    }

    @Override
    public void  onRequestPermissionsResult(int requestCode,String[] permission,int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    _user.get_uploadImage().getImage();
                }else{
                    Snackbar.make(_binding.view2, R.string.permits, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        _memoryData.saveData("DATA","");
        finish();
    }
}