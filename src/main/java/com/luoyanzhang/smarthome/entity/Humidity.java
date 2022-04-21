package com.luoyanzhang.smarthome.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Humidity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "data")
    private Float data;

    @Column(name = "date_created")
    private Date date_created = new Date();

}
