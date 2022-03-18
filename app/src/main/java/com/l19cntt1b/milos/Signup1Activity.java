package com.l19cntt1b.milos;
 import android.content.Intent;
 import android.os.Bundle;
 import android.text.Editable;
 import android.text.TextWatcher;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

public class Signup1Activity extends AppCompatActivity {
    EditText edtName;

    Button btnNext;
    boolean statusImgDarkAction=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        AnhXa();

       btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.length()>2) {
                    Intent intent = new Intent(Signup1Activity.this, Signup2Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", edtName.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Signup1Activity.this, "Vui lòng nhập tên hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AnhXa(){
        edtName = (EditText) findViewById(R.id.edtUserName);
        btnNext = (Button) findViewById(R.id.btnTiepTuc);
    }
}
