package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.DishUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishUserRep extends JpaRepository<DishUser, Long> {
}
