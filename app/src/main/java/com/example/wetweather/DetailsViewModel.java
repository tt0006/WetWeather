package com.example.wetweather;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel {

    private WeatherRepository mRepository;

    public DetailsViewModel(Application application) {
        super(application);
        mRepository = WeatherRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<WeatherItem>> getDetailsWeather(long min, long max) {

        return mRepository.getDetails(min,max);
    }
}