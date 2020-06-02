package com.rf.springsecurity.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table( name="dishes",
        uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @ManyToMany(mappedBy = "dishes")
    private List<User> users;
}