package com.example.rent.movieapp;

import android.app.Application;

import com.example.rent.movieapp.dagger.AppComponent;
import com.example.rent.movieapp.dagger.DaggerAppComponent;
import com.facebook.stetho.Stetho;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-11.
 */

public class MovieAppApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        appComponent = DaggerAppComponent.builder().build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
