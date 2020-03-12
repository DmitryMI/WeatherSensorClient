package com.dmitry.myapplication.weathersensor.net;

import com.dmitry.myapplication.weathersensor.SensorData;
import com.dmitry.myapplication.weathersensor.WeatherReport;
import com.dmitry.myapplication.weathersensor.WeatherState;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherReportDeserializer extends StdDeserializer<WeatherReport>
{
    public WeatherReportDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WeatherReport deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException
    {
        String temperatureUnits = null;
        String humidityUnits = null;
        String pressureUnits = null;
        WeatherState forecast = WeatherState.SUNNY;
        ArrayList<SensorData> history = null;

        double temperature = 0, humidity = 0, pressure = 0;
        long timestamp = 0;
        int historyLength = 0;
        boolean sensorDataPending = false;

        while(!parser.isClosed())
        {
            JsonToken jsonToken = parser.nextToken();

            if(JsonToken.FIELD_NAME.equals(jsonToken))
            {
                String fieldName = parser.getCurrentName();
                jsonToken = parser.nextToken();
                if(fieldName.equals("temperatureUnits"))
                {
                    temperatureUnits = parser.getValueAsString();
                }
                else if(fieldName.equals("humidityUnits"))
                {
                    humidityUnits = parser.getValueAsString();
                }
                else if(fieldName.equals("pressureUnits"))
                {
                    pressureUnits = parser.getValueAsString();
                }
                else if(fieldName.equals("weatherForecast"))
                {
                    String value = parser.getValueAsString();
                    forecast = WeatherState.valueOf(value);
                }
                else if(fieldName.equals("historyLength"))
                {
                    historyLength = parser.getValueAsInt();
                    history = new ArrayList<>(historyLength);
                }
                else if(fieldName.equals("sensorDataHistory"))
                {
                    // Array starts
                    if(history == null)
                    {
                        history = new ArrayList<>();
                    }
                }
                else if(fieldName.equals("temperature"))
                {
                    temperature = parser.getDoubleValue();
                }
                else if(fieldName.equals("humidity"))
                {
                    sensorDataPending = true;
                    humidity = parser.getDoubleValue();
                }
                else if(fieldName.equals("pressure"))
                {
                    sensorDataPending = true;
                    pressure = parser.getDoubleValue();
                }
                else if(fieldName.equals("timestamp"))
                {
                    sensorDataPending = true;
                    timestamp = parser.getLongValue();
                }
            }

            if(JsonToken.START_ARRAY.equals(jsonToken))
            {
                // Array starts
            }
            if(JsonToken.START_OBJECT.equals(jsonToken))
            {

            }
            if(JsonToken.END_OBJECT.equals(jsonToken))
            {
                // Sensor data ends
                if(sensorDataPending == true)
                {
                    sensorDataPending = false;
                    Date date = new Date(timestamp);
                    SensorData data = new SensorData(temperature, humidity, pressure, date);
                    if (history == null)
                    {
                        history = new ArrayList<>();
                    }
                    history.add(data);
                }
            }
            if(JsonToken.END_ARRAY.equals(jsonToken))
            {

            }
        }

        SensorData[] historyArray = new SensorData[history.size()];
        history.toArray(historyArray);
        return new WeatherReport(historyArray, forecast, temperatureUnits, humidityUnits, pressureUnits);
    }
}
