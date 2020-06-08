package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.UserDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDishRepository extends JpaRepository<UserDish, Long> {
    List<UserDish> findAllByUser(MyUser user);
    List<UserDish> findAllByDish(Dish dish);
    UserDish findByDishAndUser(Dish dish, MyUser user);
    void deleteUserDishByDishAndUser(Dish dish, MyUser user);
    void deleteAllByDish(Dish dish);
}
