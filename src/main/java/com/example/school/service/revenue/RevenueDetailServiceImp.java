package com.example.school.service.revenue;


import com.example.school.entity.Revenue_Detail;
import com.example.school.repository.RevenueDetailRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueDetailServiceImp implements RevenueDetailService {

    private RevenueDetailRepository revenueDetailRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RevenueDetailServiceImp(RevenueDetailRepository revenueDetailRepository, ModelMapper modelMapper) {
        this.revenueDetailRepository = revenueDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean existRevenueByStudent_idAndRevenue_id(int student_id, int revenue_id) {
        return revenueDetailRepository.existsByStudent_IdAndRevenue_Id(student_id,revenue_id);
    }

    @Override
    public List<Revenue_Detail> getRevenueDetailBtStudent(int id) {
        return revenueDetailRepository.findRevenue_DetailsByStudent_Id(id);
    }
}
