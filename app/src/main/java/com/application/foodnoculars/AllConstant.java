package com.application.foodnoculars;

import com.application.foodnoculars.Model.PlaceModel;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstant {

    int STORAGE_REQUEST_CODE = 1000;
    int LOCATION_REQUEST_CODE = 2000;

    ArrayList<PlaceModel> placesName = new ArrayList<>(
            Arrays.asList(
                    new PlaceModel(1, R.drawable.ic_restaurant, "Restaurant", "restaurant"),
                    new PlaceModel(2, R.drawable.ic_cafe, "Cafe", "cafe"),
                    new PlaceModel(3, R.drawable.ic_run, "Meal Takeaway", "meal_takeaway"),
                    new PlaceModel(4, R.drawable.ic_run, "Meal Delivery", "meal_delivery")
            )
    );
}
