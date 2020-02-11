package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.WetWeatherPreferences;

import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = LocationActivity.class.getSimpleName();

    private LocationAdapter mLocationAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        //set activity title using location name
        setTitle(WetWeatherPreferences.getPreferencesLocationName(this));

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mLocationAdapter = new LocationAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mLocationAdapter);

        showLoading();

        setupViewModel();
    }

    private void setupViewModel() {
        LocationViewModel viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                mLocationAdapter.setWeatherData(weatherEntries);
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

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
