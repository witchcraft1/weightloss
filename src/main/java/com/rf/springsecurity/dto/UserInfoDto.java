package com.rf.springsecurity.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserInfoDto {
    private String nick;
    private Integer weight;
    private Integer goalWeight;
    private Integer height;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String male;
    private String lifestyle;

    private Integer exercisesPerWeek;
    private Float weightLossPerWeek;

}
