package com.rf.springsecurity.controller;


import com.rf.springsecurity.dto.*;
import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.dish.Mealtime;
import com.rf.springsecurity.entity.dish.Portion;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.exercise.ExerciseMode;
import com.rf.springsecurity.entity.user_dish.UserDish;
import com.rf.springsecurity.entity.user_exercise.UserExercise;
import com.rf.springsecurity.entity.userinfo.Lifestyle;
import com.rf.springsecurity.entity.userinfo.Male;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.userinfo.UserInfo;
import com.rf.springsecurity.security.UserRole;
import com.rf.springsecurity.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

@Slf4j
@Controller
/*@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)*/
public class MainController {

    private final UserService userService;
    private final DishesService dishesService;
    private final UserInfoService userInfoService;
    private final UserDishService userDishService;
    private final UserExerciseService userExerciseService;
    private final ExerciseService exerciseService;

    @Autowired
    public MainController(UserService userService, DishesService dishesService, UserInfoService userInfoService, UserDishService userDishService, UserExerciseService userExerciseService, ExerciseService exerciseService) {
        this.userService = userService;
        this.dishesService = dishesService;
        this.userInfoService = userInfoService;
        this.userDishService = userDishService;
        this.userExerciseService = userExerciseService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public String getHomePage(@AuthenticationPrincipal MyUser user, Model model){
        List<UserDish> userDishes = userDishService.findAllByUserAndDate(user, LocalDate.now());

        userDishService.fitUserDishesToPortions(userDishes);

        Dish summary = userDishService.getSummary(userDishes);

        Map<String, Integer> map_meal_cal = userDishService.calcCaloriesForEachMealtime(userDishes);

        model.addAttribute("mealtime_calories", map_meal_cal);
        model.addAttribute("special_dishes", userDishes/*special_today_dishes*/);
        model.addAttribute("mealtimes", Mealtime.values());
        model.addAttribute("summary", summary);


        List<UserExercise> userExercises = userExerciseService.findAllByUserAndDate(user, LocalDate.now());

        userExerciseService.fitUserExercisesToMinutes(userExercises);

        Exercise summary_ex = userExerciseService.getSummary(userExercises);

        model.addAttribute("special_exercises", userExercises);
        model.addAttribute("modes", ExerciseMode.values());
        model.addAttribute("summary_ex", summary_ex);


        model.addAttribute("calories_budget",
                userInfoService.calcDailyBudgetWhenLooseWeight(userInfoService.findByUser(user)));
        return "home-beauty";
    }

    @GetMapping("/home")
    public String getHome2Page(@AuthenticationPrincipal MyUser user, Model model){
        List<UserDish> userDishes = userDishService.findAllByUserAndDate(user, LocalDate.now());

        userDishService.fitUserDishesToPortions(userDishes);

        Dish summary = userDishService.getSummary(userDishes);

        model.addAttribute("special_dishes", userDishes/*special_today_dishes*/);
        model.addAttribute("mealtimes", Mealtime.values());
        model.addAttribute("summary", summary);


        List<UserExercise> userExercises = userExerciseService.findAllByUserAndDate(user, LocalDate.now());

        userExerciseService.fitUserExercisesToMinutes(userExercises);

        Exercise summary_ex = userExerciseService.getSummary(userExercises);

        model.addAttribute("special_exercises", userExercises);
        model.addAttribute("modes", ExerciseMode.values());
        model.addAttribute("summary_ex", summary_ex);


        model.addAttribute("calories_budget",
                userInfoService.calcDailyBudgetWhenLooseWeight(userInfoService.findByUser(user)));
        return "home";
    }


    @RequestMapping("/plan")
    public String getMainPage(@AuthenticationPrincipal MyUser user, Model model/*, Principal p*/) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();

        model.addAttribute("login", user.getUsername());
        model.addAttribute("username", userInfoService.findByUser(user).getNickname());
        model.addAttribute("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        model.addAttribute("plan_dto", userInfoService.getPlan(user));
        /*model.addAttribute("daily_calorie_budget", "");
        model.addAttribute("total_weight_loss", "kilograms");
        model.addAttribute("weekly_weight_loss", "kilograms");
        model.addAttribute("goal_date", "");*/

        return "plan";
    }


    @GetMapping("/all_users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model){//TODO переробити UserInfo na User
        List<UserInfo> onlyActiveInfoList = new ArrayList<>(userInfoService.getAllUserInfos());
        model.addAttribute("users", onlyActiveInfoList);//userService.getAllUsers().getUsers());
        return "users";
    }

    @GetMapping("/dishes")
//    @PreAuthorize("hasRole('ADMIN')")
    public String getAllDishes(@AuthenticationPrincipal MyUser user, Model model){
        model.addAttribute("dishes", dishesService.findAllWhereUserIsNull());
        model.addAttribute("special_dishes", dishesService.findAllByUser(userService.getUserByUsername(user.getUsername())));
        model.addAttribute("role", userService.getUserByUsername(user.getUsername()).getRole().getAuthority());
        model.addAttribute("checkBox", new BooleanDTO());
        model.addAttribute("portions", Portion.values());
        return "dishes";
    }

    @PostMapping("/dishes")
    public String addDish(@AuthenticationPrincipal MyUser user, Dish dish, @ModelAttribute("check-box")BooleanDTO booleanDTO, Model model) {
        if(user.getRole().equals(UserRole.USER) || booleanDTO.isBoolValue()){
            dish.setUser(user);
        }else if(dishesService.existsDishByNameAndUserIsNull(dish.getName())){//TODO зробить так шоб було видно хто додав блюдо
            model.addAttribute("message", "dish already exists");
            /*log.info(dish.getName() + "dish already exists");*/
            return getAllDishes(user,model);
        }


        try {
            dishesService.saveNewDish(dish);
        } catch (Exception ex) {
//            log.info(dish.getName() + "dish already exists");
//            model.addAttribute("dishes", dishesService.findAllWhereUserIsNull());
//            model.addAttribute("message", "dish already exists");
//            model.addAttribute("special_dishes", dishesService.findAllByUser(userService.getUserByUsername(user.getUsername())));
//            model.addAttribute("role", userService.getUserByUsername(user.getUsername()).getRoles().getAuthority());
//            model.addAttribute("checkBox", new BooleanDTO());
//
//            return "dishes";
        }

        return "redirect:/dishes";
    }
    @GetMapping(value = "/dishes/delete/{id}")
    public String deleteSomeDish(@PathVariable("id")Long dishId, Model model){
        Dish delete_dish = dishesService.findById(dishId);
        userDishService.deleteAllByDish(delete_dish);

        try {
            dishesService.deleteDishById(dishId);
        }catch (Exception ex){
            model.addAttribute("message", "there are no dish with such ID");
            return "dishes";
        }
         return "redirect:/dishes";
    }
    @GetMapping("add_user_info")
    public String showUserInfo(@AuthenticationPrincipal MyUser user, Model model){
        UserInfo userInfo = userInfoService.findActiveInfoByUser(user);

        model.addAttribute("lifestyles", Lifestyle.values());
        model.addAttribute("males", Male.values());
        model.addAttribute("current_user", userInfo);
        return "add_user_info";
    }

    @PostMapping("add_user_info")
    public String addUserInfo(@AuthenticationPrincipal MyUser user,UserInfo userInfo, Model model){
        /*if(user.getUserInfo() == null) {
            userInfo.setUser(user);
            userInfoService.saveNewUserInfo(userInfo);
        }else{*/
            userInfoService.updateUserInfo(userInfo, user);
//        }
        return "redirect:/add_user_info";
    }

    @GetMapping("add_user_info/select_recent")
    public String selectSomeRecentDataForUser(Model model){
        model.addAttribute("userInfos",userInfoService);
        return "select_recent_data";
    }

    @GetMapping("/exercises")
//    @PreAuthorize("hasRole('ADMIN')")
    public String getAllExercises(@AuthenticationPrincipal MyUser user, Model model){
        model.addAttribute("exercises", exerciseService.findAllByUserIsNull());
        model.addAttribute("special_exercises", exerciseService.findAllByUser(user));
        model.addAttribute("role", user.getRole().getAuthority());
        model.addAttribute("checkBox", new BooleanDTO());
        model.addAttribute("modes", ExerciseMode.values());
        return "exercises";
    }

    @PostMapping("/exercises")
    public String addExercise(@AuthenticationPrincipal MyUser user, Exercise exercise, @ModelAttribute("check-box")BooleanDTO booleanDTO, Model model) {
        if(user.getRole().equals(UserRole.USER) || booleanDTO.isBoolValue()){
            exercise.setUser(user);
        }else if(exerciseService.existsExerciseByNameAndUserIsNull(exercise.getName())){//TODO зробить так шоб було видно хто додав блюдо
            model.addAttribute("message", "Exercise already exists");
            /*log.info(dish.getName() + "dish already exists");*/
            return getAllExercises(user,model);
        }


        try {
            exerciseService.saveNewExercise(exercise);
        } catch (Exception ex) {}

        return "redirect:/exercises";
    }
    @GetMapping(value = "/exercises/delete/{id}")
    public String deleteSomeExercise(@PathVariable("id")Long exerciseId, Model model){
        Exercise delete_exercise = exerciseService.findById(exerciseId);
        userExerciseService.deleteAllByExercise(delete_exercise);

        try {
            exerciseService.deleteExerciseById(exerciseId);
        }catch (Exception ex){
            model.addAttribute("message", "there are no exercise with such ID");
            return "exercises";
        }
        return "redirect:/exercises";
    }


    @GetMapping("/select_dishes")
    public String selectDishesPage(@AuthenticationPrincipal MyUser user,Model model){
        List<UserDish> userDishes = userDishService.findAllByUserAndDate(user, LocalDate.now());

        List<Dish> dishes = dishesService.findAllWhereUserIsNull();
        dishes.addAll(dishesService.findAllByUser(user));

        userDishService.fitUserDishesToPortions(userDishes);

        Dish summary = userDishService.getSummary(userDishes);

        model.addAttribute("special_dishes", userDishes/*special_today_dishes*/);
        model.addAttribute("select_dish", new UserDishDTO());
        model.addAttribute("dishes", dishes);

        model.addAttribute("portions", Portion.values());
        model.addAttribute("mealtimes", Mealtime.values());

        model.addAttribute("summary", summary);
        return "select_dishes";
    }

    @Transactional
    @PostMapping("/select_dishes")
    public String selectDishes(@AuthenticationPrincipal MyUser user, @ModelAttribute("select_dish") UserDishDTO userDishDTO){
        Dish dish = dishesService.findById(userDishDTO.getDish_id());

//        UserDish userDish = userDishService.findByDishAndUser(dish,current_user);
        /*if(userDish != null){
            return "redirect:/select_dishes";
        }else{*/
        /*userDish_.setUser(current_user);
        userDish_.setDate(LocalDate.now());
        userDishService.save(userDish_);*/
        userDishService.save(UserDish.builder()
                    .id(UserDish.id_st++)
                    .dish(dish)
                    .user(user)
//                    .grams(userDishDTO.getGrams())
                    .value(userDishDTO.getValue())
                    .portion(userDishDTO.getPortion())
                    .mealtime(userDishDTO.getMealtime())
                    .date(LocalDate.now())
                .build());
//        }

        return "redirect:/select_dishes";
    }
    @GetMapping(value = "/select_dishes/delete/{id}/{dish_id}")
    public String deleteSomeSelectedDish(@AuthenticationPrincipal MyUser user,
                                         @PathVariable("id") Long userDishId,
                                         @PathVariable("dish_id") Long dishId){

        Dish dish = dishesService.findById(dishId);

        userDishService.deleteById(userDishId, user, dish);
        return "redirect:/select_dishes";
    }

    @GetMapping("/select_exercises")
    public String selectExercisesPage(@AuthenticationPrincipal MyUser user, Model model){
        List<UserExercise> userExercises = userExerciseService.findAllByUserAndDate(user, LocalDate.now());

        // можно обойтись без этой строчки кода, если в select_dishes.html
        // принимать объекты класса UserDish, а не User
//        List<Dish> special_today_dishes = userExercises.stream().map(UserDish::getDish).collect(Collectors.toList());
        List<Exercise> exercises = exerciseService.findAllByUserIsNull();
        exercises.addAll(exerciseService.findAllByUser(user));

        userExerciseService.fitUserExercisesToMinutes(userExercises);

        Exercise summary = userExerciseService.getSummary(userExercises);

        model.addAttribute("special_exercises", userExercises/*special_today_dishes*/);
        model.addAttribute("select_exercise", new UserExerciseDto());
        model.addAttribute("exercises", exercises);

        model.addAttribute("modes", ExerciseMode.values());

        model.addAttribute("summary", summary);
        return "select_exercises";
    }

    @Transactional
    @PostMapping("/select_exercises")
    public String selectExercises(@AuthenticationPrincipal MyUser user, @ModelAttribute("select_exercise") UserExerciseDto userExerciseDto){
        Exercise exercise = exerciseService.findById(userExerciseDto.getExercise_id());

        userExerciseService.save(UserExercise.builder()
                    .id(userExerciseService.biggest_id()+1)
                    .exercise(exercise)
                    .user(user)
                    .exerciseMode(userExerciseDto.getMode())
                    .minutes(userExerciseDto.getMinutes())
                    .date(LocalDate.now())
                .build());

        return "redirect:/select_exercises";
    }

    @GetMapping(value = "/select_exercises/delete/{id}/{ex_id}")
    public String deleteSomeSelectedExercise(@AuthenticationPrincipal MyUser user,
                                             @PathVariable("id") Long userExerciseId,
                                             @PathVariable("ex_id") Long exId){
        Exercise delete_exercise = exerciseService.findById(exId);
        userExerciseService.deleteById(userExerciseId, user, delete_exercise);
        return "redirect:/select_exercises";
    }


    @GetMapping(value = "/dishes/history")
    public String showDishesHistory(@AuthenticationPrincipal MyUser user, Model model){
        List<UserDish> userDishes = userDishService.findAllByUser(user);
        userDishService.fitUserDishesToPortions(userDishes);

        Map<String, List<UserDish>> map = userDishes.stream().collect(Collectors.groupingBy(userDish -> userDish.getDate().toString()));
        TreeMap<String, List<UserDish>> treeMap = new TreeMap<>(Collections.reverseOrder());
        treeMap.putAll(map);

        Map<Dish,List<UserDish>> userDishesWithSummary = new LinkedHashMap<>();
        treeMap.forEach((key, value) -> userDishesWithSummary.put(
                userDishService.getSummary(value),
                value
        ));

        model.addAttribute("map", userDishesWithSummary);

        return "dishes_history";
    }

    @GetMapping("/exercises/history")
    public String showExercisesHistory(@AuthenticationPrincipal MyUser user, Model model,
                                       @RequestParam(required = false)String date){

        Map<Exercise, List<UserExercise>> userExerciseWIthSummary;

        List<UserExercise> userExercises = userExerciseService.findAllByUser(user);
        userExerciseService.fitUserExercisesToMinutes(userExercises);

        Map<String, List<UserExercise>> mapEx = userExercises.stream().collect(Collectors.groupingBy(userExercise -> userExercise.getDate().toString()));
        TreeMap<String, List<UserExercise>> treeMapEx = new TreeMap<>(Collections.reverseOrder());
        treeMapEx.putAll(mapEx);

        userExerciseWIthSummary = new LinkedHashMap<>();
        Map<Exercise, List<UserExercise>> finalUserExerciseWIthSummary = userExerciseWIthSummary;
        treeMapEx.forEach((key, value)-> finalUserExerciseWIthSummary.put(
                userExerciseService.getSummary(value),
                value
        ));

        model.addAttribute("map", finalUserExerciseWIthSummary);

        return "exercises_history";
    }


    @GetMapping("/exercises/history/{date}")
    public String showExercisesHistoryDate(@AuthenticationPrincipal MyUser user, Model model,
                                           @PathVariable(name = "date") String date) {

            List<UserExercise> allByUserAndDate = userExerciseService.findAllByUserAndDate(user, LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (allByUserAndDate == null || allByUserAndDate.isEmpty()) {
                model.addAttribute("map", null);
                return "exercises_history";
            }
            userExerciseService.fitUserExercisesToMinutes(allByUserAndDate);

            Exercise summary = userExerciseService.getSummary(allByUserAndDate);

            HashMap<Exercise, List<UserExercise>> map = new HashMap<>();
            map.put(summary, allByUserAndDate);
            model.addAttribute("map", map);


            return "exercises_history";
    }

    @GetMapping("/dishes/history/{date}")
    public String showDishesHistoryDate(@AuthenticationPrincipal MyUser user, Model model,
                                        @PathVariable(name = "date") String date){
        List<UserDish> allByUserAndDate = userDishService.findAllByUserAndDate(user, LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (allByUserAndDate == null || allByUserAndDate.isEmpty()) {
            model.addAttribute("map", null);
            return "dishes_history";
        }
        userDishService.fitUserDishesToPortions(allByUserAndDate);

        Dish summary = userDishService.getSummary(allByUserAndDate);

        HashMap<Dish, List<UserDish>> map = new HashMap<>();
        map.put(summary, allByUserAndDate);
        model.addAttribute("map", map);

        return "dishes_history";
    }

    @ModelAttribute("dateDto")
    public DateDto dateDto(){
        return new DateDto();
    }

    @PostMapping("/exercises/history/choose")
    public String chooseExerciseByDate(@ModelAttribute("dateDto")DateDto localDate,
                                       @AuthenticationPrincipal MyUser user,
                                       Model model){
        return showExercisesHistoryDate(user, model, localDate.getDob().toString());
    }
    @PostMapping("/dishes/history/choose")
    public String chooseDishByDate(@ModelAttribute("dateDto")DateDto localDate,
                                       @AuthenticationPrincipal MyUser user,
                                       Model model){
        return showDishesHistoryDate(user, model, localDate.getDob().toString());
    }

    @GetMapping("/dishes-plan")
    public String getDishesPlanPage(@AuthenticationPrincipal MyUser user,
                                    @RequestParam(required = false) String day, Model model){
        if(day != null && !day.isEmpty()){
            //if null return REST DAY
            model.addAttribute("day_", day);
            Map<Mealtime, List<String>> dayDishes = userDishService.getDishesByDay(Integer.parseInt(day));

            /*if(dayExercises == null){
                model.addAttribute("exercises", null);
                model.addAttribute("rest_day", "It's a rest day!");
                model.addAttribute("days", IntStream.rangeClosed(1,30).boxed().collect(Collectors.toList()));
                return "dishes-plan";
            }*/
//            int minutes = userExerciseService.getExercisePerformMinutes(userInfoService.findByUser(user), dayExercises);
            model.addAttribute("dishes", dayDishes);
//            model.addAttribute("minutes", minutes);
            model.addAttribute("select_exercise2", new UserExerciseDto());

        }
        else{
            model.addAttribute("content" , "select.day.to.show.plan");
        }

        model.addAttribute("days", IntStream.rangeClosed(1,30).boxed().collect(Collectors.toList()));

        return "dishes-plan";
    }

    @GetMapping("/exercises-plan")
    public String getExercisePlanPage(@AuthenticationPrincipal MyUser user,
                                      @RequestParam(required = false) String day, Model model){
        if(day != null && !day.isEmpty()){
            //if null return REST DAY
            model.addAttribute("day_", day);
            List<Exercise> dayExercises = userExerciseService.getDayExercisesByDay(Integer.parseInt(day));
            if(dayExercises == null){
                model.addAttribute("exercises", null);
                model.addAttribute("rest_day", "rest_day");
                model.addAttribute("days", IntStream.rangeClosed(1,30).boxed().collect(Collectors.toList()));
                return "exercises-plan";
            }
            int minutes = userExerciseService.getExercisePerformMinutes(userInfoService.findByUser(user), dayExercises);
            model.addAttribute("exercises", dayExercises);
            model.addAttribute("minutes", minutes);
            model.addAttribute("select_exercise2", new UserExerciseDto());

        }
        else{
            model.addAttribute("content" , "select.day.to.show.plan");
        }

        model.addAttribute("days", IntStream.rangeClosed(1,30).boxed().collect(Collectors.toList()));
        return "exercises-plan";
    }

    @Transactional
    @PostMapping("/exercises-plan")
    public String chooseExerciseFromPlan(@AuthenticationPrincipal MyUser user,
                                         @RequestParam(required = false) String day,
                                         @ModelAttribute("select_exercise2") UserExerciseDto userExerciseDto){
        Exercise exercise = exerciseService.findById(userExerciseDto.getExercise_id());

        userExerciseService.save(UserExercise.builder()
                .id(userExerciseService.biggest_id()+1)
                .exercise(exercise)
                .user(user)
                .exerciseMode(userExerciseDto.getMode())
                .minutes(userExerciseDto.getMinutes())
                .date(LocalDate.now())
                .build());
        return "redirect:/exercises-plan?day=" + day;
    }

    @GetMapping("calculate_calories")
    public String calculateCalories(@AuthenticationPrincipal MyUser user,Model model){
        MyUser current_user = userService.getUserByUsername(user.getUsername());

        List<Double> values = userService.calculateSumCalories(current_user);
        model.addAttribute("all_calories", values.get(0));
        if(values.get(1) > 0) {
            model.addAttribute("calories_to_loose", values.get(1));
            model.addAttribute("min_walk_low_temp", values.get(2));
            model.addAttribute("min_walk_medium_temp", values.get(3));
            model.addAttribute("min_walk_high_temp", values.get(4));
        }else
            model.addAttribute("calories_to_gain", -values.get(1));
        return "calculate_calories";
    }

    @GetMapping("/rec-tips")
    public String recTips(){
        return "recommend-tips";
    }

    @GetMapping("/test")
    public String testCss(Model model){
        return "test";
    }
}