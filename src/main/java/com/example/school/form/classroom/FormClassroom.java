package com.example.school.form.classroom;

import com.example.school.entity.Teacher_class;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormClassroom {

    private int id;

    @NotBlank(message = "{classroom.NotBlank}")
    private String name;

    @Length(min = 9,max = 12,message = "{classroom.Length}")
    private String academic_year;


    private String note;

    private String status;


    private List<Teacher_class> teacher_classes;
}
