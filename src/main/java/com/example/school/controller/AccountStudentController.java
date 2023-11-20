package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.UserDto;
import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.service.account.UserStudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("account/student")
public class AccountStudentController {

    @Autowired
    UserStudentService userStudentService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping()
    public String homeTeacher(Model model){
        return findPaginated(1,model,null);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") CreatingUserForm userForm ,
                       BindingResult result, Model model,
                       @ModelAttribute("usernameOrigin") String usernameOrigin,
                       @ModelAttribute("formAdd") String formAdd,
                       @ModelAttribute("formEdit") String formEdit,
                       @ModelAttribute("idStudent") String idStudent,
                       @ModelAttribute("pageNo") int pageNo,
                       RedirectAttributes redirectAttributes){

        UserDto userExist = userStudentService.getUserExist(userForm.getUsername(),usernameOrigin);
        if(userExist != null){
            result.addError(new FieldError("userForm","username","Username đã tồn tại"));
        }
        if(result.hasErrors()){

            findPaginated(pageNo,model,userForm);
            if(formAdd != null && !formAdd.trim().equals("")){
                model.addAttribute("errorFormAdd","Có lỗi xảy ra");
            }
            if(formEdit != null && !formEdit.trim().equals("")){
                model.addAttribute("errorFormEdit","Có lỗi xảy ra");
            }
            if(idStudent != null && !idStudent.trim().equals("") && !idStudent.trim().equals("0")){
                if(userExist != null){
                    redirectAttributes.addFlashAttribute("errorUsernameExist","Username đã tồn tại");
                }else {
                    redirectAttributes.addFlashAttribute("errorUsername","Vui lòng nhập ít nhất 8 ký tự");
                    redirectAttributes.addFlashAttribute("errorPassword","Vui lòng nhập ít nhất 8 ký tự");
                }
                return "redirect:/account/student?idStudent=" + idStudent;
            }
            return "/manageAccount/user_student";
        }

        int idAccountStudent =0;
        if(idStudent != null && !idStudent.trim().equals("")){
            try {
                idAccountStudent= Integer.parseInt(idStudent);
                Student student = userStudentService.saveAccountStudentId(userForm,idAccountStudent);
                if(student != null){
                    redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
                    return "redirect:/manage/student";
                }
                else{
                    redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
                    return "redirect:/account/student";
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            userStudentService.save(userForm);
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
            if(!formEdit.equals(" ")){
                return "redirect:/account/student/page/" + pageNo;
            }
            return "redirect:/account/student";
        }
        return "redirect:/account/student";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("idUserDelete") int idUserDelete,Model model,
                             RedirectAttributes redirectAttributes){
        userStudentService.delete(idUserDelete);
        redirectAttributes.addFlashAttribute("changeSuccess","Xóa thành công");
        return "redirect:/account/student";
    }

    @PostMapping("/reset")
    public String resetUser(@ModelAttribute("idUserReset") int idUserReset,
                            RedirectAttributes redirectAttributes){
        User user =  userStudentService.resetPassword(idUserReset);
        redirectAttributes.addFlashAttribute("changeSuccess","Reset thành công");
        return "redirect:/account/student";
    }

    // khóa tài khoản
    @PostMapping("/block")
    public String blockUser(@ModelAttribute("idUserBlock") int idUserBlock,
                            RedirectAttributes redirectAttributes){
        User user =  userStudentService.blockUser(idUserBlock);
        redirectAttributes.addFlashAttribute("changeSuccess","Thành công");
        return "redirect:/account/student";
    }

    @PostMapping("/enable")
    public String enableUser(@ModelAttribute("idUserEnable") int idUserEnable,
                             RedirectAttributes redirectAttributes){
        User user =  userStudentService.enableUser(idUserEnable);
        redirectAttributes.addFlashAttribute("changeSuccess","Thành công");
        return "redirect:/account/student";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model,
                                CreatingUserForm creatingUserForm){

        if(pageNo <= 0) return "redirect:/account/student";
        int pageSize = Pagination.pageSize;
        Page<UserDto> userDtos = userStudentService.findPaginated(pageNo-1,pageSize);

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
        return "/manageAccount/user_student";
    }
}
