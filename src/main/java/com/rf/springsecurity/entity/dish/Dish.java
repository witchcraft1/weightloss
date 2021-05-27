package com.rf.springsecurity.entity.dish;

import com.rf.springsecurity.entity.MyUser;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table( name="dishes")
       // uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Dish  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)//Sequence
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false) //unique = true
    private String name;

    @Column(nullable = false)
    private int calories;

    @Column(nullable = false)
    private int fat;

    @Column(nullable = false)
    private int carbs;

    @Column(nullable = false)
    private int protein;

    @Enumerated(EnumType.STRING)
    @Column
    private Portion portion;
    /*@ManyToMany(mappedBy = "dishes")
    private List<*//*DishUser*//*User> users;*/
    /*@OneToMany(mappedBy = "dish")
    private List<UserDish> userDishes;*/

    @ManyToOne/*(fetch = FetchType.LAZY)*/
    //@Column(nullable = true)
    private MyUser user;

    public Dish() {

    }
}