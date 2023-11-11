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

    @NotBlank(message = "Vui lòng nhập tên")
    private String fullName;

    @NotNull(message = "Vui lòng chọn vai trò")
    private RoleTeacher role;

    @Pattern(regexp = "^[0-9]{10}$", message = "Vui lòng nhập số điện thoại")
    private String phoneNumber;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // lưu ý khi lưu đinh dạng nếu để dd/MM/yyyy thì sẽ b lỗi
    private Date birthday;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;

    @Email(message = "Vui lòng nhập địa chỉ email")
    @NotBlank(message = "Vui lòng nhập địa chỉ email")
    private String email;

    private User user;
}
