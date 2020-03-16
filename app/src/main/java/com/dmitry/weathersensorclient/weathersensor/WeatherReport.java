package com.dmitry.weathersensorclient.weathersensor;

import java.util.Date;

public class WeatherReport
{
    private SensorData[] sensorDataHistory;
    private WeatherState weatherForecast;
    private String temperatureUnits = "°C";
    private String humidityUnits = "%";
    private String pressureUnits = "kPa";

    public SensorData elementAt(int i)
    {
        return sensorDataHistory[i];
    }

    public double pressureAt(int i){return sensorDataHistory[i].getPressure();}
    public double temperatureAt(int i){return sensorDataHistory[i].getTemperature();}
    public double humidityAt(int i){return sensorDataHistory[i].getHumidity();}

    public double[] getPressureHistory()
    {
        double[] pressure = new double[sensorDataHistory.length];
        for(int i = 0; i < sensorDataHistory.length; i++)
        {
            pressure[i] = sensorDataHistory[i].getPressure();
        }
        return pressure;
    }

    public double[] getTemperatureHistory()
    {
        double[] temperature = new double[sensorDataHistory.length];
        for(int i = 0; i < sensorDataHistory.length; i++)
        {
            temperature[i] = sensorDataHistory[i].getTemperature();
        }
        return temperature;
    }

    public double[] getHumidityHistory()
    {
        double[] humidity = new double[sensorDataHistory.length];
        for(int i = 0; i < sensorDataHistory.length; i++)
        {
            humidity[i] = sensorDataHistory[i].getHumidity();
        }
        return humidity;
    }

    public Date[] getTimestamps()
    {
        Date[] time = new Date[sensorDataHistory.length];
        for(int i = 0; i < sensorDataHistory.length; i++)
        {
            time[i] = sensorDataHistory[i].getTimestamp();
        }
        return time;
    }

    public int getHistoryLength()
    {
        return sensorDataHistory.length;
    }

    public SensorData[] extractArray()
    {
        return sensorDataHistory;
    }

    public WeatherReport(SensorData[] history, WeatherState weatherForecast)
    {
        sensorDataHistory = history;
        this.weatherForecast = weatherForecast;
    }

    public WeatherReport(SensorData[] history, WeatherState weatherForecast, String tUnits, String hUnits, String pUnits)
    {
        sensorDataHistory = history;
        this.weatherForecast = weatherForecast;
        temperatureUnits = tUnits;
        humidityUnits = hUnits;
        pressureUnits = pUnits;
    }

    public SensorData getMostRecent()
    {
        return sensorDataHistory[sensorDataHistory.length - 1];
    }

    public WeatherState getWeatherForecast()
    {
        return weatherForecast;
    }

    public String getTemperatureUnits()
    {
        return temperatureUnits;
    }

    public String getHumidityUnits()
    {
        return humidityUnits;
    }

    public String getPressureUnits()
    {
        return pressureUnits;
    }
}
