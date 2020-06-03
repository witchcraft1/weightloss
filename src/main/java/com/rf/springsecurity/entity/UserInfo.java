package com.rf.springsecurity.entity;

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

  //  @OneToOne(fetch = FetchType.LAZY, mappedBy = "userInfo") //!!!!
   // @MapsId//tells Hibernate to use the id column of address as both primary key and foreign key
   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")*/
//   @OneToOne(cascade = CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private int age;

    @Column
    private int height; //cm

    @Column
    private int weight; //kg

    @Column
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column
    private Lifestyle lifestyle;
}
