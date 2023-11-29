package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Subject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectService {

    public List<SubjectDto> getAllSubject();
    public Boolean save(SubjectDto subjectDto);
    public Boolean block(int idSubject);
    public Boolean open(int idSubject);
    public Boolean delete(int idSubject);
    public Subject findById(int id);
    public SubjectDto findByIdDto(int id);

    public List<Teacher_classDto> getSubjectsByClass_id(int class_id);

    public Page<SubjectDto> findPaginated(int pageNo, int pageSize,String keyword);
}
