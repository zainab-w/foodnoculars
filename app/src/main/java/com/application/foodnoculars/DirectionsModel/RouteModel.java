package com.application.foodnoculars.DirectionsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteModel {
    @SerializedName("legs")
    @Expose
    private List<LegModel> legs;

    @SerializedName("overview_polyline")
    @Expose
    private PolylineModel polylineModel;

    @SerializedName("summary")
    @Expose
    private String summary;

    public List<LegModel> getLegs() {
        return legs;
    }

    public void setLegs(List<LegModel> legs) {
        this.legs = legs;
    }

    public PolylineModel getPolylineModel() {
        return polylineModel;
    }

    public void setPolylineModel(PolylineModel polylineModel) {
        this.polylineModel = polylineModel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}