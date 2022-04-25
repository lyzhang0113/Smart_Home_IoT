package com.luoyanzhang.smarthome.service;

import com.luoyanzhang.smarthome.entity.User;
import com.luoyanzhang.smarthome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "User", key = "#UID")
    public User getUserByID(Integer UID) {
        System.out.println("Cached to Redis");
        return userRepository.findById(UID).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @CachePut(value = "User", key = "#UID", unless = "#IP.equals(\"0:0:0:0:0:0:0:1\")")
    public User updateIpToUserByID(String IP, Integer UID) {
        User u = userRepository.findById(UID).orElseThrow(RuntimeException::new);
//        if (IP.equals("0:0:0:0:0:0:0:1")) return u;
        u.setLast_ip_address(IP);
        return userRepository.save(u);
    }

    public Integer getUIDByUsername(String uname) {
        User u = userRepository.findByUsername(uname);
        if (u == null) return null;
        return u.getId();
    }

}
