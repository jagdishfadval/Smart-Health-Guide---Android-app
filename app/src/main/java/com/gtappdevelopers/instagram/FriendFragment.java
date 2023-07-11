package com.gtappdevelopers.instagram;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    RecyclerView userrecycler;
    EditText seachtextbox;
    private String mParam2;
    ArrayList<users> userdata;
    friendadapter adapter;
    String uid;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the adapter or fetch new data if needed
        adapter.notifyDataSetChanged();
        // Alternatively, you can call a method in the adapter to update data or perform any necessary operations
        // adapter.updateData(yourDataList);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_friend, container, false);
        context=getContext();


        SharedPreferences sharedPreferences= context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);

         uid=sharedPreferences.getString("userId","");


         userrecycler= rootView.findViewById(R.id.users);
        userdata=new ArrayList<>();


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
                List<users> filteredList = new ArrayList<>();

                for (users item : userdata) {
                    if (item.getName().toLowerCase().contains(query)) {
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




        userrecycler.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
         adapter=new friendadapter(userdata,context);
        userrecycler.setAdapter(adapter);

// Get a reference to the "users" node in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

// Attach a listener to retrieve the data for all users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve data for each user
                    String userId = userSnapshot.getKey();
                    String name = userSnapshot.child("Name").getValue(String.class);
                    String Image = userSnapshot.child("Image").getValue(String.class);

                    users user=new users(userId,Image,name);



                    if(!(userId.equals(uid)))
                    userdata.add(user);

                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
















        return rootView;



    }
}