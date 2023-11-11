package com.example.school.repository;

import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester,Integer> {

    List<Semester> findAllByOrderByIdDesc();
    Semester findAllByIdNotAndNameEquals(Integer id, String name);
}
