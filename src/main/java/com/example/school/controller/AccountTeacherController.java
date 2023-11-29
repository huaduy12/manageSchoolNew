package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.UserDto;
import com.example.school.entity.Teacher;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.account.UserTeacherService;
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
@RequestMapping("account/teacher")
public class AccountTeacherController {

    @Autowired
    UserTeacherService userTeacherService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping()
    public String homeTeacher(Model model, @RequestParam(value = "keyword",required = false) String keyword){
       return findPaginated(1,model,null,keyword);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") CreatingUserForm userForm ,
                       BindingResult result, Model model,
                       @ModelAttribute("usernameOrigin") String usernameOrigin,
                       @ModelAttribute("formAdd") String formAdd,
                       @ModelAttribute("formEdit") String formEdit,
                       @ModelAttribute("idTeacher") String idTeacher,
                       RedirectAttributes redirectAttributes,
                       @ModelAttribute("pageNo") int pageNo,
                       @RequestParam(value = "keyword",required = false) String keyword){

        UserDto userExist = userTeacherService.getUserExist(userForm.getUsername(),usernameOrigin);
        if(userExist != null){
            result.addError(new FieldError("userForm","username","Username đã tồn tại"));
        }
        if(result.hasErrors()){
            findPaginated(pageNo,model,userForm,keyword);
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
                return "redirect:/account/teacher?idTeacher=" + idTeacher;
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
            if(!formEdit.equals(" ")){
                return "redirect:/account/teacher/page/" + pageNo;
            }
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

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model,
                                CreatingUserForm creatingUserForm,
                                @RequestParam(value = "keyword",required = false) String keyword){

        if(pageNo <= 0) return "redirect:/account/teacher";
        int pageSize = Pagination.pageSize;
        if(keyword == null) keyword = " ";
        Page<UserDto> userDtos = userTeacherService.findPaginated(pageNo-1,pageSize,keyword);
        if(userDtos.getTotalElements() != 0 && pageNo > userDtos.getTotalPages()){
            return "redirect:/account/teacher/page/1?keyword="+keyword;
        }
        model.addAttribute("keyword",keyword);
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
        return "/manageAccount/user_teacher";
    }


}
