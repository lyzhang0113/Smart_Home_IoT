package com.luoyanzhang.smarthome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("msg", "Login/Register");
        return "index";
    }

    @GetMapping(value = {"/dashboard"})
    public String dashboard(@CookieValue(name = "UNAME") String username, Model model) {
        model.addAttribute("username", username);
        return "dashboard";
    }


}
