package com.example.apolo.sistemasescalablesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.apolo.sistemasescalablesapp.databinding.ActivityVerificarEmailBinding;

import Library.MemoryData;
import ViewModels.LoginViewModels;

public class VerificarEmail extends AppCompatActivity {
    private MemoryData memoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_verificar_email);
        memoryData = MemoryData.getInstance(this);
        if (memoryData.getData("user").equals("")) {
            ActivityVerificarEmailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verificar_email);
            binding.setEmailModel(new LoginViewModels(this, binding, null));
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark, null));
        }else{
            startActivity(new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                            .FLAG_ACTIVITY_NEW_TASK));
            //overridePendingTransition(R.anim.left_in,R.anim.left_out);
        }
    }
}



