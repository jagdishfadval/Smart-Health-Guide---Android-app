package com.gtappdevelopers.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    //arraylist for our facebook feeds.
    private ArrayList<chat> chat;
    String currentuserid;
    private Context context;

    //creating a constructor for our adapter class.
    public ChatAdapter(ArrayList<chat> chat, Context context) {
        this.chat = chat;
        this.context = context;


        SharedPreferences sharedPreferences= context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
        currentuserid= sharedPreferences.getString("userId","");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, parent, false);
        return new ChatAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        chat model=chat.get(position);

        String msg=model.getMessage();
        String time=model.getTime();


        if(model.getSendid().equals(currentuserid))
        {
            holder.msg.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.revmsg.setVisibility(View.GONE);
            holder.revtime.setVisibility(View.GONE);

            holder.msg.setText(msg);
            holder.time.setText(time);
        }
        else{

            holder.msg.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.revmsg.setVisibility(View.VISIBLE);
            holder.revtime.setVisibility(View.VISIBLE);

            holder.revmsg.setText(msg);
            holder.revtime.setText(time);

        }


    }






    @Override
    public int getItemCount() {
        //returning the size of our array list.
        return chat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //creating variables for our views of recycler view items.


        private TextView msg,revmsg,revtime;
private  TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing our variables
            msg = itemView.findViewById(R.id.msgtext);
        time=itemView.findViewById(R.id.timetext);

            revmsg = itemView.findViewById(R.id.receiverMessageText);
            revtime=itemView.findViewById(R.id.receiverTimeText);

        }
    }
}
