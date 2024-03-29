package com.dmitry.weathersensorclient.weathersensor.mock;

import com.dmitry.weathersensorclient.weathersensor.WeatherReport;
import com.dmitry.weathersensorclient.weathersensor.net.WeatherDataDownloader;
import com.dmitry.weathersensorclient.weathersensor.net.WeatherDataReadyInvokable;
import com.dmitry.weathersensorclient.weathersensor.net.WeatherReportSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.StringWriter;

public class MockJsonGenerator implements WeatherDataDownloader
{

    private MockWeatherProvider mockWeatherProvider = new MockWeatherProvider();
    private WeatherDataReadyInvokable callback;
    private String data = null;
    @Override
    public void requestData(WeatherDataReadyInvokable callbackHandler)
    {
        try
        {
            callback = callbackHandler;

            WeatherReport report = mockWeatherProvider.createMockReport();

            StringWriter writer = new StringWriter();

            ObjectMapper objectMapper = new ObjectMapper();

            SimpleModule module = new SimpleModule("WeatherReportDeserializer", new Version(1, 1, 0, null, null, null));
            WeatherReportSerializer serializer = new WeatherReportSerializer(WeatherReport.class);
            module.addSerializer(WeatherReport.class, serializer);
            objectMapper.registerModule(module);

            objectMapper.writeValue(writer, report);

            data = writer.toString();
            callbackHandler.OnWeatherDataReady(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            callback.OnWeatherDataError("UNDEFINED", "Should not happen, but shit happens");
        }
    }

    @Override
    public boolean isDataReady()
    {
        return data != null;
    }

    @Override
    public String getData()
    {
        return data;
    }
}
