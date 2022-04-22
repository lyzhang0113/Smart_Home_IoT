package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.dto.Weather;
import com.luoyanzhang.smarthome.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.text.ParseException;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<Weather> getWeather(@RequestParam(value = "q", defaultValue = "12180") String q) {
        try {
            return ResponseEntity.ok(weatherService.getWeatherByIP(q));
        } catch (ParseException | URISyntaxException exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
