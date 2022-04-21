package com.luoyanzhang.smarthome.service;

import com.luoyanzhang.smarthome.entity.Humidity;
import com.luoyanzhang.smarthome.entity.Temperature;
import com.luoyanzhang.smarthome.repository.HumidityRepository;
import com.luoyanzhang.smarthome.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SensorService {

    @Autowired
    TemperatureRepository temperatureRepository;

    @Autowired
    HumidityRepository humidityRepository;

    public TreeMap<Date, Float> getRecentTemperatureData() {
        TreeMap<Date, Float> result = new TreeMap<>();
        List<Temperature> list = temperatureRepository.findAllRecentTemperatureData();
        for (Temperature t : list) {
            result.put(t.getDate_created(), t.getData());
        }
        return result;
    }

    public TreeMap<Date, Float> getRecentHumidityData() {
        TreeMap<Date, Float> result = new TreeMap<>();
        List<Humidity> list = humidityRepository.findAllRecentHumidityData();
        for (Humidity t : list) {
            result.put(t.getDate_created(), t.getData());
        }
        return result;
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
