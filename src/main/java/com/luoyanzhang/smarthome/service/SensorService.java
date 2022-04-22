package com.luoyanzhang.smarthome.service;

import com.luoyanzhang.smarthome.dto.AtHome;
import com.luoyanzhang.smarthome.dto.Reading;
import com.luoyanzhang.smarthome.entity.Brightness;
import com.luoyanzhang.smarthome.entity.Humidity;
import com.luoyanzhang.smarthome.entity.Motion;
import com.luoyanzhang.smarthome.entity.Temperature;
import com.luoyanzhang.smarthome.repository.BrightnessRepository;
import com.luoyanzhang.smarthome.repository.HumidityRepository;
import com.luoyanzhang.smarthome.repository.MotionRepository;
import com.luoyanzhang.smarthome.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SensorService {

    @Autowired
    TemperatureRepository temperatureRepository;

    @Autowired
    HumidityRepository humidityRepository;

    @Autowired
    BrightnessRepository brightnessRepository;

    @Autowired
    MotionRepository motionRepository;

    public Reading getMostRecentSensorReading() {
        Temperature temp = temperatureRepository.findMostRecentTemperature();
        Humidity humid = humidityRepository.findMostRecentHumidity();
        Brightness bright = brightnessRepository.findMostRecentBrightness();
        return new Reading(temp.getData(), humid.getData(), bright.getData());
    }

    public Reading getYesterdayAvgSensorReading() {
        return new Reading(temperatureRepository.findAvgYesterdayTemperature(),
                humidityRepository.findAvgYesterdayHumidity(),
                brightnessRepository.findAvgYesterdayBrightness());
    }

    public AtHome isUserAtHome(int threshold) {
        Motion motion = motionRepository.findMostRecentMotion();
        long diff = TimeUnit.MINUTES.convert(new Date().getTime() - motion.getDate_created().getTime(), TimeUnit.MILLISECONDS);
        return new AtHome(diff < threshold, motion.getDate_created());
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
