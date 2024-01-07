package com.example.school.controller.Revenue;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.RevenueDto;
import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import com.example.school.entity.Classroom;
import com.example.school.entity.RevenueClass;
import com.example.school.entity.Student;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.form.revenue.FormRevenue;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.service.ClassroomService;
import com.example.school.service.StudentService;
import com.example.school.service.YearSemesterService;
import com.example.school.service.revenue.RevenueClassService;
import com.example.school.service.revenue.RevenueDetailService;
import com.example.school.service.revenue.RevenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manage/revenue")
public class ManageRevenue {
    private RevenueService revenueService;
    private RevenueClassService revenueClassService;
    private YearSemesterService yearSemesterService;
    private ClassroomService classroomService;
    private StudentService studentService;
    private RevenueDetailService revenueDetailService;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    public ManageRevenue(RevenueService revenueService, YearSemesterService yearSemesterService,
                         ClassroomService classroomService, RevenueClassService revenueClassService,
                         StudentService studentService,RevenueDetailService revenueDetailService) {
        this.revenueService = revenueService;
        this.yearSemesterService = yearSemesterService;
        this.classroomService = classroomService;
        this.revenueClassService = revenueClassService;
        this.studentService = studentService;
        this.revenueDetailService = revenueDetailService;
    }

    @GetMapping(value = {"/",""})
    public String getAllRevenue(Model model){
        List<RevenueDto> revenueDtos = revenueService.getAllRevenue();
        model.addAttribute("revenueDtos",revenueDtos);

        model.addAttribute("formRevenue",new FormRevenue());
        List<School_yearDto> school_yearDto = yearSemesterService.findAllByOrderByIdDesc();
        List<SemesterDto> semesterDtos = yearSemesterService.findAllSemester();
        List<ClassroomDto> classroomDtos = classroomService.getClassRoomStudying();
        model.addAttribute("school_years",school_yearDto);
        model.addAttribute("semesters",semesterDtos);
        model.addAttribute("classroomDtos",classroomDtos);
        return "/revenue/revenue";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("formRevenue") FormRevenue formRevenue,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       @ModelAttribute("form") String form){

        if(bindingResult.hasErrors()){
            List<RevenueDto> revenueDtos = revenueService.getAllRevenue();
            model.addAttribute("revenueDtos",revenueDtos);

           // model.addAttribute("formRevenue",new FormRevenue());
            List<School_yearDto> school_yearDto = yearSemesterService.findAllByOrderByIdDesc();
            List<SemesterDto> semesterDtos = yearSemesterService.findAllSemester();
            model.addAttribute("school_years",school_yearDto);
            model.addAttribute("semesters",semesterDtos);

            if(!form.trim().equals("")){
                model.addAttribute("formError","Có lỗi ở form");
            }
            return "/revenue/revenue";
        }
        Boolean isSuccess =revenueService.saveRevenue(formRevenue);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/revenue";
    }

    @GetMapping("/apply/{id}")
    public String getRevenueClass(@PathVariable("id") String id,Model model){
        Integer idRevenue =null;
        try{
            idRevenue = Integer.parseInt(id);
        }catch (Exception e){
            return "redirect:/manage/revenue";
        }
        model.addAttribute("idRevenue",idRevenue);
        List<RevenueClass> revenueClasses = revenueClassService.getRevenueClassByRevenue(idRevenue);
        model.addAttribute("revenueClasses",revenueClasses);
        List<Integer> ids = new ArrayList<>();
        for (RevenueClass r: revenueClasses) {
            ids.add(r.getClassroom().getId());
        }
        List<ClassroomDto> classroomsNotIn;
        if(revenueClasses.size() > 0){
            classroomsNotIn = classroomService.getClassIdNotIn(ids,true);
        }else {
            classroomsNotIn = classroomService.getClassRoom();
        }
        model.addAttribute("classroomsNotIn",classroomsNotIn);
        ids.clear();
        return "/revenue/revenueClass";
    }

