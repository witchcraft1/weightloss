package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.exercise.ExerciseMode;
import com.rf.springsecurity.entity.user_exercise.UserExercise;
import com.rf.springsecurity.entity.user_exercise.UserExercisePlanDto;
import com.rf.springsecurity.entity.userinfo.UserInfo;
import com.rf.springsecurity.repository.ExerciseRepository;
import com.rf.springsecurity.repository.UserExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserExerciseService {
    private final UserExerciseRepository userExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserInfoService userInfoService;
    @Autowired
    public UserExerciseService(UserExerciseRepository userExerciseRepository, ExerciseRepository exerciseRepository, UserInfoService userInfoService){
        this.userExerciseRepository = userExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.userInfoService = userInfoService;
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

    public int getExercisePerformMinutes(UserInfo userInfo, List<Exercise> exercises){
        int caloriesReduce = userInfoService.calcCaloriesReduce(userInfo);

        int exercisesBurnedCalPerMinute = exercises.stream().mapToInt(Exercise::getCaloriesBurnedPerMinute).sum();

        int minutes = caloriesReduce/exercisesBurnedCalPerMinute;
        return minutes;
        /*List<UserExercisePlanDto> dtos = new ArrayList<>();
        exercises.forEach(exercise -> dtos.add(
                UserExercisePlanDto.builder()
                            .exerciseMode(exercise.getExerciseMode())
                            .caloriesPerMin(exercise.getCaloriesBurnedPerMinute())
                            .minutes(minutes)

                        .build()
        ));*/
    }

    public int getExercisePerformMinutesInMode(UserInfo userInfo, List<Exercise> exercises, ExerciseMode mode){
        int caloriesReduce = userInfoService.calcCaloriesReduce(userInfo);

        int exercisesBurnedCalPerMinuteInMode = (int) exercises.stream().mapToDouble(exercise->
            exercise.getCaloriesBurnedPerMinute() * (mode.getCaloriesMultCoeff()/exercise.getExerciseMode().getCaloriesMultCoeff())
        ).sum();

        return caloriesReduce/exercisesBurnedCalPerMinuteInMode;
    }

    public List<Exercise> getDayExercisesByDay(Integer day){
        switch (day){
            case 1: return get1DayExercises();
            case 2: return get2DayExercises();
            case 3: return get3DayExercises();
            case 5: return get5DayExercises();
            case 6: return get6DayExercises();
            case 7: return get7DayExercises();
            case 9: return get9DayExercises();
            case 10: return get10DayExercises();
            case 11: return get11DayExercises();
            case 13: return get13DayExercises();
            case 14: return get14DayExercises();
            case 15: return get15DayExercises();
            case 17: return get17DayExercises();
            case 18: return get18DayExercises();
            case 19: return get19DayExercises();
            case 21: return get21DayExercises();
            case 22: return get22DayExercises();
            case 23: return get23DayExercises();
            case 25: return get25DayExercises();
            case 26: return get26DayExercises();
            case 27: return get27DayExercises();
            case 29: return get29DayExercises();
            case 30: return get30DayExercises();
            default: return null;
        }
    }

    private List<Exercise> get1DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Squats"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Jumping rope")
        );
    }
    private List<Exercise> get2DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Jumping jacks")
        );
    }
    private List<Exercise> get3DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Gymnastics")
        );
    }
    private List<Exercise> get5DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get6DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Running in place")
        );
    }
    private List<Exercise> get7DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Side lunges"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Pullups")
        );
    }
    private List<Exercise> get9DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Lunges"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get10DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Pushups")
        );
    }
    private List<Exercise> get11DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Side lunges"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Pullups"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get13DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get14DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Plie squats"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get15DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Pullups"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Claps over head")
        );
    }
    private List<Exercise> get17DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Claps over head")
        );
    }
    private List<Exercise> get18DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Squats"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Plie squats"),
                exerciseRepository.findByName("Standing bicycle crunches")
        );
    }
    private List<Exercise> get19DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get21DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get22DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Squats"),
                exerciseRepository.findByName("Pullups"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get23DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Side lunges"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get25DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get26DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Side lunges"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Fire hydrant"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Side lunges"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get27DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Squats"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Adductor stretch in standing"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Side-lying leg lift"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Standing bicycle crunches"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get29DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Plie squats"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Squats"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Donkey kicks"),
                exerciseRepository.findByName("Clapping crunches"),
                exerciseRepository.findByName("Flutter kicks"),
                exerciseRepository.findByName("Planks")
        );
    }
    private List<Exercise> get30DayExercises(){
        return Arrays.asList(
                exerciseRepository.findByName("Lunges"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks"),
                exerciseRepository.findByName("Lunges"),
                exerciseRepository.findByName("Heel touch"),
                exerciseRepository.findByName("Pushups"),
                exerciseRepository.findByName("Swimmer and superman"),
                exerciseRepository.findByName("Butt bridge"),
                exerciseRepository.findByName("Claps over head"),
                exerciseRepository.findByName("Planks")
        );
    }
}
