package com.application.foodnoculars.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;

import com.application.foodnoculars.GooglePlaceModel;
import com.application.foodnoculars.NearbyPlaceInterface;
import com.application.foodnoculars.R;
import com.application.foodnoculars.databinding.PlaceItemsRecycleviewBinding;


import java.util.List;

public class GooglePlaceAdapter extends
        RecyclerView.Adapter<GooglePlaceAdapter.ViewHolder> {

    private List<GooglePlaceModel> googlePlaceModels;
    private NearbyPlaceInterface nearbyPlaceInterface;

    public GooglePlaceAdapter(NearbyPlaceInterface nearbyPlaceInterface) {
        this.nearbyPlaceInterface = nearbyPlaceInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceItemsRecycleviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.place_items_recycleview, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (googlePlaceModels != null) {
            GooglePlaceModel placeModel = googlePlaceModels.get(position);
            holder.binding.setGooglePlaceModel(placeModel);
            holder.binding.setListener(nearbyPlaceInterface);
        }

    }

    @Override
    public int getItemCount() {
        if (googlePlaceModels != null)
            return googlePlaceModels.size();
        else
            return 0;
    }

    public void setGooglePlaceModels(List<GooglePlaceModel> googlePlaceModels) {
        this.googlePlaceModels = googlePlaceModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PlaceItemsRecycleviewBinding binding;

        public ViewHolder(@NonNull PlaceItemsRecycleviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}