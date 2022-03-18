package com.l19cntt1b.milos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.l19cntt1b.milos.Adapters.UsersAdapter;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }
    FragmentChatsBinding bd;
    ArrayList<Users> list =new ArrayList<>();
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentChatsBinding.inflate(inflater, container, false);

        database =  FirebaseDatabase.getInstance();
        UsersAdapter adapter= new UsersAdapter(list,getContext());
        bd.recyclerviewChat.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerviewChat.setLayoutManager(layoutManager);
        //Load friends
        //load user
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                        Users users = dataSnapshot.getValue(Users.class);
                        users.setUserId(dataSnapshot.getKey());
                        if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                            list.add(users);
                       }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         return bd.getRoot();
    }// end oncreat
}