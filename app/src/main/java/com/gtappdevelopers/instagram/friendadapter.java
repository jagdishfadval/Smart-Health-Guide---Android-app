package com.gtappdevelopers.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class friendadapter extends RecyclerView.Adapter<friendadapter.ViewHolder> {
    //arraylist for our facebook feeds.
    private ArrayList<users> users;
    String currentuserid;
    private Context context;

    //creating a constructor for our adapter class.
    public friendadapter(ArrayList<users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public friendadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend_adapter, parent, false);
        return new friendadapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull friendadapter.ViewHolder holder, int position) {
        //getting data from arraylist and setting it to our modal class.
        users modal = users.get(position);
        //settting data to each view of recyclerview item.
        Glide
                .with(context)

                .load(modal.getImage())
                .placeholder(R.drawable.emtyprofile)


                .into(holder.profilephoto);

//        Picasso.get().load(modal.getImage()).into(holder.profilephoto);


        holder.profilename.setText(modal.getName());



                // Get the clicked user ID
                String clickedUserId = modal.getUserId();
                SharedPreferences sharedPreferences=context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
         currentuserid= sharedPreferences.getString("userId","");


        final String[] state = {"notfollow"};

                DatabaseReference followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed");

                followedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(clickedUserId)) {
                            // The user is already followed
                           holder.follow.setText("Following");
                            holder.follow.setBackgroundColor(context.getResources().getColor(R.color.black));

                            state[0] ="follow";
                        } else {
                            state[0] ="nofollow";

                            holder.follow.setText("Follow");
                            holder.follow.setBackgroundColor(context.getResources().getColor(R.color.primary));






                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





                holder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                if(state[0].equals("follow"))
                {
                    DatabaseReference followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed");
                    followedRef.child(clickedUserId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    holder.follow.setText("Follow");
                                    holder.follow.setBackgroundColor(context.getResources().getColor(R.color.primary));




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

                   Task<Void> followedRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid).child("followed").child(clickedUserId).setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.follow.setText("Following");
                                    holder.follow.setBackgroundColor(context.getResources().getColor(R.color.black));

                                }
                            });





                }



                    }
                });







        holder.cardclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(context,Profile_Activity.class);

                i.putExtra("profileuser",modal.getUserId());
                context.startActivity(i);


            }
        });




            }






    @Override
    public int getItemCount() {
        //returning the size of our array list.
        return users.size();
    }

    public void setFilteredList(List filteredList) {

        users = (ArrayList<com.gtappdevelopers.instagram.users>) filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //creating variables for our views of recycler view items.
        private CircleImageView profilephoto;
        private TextView profilename;
        private CardView cardclick;

        private Button follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing our variables
            profilephoto = itemView.findViewById(R.id.profilephoto);
            profilename = itemView.findViewById(R.id.profilename);
            follow = itemView.findViewById(R.id.follow);
            cardclick=itemView.findViewById(R.id.cardclick);

        }
    }
}
