package com.example.school.form.classroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormSubjectClass {
    private int id;

    @NotNull(message = "{subjectClassYear}")
    @Min(value = 1, message = "{subjectClassYear}")
    private Integer school_yearId;

    @NotNull(message = "{subjectClassSemester}")
    @Min(value = 1, message = "{subjectClassSemester}")
    private Integer semesterId;

    // 0: giáo viên chủ nhiệm,  1: giáo viên bộ môn

    private int role;

    @NotNull(message = "{subjectClassTeacher}")
    private Integer teacherId;

    private int classroomId;

    @NotNull(message = "{subjectClassSubject}")
    private Integer subjectId;

    private boolean status;
}
