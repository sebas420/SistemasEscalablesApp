package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.apolo.sistemasescalablesapp.databinding.CrearClienteBinding;

import ViewModels.AddClienteViewModels;

public class CrearCliente extends AppCompatActivity {
    private AddClienteViewModels _cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.crear_cliente);
        CrearClienteBinding binding = DataBindingUtil.setContentView(this,R.layout.crear_cliente);
        _cliente = new AddClienteViewModels(this,binding);
        binding.setCliente(_cliente);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageRetunedIntent){
        super.onActivityResult(requestCode,resultCode,imageRetunedIntent);
        _cliente.onActivityResult(requestCode,resultCode);
    }
    @Override
    public void  onRequestPermissionsResult(int requestCode,String[] permission,int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
        _cliente.onRequestPermissionsResult( requestCode,permission,grantResults);
    }
}
