package com.example.school.service;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Teacher_class;
import com.example.school.form.classroom.FormSubjectClass;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherClassService {

    public Teacher_class findById(int id);
    public Teacher_classDto findByIdDto(int id);
    public Teacher_class getByIdRoleAndClass_id(int role,int class_id);  // lấy giáo viên chủ nhiệm
    public List<Teacher_classDto> getByClass_id(int class_id);
    public List<Teacher_class> getByTeacher_id(int teacher_id);
    public Page<Teacher_classDto> getByTeacher_idAndSubjectNotNull(int teacher_id,int pageNo,int pageSize,String keyword);
    public Page<ClassroomDto> getByIdRoleAndTeacher_id(int role,int teacher_id,int pageNo,int pageSize,String keyword);

    public Boolean save(Teacher_class teacher_class,int id_class);
    public Boolean saveSubjectClass(FormSubjectClass formSubjectClass);
    public Boolean deleteSubjectClass(int id);

    public List<Teacher_class> getClassroomByTeacher(int role, int teacher_id);
    public boolean isExistTeacher_class(int year_id,int semester_id,int class_id,int subject_id);
}
