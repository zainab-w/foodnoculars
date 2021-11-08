package com.example.foodnoculars.DirectionsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StepModel {

    @SerializedName("distance")
    @Expose
    private DistanceModel distance;
    @SerializedName("duration")
    @Expose
    private DurationModel duration;
    @SerializedName("end_location")
    @Expose
    private EndLocationModel endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private PolylineModel polyline;
    @SerializedName("start_location")
    @Expose
    private StartLocationModel startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;

    public DistanceModel getDistance() {
        return distance;
    }

    public void setDistance(DistanceModel distance) {
        this.distance = distance;
    }

    public DurationModel getDuration() {
        return duration;
    }

    public void setDuration(DurationModel duration) {
        this.duration = duration;
    }

    public EndLocationModel getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocationModel endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public PolylineModel getPolyline() {
        return polyline;
    }

    public void setPolyline(PolylineModel polyline) {
        this.polyline = polyline;
    }

    public StartLocationModel getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocationModel startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }
}