package com.example.weatherapiwithfirestore;

import java.sql.SQLOutput;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.metaweather.com/api/location/";

    public static Retrofit getRetrofitInstance(){

        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

       // System.out.println(retrofit);
        return retrofit;

    }

}
