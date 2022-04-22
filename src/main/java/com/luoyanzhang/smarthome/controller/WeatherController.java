package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.dto.Weather;
import com.luoyanzhang.smarthome.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<Weather> getWeather(@RequestParam(value = "q", defaultValue = "12180") String q) {
        return ResponseEntity.ok(weatherService.getWeatherByIP(q));
    }

}
