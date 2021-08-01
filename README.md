# Wet Weather Android app

This repository contains the source code for my pet Android project - weather app. I started this project because I wanted to learn Android frameworks and Java while creating something that I will use every day.

## Example Screenshots

The app contains several panels, preferences and widgets.

### Weather forecast panels

Main panel shows current weather for selected location with more data inside.

<table>
  <tr>
    <td><image src="screenshots/main_panel.jpg" /></td>
    <td><image src="screenshots/location_weather.jpg" /></td>
    <td><image src="screenshots/location_weather_tomorrow_day_expanded.jpg" /></td>
    <td><image src="screenshots/location_weather_tomorrow_hourly_forecast_expanded.jpg" /></td>
  </tr>
</table>

### Graphs

Temperature, Precipitation intensity, Pressure and Humidity forecasts are available as graphs. 

<table>
  <tr>
    <td><image src="screenshots/graphs1.jpg" /></td>
    <td><image src="screenshots/graphs2.jpg" /></td>
  </tr>
</table>

### Alerts

Sometimes weather data contains alerts for exceptional weather conditions.

<table>
  <tr>
    <td><image src="screenshots/warning_location_weather.jpg" /></td>
    <td><image src="screenshots/warning_details.jpg" /></td>
  </tr>
</table>

### Widgets

Widgets display weather on device's home screen. Depends on weather condition widget shows precipitation intensity if it is raining/snowing or UV Index if it is not.

<table>
  <tr>
    <td><image src="screenshots/widget_precipitation_intensity.jpg" /></td>
    <td><image src="screenshots/widget_uv_index.jpg" /></td>
  </tr>
</table>

### Change weather location

You can change location from app main panel with 'Change Location' button. 

<table>
  <tr>
    <td><image src="screenshots/main_panel.jpg" /></td>
    <td><image src="screenshots/change_location_panel.jpg" /></td>
  </tr>
</table>

### Weather Preferences

You can change Language, Units, set or change Update Interval and Weather Provider in Preferences.

<table>
  <tr>
    <td><image src="screenshots/preferences.jpg" /></td>
    <td><image src="screenshots/preferences_update_interval.jpg" /></td>
    <td><image src="screenshots/preferences_weather_providers.jpg" /></td>
  </tr>
</table>


### Installation

The app is not available in Play Store at the moment. To run it you will need api key for DarkSky, OpenWeather or/and Tomorrow.io v4. Download source code and open it in Android Studio, replace 'Demo' with your api key and run the app.
