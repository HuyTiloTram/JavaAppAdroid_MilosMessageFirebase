package com.l19cntt1b.milos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.l19cntt1b.milos.databinding.ActivitySettingPrivateBinding;

public class SettingPrivateActivity extends AppCompatActivity {
    ActivitySettingPrivateBinding bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivitySettingPrivateBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.textviewYourInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPrivateActivity.this,YourInformationActivity.class);
                startActivity(intent);
            }
        });
    }
}