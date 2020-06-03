package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.DishUser;
import com.rf.springsecurity.repository.DishUserRep;
import com.rf.springsecurity.repository.DishesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DishUserService {
    private final DishUserRep dishUserRep;

    @Autowired
    public DishUserService(DishUserRep dishUserRep) {
        this.dishUserRep = dishUserRep;
    }

    public void saveDishUser(DishUser dishUser){
        dishUserRep.save(dishUser);
    }
}
