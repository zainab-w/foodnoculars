package com.example.foodnoculars.Webservice;

import com.example.foodnoculars.DirectionsModel.ResponseModel;
import com.example.foodnoculars.Model.GoogleResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET
    Call<GoogleResponseModel> getNearByPlaces(@Url String url);

    @GET
    Call<ResponseModel> getDirection(@Url String url);
}