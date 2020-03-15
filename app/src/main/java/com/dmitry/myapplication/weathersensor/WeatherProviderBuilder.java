package com.dmitry.myapplication.weathersensor;

import android.content.Context;

import com.dmitry.myapplication.weathersensor.mock.MockWeatherProvider;
import com.dmitry.myapplication.weathersensor.net.JsonWeatherProvider;
import com.dmitry.myapplication.weathersensor.net.WeatherDataDownloader;
import com.dmitry.myapplication.weathersensor.net.WeatherDataDownloaderBuilder;

public class WeatherProviderBuilder
{
    public static WeatherProvider getWeatherProvider(Context context)
    {
        WeatherDataDownloader downloader = WeatherDataDownloaderBuilder.getServiceProvider(context);
        return new JsonWeatherProvider(downloader);
    }
}
