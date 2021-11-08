package com.example.foodnoculars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    Button btnNearbyRestaurants, btnFavRestaurants, btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnNearbyRestaurants = findViewById(R.id.btnNearbyRestaurants);
        btnFavRestaurants = findViewById(R.id.btnFavRestaurants);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        btnNearbyRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePage.this, NearbyLocations.class));
            }
        });
        btnFavRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePage.this, FavouriteLocations.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePage.this, Settings.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, Login.class));
            }
        });


    }
}