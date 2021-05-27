package com.rf.springsecurity.entity.user_exercise;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserExercisePK implements Serializable {
    private Long user;
    private Long exercise;
    private Long id;
}
