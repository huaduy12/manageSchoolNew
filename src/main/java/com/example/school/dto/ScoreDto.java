package com.example.school.dto;

import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {
    private int id;

    private School_yearDto school_year;


    private SemesterDto semester;


    @DecimalMin(value = "0", inclusive = true, message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10", inclusive = true, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    private Float oval_score;


    @DecimalMin(value = "0", inclusive = true, message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10", inclusive = true, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    private Float score_15;


    @DecimalMin(value = "0", inclusive = true, message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10", inclusive = true, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    private Float score_1_period;


    @DecimalMin(value = "0", inclusive = true, message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10", inclusive = true, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    private Float test_score;

    private Float medium_score;

    private SubjectDto subject;

    private StudentDto student;

    @Override
    public String toString() {
        return "ResultDto{" +
                "id=" + id +
                ", school_year=" + school_year +
                ", semester=" + semester +
                ", oval_score=" + oval_score +
                ", score_15=" + score_15 +
                ", score_1_period=" + score_1_period +
                ", test_score=" + test_score +
                ", medium_score=" + medium_score +
                ", subject=" + subject +
                ", student=" + student +
                '}';
    }
}
