package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.user_dish.UserDish;
import com.rf.springsecurity.entity.user_exercise.UserExercise;
import com.rf.springsecurity.repository.UserDishRepository;
import com.rf.springsecurity.repository.UserExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

@Service
public class UserExerciseService {
    private final UserExerciseRepository userExerciseRepository;

    @Autowired
    public UserExerciseService(UserExerciseRepository userExerciseRepository){
        this.userExerciseRepository = userExerciseRepository;
    }
    public void save(UserExercise userExercise){
        userExerciseRepository.save(userExercise);
    }
    public List<UserExercise> findAllByUser(MyUser user){
        return userExerciseRepository.findAllByUser(user);
    }

    public List<UserExercise> findAllByUserAndDate(MyUser user, LocalDate localDate){
        return userExerciseRepository.findAllByUserAndDate(user, localDate);
    }

    public List<UserExercise> findAllByExercise(Exercise exercise){
        return userExerciseRepository.findAllByExercise(exercise);
    }
    public UserExercise findByExerciseAndUser(Exercise exercise, MyUser user){
        return userExerciseRepository.findByExerciseAndUser(exercise,user);
    }

    @Transactional
    public void deleteByExerciseAndUser(Exercise exercise, MyUser user){
        userExerciseRepository.deleteUserExerciseByUserAndExercise(user,exercise);
    }

    @Transactional
    public void deleteById(Long userDishId, MyUser user, Exercise exercise){
        userExerciseRepository.deleteByIdAndUserAndExercise(userDishId,user,exercise);
    }

    @Transactional
    public void deleteAllByExercise(Exercise exercise){
        userExerciseRepository.deleteAllByExercise(exercise);
    }

    public List<UserExercise> fitUserExercisesToMinutes(List<UserExercise> userExercises){
        userExercises.forEach(userExercise -> {
            Exercise userExercise_ = userExercise.getExercise();

            double times_bigger = userExercise.getMinutes() * 1. * userExercise.getExerciseMode().getCaloriesMultCoeff() / userExercise_.getExerciseMode().getCaloriesMultCoeff();

            userExercise.setExercise(
                    Exercise.builder()
                            .id(userExercise_.getId())
                            .name(userExercise_.getName())
                            .caloriesBurnedPerMinute((int) (userExercise_.getCaloriesBurnedPerMinute() * times_bigger))
                            .build()
            );
        });
        return userExercises;
    }

    public Exercise getSummary(List<UserExercise> userExercises){
        int calories = 0;
        for (UserExercise userExercise : userExercises) {
            calories+=userExercise.getExercise().getCaloriesBurnedPerMinute();
        }
        return Exercise.builder()
                .caloriesBurnedPerMinute(calories)
                .build();
    };

    public Long biggest_id(){
        List<UserExercise> all = userExerciseRepository.findAll();

        if(all == null) return 0L;

        return all.stream().map(UserExercise::getId).mapToLong(x -> x).max().orElse(0L);
    }
}
