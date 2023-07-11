package com.gtappdevelopers.instagram;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragmnet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragmnet extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    RecyclerView recyclerView;
    Context context;
    EditText commentedittext;
    ImageView commentsendbtn;




    String postid;
    String currentuserid;
    String profileimg;

    public CommentFragmnet(String postid) {
        this.postid=postid;
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_comment_fragmnet, container, false);
        context=getContext();


        SharedPreferences sharedPreferences=context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
        currentuserid= sharedPreferences.getString("userId","");





        recyclerView=rootView.findViewById(R.id.commentrecyclerview);
        commentsendbtn=rootView.findViewById(R.id.sendcommentbutton);
        commentedittext=rootView.findViewById(R.id.commnetbox);

        ArrayList<Comments> array=new ArrayList<>();
        CommentAdapter adapter=new CommentAdapter(array,context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);



        FirebaseDatabase.getInstance().getReference().child("posts").child(postid).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                array.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Comments comment= ds.getValue(Comments.class);
                    array.add(comment);


                }


      adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













        commentsendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=commentedittext.getText().toString();

                if(!(text.equals("")))
                {



                    Comments comments=new Comments(currentuserid,text);

                    String commentid =  FirebaseDatabase.getInstance().getReference().child("posts").child(currentuserid).push().getKey();

                    FirebaseDatabase.getInstance().getReference().child("posts").child(postid).child("comments").child(commentid).setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });




                }
                else{
                    Toast.makeText(context, "Please add comment first! ", Toast.LENGTH_SHORT).show();
                }




commentedittext.setText("");

            }
        });

        return rootView;
    }



}