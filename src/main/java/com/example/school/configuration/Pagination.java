package com.example.school.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Pagination {

    public static int pageSize;
    @Value("${spring.data.web.pageable.default-page-size}")
    public void setDirectory(int value) {
        this.pageSize = value;
    }

}
