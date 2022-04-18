package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.mysql.Humidity;
import com.luoyanzhang.smarthome.entity.mysql.Temperature;
import com.luoyanzhang.smarthome.repository.mysql.HumidityRepository;
import com.luoyanzhang.smarthome.repository.mysql.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    TemperatureRepository temperatureRepository;

    @Autowired
    HumidityRepository humidityRepository;

    @PostMapping("/temp")
    public String saveTemp(@RequestParam("data") Float data) {
        Temperature n = new Temperature();
        n.setData(data);
        temperatureRepository.save(n);
        return "success";
    }

    @PostMapping("/humid")
    public String saveHumid(@RequestParam("data") Float data) {
        Humidity n = new Humidity();
        n.setData(data);
        humidityRepository.save(n);
        return "success";
    }

    @GetMapping("/get/humid")
    public String getHumid() {
        List<Humidity> humidityList = humidityRepository.findAllRecentHumidityData();
        return humidityList.toString();
    }
}
