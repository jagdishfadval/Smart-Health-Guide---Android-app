package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class addpost extends AppCompatActivity {
    EditText postdes;
    TextView authournametextview;
    ImageView authourimageview,postimageview;
    String userId ;
    String postId;
    FacebookFeedModal post;
    FloatingActionButton gallery;
    String authorImage ;
    String authourName;

    String postDate = null;
    String postDescription ;
    String postIV ;
    String postLikes="0";
    String postComments="0";
    Uri imageUri;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        authournametextview=findViewById(R.id.idTVAuthorName);
        authourimageview=findViewById(R.id.authorimage);
        postimageview=findViewById(R.id.postimage);
        postdes=findViewById(R.id.postdescription);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",addpost.MODE_PRIVATE);


        userId=  sharedPreferences.getString("userId","").toString();


           authorImage= sharedPreferences.getString("Image","").toString();
          authourName=  sharedPreferences.getString("Name","").toString();


        Glide
                .with(addpost.this)

                .load(authorImage)
                .placeholder(R.drawable.emtyprofile)

                .into(authourimageview);

//             Picasso.get().load(authorImage).into(authourimageview);


            authournametextview.setText(authourName);
            postId = databaseReference.child("posts").child(userId).push().getKey();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            String currentTime = String.format("%02d:%02d:%02d", hour, minute, second);
            String currentDate = String.format("%04d-%02d-%02d", year, month + 1, day);
             postDate=currentDate + " "+currentTime;





            gallery=findViewById(R.id.media);
            gallery.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    openGallery();
                }
            });












    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addpostmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addpostdone:


                        if(imageUri!=null)
                        uploadImageToFirebaseStorage(imageUri);
                       else {
                            postIV = "noimage";

                            postDescription = postdes.getText().toString();
                            if (postDescription.isEmpty()) {
                                Toast.makeText(addpost.this, "Please add post description first!", Toast.LENGTH_SHORT).show();


                            } else {
                                postDescription = postdes.getText().toString();




                                post = new FacebookFeedModal(
                                        postId,
                                        userId,
                                        authorImage,
                                        authourName,
                                        postDate,
                                        postDescription,
                                        postIV,
                                        postLikes,
                                        postComments,
                                        userId


                                );

                                insertdata(post);
                            }
                        }



//                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//                postIV = sharedPreferences.getString("postimageurl", "noimage");
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("postimageurl");
//                editor.apply();


                return true;

            case android.R.id.home:

               this.finish();



                return (true);


        }
        return (super.onOptionsItemSelected(item));
    }

    static final int PICK_IMAGE_REQUEST = 1;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
             imageUri = data.getData();
            postimageview.setImageURI(imageUri);
            // Upload the image to Firebase Storage and get the image location

        }
    }


    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // Get a reference to the Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Generate a unique filename for the image
        String filename = UUID.randomUUID().toString();

        // Create a reference to the location where the image will be uploaded
        StorageReference imageRef = storageReference.child("posts/" + filename);

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
                                       postIV = uri.toString();



                                        postDescription=postdes.getText().toString();
                                        if(postDescription.isEmpty())
                                        {
                                            Toast.makeText(addpost.this, "Please add post description first!", Toast.LENGTH_SHORT).show();


                                        }
                                        else {
                                            postDescription = postdes.getText().toString();




                                            post = new FacebookFeedModal(
                                                    postId,
                                                    userId,
                                                    authorImage,
                                                    authourName,
                                                    postDate,
                                                    postDescription,
                                                    postIV,
                                                    postLikes,
                                                    postComments,
                                                  userId


                                            );

                                            insertdata(post);




                                        }






//                                      SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
//
//
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//
//                                            editor.putString("postimageurl",postIV);
//                                            editor.apply();
//
//                                        Log.d("url111", postIV);
                                        // Handle the image location as needed
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




    private void insertdata(FacebookFeedModal post) {



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();



        rootRef.child("posts").child(postId).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Toast.makeText(addpost.this, "Post Added Success.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(addpost.this,MyMainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to insert data
                    }
                });
    }
}