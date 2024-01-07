package com.example.school.controller;

import com.example.school.configuration.Pagination;
import com.example.school.dto.*;
import com.example.school.entity.Classroom;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import com.example.school.entity.Teacher_class;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.form.classroom.FormSubjectClass;
import com.example.school.form.student.FormStudent;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import com.example.school.repository.TeacherClassRepository;
import com.example.school.service.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage/class")
public class ManageClass {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private YearSemesterService yearSemesterService;

    @Autowired
   private TeacherClassService teacherClassService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public String homeClass(Model model,@RequestParam(value = "keyword",required = false) String keyword){
       return findPaginated(1,model,null,keyword);
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formClassroom") FormClassroom formClassroom,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form,  @ModelAttribute("pageNo") int pageNo,
                       @RequestParam(value = "keyword",required = false) String keyword){


        if(bindingResult.hasErrors()){
            findPaginated(pageNo,model,formClassroom,keyword);
            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/manage_class";
        }
        Boolean isSuccess =  classroomService.save(formClassroom);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/class/page/"+ pageNo;
    }

    @PostMapping("/block")
    public String restUser(@ModelAttribute("idBlock") int idBlock,Model model,
                           RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","thành công");
        classroomService.block(idBlock);
        return "redirect:/manage/class";
    }

    @PostMapping("/open")
    public String comebackUser(@ModelAttribute("idOpen") int idOpen,Model model,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("changeSuccess","Xóa thành công");
        classroomService.open(idOpen);
        return "redirect:/manage/class";
    }

    // lấy ra những môn học của lớp tại bảng teacher_class, từ đó bít dc ai dạy,môn học nào .....
    @GetMapping("/subject")
    public String showClassById(@RequestParam("id") String id, Model model){
        int idClass = 0;
        try{
            idClass = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/manage/class";
        }
        Classroom classroom = classroomService.findById(idClass);
        model.addAttribute("nameClass",classroom);

        // tìm những môn học của class có id
        List<Teacher_classDto> teacher_classes = teacherClassService.getByClass_id(idClass);
        model.addAttribute("teacher_classes",teacher_classes);


        List<School_yearDto> school_years = yearSemesterService.findAllByOrderByIdDesc();
        model.addAttribute("school_years",school_years);

        List<SemesterDto> semesters = yearSemesterService.findAllSemester();
        model.addAttribute("semesters",semesters);

        // lâý môn học
        List<SubjectDto> subjectDtos = subjectService.getAllSubject();
        model.addAttribute("subjects",subjectDtos);

        List<TeacherDto> teacherDtos = teacherService.teacherList();
        model.addAttribute("teacherList",teacherDtos);

        FormSubjectClass formSubjectClass = new FormSubjectClass();
        formSubjectClass.setId(0);
        model.addAttribute("formSubjectClass",formSubjectClass);

        return "/subject_class";
    }
    // lưu môn học cần thêm và sửa cho lơp
    // lưu ý khi thêm thì đòng thời điểm với học sinh sẽ được thêm vào bảng resuilt
    @PostMapping("/saveSubject")
    public String save(@Valid @ModelAttribute("formSubjectClass") FormSubjectClass formSubjectClass,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form){

        List<Teacher_classDto> teacher_classes = teacherClassService.getByClass_id(formSubjectClass.getClassroomId());
        List<School_yearDto> school_years = yearSemesterService.findAllByOrderByIdDesc();
        List<SemesterDto> semesters = yearSemesterService.findAllSemester();
        List<SubjectDto> subjectDtos = subjectService.getAllSubject();
        List<TeacherDto> teacherDtos = teacherService.teacherList();
        Classroom classroom = classroomService.findById(formSubjectClass.getClassroomId());
        if(bindingResult.hasErrors()){
            model.addAttribute("nameClass",classroom);
            model.addAttribute("teacher_classes",teacher_classes);
            model.addAttribute("school_years",school_years);
            model.addAttribute("semesters",semesters);
            model.addAttribute("subjects",subjectDtos);
            model.addAttribute("teacherList",teacherDtos);

            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/subject_class";
        }

        boolean isSave =  teacherClassService.saveSubjectClass(formSubjectClass);
        if(isSave){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }
        else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/class/subject?id=" + formSubjectClass.getClassroomId();
    }

    // xóa lớp học cần thêm
    @PostMapping("/deleteSubject")
    public String deleteStudent(@ModelAttribute("idDelete") int idDelete,
                                @ModelAttribute("idClassroom") int idClassroom,
                                RedirectAttributes redirectAttributes){
        Boolean isDelete = teacherClassService.deleteSubjectClass(idDelete);
        if(isDelete){
            redirectAttributes.addFlashAttribute("changeSuccess", " Xóa thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeDeleteFail", " Xóa thất bại");
        }
        return "redirect:/manage/class/subject?id="+ idClassroom;
    }


