package com.application.foodnoculars.DirectionsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel {


        @SerializedName("routes")
        @Expose
        List<RouteModel> routeModels;


        public List<RouteModel> getDirectionRouteModels() {
        return routeModels;
    }

        public void setDirectionRouteModels(List<RouteModel> routeModels) {
        this.routeModels = routeModels;
    }
}