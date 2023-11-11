package com.example.school.service;

import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Teacher_class;
import com.example.school.form.classroom.FormSubjectClass;

import java.util.List;

public interface TeacherClassService {

    public Teacher_class findById(int id);
    public Teacher_classDto findByIdDto(int id);
    public Teacher_class getByIdRoleAndClass_id(int role,int class_id);  // lấy giáo viên chủ nhiệm
    public List<Teacher_classDto> getByClass_id(int class_id);
    public List<Teacher_class> getByTeacher_id(int teacher_id);
    public List<Teacher_classDto> getByTeacher_idAndSubjectNotNull(int teacher_id);
    public List<Teacher_class> getByIdRoleAndTeacher_id(int role,int teacher_id);

    public Boolean save(Teacher_class teacher_class,int id_class);
    public Boolean saveSubjectClass(FormSubjectClass formSubjectClass);
    public Boolean deleteSubjectClass(int id);

}
