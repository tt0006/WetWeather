package com.example.wetweather;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class HourlyViewModel extends AndroidViewModel {
    private LiveData<List<WeatherItem>> mWeathers;

    public HourlyViewModel(Application application) {
        super(application);
        WeatherRepository repository = WeatherRepository.getInstance(application.getApplicationContext());
        mWeathers = repository.getHourlyWeather();
    }

    public LiveData<List<WeatherItem>> getHourlyWeather() {
        return mWeathers;
    }
}
