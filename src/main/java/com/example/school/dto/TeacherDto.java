package com.example.school.dto;

import com.example.school.entity.RoleTeacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private int id;
    private String fullName;
    private RoleTeacher role;
    private String phoneNumber;
    private Date birthday;
    private String address;
    private String email;
    private boolean status;

    private UserDto user;

}
