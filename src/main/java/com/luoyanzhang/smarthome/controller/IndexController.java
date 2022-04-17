package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private SensorService sensorService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("msg", "Login/Register");
        return "index";
    }

    @GetMapping(value = {"/dashboard"})
    public String dashboard(@CookieValue(name = "UNAME") String username, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("temp_map", sensorService.getRecentTemperatureData());
        model.addAttribute("humid_map", sensorService.getRecentHumidityData());

        return "dashboard";
    }


}
