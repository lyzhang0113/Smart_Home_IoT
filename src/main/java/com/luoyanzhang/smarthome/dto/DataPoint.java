package com.luoyanzhang.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DataPoint {
    private Date date;
    private Float data;
}
