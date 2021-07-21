package com.example.wetweather.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Part of Room database, entity is an annotated class that describes a database table.
 */
@Entity(tableName = "WeatherData")
public class WeatherItem {
    // fields represent table columns
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int weatherType;
    private long dateTimeInSeconds;
    private String summary;
    private String icon;
    private double pressure;
    private double humidity;
    private String precipIntensity;
    private String precipProbability;
    private String precipType;
    private long sunriseTime;
    private long sunsetTime;
    private String windSpeed;
    private String windGust;
    private int windDirection;
    private String moonPhase;
    private String dewPoint;
    private double cloudCover;
    private String uvIndex;
    private String visibility;
    private String ozone;
    private String temperatureHigh;
    private String temperatureLow;
    private String apparentTemperatureHigh;
    private String apparentTemperatureLow;
    private String apparentTemperature;
    private String temperature;
    private boolean expanded;

    /**
     * Constructor for currently weather, daily and hourly forecast objects.
     */
    @Ignore
    public WeatherItem(int weatherType, long dateTimeInSeconds, String summary, String icon, double pressure,
                       double humidity, String precipIntensity, String precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, String windSpeed,
                       String windGust, int windDirection, String moonPhase, String dewPoint,
                       double cloudCover, String uvIndex, String visibility, String ozone,
                       String temperatureHigh, String temperatureLow, String apparentTemperatureHigh,
                       String apparentTemperatureLow, String apparentTemperature, String temperature) {
        this.weatherType = weatherType;
        this.dateTimeInSeconds = dateTimeInSeconds;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windDirection = windDirection;
        this.moonPhase = moonPhase;
        this.dewPoint = dewPoint;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.visibility = visibility;
        this.ozone = ozone;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.apparentTemperatureHigh = apparentTemperatureHigh;
        this.apparentTemperatureLow = apparentTemperatureLow;
        this.apparentTemperature = apparentTemperature;
        this.temperature = temperature;
    }

    public WeatherItem(int id, int weatherType, long dateTimeInSeconds, String summary, String icon, double pressure,
                       double humidity, String precipIntensity, String precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, String windSpeed,
                       String windGust, int windDirection, String moonPhase, String dewPoint,
                       double cloudCover, String uvIndex, String visibility, String ozone,
                       String temperatureHigh, String temperatureLow, String apparentTemperatureHigh,
                       String apparentTemperatureLow, String apparentTemperature, String temperature) {
        this.id = id;
        this.weatherType = weatherType;
        this.dateTimeInSeconds = dateTimeInSeconds;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windDirection = windDirection;
        this.moonPhase = moonPhase;
        this.dewPoint = dewPoint;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.visibility = visibility;
        this.ozone = ozone;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.apparentTemperatureHigh = apparentTemperatureHigh;
        this.apparentTemperatureLow = apparentTemperatureLow;
        this.apparentTemperature = apparentTemperature;
        this.temperature = temperature;
    }

    /**
     * Constructor for info and alert summary objects
     */
    @Ignore
    public WeatherItem(int weatherType, String summary, String icon) {
        this.weatherType = weatherType;
        this.summary = summary;
        this.icon = icon;
    }

    /**
     * Constructor for alerts objects
     */
    @Ignore
    public WeatherItem(int weatherType, String title, String severity, long time, long expires,
                       String description, String uri) {
        this.weatherType = weatherType;
        this.dewPoint = title;
        this.moonPhase = severity;
        this.dateTimeInSeconds = time;
        this.sunriseTime = expires;
        this.summary = description;
        this.precipType = uri;
    }


    // public getters and setters for fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateTimeInSeconds() {
        return dateTimeInSeconds;
    }

    public void setDateTimeInSeconds(long dateTimeInSeconds) {
        this.dateTimeInSeconds = dateTimeInSeconds;
    }

    public long getAlertStartTime() {
        return dateTimeInSeconds;
    }

    public long getAlertEntTime() {
        return sunriseTime;
    }

    public String getAlertTitle() {
        return dewPoint;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(String precipIntensity) { this.precipIntensity = precipIntensity; }

    public String getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(String precipProbability) { this.precipProbability = precipProbability; }

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getWindSpeed() { return windSpeed; }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean state) {
        this.expanded = state;
    }

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int value) { this.weatherType = value; }

    public String getWindGust() { return windGust; }

    public void setWindGust(String value) { this.windGust = value; }

    public String getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(String moonPhase) {
        this.moonPhase = moonPhase;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public String getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(String temperatureHigh) { this.temperatureHigh = temperatureHigh; }

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(String temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public String getApparentTemperatureHigh() {
        return apparentTemperatureHigh;
    }

    public void setApparentTemperatureHigh(String apparentTemperatureHigh) { this.apparentTemperatureHigh = apparentTemperatureHigh; }

    public String getApparentTemperatureLow() {
        return apparentTemperatureLow;
    }

    public void setApparentTemperatureLow(String apparentTemperatureLow) { this.apparentTemperatureLow = apparentTemperatureLow; }

    public String getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(String apparentTemperature) { this.apparentTemperature = apparentTemperature; }

    public String getDewPoint(){ return dewPoint; }

    public void setDewPoint(String value){ dewPoint = value; }
}
