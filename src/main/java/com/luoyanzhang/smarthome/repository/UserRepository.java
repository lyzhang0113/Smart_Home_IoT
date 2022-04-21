package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.User;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
