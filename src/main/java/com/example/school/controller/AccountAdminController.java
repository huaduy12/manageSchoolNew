package com.example.school.controller;

import com.example.school.dto.UserDto;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.account.UserAdminService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/account/admin")
public class AccountAdminController {

    @Autowired
    UserAdminService userAdminService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping ()
    public String homeAdmin(@AuthenticationPrincipal EntityUserDetail userDetail, Model model){
        List<User> users = userAdminService.getAllAccountAdmin();
        Type listType =  new TypeToken<List<UserDto>> () {}.getType ();
        List<UserDto> usersDto = modelMapper.map(users,listType);
        model.addAttribute("usersDto",usersDto);

        model.addAttribute("username",userDetail.getUsername());
        CreatingUserForm creatingUserForm = new CreatingUserForm();
        creatingUserForm.setId(0);
        model.addAttribute("userForm",creatingUserForm);
        return "/manageAccount/user_admin";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") CreatingUserForm userForm ,
                       BindingResult result, Model model,
                       @ModelAttribute("usernameOrigin") String usernameOrigin,
                       @ModelAttribute("formAdd") String formAdd,
                       @ModelAttribute("formEdit") String formEdit,
                       RedirectAttributes redirectAttributes){

        List<User> users = userAdminService.getAllAccountAdmin();
        Type listType =  new TypeToken<List<UserDto>> () {}.getType ();
        List<UserDto> usersDto = modelMapper.map(users,listType);

        UserDto userExist = userAdminService.getUserExist(userForm.getUsername(),usernameOrigin);
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
            return "/manageAccount/user_admin";
        }
        userAdminService.save(userForm);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/account/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("idUserDelete") int idUserDelete,Model model,
                             RedirectAttributes redirectAttributes){
        userAdminService.delete(idUserDelete);
        model.addAttribute("deleteToast", "Xóa thành công");
        redirectAttributes.addFlashAttribute("changeSuccess","Xóa thành công");
        return "redirect:/account/admin";
    }

    @PostMapping("/reset")
    public String resetUser(@ModelAttribute("idUserReset") int idUserReset,
                            RedirectAttributes redirectAttributes){
        User user =  userAdminService.resetPassword(idUserReset);
        redirectAttributes.addFlashAttribute("changeSuccess","Reset thành công");
        return "redirect:/account/admin";
    }


}
