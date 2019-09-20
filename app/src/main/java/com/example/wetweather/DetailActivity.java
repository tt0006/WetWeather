package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        final int position = intent.getIntExtra("POSITION", 0);

        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
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

        //set icon
        ImageView icon = findViewById(R.id.weather_icon);
        icon.setImageResource(WetWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherForThisDay.getIcon()));

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

        //set humidity
        TextView humidity = findViewById(R.id.humidity);
        String humidityString = getString(R.string.format_percent_value, weatherForThisDay.getHumidity()*100);
        humidity.setText(humidityString);

        //set pressure
        TextView pressure = findViewById(R.id.pressure);
        String pressureString = getString(R.string.format_pressure, weatherForThisDay.getPressure());
        pressure.setText(pressureString);

        //set wind
        TextView wind = findViewById(R.id.wind_measurement);
        String windString = WetWeatherUtils.getFormattedWind(this,
                weatherForThisDay.getWindSpeed(), weatherForThisDay.getWindDirection());
        wind.setText(windString);

        //set cloud cover
        TextView cloudCoverView = findViewById(R.id.cloud_cover_measurement);
        String cloudCoverText = getString(R.string.format_percent_value, weatherForThisDay.cloudCover*100);
        cloudCoverView.setText(cloudCoverText);

        //set rain prob
        TextView rainProb = findViewById(R.id.percip_prob_measurement);
        String rainPr = getString(R.string.format_percent_value,
                weatherForThisDay.getPrecipProbability()*100);
        rainProb.setText(rainPr);

        //set rain intens
        TextView rainIntens = findViewById(R.id.percip_inten_measurement);
        String rainIn = getString(R.string.format_percip_intens, weatherForThisDay.getPrecipIntensity());
        rainIntens.setText(rainIn);

        //set sunrise
        TextView sunrise = findViewById(R.id.sunrise_measurement);
        String sunriseTime = WetWeatherUtils.getTime(this, weatherForThisDay.getSunriseTime());
        sunrise.setText(sunriseTime);

        //setSunset
        TextView sunset = findViewById(R.id.sunset_measurement);
        String sunsetTime = WetWeatherUtils.getTime(this, weatherForThisDay.getSunsetTime());
        sunset.setText(sunsetTime);

        //set uvindex
        TextView uvindexView = findViewById(R.id.uvindex_measurement);
        String uvindexText = weatherForThisDay.uvIndex;
        uvindexView.setText(uvindexText);

        //set visibility
        TextView visibilityView = findViewById(R.id.visibility_measurement);
        String visibilityText = getString(R.string.format_visibility, weatherForThisDay.visibility);
        visibilityView.setText(visibilityText);

    }
}
