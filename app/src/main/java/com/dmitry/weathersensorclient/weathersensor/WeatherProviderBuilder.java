package com.dmitry.weathersensorclient.weathersensor;

import android.content.Context;

import com.dmitry.weathersensorclient.weathersensor.net.JsonWeatherProvider;
import com.dmitry.weathersensorclient.weathersensor.net.WeatherDataDownloader;
import com.dmitry.weathersensorclient.weathersensor.net.WeatherDataDownloaderBuilder;

public class WeatherProviderBuilder
{
    public static WeatherProvider getWeatherProvider(Context context)
    {
        WeatherDataDownloader downloader = WeatherDataDownloaderBuilder.getServiceProvider(context);
        return new JsonWeatherProvider(downloader);
    }
}
