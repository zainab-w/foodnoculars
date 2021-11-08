package com.example.foodnoculars.Adapter;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodnoculars.DirectionsModel.StepModel;
import com.example.foodnoculars.databinding.DirectionsStepItemsBinding;


import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<StepModel> stepModels;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DirectionsStepItemsBinding binding = DirectionsStepItemsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (stepModels != null) {
            StepModel stepModel = stepModels.get(position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.txtStepHtml.setText(Html.fromHtml(stepModel.getHtmlInstructions(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.binding.txtStepHtml.setText(Html.fromHtml(stepModel.getHtmlInstructions()));
            }

            holder.binding.txtStepTime.setText(stepModel.getDuration().getText());
            holder.binding.txtStepDistance.setText(stepModel.getDistance().getText());
        }

    }

    @Override
    public int getItemCount() {

        if (stepModels != null)
            return stepModels.size();
        else
            return 0;
    }

    public void setDirectionStepModels(List<StepModel> directionStepModels) {
        this.stepModels = directionStepModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private DirectionsStepItemsBinding binding;

        public ViewHolder(@NonNull DirectionsStepItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}