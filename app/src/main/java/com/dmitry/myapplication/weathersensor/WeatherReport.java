package com.dmitry.myapplication.weathersensor;

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
