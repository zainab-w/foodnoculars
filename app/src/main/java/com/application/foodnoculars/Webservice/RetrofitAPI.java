package com.application.foodnoculars.Webservice;

import com.application.foodnoculars.DirectionsModel.ResponseModel;
import com.application.foodnoculars.Model.GoogleResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET
    Call<GoogleResponseModel> getNearByPlaces(@Url String url);

    @GET
    Call<ResponseModel> getDirection(@Url String url);
}