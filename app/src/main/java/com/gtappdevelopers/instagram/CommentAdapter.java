package com.gtappdevelopers.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    //arraylist for our facebook feeds.
    private ArrayList<Comments> comments;
    String currentuserid;
    private Context context;

    //creating a constructor for our adapter class.
    public CommentAdapter(ArrayList<Comments> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter_layout, parent, false);
        return new CommentAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comments modal=comments.get(position);





        FirebaseDatabase.getInstance().getReference().child("users").child(modal.authourid).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.getValue(String.class);
                holder.profilename.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(modal.authourid).child("Image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photo=snapshot.getValue(String.class);

                Glide
                        .with(context)

                        .load(photo)
                        .placeholder(R.drawable.emtyprofile)

                        .into(holder.profilephoto);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        holder.commet.setText(modal.getCommnettext());

        holder.profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Profile_Activity.class);

                intent.putExtra("profileuser",modal.getAuthourid());
                context.startActivity(intent);
            }
        });






    }






    @Override
    public int getItemCount() {
        //returning the size of our array list.
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //creating variables for our views of recycler view items.
        private CircleImageView profilephoto;
        private TextView profilename,commet;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing our variables
            profilephoto = itemView.findViewById(R.id.commnetprofilephoto);
            profilename = itemView.findViewById(R.id.commentauthourname);
            commet = itemView.findViewById(R.id.commenttext);




        }
    }
}
