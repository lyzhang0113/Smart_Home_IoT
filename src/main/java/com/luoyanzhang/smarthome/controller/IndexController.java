package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.dto.Reading;
import com.luoyanzhang.smarthome.dto.Weather;
import com.luoyanzhang.smarthome.entity.User;
import com.luoyanzhang.smarthome.service.SensorService;
import com.luoyanzhang.smarthome.service.UserService;
import com.luoyanzhang.smarthome.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
public class IndexController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("msg", "Login/Register");
        return "index";
    }

    @GetMapping(value = {"/dashboard"})
    public String dashboard(@CookieValue(name = "UID") String uid, Model model) {
        User u = userService.getUserByID(Integer.parseInt(uid));
        Reading recentReading = sensorService.getMostRecentSensorReading();
        Weather weather = weatherService.getWeatherByIP(u.getLast_ip_address());

        model.addAttribute("weather", weather);
        model.addAttribute("username", u.getUsername());
        model.addAttribute("curr_reading", recentReading);
        model.addAttribute("dayofweek", LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US));

        return "pages/dashboard";
    }


}
