package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.SettingsActivity;
import com.example.wetweather.prefs.WetWeatherPreferences;
import com.example.wetweather.sync.WeatherSyncUtils;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = PlacesActivity.class.getSimpleName();
    private Context mActivityContext;
    private ProgressBar mLoadingIndicator;
    private View mEmbedded;
    ImageView iconView;
    TextView locationView, descriptionView, updated, feelsLike, percipProb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Button addLocation = findViewById(R.id.add_location_button);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLocIntet = new Intent(PlacesActivity.this, AddLocationActivity.class);
                startActivity(addLocIntet);
            }
        });

        mEmbedded = findViewById(R.id.location_0);
        mEmbedded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showWeather = new Intent(PlacesActivity.this, LocationActivity.class);
                startActivity(showWeather);
            }
        });

        iconView = findViewById(R.id.weather_icon);
        locationView = findViewById(R.id.location);
        feelsLike = findViewById(R.id.feels_like);
        descriptionView = findViewById(R.id.weather_description);
        percipProb = findViewById(R.id.percip_prob);
        updated = findViewById(R.id.updated_at);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mActivityContext = this;
        final SwipeRefreshLayout swipeToRefresh = findViewById(R.id.swipeToRefresh);

        showLoading();

        setupViewModel();

        if (isNetworkAvailable()) {
            WeatherSyncUtils.initialize(this);
        } else{
            Toast.makeText(this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        //set swipe to refresh
        swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkAvailable()) {
                            WeatherSyncUtils.startImmediateSync(mActivityContext);
                        } else {
                            Toast.makeText(mActivityContext, R.string.network_not_available,
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeToRefresh.setRefreshing(false);
                    }
                }
        );
    }

    private void setupViewModel() {
        LocationViewModel viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                setWeatherData(weatherEntries);
                showWeatherDataView();
            }
        });
    }

    private void setWeatherData(List<WeatherItem> weatherEntries){

        if (weatherEntries.size() == 0) return;

        WeatherItem weatherForThisDay = weatherEntries.get(0);
        if (weatherForThisDay.weatherType == 5 & weatherEntries.size() >1){
            weatherForThisDay = weatherEntries.get(1);
        }

        int weatherImageId = WetWeatherUtils
                .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
        locationView.setText(String.format("%1$s %2$s",
                WetWeatherPreferences.getPreferencesLocationName(this),
                WetWeatherUtils.formatTemperature(this, weatherForThisDay.getTemperature())));
        percipProb.setText(String.format("%1$s %2$s", this.getString(R.string.hourly_rain_prob_label),
                this.getString(R.string.format_percent_value,
                weatherForThisDay.getPrecipProbability()*100)));
        updated.setText(WetWeatherUtils.getUpdateTime(this, weatherForThisDay.getDateTimeInSeconds()));
        feelsLike.setText(String.format("%1$s %2$s", this.getString(R.string.feels_like_label),
                WetWeatherUtils.formatTemperature(this, weatherForThisDay.apparentTemperature)));
        iconView.setImageResource(weatherImageId);
        descriptionView.setText(weatherForThisDay.getSummary());

    }

    //Helper method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * This method will make the the weather View visible and hide loading indicator
     */
    private void showWeatherDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mEmbedded.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the weather View
     */
    private void showLoading() {
        mEmbedded.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This is where we inflate and set up the menu for this Activity
     *
     * @param menu The options menu in which we place our items.
     * @return We must return true for the menu to be displayed;
     * if return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu
     *
     * @param item The menu item that was selected by the user
     * @return true if we handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
