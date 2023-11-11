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

    @Length(min = 6, max = 100,message = "Vui lòng nhập tên hợp lệ")
    private String fullName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Vui lòng nhập số điện thoại hợp lệ")
    private String phoneNumber;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // lưu ý khi lưu đinh dạng nếu để dd/MM/yyyy thì sẽ b lỗi
    private Date birthday;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;

}
