package com.example.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
    @GetMapping("/showLoginPage")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/contact")
    public String showContactPage(){
        return "contactLogin";
    }
}
