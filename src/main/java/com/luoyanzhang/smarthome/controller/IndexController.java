package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.User;
import com.luoyanzhang.smarthome.service.SensorService;
import com.luoyanzhang.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class IndexController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("msg", "Login/Register");
        return "index";
    }

    @GetMapping(value = {"/dashboard"})
    public String dashboard(@CookieValue(name = "UID") String uid, Model model) {
        User u = userService.getUserByID(Integer.parseInt(uid));
        TreeMap<Date, Float> temp_map = sensorService.getRecentTemperatureData();
        TreeMap<Date, Float> humid_map = sensorService.getRecentHumidityData();

        model.addAttribute("username", u.getUsername());
        model.addAttribute("temp_map", temp_map);
        model.addAttribute("curr_temp", temp_map.firstEntry().getValue());
        model.addAttribute("curr_humid", humid_map.firstEntry().getValue());
        model.addAttribute("humid_map", humid_map);

        return "pages/dashboard";
    }


}
