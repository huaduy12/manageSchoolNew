package com.example.school.service;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.StudentDto;
import com.example.school.entity.Classroom;
import com.example.school.entity.Student;
import com.example.school.form.classroom.FormClassroom;
import com.example.school.repository.ClassroomRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class ClassroomServiceImp implements ClassroomService{

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ClassroomDto> getClassRoom() {
        List<Classroom> classrooms = classroomRepository.findAll();
//        System.out.println("classroom: " + classrooms);
        List<ClassroomDto> classroomDtos = null;
        if (classrooms != null){
            Type listType =  new TypeToken<List<ClassroomDto>>() {}.getType ();
            classroomDtos = modelMapper.map(classrooms,listType);
        }
        return classroomDtos;
    }

    @Override
    public List<ClassroomDto> getClassRoomStudying() {
        List<Classroom> classrooms = classroomRepository.findByStatusEquals(true);
        List<ClassroomDto> classroomDtos = null;
        if (classrooms != null){
            Type listType =  new TypeToken<List<ClassroomDto>>() {}.getType ();
            classroomDtos = modelMapper.map(classrooms,listType);
        }
        return classroomDtos;
    }

    @Override
    public boolean save(FormClassroom formClassroom) {
        Classroom classroom = null;
        if(formClassroom != null){
            classroom = modelMapper.map(formClassroom,Classroom.class);
        }
        if(classroom != null){
            classroom.setStatus(true);
            classroomRepository.save(classroom);
            return true;
        }
        return false;
    }

    @Override
    public void delete(int idClass) {
        classroomRepository.deleteById(idClass);
    }

    @Override
    public void block(int idClass) {
        Classroom classroom = findById(idClass);
        classroom.setStatus(false);
        classroomRepository.save(classroom);
    }

    @Override
    public void open(int idClass) {
        Classroom classroom = findById(idClass);
        classroom.setStatus(true);
        classroomRepository.save(classroom);
    }

    @Override
    public Classroom findById(int id) {
        Optional<Classroom> result = classroomRepository.findById(id);
        Classroom classroom = null;
        if(result.isPresent()){
            classroom = result.get();
        }
        return classroom;
    }
}
