package com.example.wetweather;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class HourlyViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<WeatherItem>> weathers;

    public HourlyViewModel(Application application) {
        super(application);
        WeatherDB database = WeatherDB.getInstance(this.getApplication());
        Log.i(TAG, "Actively retrieving hourly weather from the DataBase");
        weathers = database.weatherDao().loadHourlyWeather();
    }

    public LiveData<List<WeatherItem>> getHourlyWeather() {
        return weathers;
    }
}
