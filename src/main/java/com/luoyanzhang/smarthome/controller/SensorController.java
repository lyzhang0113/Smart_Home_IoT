package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.Brightness;
import com.luoyanzhang.smarthome.entity.Humidity;
import com.luoyanzhang.smarthome.entity.Temperature;
import com.luoyanzhang.smarthome.repository.BrightnessRepository;
import com.luoyanzhang.smarthome.repository.HumidityRepository;
import com.luoyanzhang.smarthome.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private HumidityRepository humidityRepository;

    @Autowired
    private BrightnessRepository brightnessRepository;

    @PostMapping("/temp")
    public ResponseEntity<String> saveTemp(@RequestParam("data") Float data) {
        Temperature n = new Temperature();
        n.setData(data);
        temperatureRepository.save(n);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/humid")
    public ResponseEntity<String> saveHumid(@RequestParam("data") Float data) {
        Humidity n = new Humidity();
        n.setData(data);
        humidityRepository.save(n);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/light")
    public ResponseEntity<String> saveBrightness(@RequestParam("data") Float data) {
        Brightness b = new Brightness();
        b.setData(data);
        brightnessRepository.save(b);
        return ResponseEntity.ok("Success");
    }


    @GetMapping("/get/humid")
    public ResponseEntity<List<Humidity>> getHumid() {
        List<Humidity> humidityList = humidityRepository.findAllRecentHumidityData();
        return ResponseEntity.ok(humidityList);
    }
}
