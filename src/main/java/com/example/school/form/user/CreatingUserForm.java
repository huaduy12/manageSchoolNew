package com.example.school.form.user;

import com.example.school.entity.Role;
import jakarta.validation.ReportAsSingleViolation;
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


    @Pattern(regexp = "^\\S{8,50}$", message = "{user.username}")
    private String username;

    @Pattern(regexp = "^\\S{8,100}$", message = "{user.password}")
    private String password;

    private List<Role> roles;
}
