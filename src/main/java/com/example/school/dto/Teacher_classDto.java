package com.example.school.dto;

import com.example.school.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher_classDto {

    private int id;

    // 0: giáo viên chủ nhiệm,  1: giáo viên bộ môn
    private int role;
    private School_yearDto school_year;
    private SemesterDto semester;
    private TeacherDto teacher;
    private ClassroomDto classroom;
    private SubjectDto subject;
    private boolean status;



}
