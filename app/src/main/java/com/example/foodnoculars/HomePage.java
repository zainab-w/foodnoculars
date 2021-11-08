package com.example.foodnoculars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button btnNearbyRestaurants, btnFavRestaurants, btnAllRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnNearbyRestaurants = findViewById(R.id.btnNearbyRestaurants);
        btnFavRestaurants = findViewById(R.id.btnFavRestaurants);
        btnAllRestaurants = findViewById(R.id.btnAllRestaurants);

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

    }
}