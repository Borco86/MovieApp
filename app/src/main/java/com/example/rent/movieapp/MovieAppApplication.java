package com.example.rent.movieapp;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-11.
 */

public class MovieAppApplication extends Application implements RetrofitProvider{

    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
        // konfiguracja retrofita
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.omdbapi.com/")
                .build();
    }

    @Override
    public Retrofit provideRetrofit() {
        return retrofit;
    }
}
