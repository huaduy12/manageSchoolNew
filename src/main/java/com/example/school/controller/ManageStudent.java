package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.ClassroomDto;
import com.example.school.dto.ScoreDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Parent;
import com.example.school.entity.Student;
import com.example.school.entity.StudentCard;
import com.example.school.form.student.FormStudent;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.service.ClassroomService;
import com.example.school.service.ScoreService;
import com.example.school.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/manage/student")
public class ManageStudent {
    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ScoreService scoreService;

    @GetMapping()
    public String homeStudent(Model model,@RequestParam(value = "keyword",required = false) String keyword){
        return findPaginated(1,model,null,keyword);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,Model model,FormStudent formStudent,
                                @RequestParam(value = "keyword",required = false) String keyword){

        if(pageNo <= 0) return "redirect:/manage/student";
        int pageSize = Pagination.pageSize;
        Page<StudentDto> studentDtos = studentService.findPaginated(pageNo-1,pageSize,keyword);
        if(studentDtos.getTotalElements() != 0 && pageNo > studentDtos.getTotalPages()){
            return "redirect:/manage/student/page/1?keyword="+keyword;
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("studentDtos",studentDtos);
        // thông tin các lớp đang còn học để phục vụ cho form add, edit
        List<ClassroomDto> classroomsDto = classroomService.getClassRoomStudying();
        model.addAttribute("classroomsDto",classroomsDto);
        if(formStudent == null){
            formStudent = new FormStudent();
            formStudent.setId(0);
            model.addAttribute("formStudent",formStudent);
        }else {
            model.addAttribute("formStudent",formStudent);
        }


        return "manage_student";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formStudent") FormStudent formStudent,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form,
                       @ModelAttribute("pageNo") int pageNo
                        ,@RequestParam(value = "keyword",required = false) String keyword){


        if(bindingResult.hasErrors()){
            findPaginated(pageNo,model,formStudent,keyword);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_student";
        }
        studentService.save(formStudent);
        redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        return "redirect:/manage/student/page/"+pageNo;
    }

    @PostMapping("/delete")
    public String deleteStudent(@ModelAttribute("idDeleteStudent") int idDeleteStudent,
                                RedirectAttributes redirectAttributes){
        Boolean isDelete = studentService.delete(idDeleteStudent);
        if(isDelete){
            redirectAttributes.addFlashAttribute("changeSuccess", " Xóa thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail", " Xóa thất bại");
        }
        return "redirect:/manage/student";
    }

    @PostMapping("/rest")
    public String restStudent(@ModelAttribute("idRestStudent") int idRestStudent,
                                RedirectAttributes redirectAttributes){
        Boolean isRest = studentService.restStudent(idRestStudent);
        if(isRest){
            redirectAttributes.addFlashAttribute("changeSuccess", " Thay đổi thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
        }
        return "redirect:/manage/student";
    }

    @PostMapping("/comeback")
    public String comebackStudent(@ModelAttribute("idComebackStudent") int idComebackStudent,
                                RedirectAttributes redirectAttributes){
        Boolean isComeback = studentService.comebackStudent(idComebackStudent);
        if(isComeback){
            redirectAttributes.addFlashAttribute("changeSuccess", " Thay đổi thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
        }
        return "redirect:/manage/student";
    }

    @GetMapping("/parent")
    public String getParentStudent(@RequestParam(name = "idStudent") int idStudent,
            @RequestParam(name = "id") int id, Model model){

        Parent parent = studentService.getParentById(id);
        if(parent == null){
            parent = new Parent();
        }
        model.addAttribute("parent",parent);
        model.addAttribute("idStudent",idStudent);
        return "parent_student";
    }

    @PostMapping("/saveParent")
    public String saveParent(@ModelAttribute("parent") Parent parent,
                             @ModelAttribute("idStudent") int idStudent,
                             RedirectAttributes redirectAttributes){

       Boolean isSuccess =  studentService.saveParent(idStudent,parent);
       if(isSuccess){
           redirectAttributes.addFlashAttribute("changeSuccess", " Thay đổi thành công");
       }else {
           redirectAttributes.addFlashAttribute("changeFail", " Thay đổi thất bại");
       }
        return "redirect:/manage/student";
    }

    @GetMapping("/card")
    public String getCardStudent(@RequestParam("idStudent") String id,Model model){
        int idStudent = 0;
        try{
            idStudent = Integer.parseInt(id);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return "redirect:/manage/student";
        }

        StudentDto studentDto = studentService.findByIdDto(idStudent);
        Student student = studentService.findById(idStudent);
        if(studentDto == null){
            return "redirect:/manage/student";
        }else {
            StudentCard studentCard = studentService.getStudentCardByStudentId(idStudent);
            model.addAttribute("studentDto",studentDto);
            if(studentCard == null){
                StudentCard studentCard1 = new StudentCard();
                studentCard1.setStudent(student);
                model.addAttribute("studentCard",studentCard1);
            }else {
                model.addAttribute("studentCard",studentCard);
            }
            System.out.println(studentCard);

        }
        return "/student_card";

    }

    @PostMapping("/imageUpload")
    public String imageUpload(@ModelAttribute("studentCard") StudentCard studentCard,@RequestParam MultipartFile img) {

        StudentCard studentSave =  studentService.saveStudentCard(studentCard,img);

        // ảnh sau khi tải lên sẽ được lưu vào ổ D: nơi lưu project template/stactic/images
        if (studentSave != null) {
            // lưu  ở local
//            try {
//                // noi lưu file ảnh sau  khi tạo, update
//                // E:\Spring\Project_manage_school\manage_school\target\classes\static\images
//                File saveFile = new ClassPathResource("/static/images").getFile();
////                System.out.println(saveFile);
//
//                // nơi lưu ảnh goc
//                //E:\Spring\Project_manage_school\manage_school\target\classes\static\images\Screenshot_6.png
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
////                System.out.println(path);
//
//                // copy ảnh, tạo ảnh được copy được thêm mới vào thư mục trên
//                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return "redirect:/manage/student/card?idStudent=" + studentCard.getStudent().getId();
    }

    @GetMapping("/score")
    public String scoreStudent(@RequestParam("id") String id, Model model){
        int idStudent = 0;
        try{
            idStudent = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/student";
        }
        String nameStudent = studentService.findFullnameById(idStudent);
        model.addAttribute("nameStudent",nameStudent);

        List<ScoreDto> scoreDtos = scoreService.getAllStudentId(idStudent);
        model.addAttribute("scoreDtos",scoreDtos);

        ScoreDto scoreDto = new ScoreDto();
        model.addAttribute("scoreDto",scoreDto);
        model.addAttribute("teacher_classId",0);

        return "subject_score";
    }
}
