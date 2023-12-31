package com.example.school.form.revenue;

import com.example.school.dto.School_yearDto;
import com.example.school.dto.SemesterDto;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormRevenue {


    private int id;

    @NotBlank(message = "{revenue.name}")
    private String name;

    private String description;

    @NotNull(message = "{revenue.price}")
    private Long price;


    @NotNull(message = "{revenue.expire}")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // lưu ý khi lưu đinh dạng nếu để dd/MM/yyyy thì sẽ b lỗi
    private Date expiredAt;

    @NotNull(message = "{revenue.year}")
    private Integer year_id;

    @NotNull(message = "{revenue.semester}")
    private Integer semester_id;
}
