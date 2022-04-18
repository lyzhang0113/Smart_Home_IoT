package com.luoyanzhang.smarthome.repository.mysql;

import com.luoyanzhang.smarthome.entity.mysql.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryMysql extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
