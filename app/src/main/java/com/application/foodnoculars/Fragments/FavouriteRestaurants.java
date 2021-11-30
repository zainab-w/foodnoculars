package com.application.foodnoculars.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.application.foodnoculars.Directions;
import com.application.foodnoculars.SavedRestaurantInterface;
import com.application.foodnoculars.SavedRestaurantModel;
import com.application.foodnoculars.R;
import com.application.foodnoculars.databinding.FragmentFavouriteRestaurantsBinding;
import com.application.foodnoculars.databinding.SavedRestaurantItemsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class FavouriteRestaurants extends Fragment implements SavedRestaurantInterface {
    private FragmentFavouriteRestaurantsBinding binding;
    //Firebase

    private FirebaseAuth firebaseAuth;
    private FirebaseRecyclerAdapter<String, ViewHolder> firebaseRecyclerAdapter;

    private ArrayList<SavedRestaurantModel> savedRestaurantModelArrayList;
    private SavedRestaurantInterface savedRestaurantInterface;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFavouriteRestaurantsBinding.inflate(inflater, container, false);
        savedRestaurantInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();
        savedRestaurantModelArrayList = new ArrayList<>();

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Saved Restaurants");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.savedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.savedRecyclerView);
        getSavedPlaces();

    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onLocationClick(SavedRestaurantModel savedPlaceModel) {

        if (savedPlaceModel.getLat() != null && savedPlaceModel.getLng() != null) {
            Intent intent = new Intent(requireContext(), Directions.class);
            intent.putExtra("placeId", savedPlaceModel.getPlaceId());
            intent.putExtra("lat", savedPlaceModel.getLat());
            intent.putExtra("lng", savedPlaceModel.getLng());

            startActivity(intent);

        } else {
            Toast.makeText(requireContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SavedRestaurantItemsBinding binding;

        public ViewHolder(@NonNull SavedRestaurantItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void getSavedPlaces() {

        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(firebaseAuth.getUid())).child("Saved Restaurants");

        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, String.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull String savePlaceId) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Places").child(savePlaceId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            SavedRestaurantModel savedRestaurantModel = snapshot.getValue(SavedRestaurantModel.class);
                            holder.binding.setSavedRestaurantModel(savedRestaurantModel);
                            holder.binding.setListener(savedRestaurantInterface);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SavedRestaurantItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                        R.layout.saved_restaurant_items, parent, false);
                return new ViewHolder(binding);
            }
        };

        binding.savedRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}