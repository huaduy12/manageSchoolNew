package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.UserDto;
import com.example.school.entity.User;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.account.UserAdminService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
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
        return findPaginated(1,model,userDetail,null);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") CreatingUserForm userForm ,
                       BindingResult result, Model model,
                       @ModelAttribute("usernameOrigin") String usernameOrigin,
                       @ModelAttribute("formAdd") String formAdd,
                       @ModelAttribute("formEdit") String formEdit,
                       RedirectAttributes redirectAttributes,
                       @AuthenticationPrincipal EntityUserDetail userDetail,
                       @ModelAttribute("pageNo") int pageNo ){

        UserDto userExist = userAdminService.getUserExist(userForm.getUsername(),usernameOrigin);
        if(userExist != null){
            result.addError(new FieldError("userForm","username","Username đã tồn tại"));
        }
        if(result.hasErrors()){
            findPaginated(pageNo,model,userDetail,userForm);

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
        if(formEdit != null){
            return "redirect:/account/admin/page/" +pageNo ;

        }
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

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model,
                                @AuthenticationPrincipal EntityUserDetail userDetail,
                                CreatingUserForm creatingUserForm){

        if(pageNo <= 0) return "redirect:/account/admin";
        int pageSize = Pagination.pageSize;
        Page<UserDto> userDtos = userAdminService.findPaginated(pageNo-1,pageSize);

        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("userDtos",userDtos);

        // thông tin các lớp đang còn học để phục vụ cho form add, edit
        if(creatingUserForm == null){
            creatingUserForm = new CreatingUserForm();
            creatingUserForm.setId(0);
            model.addAttribute("userForm",creatingUserForm);
        }else {
            model.addAttribute("userForm",creatingUserForm);

        }
        model.addAttribute("username",userDetail.getUsername());

        return "/manageAccount/user_admin";
    }

}
