package com.luoyanzhang.smarthome.service;

import com.luoyanzhang.smarthome.dto.Reading;
import com.luoyanzhang.smarthome.entity.Brightness;
import com.luoyanzhang.smarthome.entity.Humidity;
import com.luoyanzhang.smarthome.entity.Temperature;
import com.luoyanzhang.smarthome.repository.BrightnessRepository;
import com.luoyanzhang.smarthome.repository.HumidityRepository;
import com.luoyanzhang.smarthome.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    @Autowired
    TemperatureRepository temperatureRepository;

    @Autowired
    HumidityRepository humidityRepository;

    @Autowired
    BrightnessRepository brightnessRepository;

    public Reading getMostRecentSensorReading() {
        Temperature temp = temperatureRepository.findMostRecentTemperature();
        Humidity humid = humidityRepository.findMostRecentHumidity();
        Brightness bright = brightnessRepository.findMostRecentBrightness();
        return new Reading(temp.getData(), humid.getData(), bright.getData());
    }

    public Float getLastHourAverageTemperature() {
        return Float.parseFloat("0.0");
    }

    public Float getLastHourAverageHumidity() {
        return Float.parseFloat("0.0");
    }

    public Float getYesterdayAverageTemperature() {
        return Float.parseFloat("0.0");
    }

    public Float getYesterdayAverageHumidity() {
        return Float.parseFloat("0.0");
    }

}
