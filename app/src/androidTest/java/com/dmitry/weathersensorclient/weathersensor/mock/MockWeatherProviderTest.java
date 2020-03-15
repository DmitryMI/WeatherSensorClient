package com.dmitry.weathersensorclient.weathersensor.mock;

import com.dmitry.weathersensorclient.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.weathersensorclient.weathersensor.WeatherReport;

import org.junit.Test;

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