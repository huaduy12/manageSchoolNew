package com.example.school.controller.Revenue;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.RevenueDto;
import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import com.example.school.entity.Classroom;
import com.example.school.entity.RevenueClass;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.form.revenue.FormRevenue;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import com.example.school.service.ClassroomService;
import com.example.school.service.YearSemesterService;
import com.example.school.service.revenue.RevenueClassService;
import com.example.school.service.revenue.RevenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manage/revenue")
public class ManageRevenue {
    private RevenueService revenueService;
    private RevenueClassService revenueClassService;
    private YearSemesterService yearSemesterService;
    private ClassroomService classroomService;

    @Autowired
    public ManageRevenue(RevenueService revenueService, YearSemesterService yearSemesterService,
                         ClassroomService classroomService, RevenueClassService revenueClassService) {
        this.revenueService = revenueService;
        this.yearSemesterService = yearSemesterService;
        this.classroomService = classroomService;
        this.revenueClassService = revenueClassService;
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


}
