 package com.example.chatingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private boolean isChat;
    String theLastMessage;

    public UserListAdapter(Context context, List<User> users,boolean isChat) {
        this.context = context;
        this.users = users;
        this.isChat= isChat;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_shape,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUsername());
        if (users.get(position).getImageURL().equals("default")){
            holder.profilr_image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(users.get(position).getImageURL()).into(holder.profilr_image);
        }
        if (isChat){
            lastMessage(users.get(position).getId(),holder.last_mag);
        }
        else {
            holder.last_mag.setVisibility(View.GONE);
        }

        if (isChat){
            if (users.get(position).getStatus().equals("online")){
                holder.ima_on.setVisibility(View.VISIBLE);
                holder.ima_off.setVisibility(View.GONE);
            }
            else {
                holder.ima_on.setVisibility(View.GONE);
                holder.ima_off.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.ima_on.setVisibility(View.GONE);
            holder.ima_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("userid",users.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilr_image;
        TextView userName,last_mag;
        ImageView ima_on,ima_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilr_image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.username);
            ima_off=itemView.findViewById(R.id.img_off);
            ima_on=itemView.findViewById(R.id.img_on);
            last_mag=itemView.findViewById(R.id.last_mag);
        }
    }
    private void lastMessage(String userid,TextView last_mag){
        theLastMessage="default";
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid) ||
                            chat.getSender().equals(firebaseUser.getUid())&&chat.getReceiver().equals(userid)){
                        theLastMessage=chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_mag.setText("No Message");
                        break;
                    default:
                        last_mag.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
