package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.user_dish.UserDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserDishRepository extends JpaRepository<UserDish, Long> {
    List<UserDish> findAllByUser(MyUser user);
    List<UserDish> findAllByUserAndDate(MyUser user, LocalDate date);
    List<UserDish> findAllByDish(Dish dish);
    UserDish findByDishAndUser(Dish dish, MyUser user);
    void deleteUserDishByDishAndUser(Dish dish, MyUser user);
    void deleteAllByDish(Dish dish);
    void deleteByIdAndUserAndDish(Long id, MyUser user, Dish dish);
    void deleteById(Long id);
}
