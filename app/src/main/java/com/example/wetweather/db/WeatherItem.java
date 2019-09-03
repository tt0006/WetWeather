package com.example.wetweather.db;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * This class defines db structure
 */
@Entity(tableName = "WeatherData")
public class WeatherItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long dateTimeMillis;
    private String summary;
    private String icon;
    private double pressure;
    private double humidity;
    private double precipIntensity;
    private double precipProbability;
    private String precipType;
    private long sunriseTime;
    private long sunsetTime;
    private double windSpeed;
    private int windDirection;
    private String temperature;

    @Ignore
    public WeatherItem(long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double precipIntensity, double precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, double windSpeed,
                       int windDirection, String temperature){
        this.dateTimeMillis = dateTimeMillis;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.sunriseTime = sunriseTime;
        this.sunsetTime=sunsetTime;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.temperature = temperature;
    }

    public WeatherItem(int id, long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double precipIntensity, double precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, double windSpeed,
                       int windDirection, String temperature){
        this.id = id;
        this.dateTimeMillis = dateTimeMillis;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.sunriseTime = sunriseTime;
        this.sunsetTime=sunsetTime;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public long getDateTimeMillis() {return dateTimeMillis;}
    public void setDateTimeMillis(long dateTimeMillis) {this.dateTimeMillis = dateTimeMillis;}

    public String getSummary() {return summary;}
    public void setSummary(String summary) {this.summary = summary;}

    public String getIcon() {return icon;}
    public void setIcon(String icon) {this.icon = icon;}

    public double getPressure() {return pressure;}
    public void setPressure(double pressure) {this.pressure = pressure;}

    public double getHumidity() {return humidity;}
    public void setHumidity(double humidity){this.humidity = humidity;}

    public double getPrecipIntensity() {return precipIntensity;}
    public void setPrecipIntensity(double precipIntensity){this.precipIntensity = precipIntensity;}

    public double getPrecipProbability() {return precipProbability;}
    public void setPrecipProbability(double precipProbability){this.precipProbability = precipProbability;}

    public String getPrecipType(){return precipType;}
    public void  setPrecipType(String precipType){this.precipType = precipType;}

    public long getSunriseTime(){return sunriseTime;}
    public void setSunriseTime(long sunriseTime){this.sunriseTime = sunriseTime;}

    public long getSunsetTime(){return sunsetTime;}
    public void setSunsetTime(long sunsetTime){this.sunsetTime = sunsetTime;}

    public double getWindSpeed() {return windSpeed;}
    public void setWindSpeed(double windSpeed) {this.windSpeed = windSpeed;}

    public int getWindDirection() {return windDirection;}
    public void setWindDirection(int windDirection){this.windDirection = windDirection;}

    public String getTemperature() {return temperature;}
    public void setTemperature(String temperature){this.temperature = temperature;}
}
