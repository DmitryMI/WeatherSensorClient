package com.dmitry.weathersensorclient.weathersensor.net;

import android.content.Context;

public class WeatherDataDownloaderBuilder
{
    public static WeatherDataDownloader getServiceProvider(Context context)
    {
        return new UrlDownloader(context);
    }
}
