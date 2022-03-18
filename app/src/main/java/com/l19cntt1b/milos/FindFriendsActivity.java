package com.l19cntt1b.milos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.l19cntt1b.milos.Adapters.UsersAdapter;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivityFindFriendsBinding;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

public class FindFriendsActivity extends AppCompatActivity {

    ActivityFindFriendsBinding bd;
    ArrayList<Users> list =new ArrayList<>();
    UsersAdapter adapter;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivityFindFriendsBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database =  FirebaseDatabase.getInstance();
        adapter= new UsersAdapter(list,bd.getRoot().getContext());
        bd.recyclerList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(bd.getRoot().getContext());
        bd.recyclerList.setLayoutManager(layoutManager);
        bd.edtSearch.requestFocus();
        bd.edtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s= bd.edtSearch.getText().toString();
                if(s.length() >0) {
                    if (s.startsWith("0"))
                        s = "+84" + s.substring(1);
                    searchUsers(s);
                };

            }
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void searchUsers(String query) {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!users.getUserName().equals(FirebaseAuth.getInstance().getUid())){
                        if(users.getUserName().toLowerCase().contains(query.toLowerCase()) || users.getPhone().toLowerCase().contains(query.toLowerCase())){
                            list.add(users);
                        };
                    }

                }
                adapter= new UsersAdapter(list,bd.getRoot().getContext());
                adapter.notifyDataSetChanged();
                bd.recyclerList.setAdapter(adapter);
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