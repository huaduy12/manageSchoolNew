package com.example.school.controller;

import com.example.school.dto.UserDto;
import com.example.school.entity.User;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.account.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"home","/"})
public class HomeController {

    @Autowired
    private UserAdminService userAdminService;
    @GetMapping()
    public String home(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        String username = userDetail.getUsername();
        UserDto userDto = userAdminService.findByUsername(username);
        model.addAttribute("userDto",userDto);
        return "index";
    }

}


