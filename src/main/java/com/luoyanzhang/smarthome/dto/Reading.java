package com.luoyanzhang.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Reading implements Serializable {
    private Float temperature;
    private Float humidity;
    private Float brightness;
}
