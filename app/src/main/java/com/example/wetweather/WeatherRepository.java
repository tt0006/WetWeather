package com.example.wetweather;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherDao;
import com.example.wetweather.db.WeatherItem;

import java.util.List;

/**
 * Repository is a class that abstracts access to multiple data sources.
 * Used for code separation and architecture. A Repository class handles data operations.
 * It provides a clean API to the rest of the app for app data.
 */
public class WeatherRepository {

    private WeatherDao mDao;
    private LiveData<List<WeatherItem>> mMainWeatherList, mHourlyWeatherList, mAlertsList;
    private static WeatherRepository sInstance;

    private WeatherRepository(Context context){
        WeatherDB db = WeatherDB.getInstance(context);
        mDao = db.weatherDao();
        mMainWeatherList = mDao.loadMainWeather();
        mHourlyWeatherList = mDao.loadHourlyWeather();
        mAlertsList = mDao.loadAlerts();
    }

    public static WeatherRepository getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (WeatherRepository.class) {
                if (sInstance == null) {
                    sInstance = new WeatherRepository(context);
                }
            }
        }
        return sInstance;
    }

    LiveData<List<WeatherItem>> getMainWeather(){
        return mMainWeatherList;
    }

    LiveData<List<WeatherItem>> getHourlyWeather(){
        return mHourlyWeatherList;
    }

    LiveData<List<WeatherItem>> getAlerts(){
        return mAlertsList;
    }

    /**
     * Method to get weather for specific date with daily and hourly forecast for that day.
     *
     * @param minTime 00:00 of the date in seconds from epoch
     * @param maxTime 23:00 of the date in seconds from epoch
     * @return LiveData list of WeatherItems objects
     */
    LiveData<List<WeatherItem>> getDetails(long minTime, long maxTime){
        return mDao.loadDetailsWeather(minTime, maxTime);
    }

    /**
     * Method to get daily weather data for graphs
     *
     * @return LiveData list of WeatherItems objects
     */
    LiveData<List<WeatherItem>> getDailyData(){
        return mDao.loadDailyData();
    }

    public void insertData(List<WeatherItem> weatherListArray){

        if (weatherListArray.size()>0)
            mDao.deleteAll();

        for (WeatherItem item: weatherListArray){
            mDao.insertWeatherItem(item);
        }
    }
}
