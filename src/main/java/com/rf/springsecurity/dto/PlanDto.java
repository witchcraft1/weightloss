package com.rf.springsecurity.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlanDto {
    private int dailyCalorieBudget;
    private float totalWeightLoss;
    private float weeklyWeightLoss;
    private String goalDate;
}
