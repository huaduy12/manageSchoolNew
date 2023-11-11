package com.example.school.exeptionHandle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("error")
public class ErrorHandle {
    @GetMapping("/showPage403")
    public String showPage403(){
        return "error/page-403";
    }
}
