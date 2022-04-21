package com.luoyanzhang.smarthome.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@RedisHash(value = "User", timeToLive = 3600)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "last_ip_address")
    private String last_ip_address;

    @Column(name = "last_modified")
    private Date last_modified = new Date();

    @Column(name = "date_created")
    private Date date_created = new Date();
}
