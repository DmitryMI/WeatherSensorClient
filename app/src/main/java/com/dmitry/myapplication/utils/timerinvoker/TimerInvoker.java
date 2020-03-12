package com.dmitry.myapplication.utils.timerinvoker;


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
        handler = new Handler();
        runnable = new TimerRunnable(periodMs);
        handler.postDelayed(runnable, periodMs);
    }

    public void stop()
    {
        handler.removeCallbacks(runnable);
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
            handler.postDelayed(this, millis);
        }
    }
}
