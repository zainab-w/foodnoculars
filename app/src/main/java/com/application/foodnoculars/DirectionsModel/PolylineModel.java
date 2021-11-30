package com.application.foodnoculars.DirectionsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolylineModel {
    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}