package com.rf.springsecurity.entity;

import lombok.*;
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
    private LocalDate date;
}
