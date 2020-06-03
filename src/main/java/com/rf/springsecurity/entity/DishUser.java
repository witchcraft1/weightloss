package com.rf.springsecurity.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
public class DishUser  implements Serializable{
    /*@EmbeddedId
    private DishUserId id;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
//    @MapsId("dishId")
    @Id
    private Dish dish;

    @ManyToOne
    @JoinColumn
//    @MapsId("userId")
    @Id
    private User user;

    private Integer count;

    private LocalDate localDate = LocalDate.now();
}
