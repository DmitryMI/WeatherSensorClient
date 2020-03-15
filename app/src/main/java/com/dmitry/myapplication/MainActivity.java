package com.dmitry.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitry.myapplication.utils.timerinvoker.TimerInvoker;
import com.dmitry.myapplication.utils.timerinvoker.TimerInvokerCallback;
import com.dmitry.myapplication.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.myapplication.weathersensor.SensorData;
import com.dmitry.myapplication.weathersensor.WeatherProvider;
import com.dmitry.myapplication.weathersensor.WeatherProviderBuilder;
import com.dmitry.myapplication.weathersensor.WeatherReport;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherReportReadyInvokable, TimerInvokerCallback
{
    public final int REFRESH_PERIOD = 5000;

    private WeatherProvider weatherProvider = null;
    private TimerInvoker timerInvoker = null;
    private boolean animationPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(weatherProvider == null)
        {
            weatherProvider = WeatherProviderBuilder.getWeatherProvider(getApplicationContext());
        }

        SetStartAnimation();
        animationPending = true;
        weatherProvider.requestWeatherReport(this);

        if(timerInvoker != null)
        {
            timerInvoker.stop();
            timerInvoker.start(REFRESH_PERIOD);
        }
        else
        {
            timerInvoker = new TimerInvoker(this);
            timerInvoker.start(REFRESH_PERIOD);
        }
    }

    protected void SetStartAnimation()
    {
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);
        GraphView graphView = findViewById(R.id.pressureHistory);

        temperatureLabel.setAlpha(0);
        humidityLabel.setAlpha(0);
        pressureLabel.setAlpha(0);
        graphView.setAlpha(0);
    }

    protected void PlayStartAnimation()
    {
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);
        GraphView graphView = findViewById(R.id.pressureHistory);

        Animation alphaAnimation1 = AnimationUtils.loadAnimation(this, R.anim.alpha);

        long baseDuration = alphaAnimation1.getDuration();

        Animation alphaAnimation2 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation2.setDuration(baseDuration + 100);
        Animation alphaAnimation3 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation3.setDuration(baseDuration + 200);

        Animation graphAnimation = AnimationUtils.loadAnimation(this, R.anim.graph_spawn);
        graphAnimation.setDuration(baseDuration + 300);

        temperatureLabel.startAnimation(alphaAnimation1);
        humidityLabel.startAnimation(alphaAnimation2);
        pressureLabel.startAnimation(alphaAnimation3);
        graphView.startAnimation(graphAnimation);

        temperatureLabel.setAlpha(1);
        humidityLabel.setAlpha(1);
        pressureLabel.setAlpha(1);
        graphView.setAlpha(1);
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

        String temperatureUnits = report.getTemperatureUnits(); // FIXME Shitty hack
        if(temperatureUnits.equals("C"))
        {
            temperatureUnits = "°C";
        }

        temperatureStr += temperatureUnits;
        humidityStr += report.getHumidityUnits();
        pressureStr += report.getPressureUnits();

        temperatureLabel.setText(temperatureStr);
        humidityLabel.setText(humidityStr);
        pressureLabel.setText(pressureStr);
    }

    protected void UpdatePressureGraph(WeatherReport report)
    {
        GraphView graph = findViewById(R.id.pressureHistory);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        Date now = Calendar.getInstance().getTime();
        long previousTimestamp = 0;
        boolean timestampOrderOk = true;
        for(int i = 0; i < report.getHistoryLength(); i++)
        {
            Date timestamp = report.elementAt(i).getTimestamp();
            double pressure = report.elementAt(i).getPressure();
            long diffMillis = now.getTime() - timestamp.getTime();
            int hours = -(int)Math.round(diffMillis / 1000.0 / 60.0 / 60.0);
            DataPoint point = new DataPoint(hours, pressure);

            if(i > 0)
            {
                if(diffMillis >= previousTimestamp)
                {
                    timestampOrderOk = false;
                }
            }

            if(timestampOrderOk)
            {
                series.appendData(point, true, 10, true);
            }

            previousTimestamp = diffMillis;
        }
        graph.removeAllSeries();
        if(report.getHistoryLength() >= 2 && timestampOrderOk)
        {
            graph.addSeries(series);
            graph.setTitle("Pressure alteration");
        }
        else
        {
            graph.setTitle("Pressure alteration (unavailable)");
        }
    }

    @Override
    public void OnWeatherReportReady(WeatherReport report)
    {
        ClearError();
        UpdateCurrentWeather(report);
        UpdatePressureGraph(report);
        if(animationPending)
        {
            PlayStartAnimation();
        }
        animationPending = false;
    }

    @Override
    public void OnWeatherReportError(String errorCode, String message)
    {
        /*TextView errorTextView = findViewById(R.id.errorTextVIew);
        errorTextView.setText(R.string.main_activity_error);

        ViewGroup.LayoutParams layoutParams = errorTextView.getLayoutParams();
        layoutParams.height = 10;
        errorTextView.setLayoutParams(layoutParams);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.major_failure);
        animation.setRepeatCount(Animation.INFINITE);
        errorTextView.startAnimation(animation);*/

        Toast errorMessage = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        errorMessage.show();
    }

    private void ClearError()
    {
        //TextView title = findViewById(R.id.formTitle);
        //title.clearAnimation();
        //title.setText(getText(R.string.weather_sensor_client));
        //title.setAlpha(1);
    }

    @Override
    public void TimerInvoke()
    {
        weatherProvider.requestWeatherReport(this);
    }
}
