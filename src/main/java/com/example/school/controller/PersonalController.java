package com.example.school.controller;

import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.UserDto;
import com.example.school.entity.RoleTeacher;
import com.example.school.entity.StudentCard;
import com.example.school.entity.Teacher;
import com.example.school.entity.User;
import com.example.school.form.student.FormProfileStudent;
import com.example.school.form.student.FormStudent;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.StudentService;
import com.example.school.service.TeacherService;
import com.example.school.service.account.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/user")
public class PersonalController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    // thông tin của admin và manage do tính chất thoong tin khác nhau
    // nên cần làm thêm thông tin về student vì thông tin kh nhau
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());
        //tránh trường hợp người đó tạo tài khoản nhưng chưa co thong tin cá nhân
        if(teacherDto != null){
            model.addAttribute("teacherDto",teacherDto);
            model.addAttribute("formManageTeacher",teacherDto);
        }
        if(teacherDto == null){
            FormManageTeacher formManageTeacher = new FormManageTeacher();
            formManageTeacher.setId(0);
            model.addAttribute("formManageTeacher",formManageTeacher);
        }
        return "/personal/profile";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formManageTeacher") FormManageTeacher formManageTeacher,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @AuthenticationPrincipal EntityUserDetail userDetail){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        TeacherDto teacherDto = teacherService.findByUserIdDto(user.getId());

        //tránh trường hợp người đó tạo tài khoản nhưng chưa co thong tin cá nhân
        if(bindingResult.hasErrors()){
           model.addAttribute("teacherDto",teacherDto);
           model.addAttribute("formManageTeacher)",formManageTeacher);
           redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
           return "/personal/profile";
        }
        if(teacherDto == null){
            Collection<? extends GrantedAuthority> authorities  =  userDetail.getAuthorities();
           List<String> listAuthen = new ArrayList<>();
            for (GrantedAuthority g:authorities) {
                listAuthen.add(g.getAuthority());
            }
            int count = 0;
            for (String s:listAuthen) {
                if(s.equals("ROLE_ADMIN")){
                    count++;
                }
            }
            if(count > 0){
                formManageTeacher.setRole(RoleTeacher.Admin);
            }
        }
        formManageTeacher.setUser(user);
        teacherService.saveProfile(formManageTeacher);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/user/profile";
    }


    @GetMapping("/profileStudent")
    public String profileStudent(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){

        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        StudentCard studentCard = studentService.getStudentCardByStudentId(studentDto.getId());
        if(studentDto != null){
            model.addAttribute("studentDto",studentDto);
            model.addAttribute("studentCard",studentCard);
            model.addAttribute("formProfileStudent",studentDto);
        }
        //tránh trường hợp người đó tạo tài khoản nhưng chưa co thong tin cá nhân
        // bắt buộc học sinh phải tạo thông tin trước khi update qua admin
        if(studentDto == null){
            FormProfileStudent formProfileStudent = new FormProfileStudent();
            formProfileStudent.setId(0);
            model.addAttribute("formProfileStudent",formProfileStudent);
            model.addAttribute("hideButtonUpdate",true);
        }
        return "/student/profileStudent";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@Valid @ModelAttribute("formProfileStudent") FormProfileStudent formProfileStudent,
                              BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal EntityUserDetail userDetail){
        String username = userDetail.getUsername();
        User user = userService.getUserByUsername(username);
        StudentDto studentDto = studentService.findByUser_id(user.getId());

        //tránh trường hợp người đó tạo tài khoản nhưng chưa co thong tin cá nhân
        if(bindingResult.hasErrors()){
            model.addAttribute("studentDto",studentDto);
            model.addAttribute("formProfileStudent",formProfileStudent);
            redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
            return "/student/profileStudent";
        }

        studentService.saveProfileStudent(formProfileStudent);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/user/profileStudent";
    }

    @GetMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){

        return "/personal/change-password";
    }

    @PostMapping("/savePassword")
    public String savePassword(@ModelAttribute("passwordOld") String passOld,
                               @ModelAttribute("passwordNew") String passNew,
                               @ModelAttribute("passwordConfirm") String passConfirm,
                       Model model, RedirectAttributes redirectAttributes,
                       @AuthenticationPrincipal EntityUserDetail userDetail){
        String username = userDetail.getUsername();
        boolean isSuccess = userService.changePassword(passOld,passNew,passConfirm,username);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
        }

        return "redirect:/user/change-password";
    }

}
