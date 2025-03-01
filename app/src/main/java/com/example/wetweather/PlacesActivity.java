package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
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
import com.example.wetweather.utils.NetworkUtils;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = PlacesActivity.class.getSimpleName();
    private Context mActivityContext;
    private ProgressBar mLoadingIndicator;
    private View mEmbedded;
    private TextView mDefaultText;
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
                Intent addLocIntent = new Intent(PlacesActivity.this, AddLocationActivity.class);
                startActivity(addLocIntent);
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
        mDefaultText = findViewById(R.id.default_text);
        mActivityContext = this;
        final SwipeRefreshLayout swipeToRefresh = findViewById(R.id.swipeToRefresh);

        setupViewModel();

        if (NetworkUtils.isNetworkAvailable(mActivityContext)) {
            WeatherSyncUtils.initialize(mActivityContext);
        } else{
            Toast.makeText(mActivityContext, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        //set swipe to refresh
        swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (NetworkUtils.isNetworkAvailable(mActivityContext)) {
                            WeatherSyncUtils.startImmediateSync(mActivityContext);
                            showLoading();
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
        LocationViewModel viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                if (weatherEntries != null)
                setWeatherData(weatherEntries);
            }
        });
    }

    private void setWeatherData(List<WeatherItem> weatherEntries){

        if (weatherEntries.size() == 0) return;

        WeatherItem weatherForThisDay = weatherEntries.get(0);
        if (weatherForThisDay.getWeatherType() == 5 & weatherEntries.size() >1){
            weatherForThisDay = weatherEntries.get(1);
        }

        int weatherImageId = WetWeatherUtils
                .getResourceIconIdForWeatherCondition(mActivityContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
        locationView.setText(String.format("%1$s %2$s",
                WetWeatherPreferences.getPreferencesLocationName(mActivityContext),
                WetWeatherUtils.formatTemperature(mActivityContext, weatherForThisDay.getTemperature())));
        percipProb.setText(WetWeatherUtils.formatRainOrUVIndex(mActivityContext,
                weatherForThisDay.getPrecipIntensity(), weatherForThisDay.getUvIndex()));
        updated.setText(WetWeatherUtils.getUpdateTime(mActivityContext, weatherForThisDay.getDateTimeInSeconds()));
        feelsLike.setText(String.format("%1$s %2$s", mActivityContext.getString(R.string.feels_like_label),
                WetWeatherUtils.formatTemperature(mActivityContext, weatherForThisDay.getApparentTemperature())));
        iconView.setImageResource(weatherImageId);
        descriptionView.setText(weatherForThisDay.getSummary());
        showWeatherDataView();

    }

    /**
     * This method will make the weather View visible and hide loading indicator
     */
    private void showWeatherDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mDefaultText.setVisibility(View.INVISIBLE);
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
