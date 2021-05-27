package com.rf.springsecurity.dto;

import com.rf.springsecurity.entity.dish.Mealtime;
import com.rf.springsecurity.entity.dish.Portion;
import com.rf.springsecurity.entity.exercise.ExerciseMode;
import lombok.Data;

@Data
public class UserExerciseDto {
    private Long exercise_id;
    private Long minutes;
    private ExerciseMode mode;
}
