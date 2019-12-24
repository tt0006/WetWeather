package com.example.wetweather;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<WeatherItem>> mWeathers;
    private WeatherRepository mRepository;

    public MainViewModel(Application application) {
        super(application);
        mRepository = WeatherRepository.getInstance(application.getApplicationContext());
        mWeathers = mRepository.getMainWeather();
    }

    public LiveData<List<WeatherItem>> getWeathers() {
        return mWeathers;
    }
}
