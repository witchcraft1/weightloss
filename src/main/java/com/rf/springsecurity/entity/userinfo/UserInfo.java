package com.rf.springsecurity.entity.userinfo;

import com.rf.springsecurity.entity.MyUser;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString
@Entity
@Table(name="userInfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_info", nullable = false, unique = true)
    private Long id;

    @Column
    private String nickname;

    @OneToOne/*(fetch = FetchType.LAZY)*/
    private MyUser user;

    @Column
    private int weight; //kg
    @Column
    private int goalWeight;

    @Column
    private int height; //cm

    @Column
    private int age;

    @Enumerated(EnumType.STRING)
    @Column
    private Male male;

    @Enumerated(EnumType.STRING)
    @Column
    private Lifestyle lifestyle;

    @Column
    private int exercisesPerWeek;

    @Column
    private float weightLossPerWeek;
}
