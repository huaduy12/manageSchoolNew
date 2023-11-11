package com.example.school.controller;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import com.example.school.service.YearSemesterService;
import jakarta.validation.Valid;
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

import java.util.List;

@RequestMapping("/manage/year_school")
@Controller
public class ManageYearSchool {

    @Autowired
    private YearSemesterService yearSemesterService;



    @GetMapping()
    public String getYear_Semester(Model model){

        List<School_yearDto> school_yearDtos = yearSemesterService.findAllYearSchool();
        model.addAttribute("schoolYearDtos",school_yearDtos);
        List<SemesterDto> semesterDtos = yearSemesterService.findAllSemester();
        model.addAttribute("semesterDtos",semesterDtos);

        School_yearDto school_yearDto = new School_yearDto();
        school_yearDto.setId(0);
        model.addAttribute("school_yearDto",school_yearDto);

        SemesterDto semesterDto = new SemesterDto();
        semesterDto.setId(0);
        model.addAttribute("semesterDto",semesterDto);
        return "/manageYearSemester";

    }

    @PostMapping("/saveYear")
    public String save(@Valid @ModelAttribute("school_yearDto") School_yearDto school_yearDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes
                       ){

        List<School_yearDto> school_yearDtos = yearSemesterService.findAllYearSchool();
        List<SemesterDto> semesterDtos = yearSemesterService.findAllSemester();
        School_yearDto school_yearExist = yearSemesterService.getExistNameYear(school_yearDto.getId(),school_yearDto.getName());
        if(school_yearExist != null){
            bindingResult.addError(new FieldError("school_yearDto","name","Tên năm học đã tồn tại"));

        }
        if(bindingResult.hasErrors()){
            model.addAttribute("schoolYearDtos",school_yearDtos);
            model.addAttribute("semesterDtos",semesterDtos);
            model.addAttribute("school_yearDto",school_yearDto);
            SemesterDto semesterDto = new SemesterDto();
            semesterDto.setId(0);
            model.addAttribute("semesterDto",semesterDto);
//            if(!form.trim().equals("")){
                model.addAttribute("formErrorYear","Có lỗi ở form");
//            }
            return "/manageYearSemester";
        }
        Boolean isSuccess =  yearSemesterService.saveYear(school_yearDto);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/year_school";
    }

    @PostMapping("/saveSemester")
    public String saveSemester(@Valid @ModelAttribute("semesterDto") SemesterDto semesterDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes
    ){

        List<School_yearDto> school_yearDtos = yearSemesterService.findAllYearSchool();
        List<SemesterDto> semesterDtos = yearSemesterService.findAllSemester();
        SemesterDto semesterExist = yearSemesterService.getExistNameSemester(semesterDto.getId(),semesterDto.getName());
        if(semesterExist != null){
            bindingResult.addError(new FieldError("semesterDto","name","Tên kỳ học đã tồn tại"));

        }

        if(bindingResult.hasErrors()){
            model.addAttribute("schoolYearDtos",school_yearDtos);
            model.addAttribute("semesterDtos",semesterDtos);
            model.addAttribute("semesterDto",semesterDto);
            School_yearDto school_yearDto = new School_yearDto();
            school_yearDto.setId(0);
            model.addAttribute("school_yearDto",school_yearDto);
//            if(!form.trim().equals("")){
            model.addAttribute("formErrorSemester","Có lỗi ở form");
//            }
            return "/manageYearSemester";
        }
        Boolean isSuccess =  yearSemesterService.saveSemester(semesterDto);
        if(isSuccess){
            redirectAttributes.addFlashAttribute("changeSuccess","Lưu thành công");
        }else {
            redirectAttributes.addFlashAttribute("changeFail","Lưu thất bại");
        }
        return "redirect:/manage/year_school";
    }


}
