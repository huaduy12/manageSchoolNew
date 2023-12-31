package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Teacher;
import com.example.school.form.teacher.FormManageTeacher;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherService {

    public List<TeacherDto> teacherList();

    public Teacher save(FormManageTeacher formManageTeacher);
    public Teacher saveProfile(FormManageTeacher formManageTeacher);

    public void save(Teacher teacher);
    public Teacher findById(int id);
    public TeacherDto findByIdDto(int id);
    public TeacherDto findByUserIdDto(int userId);

    public Page<TeacherDto> getTeacherBySubject_id(int subject_id,int PageNo,int pageSize);

    public void rest(int id);
    public void comeback(int id);

    public Page<TeacherDto> findPaginated(int pageNo, int pageSize,String keyword);


}
