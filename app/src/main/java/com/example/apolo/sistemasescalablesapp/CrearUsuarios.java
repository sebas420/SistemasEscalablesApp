package com.example.apolo.sistemasescalablesapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.apolo.sistemasescalablesapp.databinding.CrearUsuariosBinding;

import ViewModels.AddUserViewModels;

public class CrearUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.crear_usuarios);

        CrearUsuariosBinding binding = DataBindingUtil.setContentView(this, R.layout.crear_usuarios);
        binding.setAddUserModel(new AddUserViewModels(this, binding));

    }
}