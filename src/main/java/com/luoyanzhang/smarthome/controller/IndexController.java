package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.dto.AtHome;
import com.luoyanzhang.smarthome.dto.DataPoint;
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
import java.util.List;
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
        Reading yesterdayAvgReading = sensorService.getYesterdayAvgSensorReading();
        AtHome atHome = sensorService.isUserAtHome(10);
        Weather weather = weatherService.getWeatherByIP(u.getLast_ip_address());
        List<DataPoint> last24HourTemperature = sensorService.getLast24HourTemperature();
        List<DataPoint> last24HourHumidity = sensorService.getLast24HourHumidity();
        List<DataPoint> last24HourBrightness = sensorService.getLast24HourBrightness();
        System.out.println(last24HourTemperature);

        model.addAttribute("weather", weather);
        model.addAttribute("username", u.getUsername());
        model.addAttribute("curr_reading", recentReading);
        model.addAttribute("yesterday_reading", yesterdayAvgReading);
        model.addAttribute("atHome", atHome);
        model.addAttribute("dayofweek", LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US));
        model.addAttribute("last_24h_temp_map", last24HourTemperature);
        model.addAttribute("last_24h_humid_map", last24HourHumidity);
        model.addAttribute("last_24h_bright_map", last24HourBrightness);

        return "pages/dashboard";
    }


}
