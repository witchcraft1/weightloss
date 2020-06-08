package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.UserDish;
import com.rf.springsecurity.repository.UserDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDishService {
    UserDishRepository userDishRepository;

    @Autowired
    public UserDishService(UserDishRepository userDishRepository){
        this.userDishRepository = userDishRepository;
    }
    public void save(UserDish userDish){
        userDishRepository.save(userDish);
    }
    public List<UserDish> findAllByUser(MyUser user){
        return userDishRepository.findAllByUser(user);
    }

    public List<UserDish> findAllByDish(Dish dish){
        return userDishRepository.findAllByDish(dish);
    }
    public UserDish findByDishAndUser(Dish dish, MyUser user){
        return userDishRepository.findByDishAndUser(dish,user);
    }

    @Transactional
    public void deleteByDishAndUser(Dish dish, MyUser user){
        userDishRepository.deleteUserDishByDishAndUser(dish,user);
    }

    @Transactional
    public void deleteAllByDish(Dish dish){
        userDishRepository.deleteAllByDish(dish);
    }
}
