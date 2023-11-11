package com.example.school.service;

import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;

import java.util.List;

public interface YearSemesterService {

    public List<School_yearDto> findAllYearSchool();
    public List<SemesterDto> findAllSemester();

    public boolean saveYear(School_yearDto school_yearDto);
    public boolean saveSemester(SemesterDto semesterDto);
    Semester findByIdSemester(int id);
    SemesterDto findByIdSemesterDto(int id);

    public School_yearDto getExistNameYear(int id, String name);
    public SemesterDto getExistNameSemester(int id, String name);

    List<School_yearDto> findAllByOrderByIdDesc();

    School_yearDto findAllByIdNotAndNameEquals(Integer id, String name);
    School_year findByIdSchool(int id);
    School_yearDto findByIdSchoolDto(int id);
}
