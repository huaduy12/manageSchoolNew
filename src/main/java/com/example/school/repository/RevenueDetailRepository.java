package com.example.school.repository;

import com.example.school.entity.Revenue_Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueDetailRepository extends JpaRepository<Revenue_Detail,Integer> {

    boolean existsByStudent_IdAndRevenue_Id(int student_id,int revenue_id);
    List<Revenue_Detail> findRevenue_DetailsByStudent_Id(int student_id);
 }
