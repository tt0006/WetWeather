package com.example.wetweather.db;

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
    private double windSpeed;
    private int windDirection;
    private double temperature;

    @Ignore
    public WeatherItem(long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double windSpeed, int windDirection, double temperature){
        this.dateTimeMillis = dateTimeMillis;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.temperature = temperature;
    }

    public WeatherItem(int id, long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double windSpeed, int windDirection, double temperature){
        this.id = id;
        this.dateTimeMillis = dateTimeMillis;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
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

    public double getWindSpeed() {return windSpeed;}
    public void setWindSpeed(double windSpeed) {this.windSpeed = windSpeed;}

    public int getWindDirection() {return windDirection;}
    public void setWindDirection(int windDirection){this.windDirection = windDirection;}

    public double getTemperature() {return temperature;}
    public void setTemperature(double temperature){this.temperature = temperature;}
}
