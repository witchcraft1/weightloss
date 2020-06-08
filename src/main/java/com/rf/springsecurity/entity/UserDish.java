package com.rf.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="UserDish")
@IdClass(UserDishPK.class)
public class UserDish implements Serializable {

    @ManyToOne
    @Id
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private MyUser user;

    @ManyToOne
    @Id
    @JoinColumn(name="dish_id", referencedColumnName = "id")
    private Dish dish;

    @Column(name = "grams")
    private Long grams;

    @Id
    @Column(name = "date")
    private LocalDate date;
}
