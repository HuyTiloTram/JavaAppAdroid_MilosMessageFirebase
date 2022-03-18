package com.l19cntt1b.milos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivitySignup3Binding;
import com.l19cntt1b.milos.databinding.ActivitySignup4Binding;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signup4Activity extends AppCompatActivity {
    ActivitySignup4Binding bd;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivitySignup4Binding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tạo tài khoản");
        progressDialog.setMessage("Chúng tôi đang tạo tài khoản của bạn ");
        bd.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( (bd.edittextPassword.getText().length()>5)
                    && bd.edittextPassword.getText().toString().equals(bd.edittextConfirmPassword.getText().toString()) ) {
                    SignUp();
                }
                else {
                    Toast.makeText(Signup4Activity.this, "Vui lòng nhập mật khẩu trùng khớp và ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }//end concreat

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
    private void SignUp(){
        progressDialog.show();
        Intent intent0 = getIntent();
        Bundle bundle = intent0.getExtras();
        String email=bundle.getString("Phone")+"@ntnt.com";
        String password=getMd5(bd.edittextPassword.getText().toString().trim());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Users user = new Users(bundle.getString("Username"),bundle.getString("Phone")   ,getMd5(bd.edittextPassword.getText().toString()));
                            String userId = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(userId).setValue(user);
                            progressDialog.dismiss();
                            Toast.makeText(Signup4Activity.this, "Đăng ký thành công",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Signup4Activity.this, HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Signup4Activity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(Signup4Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}