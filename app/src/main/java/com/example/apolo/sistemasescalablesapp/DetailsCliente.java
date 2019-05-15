package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.apolo.sistemasescalablesapp.databinding.DetailsClienteBinding;

import ViewModels.DetailsClienteViewModels;

public class DetailsCliente extends AppCompatActivity {
    private DetailsClienteViewModels details;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.details_cliente);
        DetailsClienteBinding binding = DataBindingUtil.setContentView(this,R.layout.details_cliente);
        details = new DetailsClienteViewModels(this,binding);
        binding.setDetails(details);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.transparent, null));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            startActivity(new Intent(this, Clientes.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, Clientes.class));
    }
}
