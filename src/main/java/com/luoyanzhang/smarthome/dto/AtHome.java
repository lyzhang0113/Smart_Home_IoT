package com.luoyanzhang.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AtHome {
    private Boolean atHome;
    private Date last_report;
}
