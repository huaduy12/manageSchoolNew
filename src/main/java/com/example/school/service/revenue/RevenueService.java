package com.example.school.service.revenue;

import com.example.school.dto.RevenueDto;
import com.example.school.entity.Revenue;
import com.example.school.form.revenue.FormRevenue;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RevenueService {

    List<RevenueDto> getAllRevenue();
    boolean saveRevenue(FormRevenue formRevenue);
    boolean deleteRevenue(int id);
    boolean lookRevenue(int id);
    RevenueDto getRevenueByIdDto(int id);
    Revenue getRevenueById(int id);
}
