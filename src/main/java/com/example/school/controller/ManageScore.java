package com.example.school.controller;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.ScoreDto;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.service.ClassroomService;
import com.example.school.service.ScoreService;
import com.example.school.service.SubjectService;
import com.example.school.service.TeacherClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage/score")
public class ManageScore {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private TeacherClassService teacherClassService;

    @GetMapping()
    public String homeClass(Model model){
        List<ClassroomDto> classroomDtos = classroomService.getClassRoom();
        model.addAttribute("classroomDtos",classroomDtos);
        FormClassroom formClassroom = new FormClassroom();
        formClassroom.setId(0);
        model.addAttribute("formClassroom",formClassroom);
        model.addAttribute("showScore",true);
        return "/manage_class";
    }

    @GetMapping("/idClass")
    public String showClassById(@RequestParam("id") String id, Model model){
        int idClass = 0;
        try{
            idClass = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/score";
        }
        // tìm những môn học của class có id
        List<Teacher_classDto> teacher_classDtos = subjectService.getSubjectsByClass_id(idClass);
        model.addAttribute("teacher_classDtos",teacher_classDtos);
        return "/subject_class_score";
    }

    @GetMapping("/subject_score")
    public String showScoreSubject(@RequestParam("id") String id, Model model){

        int idTeacher_class = 0;
        try{
            idTeacher_class = Integer.parseInt(id);

        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/score";
        }
        List<ScoreDto> scoreDtos = scoreService.findScoreByClassroomAndSubject(idTeacher_class);
        model.addAttribute("scoreDtos",scoreDtos);

        Teacher_classDto teacher_classDto = teacherClassService.findByIdDto(idTeacher_class);
        model.addAttribute("nameSubject",teacher_classDto.getSubject().getName());
        model.addAttribute("nameClassroom",teacher_classDto.getClassroom().getName());
        model.addAttribute("teacher_classId",teacher_classDto.getId());
        ScoreDto scoreDto = new ScoreDto();
        model.addAttribute("scoreDto",scoreDto);
        return "/student_score";
    }



    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("scoreDto")ScoreDto scoreDto,
                       BindingResult bindingResult , Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("teacher_classId") Integer teacher_classId){
        ScoreDto findScore = scoreService.findByIdDto(scoreDto.getId());
        if(bindingResult.hasErrors()){
            if(bindingResult.hasFieldErrors("oval_score")){
                redirectAttributes.addFlashAttribute("errorOvalScoreId",scoreDto.getId());
            }
            if(bindingResult.hasFieldErrors("score_15")){
                redirectAttributes.addFlashAttribute("errorScore15Id",scoreDto.getId());
            }
            if(bindingResult.hasFieldErrors("score_1_period")){
                redirectAttributes.addFlashAttribute("errorScore1Id",scoreDto.getId());
            }
            if(bindingResult.hasFieldErrors("test_score")){
                redirectAttributes.addFlashAttribute("errorScoreTestId",scoreDto.getId());
            }
            redirectAttributes.addFlashAttribute("errorScore","Vui lòng nhập điểm hợp lệ");
            if(teacher_classId ==0){
                return "redirect:/manage/student/score?id=" + findScore.getStudent().getId();
            }else {
                return "redirect:/manage/score/subject_score?id=" + teacher_classId;
            }

        }

        // nếu không có lỗi gì
        boolean isSuccess = scoreService.saveScoreSubject(scoreDto);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess", " Thay đổi thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
        }
        if(teacher_classId ==0){
            return "redirect:/manage/student/score?id=" + findScore.getStudent().getId();
        }else {
            return "redirect:/manage/score/subject_score?id=" + teacher_classId;
        }
    }


}
