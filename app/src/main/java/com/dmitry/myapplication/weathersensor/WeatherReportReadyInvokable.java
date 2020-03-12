package com.dmitry.myapplication.weathersensor;

public interface WeatherReportReadyInvokable
{
    void OnWeatherReportReady(WeatherReport report);
    void OnWeatherReportError(int errorCode);
}
