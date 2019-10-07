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
    public int weatherType;
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
    public String windGust;
    private int windDirection;
    public String moonPhase;
    public String dewPoint;
    public double cloudCover;
    public String uvIndex;
    public double visibility;
    public double ozone;
    public String temperatureHigh;
    public String temperatureLow;
    public String apparentTemperatureHigh;
    public String apparentTemperatureLow;
    public String apparentTemperature;
    private String temperature;
    private boolean expanded;

    @Ignore
    public WeatherItem(int weatherType, long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double precipIntensity, double precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, double windSpeed,
                       String windGust, int windDirection, String moonPhase, String dewPoint,
                       double cloudCover, String uvIndex, double visibility, double ozone,
                       String temperatureHigh, String temperatureLow, String apparentTemperatureHigh,
                       String apparentTemperatureLow, String apparentTemperature, String temperature){
        this.weatherType = weatherType;
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

    public WeatherItem(int id, int weatherType, long dateTimeMillis, String summary, String icon, double pressure,
                       double humidity, double precipIntensity, double precipProbability,
                       String precipType, long sunriseTime, long sunsetTime, double windSpeed,
                       String windGust, int windDirection, String moonPhase, String dewPoint,
                       double cloudCover, String uvIndex, double visibility, double ozone,
                       String temperatureHigh, String temperatureLow, String apparentTemperatureHigh,
                       String apparentTemperatureLow, String apparentTemperature, String temperature){
        this.id = id;
        this.weatherType = weatherType;
        this.dateTimeMillis = dateTimeMillis;
        this.summary = summary;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.sunriseTime = sunriseTime;
        this.sunsetTime= sunsetTime;
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

    @Ignore
    public WeatherItem(String summary, String icon){
        this.summary = summary;
        this.icon = icon;
    }

    @Ignore
    public WeatherItem(int weatherType, String title, String severity, long time, long expires,
                       String description, String uri){
        this.weatherType = weatherType;
        this.dewPoint = title;
        this.moonPhase = severity;
        this.dateTimeMillis = time;
        this.sunriseTime = expires;
        this.summary = description;
        this.precipType = uri;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public long getDateTimeMillis() {return dateTimeMillis;}
    public void setDateTimeMillis(long dateTimeMillis) {this.dateTimeMillis = dateTimeMillis;}

    public long getAlertStartTime() {return  dateTimeMillis;}
    public long getAlertEntTime() {return sunriseTime;}
    public String getAlertTitle() {return dewPoint;}

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

    public boolean isExpanded() {return expanded;}
    public void setExpanded(boolean state){ this.expanded = state; }
}
