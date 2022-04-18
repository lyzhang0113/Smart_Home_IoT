package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.mysql.User;
import com.luoyanzhang.smarthome.entity.redis.UserRedis;
import com.luoyanzhang.smarthome.repository.mysql.UserRepositoryMysql;
import com.luoyanzhang.smarthome.repository.redis.UserRepositoryRedis;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserRepositoryMysql userRepositoryMysql;

    @Autowired
    private UserRepositoryRedis userRepositoryRedis;

    @GetMapping("/login")
    public String loginPage(Model model) {
        // Already Logged in
        if (model.containsAttribute("id")) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpServletResponse response) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User u = userRepositoryMysql.findByUsername(username);
        if (u == null) {
            model.addAttribute("msg", "User Not Found!");
            return "login";
        }
        if (!passwordEncoder.matches(password, u.getPassword())) {
            model.addAttribute("msg", "Incorrect Credentials!");
            return "login";
        }
        Cookie uid = new Cookie("UID", u.getId().toString());
        uid.setMaxAge(24 * 60 * 60); // one day
        response.addCookie(uid);
        UserRedis user = new UserRedis();
        BeanUtils.copyProperties(u, user);
        userRepositoryRedis.save(user);
        return "redirect:dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpServletRequest request) {
        // Already Logged in
        if (WebUtils.getCookie(request, "UID") != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User n = new User();
        n.setUsername(username);
        n.setPassword(passwordEncoder.encode(password));
        userRepositoryMysql.save(n);

        model.addAttribute("msg", "Success! Please Log in.");
        return "redirect:login";
    }

}
