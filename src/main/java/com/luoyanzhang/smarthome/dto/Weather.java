package com.luoyanzhang.smarthome.dto;

import lombok.Data;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class Weather {

    final String location;
    final Date last_update;
    final Day today;
    final Forecast forecast;

    @Data
    public static class Day {
        final LocalDate date;
        final String dayofweek;
        final Float temp_c;
        final Float temp_f;
        final Float humidity;
        final String condition;
        final URI icon;
    }

    @Data
    public static class Forecast {
        final List<Day> days;
    }
}
