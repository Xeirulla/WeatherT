package com.bignerdranch.android.weather.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CityDAO {

    @android.arch.persistence.room.Query("SELECT * FROM city")
    LiveData<List<City>> getAllCity();

    @Insert
    void insert(City... city);

    @Update
    void update(City... city);

    @Delete
    void delete(City... city);


}
