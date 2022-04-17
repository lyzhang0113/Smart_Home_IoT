package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
