package com.luoyanzhang.smarthome.repository.redis;

import com.luoyanzhang.smarthome.entity.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryRedis extends CrudRepository<UserRedis, Integer> {

}
