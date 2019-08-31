package com.example.wetweather.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM WeatherData")
    LiveData<List<WeatherItem>> loadAll();

    @Insert
    void insertWeatherItem(WeatherItem weatherItem);

    @Query("DELETE FROM WeatherData")
    void deleteAll();

    @Delete
    void deleteWeatherItem(WeatherItem weatherItem);
}
