package com.dmitry.weathersensorclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitry.weathersensorclient.utils.filters.MedianFilter;
import com.dmitry.weathersensorclient.utils.timerinvoker.TimerInvoker;
import com.dmitry.weathersensorclient.utils.timerinvoker.TimerInvokerCallback;
import com.dmitry.weathersensorclient.weathersensor.WeatherReportReadyInvokable;
import com.dmitry.weathersensorclient.weathersensor.SensorData;
import com.dmitry.weathersensorclient.weathersensor.WeatherProvider;
import com.dmitry.weathersensorclient.weathersensor.WeatherProviderBuilder;
import com.dmitry.weathersensorclient.weathersensor.WeatherReport;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
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

    @Override
    protected void onPause()
    {
        super.onPause();
        timerInvoker.stop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        timerInvoker.start(REFRESH_PERIOD);
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
        TextView temperatureLabel = findViewById(R.id.temperatureLabel);
        TextView humidityLabel = findViewById(R.id.humidityLabel);
        TextView pressureLabel = findViewById(R.id.pressureLabel);

        int historyLength = report.getHistoryLength();

        if(historyLength == 0)
        {
            temperatureLabel.setText("N/A");
            humidityLabel.setText("N/A");
            pressureLabel.setText("N/A");
            return;
        }

        SensorData mostRecent = report.getMostRecent();


        String temperatureStr = String.format(Locale.US, "%1.1f", mostRecent.getTemperature());
        String humidityStr = String.format(Locale.US, "%1.1f", mostRecent.getHumidity());
        String pressureStr = String.format(Locale.US, "%2.3f", mostRecent.getPressure());

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
        graph.removeAllSeries();
        if(report.getHistoryLength() < 3)
        {
            graph.setTitle("Pressure alteration (unavailable)");
            return;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        Date now = Calendar.getInstance().getTime();
        long previousTimestamp = 0;
        boolean timestampOrderOk = true;

        double[] pressureHistory = report.getPressureHistory();
        MedianFilter filter = new MedianFilter();

        int windowSize = pressureHistory.length / 10;
        if(windowSize < 3)
            windowSize = 3;
        double[] pressureFiltered = filter.Filtrate(pressureHistory, windowSize);

        double minTime = 0;
        double minPressure = pressureFiltered[0];
        double maxPressure = pressureFiltered[0];

        for(int i = 0; i < report.getHistoryLength(); i++)
        {
            Date timestamp = report.elementAt(i).getTimestamp();
            double pressure = pressureFiltered[i];
            if(pressure < minPressure)
                minPressure = pressure;
            if(pressure > maxPressure)
                maxPressure = pressure;
            long diffMillis = now.getTime() - timestamp.getTime();
            double diffHours = (double)diffMillis / 1000.0 / 60.0 / 60.0;
            double hours = -diffHours;
            if(hours < minTime)
            {
                minTime = hours;
            }
            DataPoint point = new DataPoint(hours, pressure);

            if(i > 0 && diffMillis >= previousTimestamp)
            {
                timestampOrderOk = false;
            }

            if(timestampOrderOk)
            {
                series.appendData(point, true, 6000, true);
            }

            previousTimestamp = diffMillis;
        }
        if(timestampOrderOk)
        {
            graph.addSeries(series);
            series.setThickness(10);
            //graph.getGridLabelRenderer().setHumanRounding(false, true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMaxX(0.3);
            graph.getViewport().setMinX(minTime - 0.1);
            graph.getViewport().setMinY(minPressure - 0.01);
            graph.getViewport().setMaxY(maxPressure + 0.01);
            graph.getViewport().setMinX(minTime);
            //graph.getGridLabelRenderer().setNumVerticalLabels(4);
            graph.getGridLabelRenderer().setNumHorizontalLabels(5);
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
