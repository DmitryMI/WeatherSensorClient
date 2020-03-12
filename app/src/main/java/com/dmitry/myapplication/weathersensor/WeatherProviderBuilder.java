package com.dmitry.myapplication.weathersensor;

import com.dmitry.myapplication.weathersensor.mock.MockWeatherProvider;

public class WeatherProviderBuilder
{
    public static WeatherProvider getWeatherProvider()
    {
        return new MockWeatherProvider();
    }
}
