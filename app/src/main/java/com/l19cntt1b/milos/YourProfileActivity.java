package com.l19cntt1b.milos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.databinding.ActivityYourInformationBinding;
import com.l19cntt1b.milos.databinding.ActivityYourProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class YourProfileActivity extends AppCompatActivity {
    ActivityYourProfileBinding bd;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestorage;
    ProgressDialog progressDialog;
    String ImageUriAcessToken;
    static int PICK_IMAGE=12;
    static int PICK_IMAGE_GALLERY=13;
    Uri uriFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= ActivityYourProfileBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        firestorage = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tải ảnh lên");
        progressDialog.setMessage("Đang tải ảnh của bạn lên");
        bd.imageviewSettingPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this,SettingPrivateActivity.class);
                startActivity(intent);
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        if (users.getProfilepic()!= null)
                        Picasso.get().load(users.getProfilepic())
                                .placeholder(R.drawable.icn_avatar_default)
                                .into(bd.profileImage);
                        bd.txtUserName.setText(users.getUserName());
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        bd.imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_GALLERY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!= null){
            uriFile= data.getData();
//                Bitmap bitmap=null;
//                try{
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriFile);
//                }catch (IOException e){e.printStackTrace();};
////                ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
////                byte[] data2=byteArrayOutputStream.toByteArray;
//                UploadTask uploadTask= dd
                bd.profileImage.setImageURI(uriFile);
                progressDialog.show();
                final StorageReference storagerf = storage.getReference().child("profile_pictures").child(FirebaseAuth.getInstance().getUid());
                storagerf.putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storagerf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).
                                        child("profilepic").setValue(uri.toString());
                                progressDialog.dismiss();
                                Toast.makeText(YourProfileActivity.this, "Bạn đã thay đổi ảnh đại diện", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
        }
        else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data!= null){
            uriFile= data.getData();
            bd.imgGallery.setImageURI(uriFile);
            progressDialog.show();
            final StorageReference storagerf = storage.getReference().child("gallery_pictures").child(FirebaseAuth.getInstance().getUid());
            storagerf.putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storagerf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).
                                    child("gallerypic").setValue(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(YourProfileActivity.this, "Bạn đã thay đổi ảnh bìa", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        } else ;
    }
}