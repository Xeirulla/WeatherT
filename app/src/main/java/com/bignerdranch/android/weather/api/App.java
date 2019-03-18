package com.bignerdranch.android.weather.api;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.bignerdranch.android.weather.model.AppDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    public static App instance;
    private static AppDatabase database;
    private Retrofit mRetrofit;
    private static Webservice sHomeApi;
    private String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();

        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        sHomeApi = mRetrofit.create(Webservice.class);

    }

    public static App getInstance() {
        return instance;
    }

    public static Webservice getApi() {
        return sHomeApi;
    }

    public static AppDatabase getDb() {
        return database;
    }
}
