package com.gtappdevelopers.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;
    String name;
    String profilephoto;

    FirebaseAuth mauth;
    View rootView;
    private ArrayList<FacebookFeedModal> facebookFeedModalArrayList;
    RecyclerView instRV;
    EditText seachtextbox;
    FacebookFeedRVAdapter adapter;
    //creating variables for our requestqueue, array list, progressbar, edittext, image button and our recycler view.
    private RequestQueue mRequestQueue;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        SharedPreferences sharedPreferences= requireContext().getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
        userId= sharedPreferences.getString("userId","");
        name= sharedPreferences.getString("Name","");

        profilephoto= sharedPreferences.getString("Image","");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView=inflater.inflate(R.layout.fragment_home, container, false);
        context=getContext();
        //initializing our views.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("posts");

        getdata();



        seachtextbox= rootView.findViewById(R.id.searchEditText);
        seachtextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the data list based on the search query
                String query = charSequence.toString().toLowerCase();
                List<FacebookFeedModal> filteredList = new ArrayList<>();

                for (FacebookFeedModal item : facebookFeedModalArrayList) {
                    if (item.getPostDescription().toLowerCase().contains(query) || item.getAuthorName().toLowerCase().contains(query) ) {
                        filteredList.add(item);
                    }
                }
                adapter.setFilteredList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });







        // Inflate the layout for this fragment
        return rootView;
    }

    private void getdata() {
        FacebookFeedModal feedModal;
        facebookFeedModalArrayList = new ArrayList<>();
        adapter = new FacebookFeedRVAdapter(facebookFeedModalArrayList,context,getParentFragmentManager());
       instRV = rootView.findViewById(R.id.idRVInstaFeeds);
        ///below line is for setting linear layout manager to our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        //below line is to set layout manager to our recycler view.
        instRV.setLayoutManager(linearLayoutManager);
        //below line is to set adapter to our recycler view.
        instRV.setAdapter(adapter);

        ArrayList<String> userList= new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followed")
                        .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userList.clear();

                                // Iterate through each child node under "users"
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    // Get the user data from the snapshot
                                    String follwduser=userSnapshot.getKey();

                                    // Add the user to the userList
                                    userList.add(follwduser);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });








        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren()) {



                    FacebookFeedModal feedModal = snapshot1.getValue(FacebookFeedModal.class);

                    // Check if the post belongs to any of the followed user IDs
                    if (userList.contains(feedModal.getAuthorId())) {

                    facebookFeedModalArrayList.add(feedModal);// after getting the value we are setting
                    }




                }
                adapter.notifyDataSetChanged();



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}