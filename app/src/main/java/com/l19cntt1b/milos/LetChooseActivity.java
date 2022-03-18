package com.l19cntt1b.milos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LetChooseActivity extends AppCompatActivity {
    Button btnSignup,btnSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_let_choose);
        AnhXa();
        //Event chuyển form đăng ký
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LetChooseActivity.this, Signup1Activity.class);
                startActivity(intent);
            }
        });
        //Event chuyển form đăng nhập
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LetChooseActivity.this, Signin1Activity.class);
                startActivity(intent);
            }
        });

    }

    private void AnhXa(){
        btnSignup = (Button) findViewById(R.id.button_sign_up_1);
        btnSignin = (Button) findViewById(R.id.button_sign_in_1);

    }
}