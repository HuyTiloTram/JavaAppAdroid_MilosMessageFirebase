package com.l19cntt1b.milos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivitySignup3Binding;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class Signup3Activity extends AppCompatActivity {
    ActivitySignup3Binding bd;
    FirebaseAuth mAuth;
    private EditText edtPassword,edtConfirmPassword;
    private ImageView imgDarkAction;
    boolean statusImgDarkAction=false;
    private ProgressDialog progressDialog;
    FirebaseDatabase database;
    String verificationId="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivitySignup3Binding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle0 = getIntent().getExtras();
        Toast.makeText(this, ""+bundle0.getString("Phone"), Toast.LENGTH_SHORT).show();

        sendVerificationCode(bundle0.getString("Phone"));



        bd.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bd.edtOtp.getText().toString().isEmpty()) {
                    Toast.makeText(Signup3Activity.this, "Enter your OTP First", Toast.LENGTH_SHORT).show();
                } else {

                    verifyCode(verificationId);
                }
            }
        });
        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }




    PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
            Toast.makeText(Signup3Activity.this,"Đã gửi OTP", Toast.LENGTH_LONG).show();

        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            Toast.makeText(Signup3Activity.this,"Xác thực tự động thành công", Toast.LENGTH_LONG).show();
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                bd.edtOtp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(Signup3Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        if(code.equals(verificationId)==false){
            Toast.makeText(Signup3Activity.this,"Mã xác thực không đúng", Toast.LENGTH_LONG).show();
        } else {
            Bundle bundle = getIntent().getExtras();
            Intent intent = new Intent(Signup3Activity.this, Signup4Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
//        signInWithCredential(credential);
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(20L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
