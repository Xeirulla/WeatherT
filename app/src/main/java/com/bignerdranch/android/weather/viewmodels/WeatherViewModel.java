package com.bignerdranch.android.weather.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bignerdranch.android.weather.Resource;
import com.bignerdranch.android.weather.api.App;
import com.bignerdranch.android.weather.model.City;
import com.bignerdranch.android.weather.model.find.Cities;
import com.bignerdranch.android.weather.model.find.FindCity;
import com.bignerdranch.android.weather.model.weathers.WeatherList;
import com.bignerdranch.android.weather.model.weathers.WeatherMain;
import com.bignerdranch.android.weather.repositories.CityRepository;
import com.bignerdranch.android.weather.repositories.WeatherRepository;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<Resource<ArrayList<WeatherMain>>> currentWeather = new MutableLiveData<>();

    private WeatherRepository mRepository = new WeatherRepository(getApplication());
    private CityRepository mCityRepository = new CityRepository(App.getDb().getCityDao(), getApplication());

    private Observer<List<City>> loadFromdb = new Observer<List<City>>() {
        @Override
        public void onChanged(@Nullable List<City> cities) {
            ArrayList<Integer> ids = new ArrayList<>();
            for (City city : cities) {
                ids.add(city.id);
            }
            mRepository.loadCity(ids).observeForever(loadFromApi);
        }
    };

    private Observer<Resource<ArrayList<WeatherMain>>> loadFromApi = new Observer<Resource<ArrayList<WeatherMain>>>() {
        @Override
        public void onChanged(@Nullable Resource<ArrayList<WeatherMain>> currentWeatherResource) {
            currentWeather.postValue(currentWeatherResource);
        }
    };

    public LiveData<Resource<ArrayList<WeatherMain>>> get() {
        mCityRepository.getAll().observeForever(loadFromdb);
        return currentWeather;
    }

    public LiveData<Resource<FindCity>> find(String city) {
        return mRepository.find(city);
    }

    public void create(Cities cities) {
        mCityRepository.create(cities).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                get();
            }
        });

    }
}
