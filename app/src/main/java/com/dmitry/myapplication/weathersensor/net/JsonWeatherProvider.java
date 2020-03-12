package com.dmitry.myapplication.weathersensor.net;

import android.util.JsonReader;
import android.util.Log;

import com.dmitry.myapplication.weathersensor.WeatherProvider;
import com.dmitry.myapplication.weathersensor.WeatherReport;
import com.dmitry.myapplication.weathersensor.WeatherReportReadyInvokable;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

public class JsonWeatherProvider implements WeatherProvider, WeatherDataReadyInvokable
{
    WeatherDataDownloader downloader;
    WeatherReport report = null;
    WeatherReportReadyInvokable callback;
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
            callback.OnWeatherReportError(0);
        }
    }

    @Override
    public void OnWeatherDataError(int errorCode)
    {
        callback.OnWeatherReportError(-1);
    }
}
