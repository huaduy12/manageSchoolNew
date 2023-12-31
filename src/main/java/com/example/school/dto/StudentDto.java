package com.example.school.dto;

import com.example.school.entity.Parent;
import com.example.school.entity.StudentCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private int id;

    private String fullName;

    private String phoneNumber;

    private Date birthday;

    private String address;

    private boolean status;

    private String academy_year;

    private Long account_balance;

    private ClassroomDto classroom;

    private UserDto user;

    private Parent parent;

    private StudentCard studentCard;
}
