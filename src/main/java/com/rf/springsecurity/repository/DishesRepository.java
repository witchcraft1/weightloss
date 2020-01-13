package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishesRepository extends JpaRepository<Dish, Long> {
        Dish findByName(String name);
        void deleteById(Long id);
}