package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.apolo.sistemasescalablesapp.databinding.CrearClienteBinding;

import ViewModels.AddClienteViewModels;

public class CrearCliente extends AppCompatActivity {
    private AddClienteViewModels _cliente;
    private Bitmap _selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.crear_cliente);
        CrearClienteBinding binding = DataBindingUtil.setContentView(this,R.layout.crear_cliente);
        _cliente = new AddClienteViewModels(this,binding);
        binding.setCliente(_cliente);
        if(savedInstanceState != null){
            _selectedImage = savedInstanceState.getParcelable("image");
            if (_selectedImage != null){
                binding.imageViewCliente.setImageBitmap(_selectedImage);
                binding.imageViewCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                binding.imageViewCliente.setImageResource(R.mipmap.person_black);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageRetunedIntent){
        super.onActivityResult(requestCode,resultCode,imageRetunedIntent);
        _selectedImage =_cliente.onActivityResult(requestCode,resultCode);
    }
    @Override
    public void  onRequestPermissionsResult(int requestCode,String[] permission,int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
        _cliente.onRequestPermissionsResult( requestCode,permission,grantResults);
    }
    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        _cliente.onSaveInstance(saveInstanceState,_selectedImage);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        _cliente.onRestoreInstance(savedInstanceState);
    }
}
