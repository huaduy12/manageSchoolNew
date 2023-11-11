package com.example.school.form.user;

import com.example.school.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatingUserForm {

    private int id;

    @Length(min = 8,max = 50,message = "Username phải từ 8 ký tự")
    private String username;

    @Length(min = 8,max = 100, message = "Mật khẩu phải từ 8 ký tự")
    private String password;

    private List<Role> roles;
}
