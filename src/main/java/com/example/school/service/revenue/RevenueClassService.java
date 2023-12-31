package com.example.school.service.revenue;

import com.example.school.entity.RevenueClass;

import java.util.List;

public interface RevenueClassService {

    List<RevenueClass> getRevenueClassByRevenue(int id);
    List<RevenueClass> getRevenueClassByClass(int id);
    boolean deleteRevenueClass(int id,String idClass);

    boolean addRevenueClass(int id,String idClass);
}