    // những lớp đã được chọn nhưng hủy apply khoản thu
    @PostMapping("/saveClassApplyNot")
    public String saveClassNotIn(
            Model model, RedirectAttributes redirectAttributes, @ModelAttribute("id") int id,
            @ModelAttribute("idClass") String idClass){
        if (idClass.length() > 0){
            Boolean isSuccess = revenueClassService.deleteRevenueClass(id,idClass);
            if(isSuccess){
                redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
            }else {
                redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
            }
        }
        return "redirect:/manage/revenue/apply/"+id;
    }

    // những lớp chưa được chọn và apply mới khoản thu
    @PostMapping("/saveClassApply")
    public String saveClassApply(
            Model model, RedirectAttributes redirectAttributes, @ModelAttribute("id") int id,
            @ModelAttribute("idClass") String idClass){
        if(idClass.length() > 0){
            Boolean isSuccess =revenueClassService.addRevenueClass(id,idClass);
            if(isSuccess){
                redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
            }else {
                redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
            }
        }
        return "redirect:/manage/revenue/apply/"+id;
    }


    @GetMapping("/statistics/{id}")
    public String getRevenueStatistic(@PathVariable("id") String id,Model model){
        Integer idRevenue =null;
        try{
            idRevenue = Integer.parseInt(id);
        }catch (Exception e){
            return "redirect:/manage/revenue";
        }
        // lấy các lớp áp dụng khoản thu  --> dc labels
        List<RevenueClass> revenueClasses = revenueClassService.getRevenueClassByRevenue(idRevenue);
        List<String> nameClasses = new ArrayList<>();
        for (RevenueClass revenueClass: revenueClasses) {
            nameClasses.add(revenueClass.getClassroom().getName());
        }

        // tổng học sinh tứng lớp tương ứng
        List<Integer> totalStudent = new ArrayList<>();
        for (RevenueClass revenueClass : revenueClasses){
            totalStudent.add(studentService.countStudentByClass(revenueClass.getClassroom().getId()));
        }

        // tổng số học sinh của lớp tương ứng đã đóng tiền
        List<Integer> totalStudentTuition = new ArrayList<>();
        for (RevenueClass revenueClass: revenueClasses) {
            List<Student> listStudentClass = studentService.getStudentByClassId(revenueClass.getClassroom().getId());
            int count =0;
            if(listStudentClass != null){
                for (Student student: listStudentClass) {
                    if(revenueDetailService.existRevenueByStudent_idAndRevenue_id(student.getId(),idRevenue)){
                        count++;
                    };
                }
            }
            totalStudentTuition.add(count);
        }
        // tính tỷ lệ phần trăm
        List<Double> percentStudentTuition = new ArrayList<>();
        for (int i = 0; i < revenueClasses.size(); i++) {
            if(totalStudent.get(i) != 0){
                percentStudentTuition.add(roundToTwoDecimalPlaces(((totalStudentTuition.get(i)*1.0)/totalStudent.get(i))*100));
            }else {
                percentStudentTuition.add(0.0);
            }
        }

        // tính số tiền học sinh đã đóng
        List<Long> moneyTuition = new ArrayList<>();
        for (int i = 0; i < revenueClasses.size(); i++) {
            moneyTuition.add(totalStudentTuition.get(i) * revenueClasses.get(i).getRevenue().getPrice());
        }

        // tính số tiền đang thiếu
        List<Long> moneyTuitionNotPaid = new ArrayList<>();
        for (int i = 0; i < revenueClasses.size(); i++) {
            moneyTuitionNotPaid.add(totalStudent.get(i)*revenueClasses.get(i).getRevenue().getPrice() - moneyTuition.get(i));
        }


        model.addAttribute("labels",nameClasses);
        model.addAttribute("data",percentStudentTuition);
        model.addAttribute("totalStudentTuition",totalStudentTuition);
        model.addAttribute("totalStudent",totalStudent);
        model.addAttribute("moneyTuition",moneyTuition);
        model.addAttribute("moneyTuitionNotPaid",moneyTuitionNotPaid);

        return "revenue/revenue_statistics";
    }

    // Phương thức để làm tròn hai chữ số sau dấu thập phân
    private static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }
}
