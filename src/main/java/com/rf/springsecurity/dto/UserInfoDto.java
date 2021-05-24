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
    private int weight;
    private int goalWeight;
    private int height;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String male;
    private String lifestyle;

    private int exercisesPerWeek;
    private float weightLossPerWeek;

}
