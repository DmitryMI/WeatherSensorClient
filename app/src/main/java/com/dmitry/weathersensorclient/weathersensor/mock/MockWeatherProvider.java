package com.dmitry.weathersensorclient.weathersensor.mock;

import android.os.AsyncTask;

import com.dmitry.weathersensorclient.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.weathersensorclient.weathersensor.SensorData;
import com.dmitry.weathersensorclient.weathersensor.WeatherProvider;
import com.dmitry.weathersensorclient.weathersensor.WeatherReport;
import com.dmitry.weathersensorclient.weathersensor.WeatherState;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MockWeatherProvider implements WeatherProvider
{
    public final int HistoryLength = 5;

    private Random rnd = new Random();
    private WeatherReportReadyInvokable callbackHandler = null;
    private WeatherReport report = null;
    private WeatherReportTask task;

    private boolean generated = false;

    @Override
    public void requestWeatherReport(WeatherReportReadyInvokable callbackHandler)
    {
        this.callbackHandler = callbackHandler;
        task = new WeatherReportTask();
        task.execute(rnd, this);
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

    void setReport(WeatherReport report)
    {
        this.report = report;
        callbackHandler.OnWeatherReportReady(report);
    }

    private double getRandom(double min, double max)
    {
        return rnd.nextDouble() * (max - min) + min;
    }

    private int getRandomSign()
    {
        boolean value = rnd.nextBoolean();
        if(!value)
        {
            return -1;
        }
        return 1;
    }

    public WeatherReport createMockReport()
    {
        if(this.report == null)
        {
            int count = HistoryLength;
            SensorData[] history = new SensorData[count];

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, -count);
            Date startTime = cal.getTime();

            history[0] = new SensorData(getRandom(13, 26), getRandom(35, 70), getRandom(90, 120), startTime);

            int pressureDerivativeSign = getRandomSign();
            WeatherState state = WeatherState.SUNNY;
            if (pressureDerivativeSign == -1)
            {
                state = WeatherState.CLOUDY;
            }
            for (int i = 1; i < count; i++)
            {
                double t = history[i - 1].getTemperature() + getRandom(-2, 2);
                double h = history[i - 1].getHumidity() + getRandom(-5, +5);
                double p = history[i - 1].getPressure() + pressureDerivativeSign * getRandom(1, 3);
                cal.add(Calendar.HOUR, 1);
                Date time = cal.getTime();
                history[i] = new SensorData(t, h, p, time);
            }

            this.report = new WeatherReport(history, state);
            return report;
        }
        return this.report;
    }

    static class WeatherReportTask extends AsyncTask<Object, Void, Void>
    {
        private WeatherReport result;
        private MockWeatherProvider invoker;
        public WeatherReport getResult()
        {
            return result;
        }

        @Override
        protected Void doInBackground(Object[] objects)
        {
            Random rnd = (Random)objects[0];
            MockWeatherProvider invoker = (MockWeatherProvider)objects[1];
            this.invoker = invoker;
            int sleep = rnd.nextInt(2000) + 500;
            try
            {
                Thread.sleep(sleep);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            result = invoker.createMockReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void o)
        {
            invoker.setReport(result);
        }
    }
}
