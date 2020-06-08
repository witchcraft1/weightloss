package com.rf.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table( name="dishes",
        uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Dish  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)//Sequence
    @Column(name = "id", nullable = false, unique = true)
    private Long id;


    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int calories;

    @Column(nullable = false)
    private int fat;

    @Column(nullable = false)
    private int carbs;

    @Column(nullable = false)
    private int protein;

    /*@ManyToMany(mappedBy = "dishes")
    private List<*//*DishUser*//*User> users;*/
    @OneToMany(mappedBy = "dish")
    private List<UserDish> userDishes;
}