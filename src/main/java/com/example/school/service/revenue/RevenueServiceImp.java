package com.example.school.service.revenue;

import com.example.school.dto.RevenueDto;
import com.example.school.entity.Revenue;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import com.example.school.form.revenue.FormRevenue;
import com.example.school.repository.RevenueRepository;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RevenueServiceImp implements RevenueService {

    private RevenueRepository revenueRepository;
    private ModelMapper modelMapper;

    private SchoolYearRepository schoolYearRepository;
    private SemesterRepository semesterRepository;

    @Autowired
    public RevenueServiceImp(RevenueRepository revenueRepository,ModelMapper modelMapper,
                             SchoolYearRepository schoolYearRepository,SemesterRepository semesterRepository) {
        this.revenueRepository = revenueRepository;
        this.modelMapper  = modelMapper;
        this.schoolYearRepository = schoolYearRepository;
        this.semesterRepository = semesterRepository;
    }

    @Override
    public List<RevenueDto> getAllRevenue() {
        List<Revenue> revenues = revenueRepository.findAll();
        List<RevenueDto> revenueDtos = modelMapper.map(revenues,new TypeToken<List<RevenueDto>>(){}.getType());
        return revenueDtos;
    }

    @Override
    public boolean saveRevenue(FormRevenue formRevenue) {
        Revenue revenue= modelMapper.map(formRevenue,Revenue.class);
        revenue.setStatus(1);
        School_year school_year = schoolYearRepository.findById(formRevenue.getYear_id()).orElse(null);
        Semester semester = semesterRepository.findById(formRevenue.getSemester_id()).orElse(null);
        revenue.setSchool_year(school_year);
        revenue.setSemester(semester);

        Revenue revenueExist = getRevenueById(formRevenue.getId());
        if( revenueExist!= null){
            revenue.setCreatedAt(revenueExist.getCreatedAt());
        }
        Revenue revenueSave =  revenueRepository.save(revenue);
        return Objects.nonNull(revenueSave);
    }

    @Override
    public boolean deleteRevenue(int id) {
        revenueRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean lookRevenue(int id) {
        Revenue revenue = getRevenueById(id);
        // 0 : khóa , 1: mở bt
        revenue.setStatus(0);
        Revenue revenueLook = revenueRepository.save(revenue);
        return Objects.nonNull(revenueLook);
    }

    @Override
    public RevenueDto getRevenueByIdDto(int id) {
        Revenue revenue = revenueRepository.findById(id).orElseThrow(null);
        RevenueDto revenueDto = modelMapper.map(revenue,RevenueDto.class);
        return revenueDto;
    }

    @Override
    public Revenue getRevenueById(int id) {
        Revenue revenue = revenueRepository.findById(id).orElse(null);
        return revenue;
    }
}
