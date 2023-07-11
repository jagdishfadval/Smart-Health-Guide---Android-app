package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private EditText emailTextView, passwordTextView,confirmpasswordTextView,usernameEditText;
    private Button Btn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        usernameEditText = findViewById(R.id.name);
        emailTextView = findViewById(R.id.etEmailAddress);
        passwordTextView = findViewById(R.id.etPassword);
        confirmpasswordTextView = findViewById(R.id.etconfirmpassword);
        mAuth = FirebaseAuth.getInstance();
        Btn = findViewById(R.id.btnregister);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });


    }


    private void registerNewUser()
    {



        // Take the value of two edit texts in Strings
        String email="", password="",confirmpassword="";
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        confirmpassword = confirmpasswordTextView.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter confirm password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if(!(password.equals(confirmpassword)))
        {
            Toast.makeText(getApplicationContext(),
                            "password does not match",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {



               addusernameintodatabase();









                    // hide the progress bar

                    // if the user created intent to login activity

                }
                else {

                    // Registration failed
                    Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar

                }
            }
        });
    }

    private boolean addusernameintodatabase() {


        String name = usernameEditText.getText().toString();

        // Insert the username into the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference usernameRef = databaseReference.child("users").child(userId).child("Name");
            usernameRef.setValue(name)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Username inserted successfully
                            DatabaseReference usernameRef = databaseReference.child("users").child(userId).child("Image");
                            usernameRef.setValue("https://firebasestorage.googleapis.com/v0/b/smarthealthguide-b3eef.appspot.com/o/profiles%2Femtyprofile.png?alt=media&token=0d2759d3-e3bc-4e00-b88d-8a38ff947ada")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),
                                                            "Registration successful!",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(
                                                            getApplicationContext(),
                                                            "Registration failed!!"
                                                                    + " Please try again later",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to insert username

                        }
                    });




        }


        return false;

    }

    public void login(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}