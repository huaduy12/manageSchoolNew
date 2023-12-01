package com.example.school.form.teacher;

import com.example.school.entity.RoleTeacher;
import com.example.school.entity.User;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormManageTeacher {

    private int id;

    @NotBlank(message = "{teacher.fullName}")
    private String fullName;

    @NotNull(message = "{teacher.role}")
    private RoleTeacher role;

    @Pattern(regexp = "^[0-9]{10}$", message = "{teacher.phoneNumber}")
    private String phoneNumber;

    @NotNull(message = "{teacher.birthDay}")
    @Past(message = "{teacher.birthDayPast}")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // lưu ý khi lưu đinh dạng nếu để dd/MM/yyyy thì sẽ b lỗi
    private Date birthday;

    @NotBlank(message = "{teacher.address}")
    private String address;

    @Email(message = "{teacher.email}")
    @NotBlank(message = "{teacher.email}")
    private String email;

    private User user;
}
