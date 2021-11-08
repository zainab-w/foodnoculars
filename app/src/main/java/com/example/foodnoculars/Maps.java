package com.example.foodnoculars;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;


import com.example.foodnoculars.databinding.ActivityMapsBinding;
import com.example.foodnoculars.databinding.NavDrawerLayoutBinding;
import com.example.foodnoculars.databinding.ToolbarLayoutBinding;


public class Maps extends AppCompatActivity {

    private NavDrawerLayoutBinding navDrawerLayoutBinding;
    private ActivityMapsBinding activityMapsBinding;
    private ToolbarLayoutBinding toolbarLayoutBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        navDrawerLayoutBinding = NavDrawerLayoutBinding
//                .inflate(getLayoutInflater());
//        activityMapsBinding = navDrawerLayoutBinding.mainActivity;
//        toolbarLayoutBinding = activityMapsBinding.toolbar;

//        setSupportActionBar(toolbarLayoutBinding.toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this,
//                navDrawerLayoutBinding.navDrawer,
//                toolbarLayoutBinding.toolbar,
//                R.string.open_navigation_drawer,
//                R.string.close_navigation_drawer
//        );
//
//        navDrawerLayoutBinding.navDrawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavController navController = Navigation.findNavController(
//                this,R.id.fragmentContainer);
//
//        NavigationUI.setupWithNavController(
//                navDrawerLayoutBinding.navigationView,
//                navController
//        );

    }

}