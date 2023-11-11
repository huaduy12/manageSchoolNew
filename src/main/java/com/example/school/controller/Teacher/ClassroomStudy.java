package com.example.school.controller.Teacher;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Teacher_class;
import com.example.school.entity.User;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.ClassroomService;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import com.example.school.service.account.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class ClassroomStudy {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TeacherClassService teacherClassService;

    @GetMapping("/homeroom")
    public String getHomeRoomClass(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());
        List<Teacher_class> teacherClassServices = teacherClassService.getByIdRoleAndTeacher_id(0,teacherDto.getId());
        List<ClassroomDto> classroomDtos = new ArrayList<>();
        for (Teacher_class t:teacherClassServices) {
            ClassroomDto classroomDto = modelMapper.map(t.getClassroom(),ClassroomDto.class);
            classroomDtos.add(classroomDto);
        }
        model.addAttribute("classroomDtos",classroomDtos);
        FormClassroom formClassroom = new FormClassroom();
        formClassroom.setId(0);
        model.addAttribute("formClassroom",formClassroom);
        model.addAttribute("confirmHomeroom",true);
        return "/teacher/homeroomClass";
    }

    @GetMapping("/study")
    public String getStudyClass(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());
        List<Teacher_class> teacherClassServices = teacherClassService.getByIdRoleAndTeacher_id(1,teacherDto.getId());
        List<ClassroomDto> classroomDtos = new ArrayList<>();
        for (Teacher_class t:teacherClassServices) {
            ClassroomDto classroomDto = modelMapper.map(t.getClassroom(),ClassroomDto.class);
            classroomDtos.add(classroomDto);
        }
        model.addAttribute("classroomDtos",classroomDtos);
        FormClassroom formClassroom = new FormClassroom();
        formClassroom.setId(0);
        model.addAttribute("formClassroom",formClassroom);
        model.addAttribute("confirmStuding",true);
        return "/teacher/homeroomClass";
    }

    @GetMapping("/score")
    public String getStudyClassScore(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());

        List<Teacher_classDto> teacher_classDtos = teacherClassService.getByTeacher_idAndSubjectNotNull(teacherDto.getId());
        model.addAttribute("teacher_classDtos",teacher_classDtos);

        return "/teacher/classroom_score";
    }
}
