package com.dmitry.myapplication.weathersensor.net;

import com.dmitry.myapplication.weathersensor.mock.MockJsonGenerator;

public class WeatherDataDownloaderBuilder
{
    public static WeatherDataDownloader getServiceProvider()
    {
        return new MockJsonGenerator();
    }
}
