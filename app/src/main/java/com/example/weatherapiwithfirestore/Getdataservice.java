package com.example.weatherapiwithfirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Getdataservice {

    @GET("3534")
    Call<Weather> getWeather();

}
