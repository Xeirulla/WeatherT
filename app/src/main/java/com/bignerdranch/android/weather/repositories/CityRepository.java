package com.bignerdranch.android.weather.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.bignerdranch.android.weather.Resource;
import com.bignerdranch.android.weather.model.City;
import com.bignerdranch.android.weather.model.CityDAO;
import com.bignerdranch.android.weather.model.find.Cities;
import com.bignerdranch.android.weather.model.weathers.WeatherMain;

import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    private CityDAO cityDao;

    private MutableLiveData<Boolean> success = new MutableLiveData();

    private static final String FIRST_RUN = "FIRST_RUN";


    public CityRepository(final CityDAO cityDao, Context context) {
        this.cityDao = cityDao;
        SharedPreferences sharedPreferences = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FIRST_RUN, true)) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        City defaultCity = new City();
                        defaultCity.id = 524901;
                        defaultCity.cityName = "Moscow";
                        defaultCity.countryName = "RU";
                        cityDao.insert(defaultCity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            sharedPreferences.edit().putBoolean(FIRST_RUN, false).commit();
        }
    }

    public LiveData<List<City>> getAll() {
        return cityDao.getAllCity();
    }

    public LiveData<Boolean> create(final Cities cities) {
        new Thread() {
            @Override
            public void run() {
                try {
                    City newCity = new City();
                    newCity.id = cities.getId();
                    newCity.cityName = cities.getName();
                    newCity.countryName = cities.getSys().getCountry();

                    cityDao.insert(newCity);

                    success.postValue(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return success;
    }
}
