package com.example.school.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class School_yearDto {

    private int id;

    @Pattern(regexp = "20\\d{2}-20\\d{2}", message = "Tên năm học không hợp lệ")
    private String name;
}
