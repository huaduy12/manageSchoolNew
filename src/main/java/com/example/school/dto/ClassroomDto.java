package com.example.school.dto;

import com.example.school.entity.Teacher_class;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDto {

    private int id;
    private String name;
    private String academic_year;
    private String note;
    private String status;
    private List<Teacher_class> teacher_classes;
}
