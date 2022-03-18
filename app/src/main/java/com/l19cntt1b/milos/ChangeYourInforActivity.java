package com.l19cntt1b.milos;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivityChangeYourInforBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChangeYourInforActivity extends AppCompatActivity {
    ActivityChangeYourInforBinding bd;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_your_infor);
        bd= ActivityChangeYourInforBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();

        bd.btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sex = bd.edtSex.getText().toString();
                String username = bd.edtUserName.getText().toString();
                String about= bd.edtAbout.getText().toString();
                String birthday= bd.edtBirthday.getText().toString();
                HashMap<String, Object> hmobj = new HashMap<>();
                hmobj.put("userName",username);
                hmobj.put("sex",sex);
                hmobj.put("birthday",birthday);
                hmobj.put("about",about);
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(hmobj);
                Toast.makeText(ChangeYourInforActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilepic())
                                .placeholder(R.drawable.icn_avatar_default)
                                .into(bd.profileImage);
                        bd.edtUserName.setText(users.getUserName());
                        bd.edtBirthday.setText(users.getBirthday());
                        bd.edtSex.setText(users.getSex());
                        bd.edtAbout.setText(users.getAbout());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        bd.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}