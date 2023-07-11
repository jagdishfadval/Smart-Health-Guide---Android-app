package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View rootView;
    String userId;

    Button followbtn,msgbtn;
    ImageView profileimageview;
    TextView bioview,websiteview,phoneview,addressview;




    TextView usernameview;
    String userName,currentuserid;

    String profileImage;


    String Bio;
    String Address;
    String Website;
    String Phone;
    private ArrayList<FacebookFeedModal> facebookFeedModalArrayList;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Profile_Activity.MODE_PRIVATE);

        currentuserid= sharedPreferences.getString("userId","");

        usernameview=findViewById(R.id.personname);
                bioview=findViewById(R.id.bio);


                phoneview=findViewById(R.id.phone);
        websiteview=findViewById(R.id.website);

                addressview=findViewById(R.id.address);
        profileimageview=findViewById(R.id.profilephtofragment);

        followbtn=findViewById(R.id.followbtn);

        msgbtn=findViewById(R.id.msgbtn);




        Intent intent = getIntent();


              userId = intent.getStringExtra("profileuser");

        if(userId.equals(currentuserid))
        {
            msgbtn.setClickable(false);
            msgbtn.setBackgroundColor(Color.GRAY);
        }





              getuserdata();
        getdata();


        final String[] state = {"notfollow"};

        DatabaseReference followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed");

        followedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    // The user is already followed
                    followbtn.setText("Following");
                    followbtn.setBackgroundColor(getResources().getColor(R.color.black));

                    state[0] ="follow";
                } else {
                    state[0] ="nofollow";

                    followbtn.setText("Follow");
                    followbtn.setBackgroundColor(getResources().getColor(R.color.primary));






                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        profileimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder<>(Profile_Activity.this, new String[]{profileImage})
                        .setStartPosition(0)

                        .show();
            }
        });


       followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state[0].equals("follow"))
                {
                    DatabaseReference followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed");
                    followedRef.child(userId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    followbtn.setText("Follow");
                                    followbtn.setBackgroundColor(getResources().getColor(R.color.primary));




                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to remove the followed user

                                }
                            });
                }
                else{

                    Task<Void> followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed").child(userId).setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    followbtn.setText("Following");
                                    followbtn.setBackgroundColor(getResources().getColor(R.color.black));

                                }
                            });





                }



            }
        });


       msgbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              if(!(userId.equals(currentuserid)))
              {
                  Intent intent=new Intent(Profile_Activity.this,ActivityChat.class);

                  intent.putExtra("senderid",currentuserid);
                  intent.putExtra("receiverid",userId);
                  intent.putExtra("name",userName)    ;


                  startActivity(intent);

              }

           }
       });


























    }

    private void getuserdata() {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if(snapshot.hasChild("Bio"))
                    Bio= snapshot.child("Bio").getValue().toString();

                if(snapshot.hasChild("Address"))
                    Address= snapshot.child("Address").getValue().toString();

                if(snapshot.hasChild("Phone"))
                    Phone=  snapshot.child("Phone").getValue().toString();

                if(snapshot.hasChild("Website"))
                    Website= snapshot.child("Website").getValue().toString();

                if(snapshot.hasChild("Name"))
                    userName= snapshot.child("Name").getValue().toString();

                profileImage=snapshot.child("Image").getValue().toString();


                if(Bio!=null)
                    bioview.setText(Bio);

                if(Website!=null)
                    websiteview.setText(Website);

                if(userName!=null)
                    usernameview.setText(userName);

                if(Phone!=null)
                    phoneview.setText(Phone);

                if(Address!=null)
                    addressview.setText(Address);
                if(Address!=null)
                    addressview.setText(Address);

                if(profileImage!=null) {
                    Glide
                            .with(Profile_Activity.this)

                            .load(profileImage)
                            .placeholder(R.drawable.emtyprofile)

                            .into(profileimageview);
                }
//



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void getdata() {

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("posts");

        FacebookFeedModal feedModal;
        facebookFeedModalArrayList = new ArrayList<>();
        FacebookFeedRVAdapter adapter = new FacebookFeedRVAdapter(facebookFeedModalArrayList,Profile_Activity.this,getSupportFragmentManager());
        RecyclerView instRV = findViewById(R.id.postrecyclerview);

        ///below line is for setting linear layout manager to our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Profile_Activity.this, RecyclerView.VERTICAL, false);
        //below line is to set layout manager to our recycler view.
        instRV.setLayoutManager(linearLayoutManager);
        //below line is to set adapter to our recycler view.
        instRV.setAdapter(adapter);

        ArrayList<String> userList= new ArrayList<>();









        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren()) {



                    FacebookFeedModal feedModal = snapshot1.getValue(FacebookFeedModal.class);

                    // Check if the post belongs to any of the followed user IDs

                    if (feedModal.getAuthorId().equals(userId)) {



                        facebookFeedModalArrayList.add(feedModal);// after getting the value we are setting
                    }


                    // our value to our text view in below line.

                }
                adapter.notifyDataSetChanged();



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Profile_Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}