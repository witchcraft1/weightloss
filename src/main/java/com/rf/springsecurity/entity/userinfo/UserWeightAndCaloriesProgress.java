package com.rf.springsecurity.entity.userinfo;

import com.rf.springsecurity.entity.MyUser;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name="user_progress")
public class UserWeightAndCaloriesProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @ManyToOne
    private MyUser user;

    @Column
    private LocalDate date;

    @Column
    private Integer delta_calories;

    @Column
    private Integer weight;
}
