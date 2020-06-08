package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.Dish;
import com.rf.springsecurity.entity.UserDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishesRepository extends JpaRepository<Dish, Long> {
        Dish findByName(String name);
        void deleteById(Long id);
        Optional<Dish> findById(Long id);
//        List<Dish> findAllByUsers(User user);
        List<Dish> findAllByUserDishes(UserDish userDish);
}