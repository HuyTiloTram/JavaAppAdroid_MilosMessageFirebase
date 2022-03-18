package com.l19cntt1b.milos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.l19cntt1b.milos.Adapters.ChatAdapter;
import com.l19cntt1b.milos.Models.MessageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ChatDetailActivity extends AppCompatActivity {
    TextView textViewUserName,txtLastOnline;
    ImageView imgBackArrow,imgSend,imgSendEmoji,imgSendPictures,imgSendOther,imgSendMic;
    Toolbar toolbarTop;
    RecyclerView rvChat;
    EditText edtMessage;
    Button btnSend;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int PICK_IMAGE=12;
    Uri uriFile;
    String senderRoom,reciverRoom,senderId,recieveId;
    LinearLayout statusTyping;
    ValueEventListener listenerload;
    ArrayList<MessageModel> arlmessageModels;
    ChatAdapter adtChat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        AnhXa();


        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        auth =FirebaseAuth.getInstance();
        senderId = auth.getUid();
        recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        textViewUserName.setText(userName);
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        final Handler handler= new Handler();
        edtMessage.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                database.getReference().child("presence").child(senderId).child("type").setValue("đang nhập....");
                handler.removeCallbacks(null);
                handler.postDelayed(userStoppedTyping,1000);

            }
            public void afterTextChanged(Editable editable) {
                if(edtMessage.getText().length() >0) {
                    imgSend.setVisibility(View.VISIBLE);
                    imgSendOther.setVisibility(View.GONE);
                    imgSendPictures.setVisibility(View.GONE);
                    imgSendMic.setVisibility(View.GONE);
                }else {
                    imgSend.setVisibility(View.GONE);
                    imgSendOther.setVisibility(View.VISIBLE);
                    imgSendPictures.setVisibility(View.VISIBLE);
                    imgSendMic.setVisibility(View.VISIBLE);
                };

            }
            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("presence").child(senderId).child("type").setValue(null);
                }
            };
        });

        senderRoom = senderId + recieveId;
        reciverRoom = recieveId + senderId;
        arlmessageModels = new ArrayList<>();
        adtChat = new ChatAdapter(arlmessageModels,this,senderRoom,reciverRoom);
        rvChat.setAdapter(adtChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);

        //load lasttime online
        database.getReference().child("presence").child(recieveId).child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Long status=snapshot.getValue(Long.class);
                    if(status>0)
                        {
                            Long distance= TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - status);
                            txtLastOnline.setText(getTimeAsString(distance));
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // load typing
        database.getReference().child("presence").child(recieveId).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status=snapshot.getValue(String.class);
                    if(status!=null)  statusTyping.setVisibility(View.VISIBLE);
                    else statusTyping.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //load chat
        listenerload = database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arlmessageModels.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    arlmessageModels.add(model);
                };
                adtChat.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                String randomkey = database.getReference().push().getKey();
                HashMap<String, Object> hmobj = new HashMap<>();
                hmobj.put("uId",senderId);
                hmobj.put("message",message);
                hmobj.put("rId",randomkey);
                hmobj.put("timestamp",new Date().getTime());
                setvalueMessage(randomkey,hmobj);
                edtMessage.setText("");
                rvChat.scrollToPosition(arlmessageModels.size());
            }
        });
        imgSendPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }//end oncreat

    private void setvalueMessage(String randomkey, HashMap<String, Object> hmobj) {
        database.getReference().child("chats").child(senderRoom).child(randomkey).setValue(hmobj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.getReference().child("chats").child(reciverRoom).child(randomkey).setValue(hmobj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ; }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!= null){
            if(data.getData()!=null){
                progressDialog.show();
            uriFile= data.getData();
            Calendar calendar = Calendar.getInstance();
            StorageReference storagerf = storage.getReference().child("chats").child(FirebaseAuth.getInstance().getUid()).child("image").child(calendar.getTimeInMillis()+"");
            storagerf.putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storagerf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String randomkey = database.getReference().push().getKey();
                            HashMap<String, Object> hmobj = new HashMap<>();
                            hmobj.put("message","[Hình ảnh]");
                            hmobj.put("rId",randomkey);
                            hmobj.put("uId",auth.getUid());
                            hmobj.put("url",uri.toString());
                            hmobj.put("timestamp",new Date().getTime());
                            setvalueMessage(randomkey,hmobj);
                            progressDialog.dismiss();
                            Toast.makeText(ChatDetailActivity.this, "Bạn đã gửi ảnh", Toast.LENGTH_SHORT).show();
                            rvChat.scrollToPosition(arlmessageModels.size());
                        }
                    });
                }
            });

            };
        }
    }
    private void AnhXa(){
        textViewUserName = (TextView) findViewById(R.id.textview_username);
        txtLastOnline = (TextView) findViewById(R.id.txtLastOnline);
        imgBackArrow = (ImageView) findViewById(R.id.imageview_back_arrow);
        imgSend = (ImageView) findViewById(R.id.imageview_send);
        imgSendEmoji = (ImageView) findViewById(R.id.imageview_send_emoji);
        imgSendOther = (ImageView) findViewById(R.id.imageview_send_other);
        imgSendPictures = (ImageView) findViewById(R.id.imageview_send_pictures);
        imgSendMic= (ImageView) findViewById(R.id.imageview_send_mic);
        rvChat= (RecyclerView) findViewById(R.id.recyclerview_chat);
        edtMessage= (EditText) findViewById(R.id.edittext_message);
        statusTyping =(LinearLayout) findViewById(R.id.status_typing);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tải ảnh lên");
        progressDialog.setMessage("Đang gửi ảnh");
    }
    private String getTimeAsString(long seconds) {
        if (seconds < 60) {     // rule 1
            return "Vừa mới truy cập";
        } else if (seconds < 3600) {  // rule 2
            return String.format("Truy cập %s phút trước", seconds/60);
        } else if (seconds < 86400){
            return String.format("Truy cập %s giờ trước", seconds/3600);
        }else return "Truy cập hơn 1 tháng trước";
    }
}
