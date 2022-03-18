package com.l19cntt1b.milos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.l19cntt1b.milos.ChatDetailActivity;
import com.l19cntt1b.milos.Models.Users;
import com.l19cntt1b.milos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {

    ArrayList<Users> listUsers;
    Context context;

    public UsersAdapter(ArrayList<Users> listUsers, Context context) {
        this.listUsers = listUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Users users = listUsers.get(position);
    if(users.getProfilepic()!=null)
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.icn_avatar_default).into(holder.image);
        holder.userName.setText(users.getUserName());
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+ users.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                                holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilepic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView userName,lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.textview_username_list);
            lastMessage = itemView.findViewById(R.id.textview_last_message);

        }
    }
}
