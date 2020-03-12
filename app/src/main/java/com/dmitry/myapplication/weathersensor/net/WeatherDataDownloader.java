package com.dmitry.myapplication.weathersensor.net;

import com.dmitry.myapplication.weathersensor.WeatherReportReadyInvokable;

public interface WeatherDataDownloader
{
    void requestData(WeatherDataReadyInvokable callbackHandler);
    boolean isDataReady();
    String getData();
}
