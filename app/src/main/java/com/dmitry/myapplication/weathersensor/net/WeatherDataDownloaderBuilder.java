package com.dmitry.myapplication.weathersensor.net;

import android.content.Context;

import com.dmitry.myapplication.weathersensor.mock.MockJsonGenerator;

public class WeatherDataDownloaderBuilder
{
    public static WeatherDataDownloader getServiceProvider(Context context)
    {
        return new UrlDownloader(context);
    }
}
