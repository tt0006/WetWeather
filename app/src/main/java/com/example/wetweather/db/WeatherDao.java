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
    LiveData<List<WeatherEntity>> loadAll();

    @Insert
    void insertWeatherItem(WeatherEntity weatherEntity);

    @Query("DELETE FROM WeatherData")
    void deleteAll();

    @Delete
    void deleteWeatherItem(WeatherEntity weatherEntity);
}
