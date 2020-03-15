package com.dmitry.myapplication.weathersensor.net;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.dmitry.myapplication.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Locale;

public class UrlDownloader implements WeatherDataDownloader
{
    public enum ErrorEnum
    {
        URL_OK,
        URL_CONNECTION_ERROR,
        URL_SERVER_ERROR
    }

    private String serverIp = "192.168.1.9:3000";
    private ErrorEnum error;

    private WeatherDataReadyInvokable callback = null;
    private String result = null;
    private Context context;

    public UrlDownloader(Context context, String serverIp)
    {
        this.context = context;
        this.serverIp = serverIp;
    }

    public UrlDownloader(Context context)
    {
        this.context = context;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String value)
    {
        serverIp = value;
    }


    private String readDataSync(int channelId, long fromDate, long toDate)
    {
        error = ErrorEnum.URL_OK;
        try
        {
            String query = String.format(Locale.US,
                    "http://%s/weather?channelId=%d&from=%d&to=%d", serverIp, channelId, fromDate, toDate);
            URL url = new URL(query);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode != 200)
            {
                Log.i(this.getClass().getName(),"Response code is not 200");
                error = ErrorEnum.URL_SERVER_ERROR;
                return null;
            }

            StringBuilder builder = new StringBuilder();
            InputStream stream = urlConnection.getInputStream();

            int c;
            while((c = stream.read()) > -1)
            {
                builder.append((char)c);
            }

            String result = builder.toString();

            Log.i(this.getClass().getName(), result);
            return result;
        } catch (IOException e)
        {
            error = ErrorEnum.URL_CONNECTION_ERROR;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void requestData(WeatherDataReadyInvokable callbackHandler)
    {
        this.callback = callbackHandler;
        UrlReaderAsync reader = new UrlReaderAsync();
        int channelId = 0;
        long toDate = Calendar.getInstance().getTime().getTime();
        long fromDate = toDate - 1000*60*60*5;
        reader.execute(this, channelId, fromDate, toDate);
    }

    @Override
    public boolean isDataReady()
    {
        return result != null;
    }

    @Override
    public String getData()
    {
        return result;
    }

    private void onReaderFinished(String result)
    {
        this.result = result;
        if(result != null && callback != null)
        {
            callback.OnWeatherDataReady(result);
        }
        else if (callback != null)
        {
            callback.OnWeatherDataError(error.toString(), context.getString(R.string.url_error_message));
        }
    }


    static class UrlReaderAsync extends AsyncTask<Object, Void, Void>
    {
        private UrlDownloader invoker;
        private String result = null;
        @Override
        protected Void doInBackground(Object... objects)
        {
            invoker = (UrlDownloader)objects[0];
            int channelId = (int)objects[1];
            long fromDate = (int)objects[2];
            long toDate = (int)objects[3];
            result = invoker.readDataSync(channelId, fromDate, toDate);
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            invoker.onReaderFinished(result);
        }
    }
}
