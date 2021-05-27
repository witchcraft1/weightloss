package com.rf.springsecurity.entity.exercise;


import com.rf.springsecurity.entity.MyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Builder
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)//Sequence
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private int caloriesBurnedPerMinute;

    @Enumerated(EnumType.STRING)
    @Column
    private ExerciseMode exerciseMode;

    @ManyToOne
    private MyUser user;

    public Exercise() {

    }
}
