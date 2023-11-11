package com.example.school.controller;

import com.example.school.dto.TeacherDto;
import com.example.school.entity.Teacher_class;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage/teacher")
public class ManageTeacher {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherClassService teacherClassService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public String homeTeacher(Model model){
        List<TeacherDto> teachersDto = teacherService.teacherList();
        model.addAttribute("teachersDto",teachersDto);
      //  System.out.println(teachersDto);
        FormManageTeacher formManageTeacher = new FormManageTeacher();
        formManageTeacher.setId(0);
        model.addAttribute("formManageTeacher",formManageTeacher);
        return "manage_teacher";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formManageTeacher") FormManageTeacher formManageTeacher,
                       BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form){


        List<TeacherDto> teachersDto = teacherService.teacherList();
      //  System.out.println(formManageTeacher);
        if(bindingResult.hasErrors()){
            model.addAttribute("teachersDto",teachersDto);
            model.addAttribute("formManageTeacher",formManageTeacher);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_teacher";
        }
        teacherService.save(formManageTeacher);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/manage/teacher";
    }

    @PostMapping("/rest")
    public String restUser(@ModelAttribute("idRest") int idRest,Model model,
                             RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","thành công");
        teacherService.rest(idRest);
        return "redirect:/manage/teacher";
    }

    @PostMapping("/comeback")
    public String comebackUser(@ModelAttribute("idComeback") int idComeback,Model model,
                             RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","Xóa thành công");
        teacherService.comeback(idComeback);
        return "redirect:/manage/teacher";
    }


    @GetMapping("/classDetail")
    public String showClassStudy(@RequestParam("id") String id_teacher,@RequestParam("role") String role_id, Model model){

        // role: 0- GVCN, 1- GV bộ môn
        int idTeacher = 0;
        int role = -1;
        try{
            idTeacher = Integer.parseInt(id_teacher);
            role = Integer.parseInt(role_id);
        }catch (NumberFormatException n){
            return "redirect:/manage/teacher";
        }
        if(role ==0){
            // giáo viên CNhiem
            List<Teacher_class> teacher_classes = teacherClassService.getByIdRoleAndTeacher_id(0,idTeacher);
            model.addAttribute("teacher_classes",teacher_classes);
        } else if(role ==1){
            // giáo viên bo môn có thể bao gồm cả giáo viên chủ nhiệm
            List<Teacher_class> teacher_classes = teacherClassService.getByTeacher_id(idTeacher);
            model.addAttribute("teacher_classes",teacher_classes);
         //   System.out.println(teacher_classes);
        }else{
            return "redirect:/manage/teacher";
        }
        return "homeroom_class";
    }



}
