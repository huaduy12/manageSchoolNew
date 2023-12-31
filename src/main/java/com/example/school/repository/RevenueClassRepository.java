package com.example.school.repository;

import com.example.school.entity.RevenueClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueClassRepository extends JpaRepository<RevenueClass,Integer> {

    List<RevenueClass> getRevenueClassByRevenue_Id(int id);
    List<RevenueClass> getRevenueClassByClassroom_Id(int id);
    RevenueClass findByRevenue_IdAndClassroom_Id(int revenue_id,int classroom_id);
}
