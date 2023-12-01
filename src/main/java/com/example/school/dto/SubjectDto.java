package com.example.school.dto;

import com.example.school.entity.Teacher_class;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    private int id;

    @NotBlank(message = "{subject.name}")
    private String name;
    private Boolean status;

     private List<Teacher_class> teacher_classes ;
}
