package com.example.school.controller;

import com.example.school.dto.ClassroomDto;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manage/assign")
public class TeachingAssignment {

    @Autowired
    private ClassroomService classroomService;
    @GetMapping()
    public String homeClass(Model model){
        List<ClassroomDto> classroomDtos = classroomService.getClassRoom();
        model.addAttribute("classroomDtos",classroomDtos);
        FormClassroom formClassroom = new FormClassroom();
        formClassroom.setId(0);
        model.addAttribute("formClassroom",formClassroom);
        model.addAttribute("showAssign",true);
        return "/manage_class";
    }


}
