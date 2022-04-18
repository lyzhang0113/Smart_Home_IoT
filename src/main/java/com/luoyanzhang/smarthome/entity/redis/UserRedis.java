package com.luoyanzhang.smarthome.entity.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "User", timeToLive = 3600)
@Getter
@Setter
public class UserRedis implements Serializable {
    private Integer id;
    private String username;
}
