package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.etEmailAddress);
        passwordTextView = findViewById(R.id.etPassword);
        Btn = findViewById(R.id.btnLogin);


        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);
                Boolean islogin= sharedPreferences.getBoolean("islogin",false);

                if(islogin)
                {
                    startActivity(new Intent(LoginActivity.this,MyMainActivity.class));
                }

            }
        });

    }

    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading


        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
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

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {








                                if (task.isSuccessful()) {
                                    getuserdata();
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar

                                }
                            }
                        });
    }

    private void getuserdata() {

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        Log.e("uidaaa", "getuserdata: " +user + user.getUid() );
        String userId = null;
        if(user!=null)
             userId=user.getUid();



        String abc =getString(R.string.userId);


            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);  
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("userId",userId);
            editor.putBoolean("islogin",true);
                    editor.apply();                                                                                                   

        DatabaseReference userreferace= FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        
        userreferace.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();



                    for(DataSnapshot snapshot1:snapshot.getChildren()) {


                editor.putString(snapshot1.getKey(),snapshot1.getValue().toString());




                    }

                editor.apply();





               






            }
       

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }


    public void register(View view) {

        Intent intent =new Intent(this,Registration.class);
        startActivity(intent);
    }
}