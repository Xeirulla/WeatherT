package com.bignerdranch.android.weather.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.bignerdranch.android.weather.R;
import com.bignerdranch.android.weather.Resource;
import com.bignerdranch.android.weather.api.App;
import com.bignerdranch.android.weather.model.City;
import com.bignerdranch.android.weather.model.find.FindCity;
import com.bignerdranch.android.weather.model.weathers.Weather;
import com.bignerdranch.android.weather.model.weathers.WeatherList;
import com.bignerdranch.android.weather.model.weathers.WeatherMain;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private OpenWeatherMapHelper helper;

    private MutableLiveData<Resource<ArrayList<WeatherMain>>> currentWather = new MutableLiveData<>();
    private MutableLiveData<Resource<FindCity>> findCity = new MutableLiveData<>();

    private String appid;

    public WeatherRepository(Context context) {
        appid = context.getResources().getString(R.string.api_key);
        helper = new OpenWeatherMapHelper(context.getResources().getString(R.string.api_key));
        helper.setLang(Lang.RUSSIAN);
        helper.setUnits(Units.METRIC);
    }

    public LiveData<Resource<ArrayList<WeatherMain>>> loadCity(final List<Integer> ids) {
        currentWather.setValue(Resource.<ArrayList<WeatherMain>>loading());

        final ArrayList<WeatherMain> listWeather = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (final Integer id : ids) {
                        listWeather.add(App.getApi().getWeather(id.toString(), appid).execute().body());
                    }

                    currentWather.postValue(Resource.success(listWeather));
                } catch (IOException e) {
                    currentWather.postValue(Resource.<ArrayList<WeatherMain>>error(e.getLocalizedMessage()));
                    e.printStackTrace();
                }
            }
        }.start();

        return currentWather;
    }


    public LiveData<Resource<FindCity>> find(String name) {
        findCity.setValue(Resource.<FindCity>loading());
        App.getApi().find(name, appid).enqueue(new Callback<FindCity>() {
            @Override
            public void onResponse(Call<FindCity> call, Response<FindCity> response) {
                if (response.body() != null) {
                    findCity.postValue(Resource.success(response.body()));
                } else {
                    findCity.postValue(Resource.<FindCity>error("Город не найден"));
                }
            }

            @Override
            public void onFailure(Call<FindCity> call, Throwable t) {
                findCity.postValue(Resource.<FindCity>error(t.getLocalizedMessage()));
            }
        });

        return findCity;
    }
}
