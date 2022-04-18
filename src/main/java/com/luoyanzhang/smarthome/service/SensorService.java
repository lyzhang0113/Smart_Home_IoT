package com.luoyanzhang.smarthome.service;

import com.luoyanzhang.smarthome.entity.mysql.Humidity;
import com.luoyanzhang.smarthome.entity.mysql.Temperature;
import com.luoyanzhang.smarthome.repository.mysql.HumidityRepository;
import com.luoyanzhang.smarthome.repository.mysql.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorService {

    @Autowired
    TemperatureRepository temperatureRepository;

    @Autowired
    HumidityRepository humidityRepository;

    public Map<Date, Float> getRecentTemperatureData() {
        Map<Date, Float> result = new LinkedHashMap<>();
        List<Temperature> list = temperatureRepository.findAllRecentTemperatureData();
        for (Temperature t : list) {
            result.put(t.getDate_created(), t.getData());
        }
        return result;
    }

    public Map<Date, Float> getRecentHumidityData() {
        Map<Date, Float> result = new LinkedHashMap<>();
        List<Humidity> list = humidityRepository.findAllRecentHumidityData();
        for (Humidity t : list) {
            result.put(t.getDate_created(), t.getData());
        }
        return result;
    }



}
