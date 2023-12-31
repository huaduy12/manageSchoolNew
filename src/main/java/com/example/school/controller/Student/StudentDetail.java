package com.example.school.controller.Student;

import com.example.school.dto.ScoreDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.User;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.ScoreService;
import com.example.school.service.StudentService;
import com.example.school.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentDetail {

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/subject")
    public String showSubject(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){

        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        System.out.println("StudentDto: " + studentDto);
        List<ScoreDto> scoreDtos = scoreService.getAllStudentId(studentDto.getId());

        model.addAttribute("scoreDtos",scoreDtos);

        return "/student/show_subject";
    }
}
