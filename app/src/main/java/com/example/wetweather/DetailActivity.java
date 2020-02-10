package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_forecast_today);
        Intent intent = getIntent();

        final int position = intent.getIntExtra("POSITION", 0);

        final LocationViewModel viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                viewModel.getWeathers().removeObserver(this);
                if (weatherEntries != null){
                displayDetails(weatherEntries.get(position));
                }
            }
        });
    }

    private void displayDetails(WeatherItem weatherForThisDay){
        if (weatherForThisDay == null){
            return;}
        //make view height match parent
        View entireLayout = findViewById(R.id.entire_details_layout);
        entireLayout.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

        //set icon
        ImageView icon = findViewById(R.id.weather_icon);
        icon.setImageResource(WetWeatherUtils.getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon()));

        //set Date
        TextView date = findViewById(R.id.date);
        date.setText(WetWeatherUtils.getDayName(this, weatherForThisDay.getDateTimeMillis()));

        //set description
        TextView description = findViewById(R.id.weather_description);
        description.setText(weatherForThisDay.getSummary());

        //set high temperature
        TextView hTemperature = findViewById(R.id.high_temperature);
        String hTemper = weatherForThisDay.temperatureHigh;
        if (hTemper.length() == 0){
            hTemper = weatherForThisDay.getTemperature();
        }
        hTemperature.setText(WetWeatherUtils.formatTemperature(this, hTemper));

        //set low temperature
        TextView lTemperature = findViewById(R.id.low_temperature);
        String lTemper = weatherForThisDay.temperatureLow;
        if (lTemper.length() == 0){
            lTemper = weatherForThisDay.apparentTemperature;
        }
        lTemperature.setText(WetWeatherUtils.formatTemperature(this, lTemper));

        //set rain prob
        TextView rainProb = findViewById(R.id.rain_details_probability);
        rainProb.setText(getString(R.string.format_percent_value,
                weatherForThisDay.getPrecipProbability()*100));

        //set rain intens
        TextView rainIntens = findViewById(R.id.rain_details_intensity);
        rainIntens.setText(getString(R.string.format_percip_intens, weatherForThisDay.getPrecipIntensity()));

        //set wind
        ImageView windIcon = findViewById(R.id.wind_icon);
        TextView windSpeed = findViewById(R.id.wind_details_speed);
        TextView windGustDetails = findViewById(R.id.wind_details_gust);
        windSpeed.setText(getString(R.string.format_wind_speed,
                weatherForThisDay.getWindSpeed()));
        windIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
        windGustDetails.setText(String.format("%1$s %2$s",
                weatherForThisDay.windGust, getString(R.string.speed_ms_label)));

        //set clouds
        TextView cloudsDetailsValue = findViewById(R.id.cloud_details);
        cloudsDetailsValue.setText(getString(R.string.format_percent_value,
                weatherForThisDay.cloudCover * 100));

        //set humidity
        TextView humidityDetailsValue = findViewById(R.id.humidity_details_value);
        humidityDetailsValue.setText(getString(R.string.format_percent_value,
                weatherForThisDay.getHumidity() * 100));

        //set pressure
        TextView pressureDetailsValue = findViewById(R.id.pressure_details_value);
        pressureDetailsValue.setText(getString(R.string.format_pressure,
                weatherForThisDay.getPressure()));

        //set sunrise
        TextView sunrise = findViewById(R.id.sunrise_value);
        sunrise.setText(WetWeatherUtils.getTime(this, weatherForThisDay.getSunriseTime()));

        //setSunset
        TextView sunset = findViewById(R.id.sunset_value);
        sunset.setText(WetWeatherUtils.getTime(this, weatherForThisDay.getSunsetTime()));

        //set uvindex
        TextView uvindexView = findViewById(R.id.uv_index_details_value);
        uvindexView.setText(weatherForThisDay.uvIndex);

        //set dew point
        TextView dewPointValue = findViewById(R.id.dew_point_details_value);
        dewPointValue.setText(WetWeatherUtils.formatTemperature(this, weatherForThisDay.dewPoint));

        //set visibility
        TextView visibilityView = findViewById(R.id.visibility_details_value);
        visibilityView.setText(getString(R.string.format_visibility, weatherForThisDay.visibility));
    }
}
