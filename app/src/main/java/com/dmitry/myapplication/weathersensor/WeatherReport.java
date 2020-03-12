package com.dmitry.myapplication.weathersensor;

public class WeatherReport
{
    private SensorData[] sensorDataHistory;

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
}
