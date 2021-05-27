package com.rf.springsecurity.entity.user_exercise;


import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.exercise.ExerciseMode;
import com.rf.springsecurity.entity.MyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
@Entity
@IdClass(UserExercisePK.class)
public class UserExercise {
    public static long id_st;
    @Id
    private Long id;


    @ManyToOne
    @Id
    private MyUser user;

    @ManyToOne
    @Id
    private Exercise exercise;


    @Column
    private Long minutes;

    @Enumerated(EnumType.STRING)
    @Column
    private ExerciseMode exerciseMode;

    @Id
    @Column
    private LocalDate date;

    public UserExercise() {

    }
}
