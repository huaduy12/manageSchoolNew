package com.example.school.controller;

import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Teacher_class;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.service.SubjectService;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage/subject")
public class ManageSubject {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping()
    public String homeClass(Model model){
        List<SubjectDto> subjectDtos = subjectService.getAllSubject();
        model.addAttribute("subjectDtos",subjectDtos);

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(0);
        model.addAttribute("subjectDto",subjectDto);
        model.addAttribute("showSubject",true);

        return "/manage_subject";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("subjectDto") SubjectDto subjectDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form){

        System.out.println(subjectDto);
        List<SubjectDto> subjectDtos = subjectService.getAllSubject();
        //  System.out.println(formManageTeacher);
        if(bindingResult.hasErrors()){
            model.addAttribute("subjectDtos",subjectDtos);
            model.addAttribute("subjectDto",subjectDto);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_subject";
        }
        subjectService.save(subjectDto);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/manage/subject";
    }


    @PostMapping("/block")
    public String blockSubject(@ModelAttribute("idBlock") int idBlock,Model model,
                           RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","thành công");
        subjectService.block(idBlock);
        return "redirect:/manage/subject";
    }

    @PostMapping("/open")
    public String openSubject(@ModelAttribute("idOpen") int idOpen,Model model,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","thành công");
        subjectService.open(idOpen);
        return "redirect:/manage/subject";
    }

    // xóa những môn học thêm nhầm, hoặc không có liên quan tới các bảng nếu muốn
    @PostMapping("/delete")
    public String deleteSubject(@ModelAttribute("idDelete") int idDelete,Model model,
                              RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","thành công");
        subjectService.delete(idDelete);
        return "redirect:/manage/subject";
    }

    @GetMapping("/teacher")
    public String teacherSubject(@RequestParam("id") String id, Model model){
        int idSubject = 0;
        try {
            idSubject = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/subject";
        }
        List<TeacherDto> teacherDtos = null;
        teacherDtos = teacherService.getTeacherBySubject_id(idSubject);
        model.addAttribute("teachersDto",teacherDtos);

        FormManageTeacher formManageTeacher = new FormManageTeacher();
        formManageTeacher.setId(0);
        model.addAttribute("formManageTeacher",formManageTeacher);

        return "manage_teacher";
    }
}
