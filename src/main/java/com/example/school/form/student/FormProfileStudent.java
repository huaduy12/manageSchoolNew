package com.example.school.form.student;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormProfileStudent {

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

}
