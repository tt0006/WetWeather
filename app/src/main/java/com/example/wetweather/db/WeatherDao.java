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

    @Query("SELECT * FROM WeatherData WHERE weatherType != 1")
    LiveData<List<WeatherItem>> loadMainWeather();

    @Query("SELECT * FROM WeatherData WHERE weatherType == 0")
    List<WeatherItem> loadCurrentWeather();

    @Query("SELECT * FROM WeatherData WHERE weatherType == 1")
    LiveData<List<WeatherItem>> loadHourlyWeather();

    @Insert
    void insertWeatherItem(WeatherItem weatherItem);

    @Query("DELETE FROM WeatherData")
    void deleteAll();
}
