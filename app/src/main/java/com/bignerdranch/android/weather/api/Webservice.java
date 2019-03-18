package com.bignerdranch.android.weather.api;

import com.bignerdranch.android.weather.model.City;
import com.bignerdranch.android.weather.model.find.FindCity;
import com.bignerdranch.android.weather.model.weathers.WeatherMain;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Webservice {

    @GET("forecast?mode=json&units=metric&cnt=7&lang=ru")
    Call<WeatherMain> getWeather(@Query("id") String id, @Query("appid") String appid);


    @GET("find?cnt=1&type=like&sort=population&lang=ru")
    Call<FindCity> find(@Query("q") String name, @Query("appid") String appid);
}