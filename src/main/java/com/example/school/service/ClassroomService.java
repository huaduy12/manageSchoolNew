package com.example.school.service;

import com.example.school.dto.ClassroomDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.Classroom;
import com.example.school.form.classroom.FormClassroom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomService {
    public List<ClassroomDto> getClassRoom();

    public List<ClassroomDto> getClassRoomStudying();

    public boolean save(FormClassroom  formClassroom);

    public void delete(int idClass);
    public void block(int idClass);
    public void open(int idClass);
    public Classroom findById(int id);

    public Page<ClassroomDto> findPaginated(int pageNo, int pageSize,String keyword);

}
