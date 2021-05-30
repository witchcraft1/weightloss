package com.rf.springsecurity.entity.user_exercise;

import com.rf.springsecurity.entity.exercise.ExerciseMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserExercisePlanDto {
    private String name;
    private Integer caloriesPerMin;
    private ExerciseMode exerciseMode;
    private Integer minutes;
}
