package com.rf.springsecurity.controller;


import com.rf.springsecurity.dto.UserDishDTO;
import com.rf.springsecurity.entity.*;
import com.rf.springsecurity.services.DishesService;
import com.rf.springsecurity.services.UserInfoService;
import com.rf.springsecurity.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Slf4j
@Controller
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MainController {

    private UserService userService;
    private DishesService dishesService;
    private UserInfoService userInfoService;

    @Autowired
    public MainController(UserService userService, DishesService dishesService, UserInfoService userInfoService ) {
        this.userService = userService;
        this.dishesService = dishesService;
        this.userInfoService = userInfoService;
    }

    @RequestMapping("/")
    public String getMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        model.addAttribute("login", user.getUsername());
        model.addAttribute("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        return "hello";
    }


    @GetMapping("/all_users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model){//TODO переробити UserInfo na User
        List<UserInfo> onlyActiveInfoList = userInfoService.getAllUserInfos()
                .stream().filter(UserInfo::isActive).collect(Collectors.toList());
        model.addAttribute("users", onlyActiveInfoList);//userService.getAllUsers().getUsers());
        return "users";
    }

    @GetMapping("/dishes")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllDishes(Model model){
        model.addAttribute("dishes", dishesService.getAllDishes().getDishes());
        return "dishes";
    }

    @PostMapping("/dishes")
    public String addDish(Dish dish, Model model) {
            try {
                dishesService.saveNewDish(dish);
            } catch (Exception ex) {
                log.info(dish.getName() + "dish already exists");
                model.addAttribute("dishes", dishesService.getAllDishes().getDishes());
                model.addAttribute("message", "dish already exists");
                return "dishes";
            }

        return "redirect:/dishes";
    }
    @GetMapping(value = "/dishes/delete/{id}")
    public String deleteSomeDish(@PathVariable("id")Long dishId, Model model){
        Dish delete_dish = dishesService.findById(dishId);
        for (User user : userService.getAllUsers().getUsers()) {
            user.getDishes().remove(delete_dish);
            userService.saveUser(user);
        }
        try {
            dishesService.deleteDishById(dishId);
        }catch (Exception ex){
            model.addAttribute("message", "there are no dish with such ID");
            return "dishes";
        }
         return "redirect:/dishes";
    }
    @GetMapping("add_user_info")
    public String showUserInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());
        UserInfo userInfo = userInfoService.findActiveInfoByUser(current_user);
        model.addAttribute("current_user", userInfo);
        return "add_user_info";
    }

    @PostMapping("add_user_info")
    public String addUserInfo(UserInfo userInfo, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());
        if(current_user.getUserInfo() == null) {
            userInfo.setActive(true);
            userInfo.setUser(current_user);
            userInfoService.saveNewUserInfo(userInfo);
        }else{
            userInfoService.updateUserInfo(userInfo.getAge(),userInfo.getHeight(),userInfo.getWeight(),
                    userInfo.getLifestyle(), current_user);
        }
        return "redirect:/add_user_info";
    }

    @GetMapping("add_user_info/select_recent")
    public String selectSomeRecentDataForUser(Model model){
        model.addAttribute("userInfos",userInfoService);
        return "select_recent_data";
    }

    @GetMapping("select_dishes")
    public String selectDishesPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());

        model.addAttribute("special_dishes", dishesService.findAllByUser(current_user));
        model.addAttribute("select_dish", new UserDishDTO());
        model.addAttribute("dishes", dishesService.getAllDishes().getDishes());
        return "select_dishes";
    }

    @Transactional
    @PostMapping("select_dishes")
    public String selectDishes(@ModelAttribute("select_dish") UserDishDTO userDishDTO, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());

        Dish dish = dishesService.findById(userDishDTO.getDish_id());
        if(current_user.getDishes().contains(dish))
            return "redirect:/select_dishes";
        current_user.getDishes().add(dish);
        userService.saveUser(current_user);
        return "redirect:/select_dishes";
    }
    @GetMapping(value = "/select_dishes/delete/{id}")
    public String deleteSomeSelectedDish(@PathVariable("id") Long dish_id, Model model){
        Dish delete_dish = dishesService.findById(dish_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());
        current_user.getDishes().remove(delete_dish);
        userService.saveUser(current_user);

        return "redirect:/select_dishes";
    }

    @GetMapping("calculate_calories")
    public String calculateColories(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());

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

}