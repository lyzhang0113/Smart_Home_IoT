package com.luoyanzhang.smarthome.controller;

import com.luoyanzhang.smarthome.entity.User;
//import com.luoyanzhang.smarthome.repository.redis.UserRepositoryRedis;
import com.luoyanzhang.smarthome.service.UserService;
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
    private UserService userService;

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
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Integer UID = userService.getUIDByUsername(username);
        if (UID == null) {
            model.addAttribute("msg", "User Not Found!");
            return "login";
        }
        User u = userService.getUserByID(UID);
        if (!passwordEncoder.matches(password, u.getPassword())) {
            model.addAttribute("msg", "Incorrect Credentials!");
            return "login";
        }
        userService.updateIpToUserByID(request.getRemoteAddr(), UID);

        Cookie uid = new Cookie("UID", u.getId().toString());
        uid.setMaxAge(24 * 60 * 60); // one day
        response.addCookie(uid);
        return "redirect:dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie uid = new Cookie("UID", null);
        uid.setMaxAge(0);
        response.addCookie(uid);
        return "redirect:index";
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
        Integer UID = userService.getUIDByUsername(username);
        if (UID != null) {
            model.addAttribute("msg", "User Already Exist!");
            return "register";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User n = new User();
        n.setUsername(username);
        n.setPassword(passwordEncoder.encode(password));
        userService.saveUser(n);

        model.addAttribute("msg", "Success! Please Log in.");
        return "redirect:login";
    }

}
