package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.ClassroomDto;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manage/assign")
public class TeachingAssignment {

    @Autowired
    private ClassroomService classroomService;
    @GetMapping()
    public String homeClass(Model model){
        return findPaginated(1,model);
    }
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model
    ){

        if(pageNo <= 0) return "redirect:/manage/assign";
        int pageSize = Pagination.pageSize;
        Page<ClassroomDto> classroomDtos = classroomService.findPaginated(pageNo-1,pageSize);

        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("classroomDtos",classroomDtos);

        // thông tin các lớp đang còn học để phục vụ cho form add, edit

           FormClassroom formClassroom = new FormClassroom();
            formClassroom.setId(0);
            model.addAttribute("formClassroom",formClassroom);

        model.addAttribute("showAssign",true);

        return "manage_class";
    }


}
