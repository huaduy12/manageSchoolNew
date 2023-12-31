package com.example.school.dto;

import com.example.school.entity.RevenueClass;
import com.example.school.entity.Revenue_Detail;
import com.example.school.entity.School_year;
import com.example.school.entity.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDto {

    private int id;

    private String name;

    private String description;

    private long price;

    private Date createdAt;

    private Date updatedAt;

    private Date expiredAt;

    // phục vụ cho việc khóa không cho đóng nữa
    private int status;

    private School_yearDto school_year;
    private SemesterDto semester;

}
