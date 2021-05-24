package com.rf.springsecurity.entity;

import com.rf.springsecurity.security.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table( name="users",
        uniqueConstraints={@UniqueConstraint(columnNames={"login"})})
public class MyUser implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)//SEQUENCE)
//    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
//    @ManyToOne
    private UserRole role;

//    private boolean active;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
   /* @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "userInfoId", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)//!!!
    @JoinColumn(name = "info_id")//!!!

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)*/
    /*@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserInfo userInfo;*/

   /* @ManyToMany
    @JoinTable(
            name = "users_dishes",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes;*/

   /*@OneToMany(mappedBy = "user")
    private List<UserDish> userDishes;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Dish> special_dishes;*/


   public UserRole getRole(){
       return role;
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedPermissions();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}