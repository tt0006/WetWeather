package com.example.wetweather;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WetWeatherUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity responsible for building Graphs
 */
public class GraphsActivity extends AppCompatActivity {

    private LineChart mTemperatureGraph, mPressureGraph, mPrecipitationGraph, mHumidityGraph;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        mTemperatureGraph = findViewById(R.id.tempGraph);
        mPressureGraph = findViewById(R.id.pressureGraph);
        mPrecipitationGraph = findViewById(R.id.precipGraph);
        mHumidityGraph = findViewById(R.id.humidityGraph);
        mContext = this;

        setupViewModel();
    }

    private void setupViewModel() {
        final GraphsViewModel viewModel = new ViewModelProvider(this).get(GraphsViewModel.class);
        viewModel.getGraphs().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                plotGraph(weatherEntries);
            }
        });
    }

    private void plotGraph(List<WeatherItem> weatherEntries) {

        if (weatherEntries == null) {
            return;
        }

        //--- Data section common
        List<Entry> highTempEntries = new ArrayList<>();
        List<Entry> lowTempEntries = new ArrayList<>();
        List<Entry> apparentLowTempEntries = new ArrayList<>();
        List<Entry> apparentHighTempEntries = new ArrayList<>();
        List<Entry> pressureEntries = new ArrayList<>();
        List<Entry> precipitationEntries = new ArrayList<>();
        List<Entry> humidityEntries = new ArrayList<>();

        for (WeatherItem item : weatherEntries) {
            highTempEntries.add(new Entry(item.getDateTimeInSeconds(), Float.parseFloat(item.getTemperatureHigh())));
            apparentHighTempEntries.add(new Entry(item.getDateTimeInSeconds(), Float.parseFloat(item.getApparentTemperatureHigh())));
            lowTempEntries.add(new Entry(item.getDateTimeInSeconds(), Float.parseFloat(item.getTemperatureLow())));
            apparentLowTempEntries.add(new Entry(item.getDateTimeInSeconds(), Float.parseFloat(item.getApparentTemperatureLow())));
            pressureEntries.add(new Entry(item.getDateTimeInSeconds(), (float) item.getPressure()));
            precipitationEntries.add(new Entry(item.getDateTimeInSeconds(), Float.parseFloat(item.getPrecipIntensity())));
            humidityEntries.add(new Entry(item.getDateTimeInSeconds(), (float) item.getHumidity()));
        }

        LineDataSet highTempDataSet = new LineDataSet(highTempEntries, mContext.getString(
                R.string.day_temperature_label));
        //highTempDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        highTempDataSet.setColor(Color.YELLOW);
        highTempDataSet.setDrawCircles(false);
        highTempDataSet.setDrawValues(false);

        LineDataSet apparentHighTempDataSet = new LineDataSet(apparentHighTempEntries, mContext.getString(
                R.string.day_apparent_temperature_label));
        apparentHighTempDataSet.setColor(Color.WHITE);
        apparentHighTempDataSet.setDrawCircles(false);
        apparentHighTempDataSet.setDrawValues(false);

        LineDataSet lowTempDataSet = new LineDataSet(lowTempEntries, mContext.getString(
                R.string.night_temperature_label));
        lowTempDataSet.setColor(Color.CYAN);
        lowTempDataSet.setDrawCircles(false);
        lowTempDataSet.setDrawValues(false);

        LineDataSet apparentLowTempDataSet = new LineDataSet(apparentLowTempEntries, mContext.getString(
                R.string.night_apparent_temperature_label));
        apparentLowTempDataSet.setDrawCircles(false);
        apparentLowTempDataSet.setDrawValues(false);

        LineDataSet pressureDataSet = new LineDataSet(pressureEntries, mContext.getString(
                R.string.graph_pressure_description_label));
        pressureDataSet.setColor(Color.YELLOW);
        pressureDataSet.setDrawCircles(false);
        pressureDataSet.setDrawValues(false);

        LineDataSet precipDataSet = new LineDataSet(precipitationEntries, mContext.getString(
                R.string.graph_precip_description_label));
        precipDataSet.setColor(Color.CYAN);
        precipDataSet.setDrawCircles(false);
        precipDataSet.setDrawValues(false);

        LineDataSet humidityDataSet = new LineDataSet(humidityEntries, mContext.getString(
                R.string.graph_humidity_description_label));
        humidityDataSet.setDrawCircles(false);
        humidityDataSet.setDrawValues(false);

        List<ILineDataSet> tempDataSets = new ArrayList<>();
        tempDataSets.add(highTempDataSet);
        tempDataSets.add(apparentHighTempDataSet);
        tempDataSets.add(lowTempDataSet);
        tempDataSets.add(apparentLowTempDataSet);

        LineData tempData = new LineData(tempDataSets);

        mTemperatureGraph.setData(tempData);
        mTemperatureGraph.setTouchEnabled(false);
        mPressureGraph.setData(new LineData(pressureDataSet));
        mPressureGraph.setTouchEnabled(false);
        mPrecipitationGraph.setData(new LineData(precipDataSet));
        mPrecipitationGraph.setTouchEnabled(false);
        mHumidityGraph.setData(new LineData(humidityDataSet));
        mHumidityGraph.setTouchEnabled(false);
        //--- Data section end

        ValueFormatter dateFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return WetWeatherUtils.getDayOfWeek(mContext, (long) value);
            }
        };

        ValueFormatter tempFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return mContext.getString(R.string.format_temperature, value);
            }
        };

        ValueFormatter pressureFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return mContext.getString(R.string.format_pressure_graph, value);
            }
        };

        ValueFormatter precipFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return mContext.getString(R.string.format_percip_intens_graph, value);
            }
        };

        ValueFormatter humidityFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return mContext.getString(R.string.format_percent_value, value*100);
            }
        };


        //----Styling temperature
        Description tempDescription = mTemperatureGraph.getDescription();
        tempDescription.setEnabled(false);

        Legend tempLegend = mTemperatureGraph.getLegend();
        tempLegend.setTextColor(Color.WHITE);
        tempLegend.setTextSize(12f);
        tempLegend.setXEntrySpace(20f);
        tempLegend.setForm(Legend.LegendForm.LINE);
        tempLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        XAxis tempXAxis = mTemperatureGraph.getXAxis();
        tempXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        tempXAxis.setValueFormatter(dateFormatter);
        //tempXAxis.setAxisLineWidth(3);
        tempXAxis.setTextColor(Color.WHITE);
        tempXAxis.setTextSize(12f);
        tempXAxis.setLabelCount(weatherEntries.size(), true);

        YAxis tempYAxisLeft = mTemperatureGraph.getAxisLeft();
        //tempYAxisLeft.setAxisMinimum(0);
        //tempYAxisLeft.setDrawZeroLine(true);
        tempYAxisLeft.setTextColor(Color.WHITE);
        //tempYAxisLeft.setAxisLineWidth(3);
        tempYAxisLeft.setTextSize(12f);
        tempYAxisLeft.setValueFormatter(tempFormatter);

        YAxis tempYAxisRight = mTemperatureGraph.getAxisRight();
        tempYAxisRight.setEnabled(false);

        mTemperatureGraph.invalidate(); // refresh
        //----Styling temperature end

        //----Styling pressure
        Description pressureDescription = mPressureGraph.getDescription();
        pressureDescription.setEnabled(false);

        Legend pressureLegend = mPressureGraph.getLegend();
        pressureLegend.setTextColor(Color.WHITE);
        pressureLegend.setEnabled(false);

        XAxis pressureXAxis = mPressureGraph.getXAxis();
        //xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        pressureXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        pressureXAxis.setValueFormatter(dateFormatter);
        //pressureXAxis.setAxisLineWidth(3);
        pressureXAxis.setTextColor(Color.WHITE);
        pressureXAxis.setTextSize(12f);
        pressureXAxis.setLabelCount(weatherEntries.size(), true);

        YAxis pressureYAxisLeft = mPressureGraph.getAxisLeft();
        pressureYAxisLeft.setTextColor(Color.WHITE);
        //pressureYAxisLeft.setAxisLineWidth(3);
        pressureYAxisLeft.setTextSize(12f);
        pressureYAxisLeft.setValueFormatter(pressureFormatter);

        YAxis pressureYAxisRight = mPressureGraph.getAxisRight();
        pressureYAxisRight.setEnabled(false);

        mPressureGraph.invalidate(); // refresh
        //----Styling pressure end

        //----Styling precipitation
        Description precipDescription = mPrecipitationGraph.getDescription();
        precipDescription.setEnabled(false);

        Legend precipLegend = mPrecipitationGraph.getLegend();
        precipLegend.setEnabled(false);
        precipLegend.setTextColor(Color.WHITE);

        XAxis precipXAxis = mPrecipitationGraph.getXAxis();
        precipXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        precipXAxis.setValueFormatter(dateFormatter);
        //precipXAxis.setAxisLineWidth(3);
        precipXAxis.setTextSize(12f);
        precipXAxis.setTextColor(Color.WHITE);
        precipXAxis.setLabelCount(weatherEntries.size(), true);

        YAxis precipYAxisLeft = mPrecipitationGraph.getAxisLeft();
        precipYAxisLeft.setAxisMinimum(0);
        precipYAxisLeft.setTextColor(Color.WHITE);
        //precipYAxisLeft.setAxisLineWidth(3);
        precipYAxisLeft.setTextSize(12f);
        precipYAxisLeft.setValueFormatter(precipFormatter);
        precipYAxisLeft.setDrawZeroLine(true);
        precipYAxisLeft.setGranularity(0.2f);

        YAxis precipYAxisRight = mPrecipitationGraph.getAxisRight();
        precipYAxisRight.setEnabled(false);

        mPrecipitationGraph.invalidate(); // refresh
        //----Styling precipitation end

        //----Styling humidity
        Description humidityDescription = mHumidityGraph.getDescription();
        humidityDescription.setEnabled(false);

        Legend humidityLegend = mHumidityGraph.getLegend();
        humidityLegend.setEnabled(false);
        humidityLegend.setTextColor(Color.WHITE);

        XAxis humidityXAxis = mHumidityGraph.getXAxis();
        humidityXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        humidityXAxis.setValueFormatter(dateFormatter);
        //humidityXAxis.setAxisLineWidth(3);
        humidityXAxis.setTextSize(12f);
        humidityXAxis.setTextColor(Color.WHITE);
        humidityXAxis.setLabelCount(weatherEntries.size(), true);

        YAxis humidityYAxisLeft = mHumidityGraph.getAxisLeft();
        humidityYAxisLeft.setTextColor(Color.WHITE);
        //humidityYAxisLeft.setAxisLineWidth(3);
        humidityYAxisLeft.setTextSize(12f);
        humidityYAxisLeft.setValueFormatter(humidityFormatter);

        YAxis humidityYAxisRight = mHumidityGraph.getAxisRight();
        humidityYAxisRight.setEnabled(false);

        mHumidityGraph.invalidate(); // refresh
        //----Styling humidity end

    }
}
