package com.example.school.form.student;

import com.example.school.entity.Classroom;
import com.example.school.entity.Parent;
import com.example.school.entity.User;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormStudent {

    private int id;

    @Length(min = 6, max = 100,message = "{student.fullName}")
    private String fullName;

    @Pattern(regexp = "^[0-9]{10}$", message = "{student.phoneNumber}")
    private String phoneNumber;

    @NotNull(message = "{student.birthDay}")
    @Past(message = "{student.birthDayPast}")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // lưu ý khi lưu đinh dạng nếu để dd/MM/yyyy thì sẽ b lỗi
    private Date birthday;

    @NotBlank(message = "{student.address}")
    private String address;

    @NotBlank(message = "{student.academyYear}")
    private String academy_year;

    @NotNull(message = "{student.classroom}")
    private Integer classRoomId;

    private Classroom classroom;
    private User user;
    private Parent parent;
}
