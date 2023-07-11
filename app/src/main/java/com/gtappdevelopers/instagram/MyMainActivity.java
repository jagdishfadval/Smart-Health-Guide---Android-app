package com.gtappdevelopers.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final long BACK_PRESS_INTERVAL = 2000; // Time interval in milliseconds
    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);
        Fresco.initialize(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);






        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MyMainActivity.MODE_PRIVATE);
        boolean bloginstatus=sharedPreferences.getBoolean("islogin",false);

        if(bloginstatus==false)
        {
            startActivity(new Intent(MyMainActivity.this,LoginActivity.class));
            finish();
        }



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                int itemId = item.getItemId();
                switch (itemId) {


                    case R.id.home:

                        Fragment f = getSupportFragmentManager().findFragmentByTag("HomeFragment");
                        if (f == null) {
                            f = new HomeFragment();
                        }
                        // Initialize HomeFragment as the default fragment
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, f)
                                .commit();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                        break;

                    case R.id.share:

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        String appLink = "https://play.google.com/store/apps/details?id=com.FFLOL.freefirenickname";
                        String shareMessage = "Check out this cool app:\n" + appLink;
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));


                        break;
                    case R.id.help:

// Verify if Instagram is installed on the device

                            Uri instagramWebsiteUri = Uri.parse("https://www.instagram.com/aboti_akshay/"); // Instagram Direct URL on the web
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, instagramWebsiteUri);
                            startActivity(browserIntent);

                        break;

                    case R.id.ContactUs:
                        String phoneNumber = "9737690948"; // Replace with your phone number
                        String message = "Hello, I have a question."; // Replace with your message

                        try {
                            // Use WhatsApp API to open a chat with the specified phone number and message
                            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8"));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            // Handle the exception
                        }

                        break;
                    case R.id.logout:


                        FirebaseAuth mAuth;

// Initialize FirebaseAuth in onCreate() or wherever appropriate
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MyMainActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("userId");
                        editor.remove("Image");
                        editor.remove("Name");
                        editor.remove("islogin");

                        editor.remove("postimageurl");


                        editor.apply();

                        startActivity(new Intent(MyMainActivity.this,LoginActivity.class));

                        break;
                    case R.id.rateus:

                        try {
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.FFLOL.freefirenickname"));
                            startActivity(rateIntent);
                        } catch (ActivityNotFoundException e) {
                            // If Google Play Store app is not available, open the app page in a browser
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.FFLOL.freefirenickname"));


                        }


                        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                        drawerLayout.closeDrawer(GravityCompat.START);


                }
                return true;
            }
        });



        // Set up the navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.opennavition, R.string.closenavigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        Fragment home=new HomeFragment();
        // Initialize HomeFragment as the default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, home)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f=null;

                // Handle navigation item selection here
                switch (item.getItemId()) {
                    case R.id.navigation_home:


                        f = getSupportFragmentManager().findFragmentByTag("HomeFragment");
                        if (f == null) {
                            f = new HomeFragment();
                        }
                        break;
                    case R.id.navigation_seach:

                        f=new YourPostFragment();
                        // Handle item 2 selection
                        break;
                    case R.id.navigation_addpost:
                        // Handle item 3 selection
                        Intent intent=new Intent(MyMainActivity.this,addpost.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_friend:

                        f=new FriendFragment();
                        // Handle item 3 selection
                        break;
                    case R.id.navigation_profile:

                        f=new ProfileFragment();
                        // Handle item 3 selection
                        break;
                }

                if (f != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, f)
                            .commit();

                }
                return true;
            }
        });



    }


    public void onBackPressed() {
        if (backPressTime + BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed(); // Close the app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
    // Method to load the selected fragment into the container
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                FirebaseAuth mAuth;

// Initialize FirebaseAuth in onCreate() or wherever appropriate
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MyMainActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.remove("Image");
                editor.remove("Name");
                editor.remove("islogin");

                editor.remove("postimageurl");


                editor.apply();

                startActivity(new Intent(MyMainActivity.this,LoginActivity.class));
                return true;

            case android.R.id.home:


                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                   drawerLayout.openDrawer(GravityCompat.START);
                }


                return (true);


        }
        return (super.onOptionsItemSelected(item));
    }
}