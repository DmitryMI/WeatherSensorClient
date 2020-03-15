package com.dmitry.weathersensorclient.weathersensor;

import java.util.Date;

public class SensorData
{
    private double temperature;
    private double humidity;
    private double pressure;
    private Date timestamp;

    public SensorData(double temperature, double humidity, double pressure, Date timestamp)
    {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timestamp = timestamp;
    }

    public SensorData()
    {

    }

    public double getTemperature()
    {
        return temperature;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public double getPressure()
    {
        return pressure;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }
}
