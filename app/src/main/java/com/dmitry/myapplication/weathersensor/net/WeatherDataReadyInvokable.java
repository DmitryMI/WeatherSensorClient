package com.dmitry.myapplication.weathersensor.net;

public interface WeatherDataReadyInvokable
{
    void OnWeatherDataReady(String data);
    void OnWeatherDataError(String errorCode, String message);
}
