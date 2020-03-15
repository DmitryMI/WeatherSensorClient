package com.dmitry.weathersensorclient.weathersensor.net;

public interface WeatherDataDownloader
{
    void requestData(WeatherDataReadyInvokable callbackHandler);
    boolean isDataReady();
    String getData();
}
