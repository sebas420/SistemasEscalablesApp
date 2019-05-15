package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.apolo.sistemasescalablesapp.databinding.ActivityVerificarPasswordBinding;

import ViewModels.LoginViewModels;

public class VerificarPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_verificar_password);
        ActivityVerificarPasswordBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_verificar_password);
        binding.setPasswordModel(new LoginViewModels(this,null,binding));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAzul,null));
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent= new Intent(this, VerificarEmail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }
}

