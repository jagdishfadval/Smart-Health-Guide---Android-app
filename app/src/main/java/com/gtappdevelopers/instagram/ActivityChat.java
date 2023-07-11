package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



public class ActivityChat extends AppCompatActivity {

    private ImageView sendbtn;
    private EditText msg;
    String senderid;
    String receiverid;
    String username="Smart Health Guide";

    ChatAdapter adapter;
    private  RecyclerView revview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        senderid= i.getStringExtra("senderid").toString();
        receiverid = i.getStringExtra("receiverid").toString();


        username=i.getStringExtra("name").toString();



        getSupportActionBar().setTitle(username);








        sendbtn=findViewById(R.id.sendcommentbutton);
        msg=findViewById(R.id.chatbox);
        revview=findViewById(R.id.msgviever);

        ArrayList<chat> chatlisst = new ArrayList<>();
         adapter=new ChatAdapter(chatlisst,ActivityChat.this);





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityChat.this, RecyclerView.VERTICAL, false);
        //below line is to set layout manager to our recycler view.
        revview.setLayoutManager(linearLayoutManager);

        revview.setAdapter(adapter);



        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatlisst.clear();


                String chatid=senderid+receiverid;
                String revid=receiverid+senderid;

                if (snapshot.hasChild(chatid)) {
                    snapshot.getRef().child(chatid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for(DataSnapshot snapshot1:snapshot.getChildren()) {



                                chat newchat = snapshot1.getValue(chat.class);


                                chatlisst.add(newchat);
                                revview.scrollToPosition(adapter.getItemCount()-1);


                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (snapshot.hasChild(revid)) {
                    snapshot.getRef().child(revid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot1:snapshot.getChildren()) {



                                chat newchat = snapshot1.getValue(chat.class);


                                chatlisst.add(newchat);
                                revview.scrollToPosition(adapter.getItemCount()-1);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                adapter.notifyDataSetChanged();
                revview.scrollToPosition(adapter.getItemCount()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



         sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = msg.getText().toString();

               if(!message.equals("")) {


                    String chatid = senderid + receiverid;
                    String revid = receiverid + senderid;


                    LocalDateTime currentDateTime = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        currentDateTime = LocalDateTime.now();
                    }

// Format the date and time as a string
                    DateTimeFormatter formatter = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("HH:mm");
                    }
                    String currentDateTimeString = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        currentDateTimeString = currentDateTime.format(formatter);
                    }


                    String finalCurrentDateTimeString = currentDateTimeString;
                    FirebaseDatabase.getInstance().getReference().child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String chatid = senderid + receiverid;
                            String revid = receiverid + senderid;


                            if (snapshot.hasChild(chatid)) {
                                String msgid = FirebaseDatabase.getInstance().getReference().child("chats").child(chatid).push().getKey();

                                chat newchat = new chat(message, senderid, receiverid, finalCurrentDateTimeString);

                                FirebaseDatabase.getInstance().getReference().child("chats").child(chatid).child(msgid).setValue(newchat);
                            } else if (snapshot.hasChild(revid)) {
                                String msgid = FirebaseDatabase.getInstance().getReference().child("chats").child(revid).push().getKey();

                                chat newchat = new chat(message, senderid, receiverid, finalCurrentDateTimeString);
                                FirebaseDatabase.getInstance().getReference().child("chats").child(revid).child(msgid).setValue(newchat);
                            } else {

                                String msgid = FirebaseDatabase.getInstance().getReference().child("chats").child(chatid).push().getKey();
                                chat newchat = new chat(message, senderid, receiverid, finalCurrentDateTimeString);

                                FirebaseDatabase.getInstance().getReference().child("chats").child(chatid).child(msgid).setValue(newchat);
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    msg.setText("");
                }
               else{
                   Toast.makeText(ActivityChat.this, "type something...", Toast.LENGTH_SHORT).show();
               }

            }


        });

}











    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}