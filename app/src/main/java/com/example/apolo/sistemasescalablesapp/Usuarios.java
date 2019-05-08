package com.example.apolo.sistemasescalablesapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.apolo.sistemasescalablesapp.databinding.UsuariosBinding;

import ViewModels.UserViewModels;

public class Usuarios extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.usuarios);
        UsuariosBinding _binding = DataBindingUtil.setContentView(this,R.layout.usuarios);
        _binding.setUserModel(new UserViewModels(this,_binding));
    }


}