package com.rf.springsecurity.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class DishUserId implements Serializable {
    @Column(name = "user_id2")
    private Long userId;
    @Column(name="dish_id2")
    private Long dishId;
}
