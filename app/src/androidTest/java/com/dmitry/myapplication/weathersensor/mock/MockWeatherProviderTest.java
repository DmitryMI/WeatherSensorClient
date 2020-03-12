package com.dmitry.myapplication.weathersensor.mock;

import com.dmitry.myapplication.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.myapplication.weathersensor.WeatherReport;

import org.junit.Test;

import static org.junit.Assert.*;

public class MockWeatherProviderTest
{

    @Test
    public void requestWeatherReport()
    {
        class Callback implements WeatherReportReadyInvokable
        {
            private WeatherReport result = null;
            @Override
            public void OnWeatherReportReady(WeatherReport report)
            {
                result = report;
            }
        };
        MockWeatherProvider provider = new MockWeatherProvider();
        Callback callback = new Callback();
        provider.requestWeatherReport(callback);

        int waitCounter = 0;
        while(callback.result == null && waitCounter < 5)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            waitCounter++;
        }

        if(waitCounter >= 3)
        {
            assert false : "Timeout";
        }

        assert callback.result.getHistoryLength() == provider.HistoryLength : "Lengths do not match";
    }
}