    // lâý giáo viên chủ nhiệm
    @GetMapping("/teacherHome")
    public String getTeacherHomerooms(@RequestParam(name = "id") String idClass, Model model,
                                     RedirectAttributes redirectAttributes){
        List<School_yearDto> school_years = yearSemesterService.findAllByOrderByIdDesc();
        model.addAttribute("school_years",school_years);

        List<SemesterDto> semesters = yearSemesterService.findAllSemester();
        model.addAttribute("semesters",semesters);

        List<TeacherDto> teacherDtos = teacherService.teacherList();
        model.addAttribute("teacherList",teacherDtos);

        model.addAttribute("idClass",idClass);

        int id_class = -1;
        try{
            id_class = Integer.parseInt(idClass);
        }catch (NumberFormatException n){
            return "redirect:/manage/class";
        }catch (Exception e){
            return "redirect:/manage/class";
        }

        Teacher_class teacher_class = teacherClassService.getByIdRoleAndClass_id(0,id_class);
        if(teacher_class == null){
            Teacher_class createTeacherClass = new Teacher_class();
            createTeacherClass.setId(0);
            model.addAttribute("teacherClass", createTeacherClass);
        }else {
            model.addAttribute("teacherClass",teacher_class);
        }
        return "/teacherHomeroom";
    }

    // create, update giáo viên chủ nhiệm
    @PostMapping("/saveTeacherHomeroom")
    public String saveTeacherHomeroom(@Valid @ModelAttribute("teacherClass") Teacher_class teacher_class,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes
                     ,@ModelAttribute("idClass") int idClass ){

        List<School_yearDto> school_years = yearSemesterService.findAllByOrderByIdDesc();

        List<SemesterDto> semesters = yearSemesterService.findAllSemester();

        List<TeacherDto> teacherDtos = teacherService.teacherList();

        if(bindingResult.hasErrors()){
            model.addAttribute("school_years",school_years);
            model.addAttribute("semesters",semesters);
            model.addAttribute("teacherList",teacherDtos);

            return "/teacherHomeroom";
        }
        Boolean isSuccess =  teacherClassService.save(teacher_class,idClass);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/class";
    }

   // tra về học sinh của lớp học
    @GetMapping("/classDetail")
    public String classDetail(@RequestParam("id") String idClass,Model model){

        int id_class = 0;
        try{
            id_class = Integer.parseInt(idClass);
        }catch (NumberFormatException numberFormatException){
            return "redirect:/manage/class";
        }

        List<StudentDto> studentDtos = studentService.getStudentByClassIdDto(id_class);
        model.addAttribute("studentDtos",studentDtos);

        Classroom classroom = classroomService.findById(id_class);
        model.addAttribute("classroom",classroom);
        return "class_detail";
    }


    @GetMapping("/subjectTeacher")
    public String subjectTeacher(@RequestParam("id") String idClass,Model model){

        int id_class = 0;
        try{
            id_class = Integer.parseInt(idClass);
        }catch (NumberFormatException numberFormatException){
            return "redirect:/manage/class";
        }
// lấy giáo viên bộ môn
        List<Teacher_classDto> teacher_classes = teacherClassService.getByClass_id(id_class);
        model.addAttribute("teacher_classes",teacher_classes);
        Classroom classroom = classroomService.findById(id_class);
        model.addAttribute("classroom",classroom);


        return "subject_teacher";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,Model model,
                                FormClassroom formClassroom,
                                @RequestParam(value = "keyword",required = false) String keyword
    ){

        if(pageNo <= 0) return "redirect:/manage/class";
        int pageSize = Pagination.pageSize;
        Page<ClassroomDto> classroomDtos = classroomService.findPaginated(pageNo-1,pageSize,keyword);
        if(classroomDtos.getTotalElements() != 0 && pageNo > classroomDtos.getTotalPages()){
            return "redirect:/manage/classroom/page/1?keyword="+keyword;
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("classroomDtos",classroomDtos);

        // thông tin các lớp đang còn học để phục vụ cho form add, edit
        if(formClassroom == null){
            formClassroom = new FormClassroom();
            formClassroom.setId(0);
            model.addAttribute("formClassroom",formClassroom);
        }else {
            model.addAttribute("formClassroom",formClassroom);
        }
        model.addAttribute("showClass",true);

        return "manage_class";
    }

}
