package com.dmitry.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dmitry.myapplication.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.myapplication.weathersensor.SensorData;
import com.dmitry.myapplication.weathersensor.WeatherProvider;
import com.dmitry.myapplication.weathersensor.WeatherProviderBuilder;
import com.dmitry.myapplication.weathersensor.WeatherReport;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherReportReadyInvokable
{
    private WeatherProvider weatherProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(weatherProvider == null)
        {
            weatherProvider = WeatherProviderBuilder.getWeatherProvider();
        }

        SetStartAnimation();
        weatherProvider.requestWeatherReport(this);
    }

    protected void SetStartAnimation()
    {
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);

        temperatureLabel.setAlpha(0);
        humidityLabel.setAlpha(0);
        pressureLabel.setAlpha(0);
    }

    protected void PlayStartAnimation()
    {
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);

        Animation alphaAnimation1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation1.setDuration(1000);
        Animation alphaAnimation2 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation2.setDuration(1200);
        Animation alphaAnimation3 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation3.setDuration(1300);
        temperatureLabel.startAnimation(alphaAnimation1);
        humidityLabel.startAnimation(alphaAnimation2);
        pressureLabel.startAnimation(alphaAnimation3);
        temperatureLabel.setAlpha(1);
        humidityLabel.setAlpha(1);
        pressureLabel.setAlpha(1);
    }

    protected void UpdateCurrentWeather(WeatherReport report)
    {
        SensorData mostRecent = report.getMostRecent();
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);

        String temperatureStr = String.format(Locale.US, "%1.1f", mostRecent.getTemperature());
        String humidityStr = String.format(Locale.US, "%1.1f", mostRecent.getHumidity());
        String pressureStr = String.format(Locale.US, "%2.1f", mostRecent.getPressure());

        temperatureStr += report.getTemperatureUnits();
        humidityStr += report.getHumidityUnits();
        pressureStr += report.getPressureUnits();

        temperatureLabel.setText(temperatureStr);
        humidityLabel.setText(humidityStr);
        pressureLabel.setText(pressureStr);
    }

    @Override
    public void OnWeatherReportReady(WeatherReport report)
    {
        UpdateCurrentWeather(report);
        PlayStartAnimation();
    }
}
