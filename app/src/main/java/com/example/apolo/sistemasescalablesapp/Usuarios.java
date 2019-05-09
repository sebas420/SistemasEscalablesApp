package com.example.apolo.sistemasescalablesapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.apolo.sistemasescalablesapp.databinding.UsuariosBinding;

import ViewModels.UserViewModels;

public class Usuarios extends AppCompatActivity{
    private UserViewModels user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.usuarios);
        UsuariosBinding _binding = DataBindingUtil.setContentView(this,R.layout.usuarios);
        user = new UserViewModels(this,_binding);
        _binding.setUserModel(user);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        user.onCreateOptionsMenu(menu);

        return true;
    }

}