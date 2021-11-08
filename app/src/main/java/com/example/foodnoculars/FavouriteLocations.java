package com.example.foodnoculars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.foodnoculars.Fragments.FavouriteRestaurants;
import com.example.foodnoculars.Fragments.NearbyRestaurants;

public class FavouriteLocations extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_locations);

        replace(new FavouriteRestaurants());
    }

    private void replace(FavouriteRestaurants favouriteRestaurants) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,favouriteRestaurants);
        fragmentTransaction.commit();
    }
}