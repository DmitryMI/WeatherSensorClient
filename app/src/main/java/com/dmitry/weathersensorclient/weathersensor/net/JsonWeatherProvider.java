package com.dmitry.weathersensorclient.weathersensor.net;

import android.util.Log;

import com.dmitry.weathersensorclient.weathersensor.WeatherProvider;
import com.dmitry.weathersensorclient.weathersensor.WeatherReport;
import com.dmitry.weathersensorclient.weathersensor.WeatherReportReadyInvokable;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.StringReader;

public class JsonWeatherProvider implements WeatherProvider, WeatherDataReadyInvokable
{
    public enum ErrorEnum
    {
        PROVIDER_OK,
        PROVIDER_SOURCE_ERROR,
        PROVIDER_DESERIALIZATION_ERROR
    }

    private WeatherDataDownloader downloader;
    private WeatherReport report = null;
    private WeatherReportReadyInvokable callback;
    public JsonWeatherProvider(WeatherDataDownloader downloader)
    {
        this.downloader = downloader;
    }

    @Override
    public void requestWeatherReport(WeatherReportReadyInvokable callbackHandler)
    {
        callback = callbackHandler;
        downloader.requestData(this);
    }

    @Override
    public boolean isReportReady()
    {
        return report != null;
    }

    @Override
    public WeatherReport getReport()
    {
        return report;
    }

    @Override
    public void OnWeatherDataReady(String data)
    {
        Log.i(this.getClass().getName(), "Data: \n" + data);

        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("WeatherReportSerializer", new Version(1, 1, 0, null, null, null));
            WeatherReportDeserializer deserializer = new WeatherReportDeserializer(WeatherReport.class);
            module.addDeserializer(WeatherReport.class, deserializer);
            objectMapper.registerModule(module);

            StringReader reader = new StringReader(data);
            WeatherReport report = objectMapper.readValue(reader, WeatherReport.class);
            this.report = report;
            callback.OnWeatherReportReady(report);
        } catch (IOException e)
        {
            e.printStackTrace();
            callback.OnWeatherReportError(ErrorEnum.PROVIDER_DESERIALIZATION_ERROR.toString(), e.getMessage());
        }
    }

    @Override
    public void OnWeatherDataError(String errorCode, String message)
    {
        callback.OnWeatherReportError( ErrorEnum.PROVIDER_SOURCE_ERROR.toString(), message);
    }
}
