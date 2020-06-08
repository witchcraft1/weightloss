package com.rf.springsecurity.services;

import com.rf.springsecurity.constants.Data;
import com.rf.springsecurity.entity.*;
import com.rf.springsecurity.dto.UsersDTO;
import com.rf.springsecurity.repository.DishesRepository;
import com.rf.springsecurity.repository.UserDishRepository;
import com.rf.springsecurity.repository.UserInfoRepository;
import com.rf.springsecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DishesRepository dishesRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserDishRepository userDishRepository;

    @Autowired
    public UserService(UserRepository userRepository, DishesRepository dishesRepository, UserInfoRepository userInfoRepository,UserDishRepository userDishRepository) {
        this.userRepository = userRepository;
        this.dishesRepository = dishesRepository;
        this.userInfoRepository = userInfoRepository;
        this.userDishRepository = userDishRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        MyUser user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(), Collections.singleton(user.getRoles()));
    }

    public MyUser getUserByUsername(String login) throws UsernameNotFoundException{
        MyUser user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return user;
    }

    public UsersDTO getAllUsers() {
        //TODO checking for an empty user list
        return new UsersDTO(userRepository.findAll());
    }


    public void saveNewUser (MyUser user) throws Exception{
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public void saveUser(MyUser user){
        userRepository.save(user);
    }
    public MyUser findById(Long id){
        return userRepository.findById(id).get();
    }

    public List<Double> calculateSumCalories(MyUser user){
//        List<Dish> dishes = dishesRepository.findAllByUsers(user);

        List<UserDish> userDishes = userDishRepository.findAllByUser(user);
        List<Dish> dishes = new ArrayList<>();
        userDishes.forEach(userDish -> {dishes.add(userDish.getDish());});

        UserInfo userInfo = userInfoRepository.findByUserAndActiveTrue(user);
        Lifestyle lifestyle = Lifestyle.INACTIVE;
        if(userInfo!=null)lifestyle = userInfo.getLifestyle();
        List<Double> values = new ArrayList<>();
        int sum = dishes.stream().mapToInt(Dish::getCalories).sum();
        values.add((double) sum);
        int calories_to_loose = lifestyle == Lifestyle.ACTIVE ? sum - Data.NON_ACTIVE_MAN_LOOSE_CALORIES *2 : sum - Data.NON_ACTIVE_MAN_LOOSE_CALORIES;
        values.add((double)calories_to_loose);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_LOW_TEMP);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_MEDIUM_TEMP);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_HIGH_TEMP);
        return values;
    }
}
