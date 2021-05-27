package com.rf.springsecurity.entity.user_dish;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDishPK implements Serializable {
    private Long user;
    private Long dish;
//    private LocalDate date;
    private Long id;
}
