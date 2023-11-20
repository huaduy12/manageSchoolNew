package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.ClassroomDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Teacher_class;
import com.example.school.form.student.FormStudent;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
       return findPaginated(1,model,null);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formManageTeacher") FormManageTeacher formManageTeacher,
                       BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form,@ModelAttribute("pageNo") int pageNo){

        if(bindingResult.hasErrors()){
            findPaginated(pageNo,model,formManageTeacher);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_teacher";
        }
        teacherService.save(formManageTeacher);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/manage/teacher/page/"+ pageNo;
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

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,Model model,
                                FormManageTeacher formManageTeacher
    ){

        if(pageNo <= 0) return "redirect:/manage/teacher";
        int pageSize = Pagination.pageSize;
        Page<TeacherDto> teacherDtos = teacherService.findPaginated(pageNo-1,pageSize);

        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("teacherDtos",teacherDtos);

        // thông tin các lớp đang còn học để phục vụ cho form add, edit
        if(formManageTeacher == null){
            formManageTeacher = new FormManageTeacher();
            formManageTeacher.setId(0);
            model.addAttribute("formManageTeacher",formManageTeacher);
        }else {
            model.addAttribute("formManageTeacher",formManageTeacher);
        }


        return "manage_teacher";
    }


}
