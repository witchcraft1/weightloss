package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishesRepository extends JpaRepository<Dish, Long> {
        Dish findByName(String name);
        void deleteById(Long id);
        Optional<Dish> findById(Long id);
        List<Dish> findAllByUser(MyUser user);
        List<Dish> findAllByUserIsNull();
//        List<Dish> findAllByUserDishes(UserDish userDish);
        boolean existsDishByNameAndUserIsNull(String name);
        Dish findDishById(Long id);
}