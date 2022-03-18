package com.l19cntt1b.milos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signin1Activity extends AppCompatActivity {
    private EditText edtPhone,edtPassword;
    private ImageView imgDarkAction;
    boolean statusImgDarkAction=false;
    private Button btnSignIn,btnBack;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_1);
        AnhXa();

        // Hiển thị đăng nhập sau khi nhập đủ 6 kí tự
        edtPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void afterTextChanged(Editable editable) {
                if( (edtPhone.getText().length() >5) && (edtPassword.getText().length() >5) ) {
                    statusImgDarkAction = true;
                }else {
                    statusImgDarkAction = false;
                }

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( (edtPhone.getText().length() >7) && (edtPassword.getText().length() >5) ) {
                    SignIn();
                }else {
                    Toast.makeText(Signin1Activity.this, "Mật khẩu phải trên 6 ký tự", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa(){
        edtPhone = (EditText) findViewById(R.id.edittext_phone);
        edtPassword = (EditText) findViewById(R.id.edittext_password);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnBack = (Button) findViewById(R.id.btnBack);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng nhập");
        progressDialog.setMessage("Đang đăng nhập tài khoản của bạn");
    }

    public void addTextChangedListener(){}

    private void SignIn(){
        progressDialog.show();
        String email=edtPhone.getText().toString().trim();
        if(email.startsWith("0"))
            email= "+84"+email.substring(1);
        email= email+"@ntnt.com";
        String password=getMd5(edtPassword.getText().toString().trim());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signin1Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signin1Activity.this,HomeActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signin1Activity.this, "Lỗi: "+task.getException(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(Signin1Activity.this, "Đăng Nhập Thất Bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static String getMd5(String input)
    {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
