package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.user_dish.UserDish;
import com.rf.springsecurity.repository.UserDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserDishService {
    private final UserDishRepository userDishRepository;

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

    public List<UserDish> findAllByUserAndDate(MyUser user, LocalDate localDate){
        return userDishRepository.findAllByUserAndDate(user, localDate);
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
    public void deleteById(Long id, MyUser user, Dish dish){
        userDishRepository.deleteByIdAndUserAndDish(id,user,dish);
    }

    @Transactional
    public void deleteAllByDish(Dish dish){
        userDishRepository.deleteAllByDish(dish);
    }

    public List<UserDish> fitUserDishesToPortions(List<UserDish> userDishes){
        userDishes.forEach(userDish -> {
            Dish userDish_ = userDish.getDish();

            double times_bigger = userDish.getValue() * 1. * userDish.getPortion().getGramsInPortion() / userDish_.getPortion().getGramsInPortion();

            userDish.setDish(
                    Dish.builder()
                            .id(userDish_.getId())
                            .name(userDish_.getName())
                            .calories((int) (userDish_.getCalories() * times_bigger))
                            .carbs((int)(userDish_.getCarbs() * times_bigger))
                            .fat((int)(userDish_.getFat() * times_bigger))
                            .protein((int)(userDish_.getProtein()*times_bigger))
                            .build()
            );
        });
        return userDishes;
    }

    public Dish getSummary(List<UserDish> userDishes){
        int calories = 0;
        int carbs = 0;
        int fats = 0;
        int protein = 0;
        for (UserDish userDish : userDishes) {
            calories+=userDish.getDish().getCalories();
            carbs+=userDish.getDish().getCarbs();
            fats+=userDish.getDish().getFat();
            protein+=userDish.getDish().getProtein();
        }

        return Dish.builder()
                    .calories(calories)
                    .carbs(carbs)
                    .fat(fats)
                    .protein(protein)
                .build();
    };
}
