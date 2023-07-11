package com.gtappdevelopers.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacebookFeedRVAdapter extends RecyclerView.Adapter<FacebookFeedRVAdapter.ViewHolder> {
    //arraylist for our facebook feeds.
    private ArrayList<FacebookFeedModal> facebookFeedModalArrayList;
    private DatabaseReference postLikeRef;
    String currentuserid="";
    private Context context;
    Activity activity;
    FragmentManager fragment;

    //creating a constructor for our adapter class.
    public FacebookFeedRVAdapter(ArrayList<FacebookFeedModal> facebookFeedModalArrayList, Context context,FragmentManager fragment) {
        this.facebookFeedModalArrayList = facebookFeedModalArrayList;
        SharedPreferences sharedPreferences=context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
        currentuserid= sharedPreferences.getString("userId","");
        this.context = context;
        postLikeRef= FirebaseDatabase.getInstance().getReference().child("posts");
       this.activity=activity;
       this.fragment=fragment;


    }

    public FacebookFeedRVAdapter(ArrayList<FacebookFeedModal> facebookFeedModalArrayList, Context context) {
        this.facebookFeedModalArrayList = facebookFeedModalArrayList;
        SharedPreferences sharedPreferences=context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
        currentuserid= sharedPreferences.getString("userId","");
        this.context = context;
        postLikeRef= FirebaseDatabase.getInstance().getReference().child("posts");

    }

    @NonNull
    @Override
    public FacebookFeedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facebook_feed_rv_item, parent, false);

        return new FacebookFeedRVAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //getting data from arraylist and setting it to our modal class.
        FacebookFeedModal modal = facebookFeedModalArrayList.get(position);

        //settting data to each view of recyclerview item.


        holder.timeTV.setText(modal.getPostDate());
        holder.descTV.setText(modal.getPostDescription());



///// get user data

        FirebaseDatabase.getInstance().getReference().child("users").child(modal.getAuthorId()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.getValue(String.class);
                holder.authorNameTV.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(modal.getAuthorId()).child("Image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photo=snapshot.getValue(String.class);

                Glide
                        .with(context)

                        .load(photo)
                        .placeholder(R.drawable.emtyprofile)

                        .into(holder.authorIV);
//                Picasso.get().load(photo).into(holder.authorIV);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
///////












        holder.commentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFragmnet bottomSheetFragment = new CommentFragmnet(modal.getPostId());
                bottomSheetFragment.show(fragment, bottomSheetFragment.getTag());

            }
        });


        if(modal.getPostIV().equals("noimage"))
        {
            holder.postIV.setVisibility(View.GONE);
        }
        else{
            holder.postIV.setVisibility(View.VISIBLE);

            Glide
                    .with(context)

                    .load(modal.getPostIV())
                    .placeholder(R.drawable.spinner)

                    .into(holder.postIV);
//            Picasso.get().load(modal.getPostIV()).into(holder.postIV);
        }


        FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("likedBy"))
                {
                    FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).child("likedBy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(currentuserid))
                            {
                                holder.likesTV.setImageResource(R.drawable.heartliked);
                                holder.likesTV.setTag("true");
                            }
                            else{
                                holder.likesTV.setImageResource(R.drawable.heart);
                                holder.likesTV.setTag("false");
                            }



                            int likes= (int) snapshot.getChildrenCount();
                            String likess=String.valueOf(likes);

                            holder.likescount.setText(likess);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                else{




                    holder.likesTV.setImageResource(R.drawable.heart);
                    holder.likesTV.setTag("false");

                    holder.likescount.setText("0");

                }


                if(snapshot.hasChild("comments"))
                {
                    FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int comments= (int) snapshot.getChildrenCount();
                            String comment=String.valueOf(comments);

                            holder.commentcount.setText(comment);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{



                    holder.commentcount.setText("0");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        holder.likesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String likestatus= (String) holder.likesTV.getTag();
                if(likestatus.equals("true")) {


                    FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("likedBy"))
                            {

                               FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).child("likedBy").child(currentuserid).removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }
                if(likestatus.equals("false")) {



                    FirebaseDatabase.getInstance().getReference().child("posts").child(modal.getPostId()).child("likedBy").child(currentuserid).setValue(true);




                }




            }
        });





       holder.shareLL.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
               shareIntent.setType("text/plain"); // Set the type of content to be shared, in this case, an image

               // Set the text content (optional)
               shareIntent.putExtra(Intent.EXTRA_TEXT, modal.getPostDescription());

               // Set the image content (optional)


               // Start the activity for sharing
               context.startActivity(Intent.createChooser(shareIntent, "Share via"));
           }
       });



      holder.idLLTopBar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(context,Profile_Activity.class);

              intent.putExtra("profileuser",modal.getAuthorId());
              context.startActivity(intent);
          }
      });





      holder.postIV.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              openImageFullScreen(modal.getPostIV());
          }
      });

    }
    private void openImageFullScreen(String imageUrl) {
        new ImageViewer.Builder<>(context, new String[]{imageUrl})
                .setStartPosition(0)

                .show();
    }






    @Override
    public int getItemCount() {
        //returning the size of our array list.
        return facebookFeedModalArrayList.size();
    }

    public void setFilteredList(List<FacebookFeedModal> filteredList) {
        facebookFeedModalArrayList = (ArrayList<FacebookFeedModal>) filteredList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //creating variables for our views of recycler view items.
        private CircleImageView authorIV;
        private TextView authorNameTV, timeTV, descTV,likescount,commentcount;
        private ImageView postIV;
        private ImageView likesTV, commentsTV;
        private  LinearLayout commentlayout;
        private LinearLayout shareLL,idLLTopBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing our variables
            shareLL = itemView.findViewById(R.id.idLLShare);
            authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorNameTV = itemView.findViewById(R.id.idTVAuthorName);
            timeTV = itemView.findViewById(R.id.idTVTime);
            descTV = itemView.findViewById(R.id.idTVDescription);
            postIV = itemView.findViewById(R.id.idIVPost);
            likesTV = itemView.findViewById(R.id.likebtn);
            likescount=itemView.findViewById(R.id.likescount);
            commentsTV = itemView.findViewById(R.id.commentbtn);
            commentcount = itemView.findViewById(R.id.commentcount);
            commentlayout=itemView.findViewById(R.id.commentlayout);
            idLLTopBar=itemView.findViewById(R.id.idLLTopBar);
        }
    }
}
