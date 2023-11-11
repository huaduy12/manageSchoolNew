package com.example.school.controller;

import com.example.school.dto.UserDto;
import com.example.school.entity.Teacher;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.service.account.UserTeacherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("account/teacher")
public class AccountTeacherController {

    @Autowired
    UserTeacherService userTeacherService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping()
    public String homeTeacher(Model model){
        List<User> users = userTeacherService.getAllAccountTeacher();
        Type listType =  new TypeToken<List<UserDto>>() {}.getType ();
        List<UserDto> usersDto = modelMapper.map(users,listType);
//         System.out.println("Đây" +userDtos);
        model.addAttribute("usersDto",usersDto);

        CreatingUserForm creatingUserForm = new CreatingUserForm();
        creatingUserForm.setId(0);
        model.addAttribute("userForm",creatingUserForm);

        return "manageAccount/user_teacher";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") CreatingUserForm userForm ,
                       BindingResult result, Model model,
                       @ModelAttribute("usernameOrigin") String usernameOrigin,
                       @ModelAttribute("formAdd") String formAdd,
                       @ModelAttribute("formEdit") String formEdit,
                       @ModelAttribute("idTeacher") String idTeacher,
                       RedirectAttributes redirectAttributes){

        List<User> users = userTeacherService.getAllAccountTeacher();
        Type listType =  new TypeToken<List<UserDto>> () {}.getType ();
        List<UserDto> usersDto = modelMapper.map(users,listType);

        UserDto userExist = userTeacherService.getUserExist(userForm.getUsername(),usernameOrigin);
        if(userExist != null){
            result.addError(new FieldError("userForm","username","Username đã tồn tại"));
        }
        if(result.hasErrors()){
            model.addAttribute("usersDto",usersDto);
            model.addAttribute("userForm",userForm);
            // System.out.println("Lỗi ở đây" + result.getFieldError());
            if(formAdd != null && !formAdd.trim().equals("")){
                model.addAttribute("errorFormAdd","Có lỗi xảy ra");
            }
            if(formEdit != null && !formEdit.trim().equals("")){
                model.addAttribute("errorFormEdit","Có lỗi xảy ra");
            }
            if(idTeacher != null && !idTeacher.trim().equals("") && !idTeacher.trim().equals("0")){
                if(userExist != null){
                    redirectAttributes.addFlashAttribute("errorUsernameExist","Username đã tồn tại");
                }else {
                    redirectAttributes.addFlashAttribute("errorUsername","Vui lòng nhập ít nhất 8 ký tự");
                    redirectAttributes.addFlashAttribute("errorPassword","Vui lòng nhập ít nhất 8 ký tự");
                }
                return "redirect:/account/teacher?id=" + idTeacher;
            }
            return "/manageAccount/user_teacher";
        }
        int idAcTeacher =0;
        if(idTeacher != null && !idTeacher.trim().equals("")){
            try {
                idAcTeacher= Integer.parseInt(idTeacher);
                Teacher teacher = userTeacherService.saveAccountTeacherId(userForm,idAcTeacher);
                if(teacher != null){
                    redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
                    return "redirect:/manage/teacher";
                }
                else{
                    redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
                    return "redirect:/account/teacher";
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            userTeacherService.save(userForm);
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
            return "redirect:/account/teacher";
        }
        return "redirect:/account/teacher";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("idUserDelete") int idUserDelete,Model model,
                             RedirectAttributes redirectAttributes){
        userTeacherService.delete(idUserDelete);
        redirectAttributes.addFlashAttribute("changeSuccess","Xóa thành công");
        return "redirect:/account/teacher";
    }

    @PostMapping("/reset")
    public String resetUser(@ModelAttribute("idUserReset") int idUserReset,
                            RedirectAttributes redirectAttributes){
        User user =  userTeacherService.resetPassword(idUserReset);
        redirectAttributes.addFlashAttribute("changeSuccess","Reset thành công");
        return "redirect:/account/teacher";
    }

    // khóa tài khoản
    @PostMapping("/block")
    public String blockUser(@ModelAttribute("idUserBlock") int idUserBlock,
                            RedirectAttributes redirectAttributes){
        User user =  userTeacherService.blockUser(idUserBlock);
        redirectAttributes.addFlashAttribute("blockSuccess","Thành công");
        return "redirect:/account/teacher";
    }

    @PostMapping("/enable")
    public String enableUser(@ModelAttribute("idUserEnable") int idUserEnable,
                             RedirectAttributes redirectAttributes){
        User user =  userTeacherService.enableUser(idUserEnable);
        redirectAttributes.addFlashAttribute("enableSuccess","Thành công");
        return "redirect:/account/teacher";
    }
}
