package com.l19cntt1b.milos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivityYourInformationBinding;
import com.squareup.picasso.Picasso;

public class YourInformationActivity extends AppCompatActivity {
    ActivityYourInformationBinding bd;
    FirebaseStorage storage;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivityYourInformationBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();

        bd.btnChangeInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourInformationActivity.this,ChangeYourInforActivity.class);
                startActivity(intent);
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
                        bd.txtUserName.setText(users.getUserName());
                        bd.txtBirthday.setText(users.getBirthday());
                        bd.txtSex.setText(users.getSex());
                        bd.txtPhone.setText(users.getPhone());
                        bd.txtUserId.setText(FirebaseAuth.getInstance().getUid());
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