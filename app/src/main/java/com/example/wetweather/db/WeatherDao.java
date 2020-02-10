package com.example.wetweather.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


/**
 * The data access object 'Dao' annotated class where we specify SQL queries and associate them
 * with method calls. It is a part of Room database implementation.
 */
@Dao
public interface WeatherDao {

    // load data for location main window
    @Query("SELECT * FROM WeatherData WHERE weatherType NOT IN (2, 4)")
    LiveData<List<WeatherItem>> loadMainWeather();

    // load current weather for widget
    @Query("SELECT * FROM WeatherData WHERE weatherType == 1")
    List<WeatherItem> loadCurrentWeather();

    @Query("SELECT * FROM WeatherData WHERE weatherType == 2")
    LiveData<List<WeatherItem>> loadHourlyWeather();

    @Query("SELECT * FROM WeatherData WHERE weatherType == 4")
    LiveData<List<WeatherItem>> loadAlerts();

    // populate db with new data
    @Insert
    void insertWeatherItem(WeatherItem weatherItem);

    // delete all data from db
    @Query("DELETE FROM WeatherData")
    void deleteAll();
}
