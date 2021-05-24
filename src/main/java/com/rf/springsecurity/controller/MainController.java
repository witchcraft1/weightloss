package com.rf.springsecurity.controller;


import com.rf.springsecurity.dto.BooleanDTO;
import com.rf.springsecurity.dto.PlanDto;
import com.rf.springsecurity.dto.UserDishDTO;
import com.rf.springsecurity.entity.*;
import com.rf.springsecurity.security.UserRole;
import com.rf.springsecurity.services.DishesService;
import com.rf.springsecurity.services.UserDishService;
import com.rf.springsecurity.services.UserInfoService;
import com.rf.springsecurity.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Slf4j
@Controller
/*@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)*/
public class MainController {

    private UserService userService;
    private DishesService dishesService;
    private UserInfoService userInfoService;
    private UserDishService userDishService;

    @Autowired
    public MainController(UserService userService, DishesService dishesService, UserInfoService userInfoService, UserDishService userDishService ) {
        this.userService = userService;
        this.dishesService = dishesService;
        this.userInfoService = userInfoService;
        this.userDishService = userDishService;
    }

    @RequestMapping("/")
    public String getMainPage(@AuthenticationPrincipal MyUser user, Model model/*, Principal p*/) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
        model.addAttribute("login", user.getUsername());
        model.addAttribute("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        model.addAttribute("plan_dto", userInfoService.getPlan(user));
        /*model.addAttribute("daily_calorie_budget", "");
        model.addAttribute("total_weight_loss", "kilograms");
        model.addAttribute("weekly_weight_loss", "kilograms");
        model.addAttribute("goal_date", "");*/

        return "hello";
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
        return "dishes";
    }

    @PostMapping("/dishes")
    public String addDish(@AuthenticationPrincipal MyUser user,Dish dish, @ModelAttribute("check-box")BooleanDTO booleanDTO, Model model) {
        if(dishesService.existsDishByNameAndUserIsNull(dish.getName())){//TODO зробить так шоб було видно хто додав блюдо
            model.addAttribute("message", "dish already exists");
            /*log.info(dish.getName() + "dish already exists");
            model.addAttribute("dishes", dishesService.findAllWhereUserIsNull());
            model.addAttribute("special_dishes", dishesService.findAllByUser(userService.getUserByUsername(user.getUsername())));
            model.addAttribute("role", userService.getUserByUsername(user.getUsername()).getRoles().getAuthority());
            model.addAttribute("checkBox", new BooleanDTO());*/

            return getAllDishes(user,model);
//            return "dishes";
        }

        if(user.getRole().equals(UserRole.USER) || booleanDTO.isBoolValue()){
            dish.setUser(userService.getUserByUsername(user.getUsername()));
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

    @GetMapping("select_dishes")
    public String selectDishesPage(@AuthenticationPrincipal MyUser user,Model model){
        MyUser current_user = userService.getUserByUsername(user.getUsername());
        List<UserDish> userDishes = userDishService.findAllByUserAndDate(current_user, LocalDate.now());

        // можно обойтись без этой строчки кода, если в select_dishes.html
        // принимать объекты класса UserDish, а не User
        List<Dish> special_today_dishes = userDishes.stream().map(UserDish::getDish).collect(Collectors.toList());
        List<Dish> dishes = dishesService.findAllWhereUserIsNull();
        dishes.addAll(dishesService.findAllByUser(current_user));

        model.addAttribute("special_dishes", special_today_dishes);
        model.addAttribute("select_dish", new UserDishDTO());
        model.addAttribute("dishes", dishes);
        return "select_dishes";
    }

    @Transactional
    @PostMapping("select_dishes")
    public String selectDishes(@AuthenticationPrincipal MyUser user,@ModelAttribute("select_dish") UserDishDTO userDishDTO, Model model){
        MyUser current_user = userService.getUserByUsername(user.getUsername());

        Dish dish = dishesService.findById(userDishDTO.getDish_id());

        UserDish userDish = userDishService.findByDishAndUser(dish,current_user);
        /*if(userDish != null){
            return "redirect:/select_dishes";
        }else{*/
            userDishService.save(UserDish.builder()
                    .dish(dish)
                    .user(current_user)
                    .grams(userDishDTO.getGrams())
                    .date(LocalDate.now())
                    .build());
//        }

        return "redirect:/select_dishes";
    }
    @GetMapping(value = "/select_dishes/delete/{id}")
    public String deleteSomeSelectedDish(@AuthenticationPrincipal MyUser user, @PathVariable("id") Long dish_id, Model model){
        Dish delete_dish = dishesService.findById(dish_id);
        MyUser current_user = userService.getUserByUsername(user.getUsername());
        userDishService.deleteByDishAndUser(delete_dish, current_user);
        return "redirect:/select_dishes";
    }

    @GetMapping(value = "/select_dishes-history")
    public String showHistory(@AuthenticationPrincipal MyUser user, Model model){
//        userDishService.findAllByUser();
        List<UserDish> userDishes = userDishService.findAllByUser(userService.getUserByUsername(user.getUsername()));

        Map<String, List<UserDish>> map = userDishes.stream().collect(Collectors.groupingBy(userDish -> userDish.getDate().toString()));
        TreeMap<String, List<UserDish>> treeMap = new TreeMap<>(Collections.reverseOrder());
        treeMap.putAll(map);
        model.addAttribute("map", treeMap);

        return "dishes_history";
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
    @GetMapping("/test")
    public String testCss(Model model){
        return "test";
    }
}