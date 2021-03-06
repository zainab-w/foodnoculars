package com.application.foodnoculars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.application.foodnoculars.Fragments.NearbyRestaurants;

public class NearbyLocations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);


        replace(new NearbyRestaurants());


    }

    private void replace(NearbyRestaurants nearbyRestaurants) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,nearbyRestaurants);
        fragmentTransaction.commit();
   }


   //menu bar code
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