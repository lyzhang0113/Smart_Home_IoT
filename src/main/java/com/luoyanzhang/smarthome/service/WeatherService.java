package com.luoyanzhang.smarthome.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luoyanzhang.smarthome.dto.Weather;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    public Weather getWeatherByIP(String IP) throws ParseException, URISyntaxException {
        final String uri = "http://api.weatherapi.com/v1/forecast.json?key=bcb8176380b14ffbbec194410222104&q=" + IP + "&days=7&aqi=no";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JSONObject jsonObject = JSON.parseObject(result);

        JSONObject location = jsonObject.getJSONObject("location");
        String loc = location.getString("name") + ", " + location.getString("region");

        JSONObject current = jsonObject.getJSONObject("current");
        JSONObject condition = current.getJSONObject("condition");
        LocalDate last_updated = LocalDate.parse(current.getString("last_updated"));
        Weather.Day today = new Weather.Day(
                LocalDate.now(),
                current.getFloat("temp_c"),
                current.getFloat("temp_f"),
                condition.getString("text"),
                new URI("http:" + condition.getString("icon")));


        JSONArray forecastday = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");
        List<Weather.Day> forcast_days = new ArrayList<>();
        for (int i = 0; i < forecastday.size(); i++) {
            JSONObject obj = forecastday.getJSONObject(i);
            JSONObject day_detail = obj.getJSONObject("day");
            Weather.Day day = new Weather.Day(
                    LocalDate.parse(obj.getString("date")),
                    day_detail.getFloat("avgtemp_c"),
                    day_detail.getFloat("avgtemp_f"),
                    day_detail.getJSONObject("condition").getString("text"),
                    new URI("http:" + day_detail.getJSONObject("condition").getString("icon")));
            forcast_days.add(day);
        }
        return new Weather(loc, last_updated, today, new Weather.Forecast(forcast_days));
    }
}
