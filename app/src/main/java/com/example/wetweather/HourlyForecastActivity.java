package com.example.wetweather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class HourlyForecastActivity extends AppCompatActivity {

    private static final String TAG = HourlyForecastActivity.class.getSimpleName();

    private HourlyForecastAdapter mForecastAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hourly_activity);

        mRecyclerView = findViewById(R.id.recyclerview_forecast_hourly);
        mLoadingIndicator = findViewById(R.id.loading_indicator_hourly);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mForecastAdapter = new HourlyForecastAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mForecastAdapter);

        showLoading();

        setupViewModel();
    }

    private void setupViewModel() {
        final HourlyViewModel viewModel = ViewModelProviders.of(this).get(HourlyViewModel.class);
        viewModel.getHourlyWeather().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                //viewModel.getHourlyWeather().removeObserver(this);
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mForecastAdapter.setWeatherData(weatherEntries);
                showWeatherDataView();
            }
        });
    }

    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
