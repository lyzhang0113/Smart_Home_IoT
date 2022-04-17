package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.Humidity;
import com.luoyanzhang.smarthome.entity.Temperature;
import com.luoyanzhang.smarthome.repository.HumidityRepository;
import com.luoyanzhang.smarthome.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
