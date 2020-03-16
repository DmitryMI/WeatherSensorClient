package com.dmitry.weathersensorclient.utils.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MedianFilter
{
    public MedianFilter()
    {

    }

    static class MedianComparator<T> implements Comparator<T>
    {
        @Override
        public int compare(Object o, Object t1)
        {
            //if(o instanceof Comparable<?> && t1 instanceof Comparable<?>)
            {
                Comparable a = (Comparable) o;
                Comparable b = (Comparable) t1;

                return a.compareTo(b);
            }

            /*if(o.getClass().equals(int.class))
            {
                int a = (int)o;
                int b = (int)t1;

                if (a < b)
                    return -1;
                else if (a > b)
                    return 1;
            }
            else if(o.getClass().equals(double.class))
            {
                double a = (double)o;
                double b = (double)t1;

                if (a < b)
                    return -1;
                else if (a > b)
                    return 1;
            }

            return 0;*/
        }
    }

    public int[] Filtrate(int[] values, int windowSize)
    {
        int[] result = new int[values.length];
        int whSize = windowSize / 2;
        MedianComparator<Integer> comparator = new MedianComparator<>();
        for(int i = 0; i < values.length; i++)
        {
            ArrayList<Integer> window = new ArrayList<>();
            for(int w = -whSize; w < whSize; w++)
            {
                if(w < 0)
                {
                    window.add(values[0]);
                }
                else if(w >= values.length)
                {
                    window.add(values[values.length - 1]);
                }
                else
                {
                    window.add(values[w]);
                }
            }

            window.sort(comparator);

            if(windowSize % 2 == 0)
            {
                int right = whSize - 1;
                int left = whSize;
                result[i] = (window.get(left) + window.get(right)) / 2;
            }
            else
            {
                result[i] = window.get(whSize);
            }
        }

        return result;
    }

    public long[] Filtrate(long[] values, int windowSize)
    {
        long[] result = new long[values.length];
        int whSize = windowSize / 2;
        MedianComparator<Long> comparator = new MedianComparator<>();
        for(int i = 0; i < values.length; i++)
        {
            ArrayList<Long> window = new ArrayList<>();
            for(int w = -whSize; w < whSize; w++)
            {
                if(w < 0)
                {
                    window.add(values[0]);
                }
                else if(w >= values.length)
                {
                    window.add(values[values.length - 1]);
                }
                else
                {
                    window.add(values[w]);
                }
            }

            window.sort(comparator);

            if(windowSize % 2 == 0)
            {
                int right = whSize - 1;
                int left = whSize;
                result[i] = (window.get(left) + window.get(right)) / 2;
            }
            else
            {
                result[i] = window.get(whSize);
            }
        }

        return result;
    }

    public double[] Filtrate(double[] values, int windowSize)
    {
        double[] result = new double[values.length];
        int whSize = windowSize / 2;
        MedianComparator<Double> comparator = new MedianComparator<>();
        for(int i = 0; i < values.length; i++)
        {
            ArrayList<Double> window = new ArrayList<>();
            for(int w = -whSize; w < whSize; w++)
            {
                if(i + w < 0)
                {
                    window.add(values[0]);
                }
                else if(i + w >= values.length)
                {
                    window.add(values[values.length - 1]);
                }
                else
                {
                    window.add(values[i + w]);
                }
            }

            window.sort(comparator);

            if(windowSize % 2 == 0)
            {
                int right = whSize - 1;
                int left = whSize;
                result[i] = (window.get(left) + window.get(right)) / 2;
            }
            else
            {
                result[i] = window.get(whSize);
            }
        }

        return result;
    }
}
