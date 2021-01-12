package com.example.chatingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatingapp.Activity.MessageActivity;
import com.example.chatingapp.Model.Chat;
import com.example.chatingapp.Model.User;
import com.example.chatingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private List<Chat> mChat;
    private  String imageURL;
    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> mChat,String imageURL) {
        this.context = context;
        this.mChat = mChat;
        this.imageURL= imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
        MessageAdapter.ViewHolder holder=new MessageAdapter.ViewHolder(view);
        return holder;
        }
        else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            MessageAdapter.ViewHolder holder=new MessageAdapter.ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        if (imageURL.equals("default")){
            holder.profilr_image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(imageURL).into(holder.profilr_image);
        }
        if (position==mChat.size()-1){
            if (chat.getIsseen()){
                holder.txt_seen.setText("Seen");
            }
            else {
                holder.txt_seen.setText("Delivered");
            }
        }
        else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilr_image;
        TextView show_message,txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilr_image=itemView.findViewById(R.id.profile_image);
            show_message=itemView.findViewById(R.id.show_message);
            txt_seen=itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

}

