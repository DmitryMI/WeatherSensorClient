package com.dmitry.myapplication.weathersensor;

public interface WeatherProvider
{
    void requestWeatherReport(WeatherReportReadyInvokable callbackHandler);
    boolean isReportReady();
    WeatherReport getReport();
}
