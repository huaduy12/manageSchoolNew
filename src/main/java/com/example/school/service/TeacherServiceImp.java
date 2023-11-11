package com.example.school.service;

import com.example.school.dto.TeacherDto;
import com.example.school.dto.UserDto;
import com.example.school.entity.RoleTeacher;
import com.example.school.entity.Teacher;
import com.example.school.entity.Teacher_class;
import com.example.school.form.teacher.FormManageTeacher;
import com.example.school.repository.TeacherClassRepository;
import com.example.school.repository.TeacherRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImp implements  TeacherService{

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherClassRepository teacherClassRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TeacherDto> teacherList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        // lọc không hiển thị admin ra
        List<Teacher> teacherFilter = new ArrayList<>();

        for (Teacher t:teacherList) {
            if(t.getRole() != RoleTeacher.Admin){
                teacherFilter.add(t);
            }
        }
        List<TeacherDto> teachersDto = null;
        if(teacherList != null){
            Type listType =  new TypeToken<List<TeacherDto>>() {}.getType ();
            teachersDto = modelMapper.map(teacherFilter,listType);
        }
        return teachersDto;
    }

@Override
public Teacher save(FormManageTeacher formManageTeacher) {
    Teacher teacher = null;

    Teacher CopyTeacher = findById(formManageTeacher.getId());
    if(CopyTeacher != null){
        formManageTeacher.setUser(CopyTeacher.getUser());
    }
    if(formManageTeacher != null){
        teacher = modelMapper.map(formManageTeacher,Teacher.class);
    }
    if(CopyTeacher != null){
        teacher.setCreatedAt(CopyTeacher.getCreatedAt());
    }
    if(teacher != null){
         teacher.setStatus(true);
         return teacherRepository.save(teacher);
     }
      return null;

}

    @Override
    public Teacher saveProfile(FormManageTeacher formManageTeacher) {
        Teacher teacher = null;
        Teacher CopyTeacher = findById(formManageTeacher.getId());

        if(formManageTeacher != null){
            teacher = modelMapper.map(formManageTeacher,Teacher.class);
        }
        if(CopyTeacher != null){
            teacher.setCreatedAt(CopyTeacher.getCreatedAt());
        }
        if(teacher != null){
            teacher.setStatus(true);
            return teacherRepository.save(teacher);
        }
        return null;
    }

    @Override
    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public Teacher findById(int id) {
        Optional<Teacher> result = teacherRepository.findById(id);
        Teacher teacher = null;

        if(result.isPresent()){
            teacher = result.get();

        }
        return teacher;
    }



    @Override
    public TeacherDto findByIdDto(int id) {
        Optional<Teacher> result = teacherRepository.findById(id);
        Teacher teacher = null;
        TeacherDto teacherDto = null;
        if(result.isPresent()){
            teacher = result.get();
            teacherDto = modelMapper.map(teacher,TeacherDto.class);
        }

        return teacherDto;
    }

    @Override
    public TeacherDto findByUserIdDto(int userId) {
        TeacherDto teacherDto = null;
        Teacher teacher = teacherRepository.findByUser_Id(userId);
        if(teacher != null){
            teacherDto = modelMapper.map(teacher,TeacherDto.class);
        }
        return teacherDto;
    }

    @Override
    public List<TeacherDto> getTeacherBySubject_id(int subject_id) {
        List<Teacher_class> teacher_classes = teacherClassRepository.findBySubject_Id(subject_id);
        List<TeacherDto> teacherDtos = new ArrayList<>();
        if(teacher_classes != null){
            for(Teacher_class teacher_class : teacher_classes){
                TeacherDto teacherDto = findByIdDto(teacher_class.getTeacher().getId());
                teacherDtos.add(teacherDto);
            }
        }
        return teacherDtos;
    }

    @Override
    public void rest(int id) {
        // đánh dấu nghỉ dạy
        Teacher teacher = findById(id);
        teacher.setStatus(false);
        teacherRepository.save(teacher);
    }

    @Override
    public void comeback(int id) {
        // đánh dấu nghỉ dạy
        Teacher teacher = findById(id);
        teacher.setStatus(true);
        teacherRepository.save(teacher);
    }
}
