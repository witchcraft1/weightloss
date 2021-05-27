package com.rf.springsecurity.services;

import com.rf.springsecurity.dto.DishesDTO;
import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.repository.DishesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Dish> findAllByUser(MyUser user){
        return dishesRepository.findAllByUser(user);
    }

    public List<Dish> findAllWhereUserIsNull(){
        return dishesRepository.findAllByUserIsNull();
    }

    public boolean existsDishByNameAndUserIsNull(String name){
        return dishesRepository.existsDishByNameAndUserIsNull(name);
    }
}
