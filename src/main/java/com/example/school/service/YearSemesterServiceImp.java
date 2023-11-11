package com.example.school.service;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import com.example.school.entity.Teacher_class;
import com.example.school.repository.SchoolYearRepository;
import com.example.school.repository.SemesterRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class YearSemesterServiceImp implements YearSemesterService {

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<School_yearDto> findAllYearSchool() {
        List<School_year> school_years = schoolYearRepository.findAllByOrderByIdDesc();
        List<School_yearDto> school_yearDtos = null;
        if(school_years != null){
            Type listType =  new TypeToken<List<School_yearDto>>() {}.getType ();
            school_yearDtos = modelMapper.map(school_years,listType);
        }
        return school_yearDtos;
    }

    @Override
    public List<SemesterDto> findAllSemester() {
        List<Semester> semesters = semesterRepository.findAllByOrderByIdDesc();
        List<SemesterDto> semesterDtos = null;
        if(semesters != null){
            Type listType =  new TypeToken<List<SemesterDto>>() {}.getType ();
            semesterDtos = modelMapper.map(semesters,listType);
        }
        return semesterDtos;
    }

    @Override
    public boolean saveYear(School_yearDto school_yearDto) {

        School_year school_year = null;

        if(school_yearDto != null){
            school_year = modelMapper.map(school_yearDto,School_year.class);
            schoolYearRepository.save(school_year);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveSemester(SemesterDto semesterDto) {
        Semester semester = new Semester();
        semester.setId(semesterDto.getId());
        semester.setName(semesterDto.getName());
        if(semesterDto != null){
            semesterRepository.save(semester);
            return true;
        }
        return false;
    }

    @Override
    public Semester findByIdSemester(int id) {
        Optional<Semester> result = semesterRepository.findById(id);
        Semester semester = null;
        if(result.isPresent()){
            semester = result.get();
        }
        return semester;
    }

    @Override
    public SemesterDto findByIdSemesterDto(int id) {
        SemesterDto semesterDto = null;
        Semester semester = findByIdSemester(id);
        if(semester != null){
            semesterDto = modelMapper.map(semester,SemesterDto.class);
        }
        return semesterDto;
    }

    @Override
    public School_yearDto getExistNameYear(int id, String name) {
        School_year school_year = schoolYearRepository.findAllByIdNotAndNameEquals(id,name.trim());
        if(school_year != null){
            School_yearDto school_yearDto = modelMapper.map(school_year,School_yearDto.class);
            return school_yearDto;
        }
        return null;
    }

    @Override
    public SemesterDto getExistNameSemester(int id, String name) {
        Semester semester = semesterRepository.findAllByIdNotAndNameEquals(id,name.trim());
        if(semester != null){
            SemesterDto semesterDto = modelMapper.map(semester,SemesterDto.class);
            return semesterDto;
        }
        return null;
    }

    @Override
    public List<School_yearDto> findAllByOrderByIdDesc() {
        List<School_year> school_years = schoolYearRepository.findAllByOrderByIdDesc();
        List<School_yearDto> school_yearDtos = null;
        if(school_years !=  null){
            Type listType =  new TypeToken<List<School_yearDto>>() {}.getType ();
            school_yearDtos = modelMapper.map(school_years,listType);
        }

        return school_yearDtos ;
    }

    @Override
    public School_yearDto findAllByIdNotAndNameEquals(Integer id, String name) {
        School_yearDto school_yearDto = null;
        School_year school_year = schoolYearRepository.findAllByIdNotAndNameEquals(id,name);
        school_yearDto = modelMapper.map(school_year,School_yearDto.class);
        return school_yearDto;
    }

    @Override
    public School_year findByIdSchool(int id) {
        Optional<School_year> result = schoolYearRepository.findById(id);
        School_year school_year = null;
        if(result.isPresent()){
            school_year = result.get();
        }
        return school_year;
    }

    @Override
    public School_yearDto findByIdSchoolDto(int id) {
        School_yearDto school_yearDto = null;
        School_year school_year = findByIdSchool(id);
        school_yearDto = modelMapper.map(school_year,School_yearDto.class);
        return school_yearDto;
    }
}
