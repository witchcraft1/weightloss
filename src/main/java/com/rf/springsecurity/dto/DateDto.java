package com.rf.springsecurity.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DateDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
}
