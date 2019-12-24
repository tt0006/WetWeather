package com.example.wetweather;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherDao;
import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class WeatherRepository {

    private WeatherDao mDao;
    private LiveData<List<WeatherItem>> mWeatherItemsList;
    private static WeatherRepository sInstance;

    private WeatherRepository(Context context){
        WeatherDB db = WeatherDB.getInstance(context);
        mDao = db.weatherDao();
        mWeatherItemsList = mDao.loadMainWeather();
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
        return mWeatherItemsList;
    }

    public void insertData(List<WeatherItem> weatherListArray){

        if (weatherListArray.size()>0)
            mDao.deleteAll();

        for (WeatherItem item: weatherListArray){
            mDao.insertWeatherItem(item);
        }
    }
}
