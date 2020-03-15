package com.dmitry.weathersensorclient.weathersensor.net;

public interface WeatherDataReadyInvokable
{
    void OnWeatherDataReady(String data);
    void OnWeatherDataError(String errorCode, String message);
}
