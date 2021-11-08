package com.example.foodnoculars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.foodnoculars.Fragments.FavouriteRestaurants;
import com.example.foodnoculars.Fragments.NearbyRestaurants;
import com.example.foodnoculars.Fragments.Settings;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class NearbyLocations extends AppCompatActivity {
    //Menu Bar
    private ImageView btnMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);
//        btnMenu = findViewById(R.id.bt_menu);
        drawerLayout = findViewById(R.id.navDrawer);
//        abt = new ActionBarDrawerToggle(this, drawerLayout,
//                R.string.open_navigation_drawer, R.string.close_navigation_drawer);
//        abt.setDrawerIndicatorEnabled(true);

        //drawerLayout.addDrawerListener(abt);
       // abt.syncState();
        replace(new NearbyRestaurants());

        //Menu Bar
//        MenuBar();
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//
//        });
    }

    private void replace(NearbyRestaurants nearbyRestaurants) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,nearbyRestaurants);
        fragmentTransaction.commit();
//    }
//    private void replace(FavouriteRestaurants favouriteRestaurants) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,favouriteRestaurants);
//        fragmentTransaction.commit();
//    }
//    private void replace(Settings settings) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,settings);
//        fragmentTransaction.commit();
//    }
//    private void MenuBar() {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item)
//            {
//                int id = item.getItemId();
//                if (id == R.id.btnNearbyRestaurants)
//                {
//                    replace(new NearbyRestaurants());
//                }
//                else if (id == R.id.btnFavRestaurants)
//                {
//                    replace(new FavouriteRestaurants());
//                }
//                else if (id == R.id.btnSettings)
//                {
//                    replace(new Settings());
//                }
//                else if (id == R.id.btnLogout)
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(NearbyLocations.this);
//                    builder.setTitle("Logout");
//                    builder.setMessage("Are you sure you want to logout?");
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            FirebaseAuth.getInstance().signOut();
//                            Intent intent2 = new Intent(NearbyLocations.this,Login.class);
//                            startActivity(intent2);
//                        }
//                    });
//                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //dismiss Dialog
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.show();
//                }
//                return true;
//            }
//        });
    }
}