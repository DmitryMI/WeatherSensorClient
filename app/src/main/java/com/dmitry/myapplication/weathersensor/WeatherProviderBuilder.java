package com.dmitry.myapplication.weathersensor;

import com.dmitry.myapplication.weathersensor.mock.MockWeatherProvider;
import com.dmitry.myapplication.weathersensor.net.JsonWeatherProvider;
import com.dmitry.myapplication.weathersensor.net.WeatherDataDownloader;
import com.dmitry.myapplication.weathersensor.net.WeatherDataDownloaderBuilder;

public class WeatherProviderBuilder
{
    public static WeatherProvider getWeatherProvider()
    {
        WeatherDataDownloader downloader = WeatherDataDownloaderBuilder.getServiceProvider();
        return new JsonWeatherProvider(downloader);
    }
}
