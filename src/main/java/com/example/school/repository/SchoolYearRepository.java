package com.example.school.repository;

import com.example.school.entity.School_year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolYearRepository extends JpaRepository<School_year,Integer> {

    List<School_year> findAllByOrderByIdDesc();

    School_year findAllByIdNotAndNameEquals(Integer id, String name);
}
