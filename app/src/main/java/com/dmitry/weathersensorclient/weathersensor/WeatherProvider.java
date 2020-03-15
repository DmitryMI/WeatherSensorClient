package com.dmitry.weathersensorclient.weathersensor;

public interface WeatherProvider
{
    void requestWeatherReport(WeatherReportReadyInvokable callbackHandler);
    boolean isReportReady();
    WeatherReport getReport();
}
