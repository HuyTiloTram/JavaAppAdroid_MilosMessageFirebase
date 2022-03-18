package com.l19cntt1b.milos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.l19cntt1b.milos.ChangeYourInforActivity;
import com.l19cntt1b.milos.HomeActivity;
import com.l19cntt1b.milos.LetChooseActivity;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.R;
import com.l19cntt1b.milos.SettingPrivateActivity;
import com.l19cntt1b.milos.SlashActivity;
import com.l19cntt1b.milos.YourInformationActivity;
import com.l19cntt1b.milos.YourProfileActivity;
import com.l19cntt1b.milos.databinding.FragmentIndividualBinding;
import com.squareup.picasso.Picasso;

public class IndividualFragment extends Fragment {


    public IndividualFragment() {
        // Required empty public constructor
    }
    FragmentIndividualBinding bd;
    FirebaseDatabase database;
    int  PICK_IMAGE=12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentIndividualBinding.inflate(inflater, container, false);

        database =  FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users = snapshot.getValue(Users.class);
                    bd.txtUserName.setText(users.getUserName().toString());
                    if (users.getProfilepic()!= null)
                Picasso.get().load(users.getProfilepic().toString())
                        .placeholder(R.drawable.icn_avatar_default)
                        .into(bd.profileImage);
                if(users.getAbout()!=null)
                    bd.txtAbout.setText(users.getAbout());
                if(users.getGallerypic()!=null)
                    Picasso.get().load(users.getGallerypic().toString())
                            .placeholder(R.drawable.background_snow)
                            .into(bd.imgGallery);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bd.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourProfileActivity.class);
                startActivity(intent);
            }
        });


        bd.txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SlashActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        bd.textviewYourInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourInformationActivity.class);
                startActivity(intent);
            }
        });
        bd.txtChangeYourInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeYourInforActivity.class);
                startActivity(intent);
            }
        });
        bd.txtDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().delete();
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(null);
                Intent intent = new Intent(getActivity(), LetChooseActivity.class);
                startActivity(intent);

            }
        });
        return bd.getRoot();
    }//end oncreat





}