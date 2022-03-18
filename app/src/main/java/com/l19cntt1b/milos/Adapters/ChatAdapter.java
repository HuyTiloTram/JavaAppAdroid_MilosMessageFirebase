package com.l19cntt1b.milos.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.l19cntt1b.milos.Models.MessageModel;
import com.l19cntt1b.milos.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECIVER_VIEW_TYPE = 2;
    String senderRoom;
    String reciverRoom;

    FirebaseDatabase database;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context,String senderRoom,String reciverRoom) {
        this.senderRoom =senderRoom;
        this.reciverRoom = reciverRoom;
        this.messageModels = messageModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);

        int reactions[] = new int[]{
        R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry,
                R.drawable.ic_fb_remove
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(pos!=-1){
                if(holder.getClass()==SenderViewHolder.class){
                SenderViewHolder vh =((SenderViewHolder)holder);
                    HashMap<String, Object> hmobj = new HashMap<>();
                    hmobj.put("totalfeeling",messageModel.getTotalfeeling()+1);
                    String roomId=messageModel.getrId();
                    switch (pos){
                        case 0:
                            hmobj.put("feelinglike",messageModel.getFeelinglike()+1);break;
                        case 1:
                            hmobj.put("feelinglove",messageModel.getFeelinglove()+1);break;
                        case 2:
                            hmobj.put("feelinglaugh",messageModel.getFeelinglaugh()+1);break;
                        case 3:
                            hmobj.put("feelingwow",messageModel.getFeelingwow()+1);break;
                        case 4:
                            hmobj.put("feelingsad",messageModel.getFeelingsad()+1);break;
                        case 5:
                            hmobj.put("feelingangry",messageModel.getFeelinglove()+1);break;
                        case 6:
                            hmobj.put("feelinglike",null);hmobj.put("feelinglove",null);hmobj.put("feelinglaugh",null);hmobj.put("feelingwow",null);hmobj.put("feelingsad",null);hmobj.put("feelingangry",null);hmobj.put("totalfeeling",null);break;
                    };
                    updatefeeling(hmobj,roomId);
                } else{
                ReciverViewHolder vh =((ReciverViewHolder)holder);
                    HashMap<String, Object> hmobj = new HashMap<>();
                    hmobj.put("totalfeeling",messageModel.getTotalfeeling()+1);
                    String roomId=messageModel.getrId();
                    switch (pos){
                        case 0:
                            hmobj.put("feelinglike",messageModel.getFeelinglike()+1);break;
                        case 1:
                            hmobj.put("feelinglove",messageModel.getFeelinglove()+1);break;
                        case 2:
                            hmobj.put("feelinglaugh",messageModel.getFeelinglaugh()+1);break;
                        case 3:
                            hmobj.put("feelingwow",messageModel.getFeelingwow()+1);break;
                        case 4:
                            hmobj.put("feelingsad",messageModel.getFeelingsad()+1);break;
                        case 5:
                            hmobj.put("feelingangry",messageModel.getFeelinglove()+1);break;
                        case 6:
                            hmobj.put("feelinglike",null);hmobj.put("feelinglove",null);hmobj.put("feelinglaugh",null);hmobj.put("feelingwow",null);hmobj.put("feelingsad",null);hmobj.put("feelingangry",null);hmobj.put("totalfeeling",null);break;
                    };
                    updatefeeling(hmobj,roomId);
                }
            };
            return true; // true is closing popup, false is requesting a new selection
        });


        //Load chat
        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder vh =((SenderViewHolder)holder);
            vh.setIsRecyclable(false);
            if(messageModel.getMessage().equals("[Hình ảnh]")){
                try {
                    Glide.with(context).load(messageModel.getUrl()).into(vh.imgFileSender);
                }catch(Exception e){};
                vh.imgFileSender.setVisibility(View.VISIBLE);
                vh.senderMessage.setVisibility(View.GONE);
                String dateString = new SimpleDateFormat("HH:mm").format(new Date(messageModel.getTimestamp()));
                vh.senderTime.setText(dateString);
            }
            else {
                vh.senderMessage.setText(messageModel.getMessage());
                String dateString = new SimpleDateFormat("HH:mm").format(new Date(messageModel.getTimestamp()));
                vh.senderTime.setText(dateString);
            };
            //load feeling
            if (messageModel.getTotalfeeling() > 0) {
                vh.feeling.setVisibility(View.VISIBLE);
                vh.txtTotalFeeling.setVisibility(View.VISIBLE);
                vh.txtTotalFeeling.setText("" + messageModel.getTotalfeeling());
                if (messageModel.getFeelinglike() > 0)
                    vh.imgFeelingLike.setVisibility(View.VISIBLE);
                if (messageModel.getFeelinglove() > 0)
                    vh.imgFeelingLove.setVisibility(View.VISIBLE);
                if (messageModel.getFeelinglaugh() > 0)
                    vh.imgFeelingLaugh.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingwow() > 0)
                    vh.imgFeelingWow.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingsad() > 0)
                    vh.imgFeelingSad.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingangry() > 0)
                    vh.imgFeelingAngry.setVisibility(View.VISIBLE);
            } else {
                vh.feeling.setVisibility(View.GONE);
                vh.txtTotalFeeling.setVisibility(View.GONE);
                vh.imgFeelingLike.setVisibility(View.GONE);
                vh.imgFeelingLove.setVisibility(View.GONE);
                vh.imgFeelingLaugh.setVisibility(View.GONE);
                vh.imgFeelingWow.setVisibility(View.GONE);
                vh.imgFeelingSad.setVisibility(View.GONE);
                vh.imgFeelingAngry.setVisibility(View.GONE);
            };
            //add event
            vh.senderTime.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });

        }else {
            ReciverViewHolder vh =((ReciverViewHolder)holder);
            vh.setIsRecyclable(false);
            if(messageModel.getMessage().equals("[Hình ảnh]")){
                try {
                    Glide.with(context).load(messageModel.getUrl()).into(vh.imgFileReciver);
                }catch(Exception e){};
                vh.imgFileReciver.setVisibility(View.VISIBLE);
                String dateString = new SimpleDateFormat("HH:mm").format(new Date(messageModel.getTimestamp()));
                vh.reciverMessage.setVisibility(View.GONE);
                vh.reciverTime.setText(dateString);
            } else {
                vh.reciverMessage.setText(messageModel.getMessage());
                String dateString = new SimpleDateFormat("HH:mm").format(new Date(messageModel.getTimestamp()));
                vh.reciverTime.setText(dateString);
            };
            //add feeling
            if (messageModel.getTotalfeeling() > 0) {
                vh.feeling.setVisibility(View.VISIBLE);
                vh.txtTotalFeeling.setVisibility(View.VISIBLE);
                vh.txtTotalFeeling.setText("" + messageModel.getTotalfeeling());
                if (messageModel.getFeelinglike() > 0)
                    vh.imgFeelingLike.setVisibility(View.VISIBLE);
                if (messageModel.getFeelinglove() > 0)
                    vh.imgFeelingLove.setVisibility(View.VISIBLE);
                if (messageModel.getFeelinglaugh() > 0)
                    vh.imgFeelingLaugh.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingwow() > 0)
                    vh.imgFeelingWow.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingsad() > 0)
                    vh.imgFeelingSad.setVisibility(View.VISIBLE);
                if (messageModel.getFeelingangry() > 0)
                    vh.imgFeelingAngry.setVisibility(View.VISIBLE);
            } else {
                vh.feeling.setVisibility(View.GONE);
                vh.txtTotalFeeling.setVisibility(View.GONE);
                vh.imgFeelingLike.setVisibility(View.GONE);
                vh.imgFeelingLove.setVisibility(View.GONE);
                vh.imgFeelingLaugh.setVisibility(View.GONE);
                vh.imgFeelingWow.setVisibility(View.GONE);
                vh.imgFeelingSad.setVisibility(View.GONE);
                vh.imgFeelingAngry.setVisibility(View.GONE);
            };
            //add event
            vh.reciverTime.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }
        //
    }

    private void updatefeeling(HashMap<String, Object> hmobj, String roomId) {
        database.getReference().child("chats").child(senderRoom).child(roomId).updateChildren(hmobj);
        database.getReference().child("chats").child(reciverRoom).child(roomId).updateChildren(hmobj);
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position){
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECIVER_VIEW_TYPE;
        }

    }

    public class ReciverViewHolder extends RecyclerView.ViewHolder {
        TextView reciverMessage, reciverTime,txtTotalFeeling;
        LinearLayout feeling;
        ConstraintLayout clayoutmess;
        ImageView imgFeelingLike,imgFeelingLove,imgFeelingLaugh,imgFeelingWow,imgFeelingSad,imgFeelingAngry,imgFileReciver;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            reciverMessage = itemView.findViewById(R.id.textview_reciver_text);
            reciverTime = itemView.findViewById(R.id.textview_reciver_time);
            feeling = itemView.findViewById(R.id.linearlayout_feeling);
            txtTotalFeeling = itemView.findViewById(R.id.txtTotalFeeling);
            imgFeelingLike = itemView.findViewById(R.id.imgFeelingLike);
            imgFeelingLove = itemView.findViewById(R.id.imgFeelingLove);
            imgFeelingLaugh = itemView.findViewById(R.id.imgFeelingLaugh);
            imgFeelingWow = itemView.findViewById(R.id.imgFeelingWow);
            imgFeelingSad = itemView.findViewById(R.id.imgFeelingSad);
            imgFeelingAngry = itemView.findViewById(R.id.imgFeelingAngry);
            imgFileReciver = itemView.findViewById(R.id.imgFileReciver);
            clayoutmess = itemView.findViewById(R.id.constrainlayoutMess);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime,txtTotalFeeling;
        LinearLayout feeling;
        ConstraintLayout clayoutmess;
        ImageView imgFeelingLike,imgFeelingLove,imgFeelingLaugh,imgFeelingWow,imgFeelingSad,imgFeelingAngry,imgFileSender;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.textview_sender_text);
            senderTime = itemView.findViewById(R.id.textview_sender_time);
            feeling = itemView.findViewById(R.id.linearlayout_feeling);
            txtTotalFeeling = itemView.findViewById(R.id.txtTotalFeeling);
            imgFeelingLike = itemView.findViewById(R.id.imgFeelingLike);
            imgFeelingLove = itemView.findViewById(R.id.imgFeelingLove);
            imgFeelingLaugh = itemView.findViewById(R.id.imgFeelingLaugh);
            imgFeelingWow = itemView.findViewById(R.id.imgFeelingWow);
            imgFeelingSad = itemView.findViewById(R.id.imgFeelingSad);
            imgFeelingAngry = itemView.findViewById(R.id.imgFeelingAngry);
            imgFileSender = itemView.findViewById(R.id.imgFileSender);
            clayoutmess = itemView.findViewById(R.id.constrainlayoutMess);
        }
    }

}
