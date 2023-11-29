package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Subject;
import com.example.school.entity.Teacher_class;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.service.SubjectService;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String homeClass(Model model, @RequestParam(value = "keyword",required = false) String keyword){
        return findPaginated(1,model,null,keyword);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("subjectDto") SubjectDto subjectDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form,@ModelAttribute("pageNo") int pageNo,
                       @RequestParam(value = "keyword",required = false) String keyword){

        if(bindingResult.hasErrors()){
            findPaginated(pageNo,model,subjectDto,keyword);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_subject";
        }
        subjectService.save(subjectDto);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/manage/subject/page/"+pageNo;
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
    public String teacherSubject(@RequestParam("id") String id,@RequestParam("page") int pageNo,
                                 Model model){
        int idSubject = 0;
        try {
            idSubject = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/subject";
        }
        if(pageNo <= 0) return "redirect:/manage/subject";
        int pageSize = Pagination.pageSize;
        Page<TeacherDto> teacherDtos = null;
        teacherDtos = teacherService.getTeacherBySubject_id(idSubject,pageNo-1,pageSize);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("id",idSubject);
        model.addAttribute("teachersDto",teacherDtos);

        return "subject/teacher_subject";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,Model model,
                                SubjectDto subjectDto,
                                @RequestParam(value = "keyword",required = false) String keyword
    ){

        if(pageNo <= 0) return "redirect:/manage/subject";
        int pageSize = Pagination.pageSize;
        Page<SubjectDto> subjectDtos = subjectService.findPaginated(pageNo-1,pageSize,keyword);
        if(subjectDtos.getTotalElements() != 0 && pageNo > subjectDtos.getTotalPages()){
            return "redirect:/manage/classroom/page/1?keyword="+keyword;
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("subjectDtos",subjectDtos);

        // thông tin các lớp đang còn học để phục vụ cho form add, edit
        if(subjectDto == null){
            subjectDto = new SubjectDto();
            subjectDto.setId(0);
            model.addAttribute("subjectDto",subjectDto);
        }else {
            model.addAttribute("subjectDto",subjectDto);
        }
        model.addAttribute("showSubject",true);
        return "manage_subject";
    }


}
