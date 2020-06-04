package com.rf.springsecurity.services;

import com.rf.springsecurity.dto.DishesDTO;
import com.rf.springsecurity.dto.UsersDTO;
import com.rf.springsecurity.entity.Dish;
import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.repository.DishesRepository;
import com.rf.springsecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DishesService {
    private final DishesRepository dishesRepository;

    @Autowired
    public DishesService(DishesRepository dishesRepository) {
        this.dishesRepository = dishesRepository;
    }

    public DishesDTO getAllDishes() {
        //TODO checking for an empty user list

        return new DishesDTO(dishesRepository.findAll());
    }

    public void saveNewDish (Dish dish) throws Exception{
        dishesRepository.save(dish);
    }

    public void deleteDishById(Long id) {
        dishesRepository.deleteById(id);
    }
    public Dish findById(Long id){
        return dishesRepository.findById(id).get();
    }
    public List<Dish> findAllByUser(User user){
        return dishesRepository.findAllByUsers(user);
    }

}
