package com.example.school.service.revenue;

import com.example.school.entity.Revenue_Detail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RevenueDetailService {

    boolean existRevenueByStudent_idAndRevenue_id(int student_id,int revenue_id);
    List<Revenue_Detail> getRevenueDetailBtStudent(int id);
}
