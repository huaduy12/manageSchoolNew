package com.example.school.service.revenue;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.RevenueDto;
import com.example.school.dto.StudentDto;
import com.example.school.entity.*;
import com.example.school.repository.RevenueDetailRepository;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.ClassroomService;
import com.example.school.service.StudentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class TuitionServiceImp implements TuitionService {

    private StudentService studentService;
    private ModelMapper modelMapper;
    private ClassroomService classroomService;
    private RevenueService revenueService;
    private RevenueClassService revenueClassService;
    private RevenueDetailService revenueDetailService;

    private RevenueDetailRepository revenueDetailRepository;

    @Autowired
    public TuitionServiceImp(StudentService studentService, ModelMapper modelMapper, ClassroomService classroomService, RevenueService revenueService, RevenueClassService revenueClassService,
                             RevenueDetailService revenueDetailService,RevenueDetailRepository revenueDetailRepository) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
        this.classroomService = classroomService;
        this.revenueService = revenueService;
        this.revenueClassService = revenueClassService;
        this.revenueDetailService = revenueDetailService;
        this.revenueDetailRepository = revenueDetailRepository;
    }

    @Override
    public List<RevenueClass> getTuitionNotPaid(@AuthenticationPrincipal EntityUserDetail userDetail) {
        // lâý ra danh sách những khoản thu chưa được đóng

        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        Classroom classroom = classroomService.findById(studentDto.getClassroom().getId());
        ClassroomDto classroomDto = modelMapper.map(classroom,ClassroomDto.class);

        // lấy những khoản thu đã được áp dụng cho lớp này
        List<RevenueClass> revenueClasses = revenueClassService.getRevenueClassByClass(classroomDto.getId());
        List<RevenueClass> revenueNotPaid = new ArrayList<>();
        for (RevenueClass r: revenueClasses) {
            if(!(revenueDetailService.existRevenueByStudent_idAndRevenue_id(studentDto.getId(),r.getRevenue().getId()))){
                revenueNotPaid.add(r);
            }
        }
        return revenueNotPaid;
    }

    @Override
    public List<RevenueClass> getTuitionPaid() {
        return null;
    }

    @Override
    public void addMoneyStudent(EntityUserDetail userDetail, Long money) {
        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        Student student = studentService.findById(studentDto.getId());
        student.setAccount_balance(student.getAccount_balance() + money);
        studentService.save(student);
    }

    @Override
    @Transactional
    public boolean paymentTuitionPersonal(EntityUserDetail userDetail, int idRevenue) {
        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        Revenue revenue = revenueService.getRevenueById(idRevenue);
        if(studentDto.getAccount_balance() >= revenue.getPrice()){
            Student student = studentService.findById(studentDto.getId());
            Date currentDate = new Date();
            Timestamp timestamp = new Timestamp(currentDate.getTime());
            Revenue_Detail revenue_detail = new Revenue_Detail();
            revenue_detail.setPayment_time(timestamp);
            revenue_detail.setStudent(student);
            revenue_detail.setRevenue(revenue);
            revenueDetailRepository.save(revenue_detail);
            student.setAccount_balance(student.getAccount_balance()-revenue.getPrice());
            studentService.save(student);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean paymentTuitionOnline(EntityUserDetail userDetail, int idRevenue) {
        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        Revenue revenue = revenueService.getRevenueById(idRevenue);
            Student student = studentService.findById(studentDto.getId());
            Date currentDate = new Date();
            Timestamp timestamp = new Timestamp(currentDate.getTime());
            Revenue_Detail revenue_detail = new Revenue_Detail();
            revenue_detail.setPayment_time(timestamp);
            revenue_detail.setStudent(student);
            revenue_detail.setRevenue(revenue);
            revenueDetailRepository.save(revenue_detail);
            return true;


    }
}
