package com.l19cntt1b.milos;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
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
        import com.l19cntt1b.milos.databinding.ActivitySignup2Binding;
        import com.l19cntt1b.milos.databinding.ActivitySignup3Binding;

        import java.util.concurrent.TimeUnit;

public class Signup2Activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText edtPhone,edtOTP;
    ImageView imgDarkAction;
    Button btnSendOTP,btnVerifyOTP;
    String verificationId;
    boolean statusImgDarkAction=false;
    ActivitySignup2Binding bd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivitySignup2Binding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        Intent intent0 = getIntent();
        Bundle bundle = intent0.getExtras();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        bd.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(bd.edtPhone.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(Signup2Activity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Signup2Activity.this, Signup3Activity.class);
                    Bundle bundle = getIntent().getExtras();
                    String phone = bd.edtPhone.getText().toString();
                    if(phone.startsWith("0"))
                        phone= "+84"+phone.substring(1);
                    else phone = "+84"+ phone;
                    bundle.putString("Phone", phone);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }//end oncreat

}








