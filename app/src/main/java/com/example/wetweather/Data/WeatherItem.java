package com.example.wetweather.Data;

/**
Helper class to store weather data
 */
public class WeatherItem {

    private long mDateTimeMillis;
    private String mSummary;
    private String mIcon;
    private double mPressure;
    private double mHumidity;
    private double mWindSpeed;
    private int mWindDirection;
    private double mTemperature;

    public WeatherItem(long dateTimeMillis, String summary, String icon, double pressure, double humidity, double windSpeed, int windDirection, double temperature){
        mDateTimeMillis = dateTimeMillis;
        mSummary = summary;
        mIcon = icon;
        mPressure = pressure;
        mHumidity = humidity;
        mWindSpeed = windSpeed;
        mWindDirection = windDirection;
        mTemperature = temperature;
    }

    public long getDateTimeMillis(){
        return mDateTimeMillis;
    }

    public String getSummary() { return mSummary; }

    public String getIcon() { return mIcon; }

    public double getPressure() { return mPressure; }

    public double getHumidity() { return mHumidity; }

    public double getWindSpeed() { return mWindSpeed; }

    public int getWindDirection() { return mWindDirection; }

    public double getmTemperature() { return mTemperature; }
}
