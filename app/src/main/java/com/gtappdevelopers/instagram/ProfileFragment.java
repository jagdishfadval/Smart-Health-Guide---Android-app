package com.gtappdevelopers.instagram;

import static android.app.Activity.RESULT_OK;
import static com.gtappdevelopers.instagram.addpost.PICK_IMAGE_REQUEST;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaDataSource;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    Button yourpostbtn,addpostbtn,editbtn,updatebtn;
    ImageView profileimageview;
    EditText bioview,websiteview,phoneview,addressview;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String profilepostIV;
    Uri imageUri;
    TextView nameview;
String userName;
users UserData;
String profileImage;
String currentuserId;


String Bio;
String Address;
String Website;
String Phone;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_profile, container, false);





        UserData=new users();



        context=getContext();

        yourpostbtn=rootView.findViewById(R.id.yourpostbtn);
        updatebtn=rootView.findViewById(R.id.updatebtn);
        addpostbtn=rootView.findViewById(R.id.addpostbtn);
        editbtn=rootView.findViewById(R.id.editbtn);
        bioview=rootView.findViewById(R.id.bio);
        websiteview=rootView.findViewById(R.id.website);
        phoneview=rootView.findViewById(R.id.phone);
        addressview=rootView.findViewById(R.id.address);
        profileimageview=rootView.findViewById(R.id.profilephtofragment);
        nameview= rootView.findViewById(R.id.personname);

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", context.MODE_PRIVATE);

        currentuserId= sharedPreferences.getString("userId","");
        profileImage= sharedPreferences.getString("Image","");
        userName= sharedPreferences.getString("Name","");




        nameview.setText(userName);


        getUserData();

        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).addListenerForSingleValueEvent(new ValueEventListener() {
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


               profileImage=snapshot.child("Image").getValue().toString();


                if(Bio!=null)
                bioview.setText(Bio);

                if(Website!=null)
                websiteview.setText(Website);

                if(Phone!=null)
                phoneview.setText(Phone);

                if(Address!=null)
                addressview.setText(Address);

                if(profileImage!=null) {
                    Glide
                            .with(context)

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









        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(context,addpost.class));
            }
        });

        yourpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YourPostFragment destgmeninationFragment = new YourPostFragment();

                // Get the FragmentManager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Begin the fragment transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the source fragment with the destination fragment
                fragmentTransaction.replace(R.id.fragment, destgmeninationFragment);

                // Add the transaction to the back stack (optional)
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();

                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.navigation_seach);
            }
        });



        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                    boolean stataubio=bioview.isEnabled();
                    boolean statusaddress=addressview.isEnabled();
                boolean statuswebsite=websiteview.isEnabled();
                boolean statusphone=phoneview.isEnabled();



                bioview.setEnabled(!stataubio);
                addressview.setEnabled(!statusaddress);
                websiteview.setEnabled(!statuswebsite);
                phoneview.setEnabled(!statusphone);





                String btnt= (String) editbtn.getTag();

                if(btnt.equals("false"))
                {
                    editbtn.setText("UNEDIT");
                    editbtn.setTag("true");
                }
                else{
                    editbtn.setText("EDIT");
                    editbtn.setTag("false");
                }

            }
        });


        profileimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                openGallery();


            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                
                if(imageUri!=null)
                    uploadImageToFirebaseStorage(imageUri);


               else {
                    profilepostIV = profileImage;


                    Bio = bioview.getText().toString();
                    Address = addressview.getText().toString();
                    Phone = phoneview.getText().toString();
                    Website = websiteview.getText().toString();


                    FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Bio").setValue(Bio);

                    FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Address").setValue(Address);

                    FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Phone").setValue(Phone);

                    FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Website").setValue(Website);

                    FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Image").setValue(profilepostIV);
                    SharedPreferences sharedPreferences= requireContext().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("Image",profilepostIV);

                    editor.apply();

                    Toast.makeText(context, "Profile Updated Success", Toast.LENGTH_LONG).show();
                }







            }
        });








        // Inflate the layout for this fragment
        return rootView;
    }

    private void getUserData() {




    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            imageUri = data.getData();
            profileimageview.setImageURI(imageUri);

        }
    }


    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // Get a reference to the Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Generate a unique filename for the image
        String filename = UUID.randomUUID().toString();

        // Create a reference to the location where the image will be uploaded
        StorageReference imageRef = storageReference.child("profiles/" + filename);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the image location
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       profilepostIV = uri.toString();


                                        Bio = bioview.getText().toString();
                                        Address = addressview.getText().toString();
                                        Phone = phoneview.getText().toString();
                                        Website = websiteview.getText().toString();


                                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Bio").setValue(Bio);

                                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Address").setValue(Address);

                                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Phone").setValue(Phone);

                                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Website").setValue(Website);

                                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuserId).child("Image").setValue(profilepostIV);

                                        SharedPreferences sharedPreferences= requireContext().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("Image",profilepostIV);

                                        editor.apply();
                                        Toast.makeText(context, "Profile Updated Success", Toast.LENGTH_LONG).show();


                                        // e.g., save it to Firebase Realtime Database

                                        // TODO: Handle the image location

                                        // Return the image location if needed
                                        // e.g., pass it to another activity or display it to the user

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload the image
                    }
                });
    }





}