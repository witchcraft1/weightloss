package com.rf.springsecurity.dto;

import com.rf.springsecurity.entity.dish.Mealtime;
import com.rf.springsecurity.entity.dish.Portion;
import lombok.Data;

@Data
public class UserDishDTO {
    private Long dish_id;
    private Long value;
    private Portion portion;
    private Mealtime mealtime;
}
