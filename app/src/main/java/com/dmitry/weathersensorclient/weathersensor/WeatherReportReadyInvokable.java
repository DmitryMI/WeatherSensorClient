package com.dmitry.weathersensorclient.weathersensor;

public interface WeatherReportReadyInvokable
{
    void OnWeatherReportReady(WeatherReport report);
    void OnWeatherReportError(String errorCode, String message);
}
