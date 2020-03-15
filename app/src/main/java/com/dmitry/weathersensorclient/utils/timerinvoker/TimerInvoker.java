package com.dmitry.weathersensorclient.utils.timerinvoker;


import android.os.Handler;

public class TimerInvoker
{
    private Handler handler;
    private TimerInvokerCallback callback;
    private TimerRunnable runnable;
    public TimerInvoker(TimerInvokerCallback callback)
    {
        this.callback = callback;
    }

    public void start(int periodMs)
    {
        if(handler != null)
        {
            stop();
        }
        handler = new Handler();
        runnable = new TimerRunnable(periodMs);
        handler.postDelayed(runnable, periodMs);
    }

    public void stop()
    {
        if(handler != null)
        {
            handler.removeCallbacks(runnable);
        }
        runnable = null;
        handler = null;
    }

    private void periodElapsed()
    {
        callback.TimerInvoke();
    }

    private class TimerRunnable implements Runnable
    {
        private long millis;
        TimerRunnable(long period)
        {
            millis = period;
        }

        @Override
        public void run()
        {
            periodElapsed();
            if(handler != null)
            {
                handler.postDelayed(this, millis);
            }
        }
    }
}
