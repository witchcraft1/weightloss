package com.rf.springsecurity.entity.user_dish;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.dish.Mealtime;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.dish.Portion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name="UserDish")
@IdClass(UserDishPK.class)
public class UserDish implements Serializable {
    public static long id_st;
    @Id
    private Long id;

    @ManyToOne
    @Id
//    @JoinColumn(name="user_id", referencedColumnName = "id")
    private MyUser user;

    @ManyToOne
    @Id
//    @JoinColumn(name="dish_id", referencedColumnName = "id")
    private Dish dish;

    @Column
    private Long value;

    @Enumerated(EnumType.STRING)
    @Column
    private Portion portion;

    @Enumerated(EnumType.STRING)
    @Column
    private Mealtime mealtime;

//    @Id
    @Column(name = "date")
    private LocalDate date;

    public UserDish() {

    }
}
