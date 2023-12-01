package com.example.school.controller.Teacher;

import com.example.school.configuration.Pagination;
import com.example.school.dto.ClassroomDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Teacher_class;
import com.example.school.entity.User;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.form.student.FormStudent;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.ClassroomService;
import com.example.school.service.TeacherClassService;
import com.example.school.service.TeacherService;
import com.example.school.service.account.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getHomeRoomClass(Model model,@RequestParam(value = "keyword",required = false) String keyword,
                                   @AuthenticationPrincipal EntityUserDetail userDetail){
        return findPaginated(1,model,keyword,userDetail,0);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model,
                                @RequestParam(value = "keyword",required = false) String keyword,
                                @AuthenticationPrincipal EntityUserDetail userDetail,
                                @RequestParam(value = "role",required = false) int role){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());
        if(pageNo <= 0) return "redirect:/manage/student";
        int pageSize = Pagination.pageSize;
        Page<ClassroomDto> classroomDtos = teacherClassService.getByIdRoleAndTeacher_id(role,teacherDto.getId(),pageNo-1,pageSize,keyword);

        if(classroomDtos.getTotalElements() != 0 && pageNo > classroomDtos.getTotalPages()){
            return "redirect:/teacher/homeroom/page/1?keyword="+keyword;
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("classroomDtos",classroomDtos);

        if(role == 0){
            model.addAttribute("confirmHomeroom",true);
        }else if(role == 1){
            model.addAttribute("confirmStuding",true);
        }


        return "/teacher/homeroomClass";
    }

    @GetMapping("/study")
    public String getStudyClass(Model model,@RequestParam(value = "keyword",required = false) String keyword,
                                @AuthenticationPrincipal EntityUserDetail userDetail){
        return findPaginated(1,model,keyword,userDetail,1);

    }

    @GetMapping("/score/{pageNo}")
    public String getStudyClassScore(@PathVariable(value = "pageNo",required = false) Integer pageNo
            ,@AuthenticationPrincipal EntityUserDetail userDetail, Model model,
                                     @RequestParam(value = "keyword",required = false) String keyword){

        if(pageNo == null) {
            pageNo =1;
        }
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());

        if(pageNo <= 0) return "redirect:/teacher/score";
        int pageSize = Pagination.pageSize;
        Page<Teacher_classDto> teacher_classDtos = teacherClassService.
                getByTeacher_idAndSubjectNotNull(teacherDto.getId(),pageNo-1,pageSize,keyword);

        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("keyword",keyword);
        model.addAttribute("teacher_classDtos",teacher_classDtos);

        return "/teacher/classroom_score";
    }
}
