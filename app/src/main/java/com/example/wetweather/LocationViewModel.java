package com.example.wetweather;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

/**
 * ViewModel is a class whose role is to provide data to the UI and survive configuration changes.
 * A ViewModel acts as a communication center between the Repository and the UI.
 */
public class LocationViewModel extends AndroidViewModel {

    private LiveData<List<WeatherItem>> mWeathers;

    public LocationViewModel(Application application) {
        super(application);
        WeatherRepository repository = WeatherRepository.getInstance(application.getApplicationContext());
        mWeathers = repository.getMainWeather();
    }

    public LiveData<List<WeatherItem>> getWeathers() {
        return mWeathers;
    }
}
