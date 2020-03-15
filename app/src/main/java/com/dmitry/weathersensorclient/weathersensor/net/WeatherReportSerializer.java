package com.dmitry.weathersensorclient.weathersensor.net;

import com.dmitry.weathersensorclient.weathersensor.SensorData;
import com.dmitry.weathersensorclient.weathersensor.WeatherReport;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class WeatherReportSerializer extends StdSerializer<WeatherReport>
{
    public WeatherReportSerializer(Class<WeatherReport> t)
    {
        super(t);
    }

    public void serialize(
            WeatherReport report,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    )
            throws IOException
    {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("temperatureUnits", report.getTemperatureUnits());
        jsonGenerator.writeStringField("humidityUnits", report.getHumidityUnits());
        jsonGenerator.writeStringField("pressureUnits", report.getPressureUnits());
        jsonGenerator.writeStringField("weatherForecast", report.getWeatherForecast().toString());

        int historyLength = report.getHistoryLength();
        jsonGenerator.writeNumberField("historyLength", historyLength);

        jsonGenerator.writeFieldName("sensorDataHistory");
        jsonGenerator.writeStartArray();
        {
            for(int i = 0; i < historyLength; i++)
            {
                SensorData sensorData = report.elementAt(i);
                jsonGenerator.writeStartObject();

                jsonGenerator.writeNumberField("temperature", sensorData.getTemperature());
                jsonGenerator.writeNumberField("humidity", sensorData.getHumidity());
                jsonGenerator.writeNumberField("pressure", sensorData.getPressure());
                jsonGenerator.writeNumberField("timestamp", sensorData.getTimestamp().getTime());

                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

}
