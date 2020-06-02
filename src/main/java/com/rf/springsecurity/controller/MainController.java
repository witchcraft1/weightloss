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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public MainController(UserService userService, DishesService dishesService, UserInfoService userInfoService) {
        this.userService = userService;
        this.dishesService = dishesService;
        this.userInfoService = userInfoService;
    }

    @RequestMapping("/")
    public String getMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        model.addAttribute("login", user.getUsername() + " " + user.getPassword());
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
        try{
            dishesService.saveNewDish(dish);
        }catch (Exception ex){
            log.info(dish.getName() + "dish already exists");
            model.addAttribute("dishes", dishesService.getAllDishes().getDishes());
            model.addAttribute("message", "dish already exists");
            return "dishes";
        }
        return "redirect:/dishes";
    }
    @RequestMapping(value = "/dishes/{id}/delete",method = RequestMethod.POST)
    public String deleteSomeDish(@PathVariable("id") Long dishId, Model model){
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

//        current_user.addUserInfo(userInfo);

//        userService.updateUser(current_user, userInfo);
        if(current_user.getUserInfo() == null) {
            userInfo.setActive(true);
            userInfo.setUser(current_user);
            //userInfo.setId(userService.getUserByUsername(user.getUsername()).getId());
//        userInfoService.findActiveInfoByUser(current_user).setActive(false);
//        userInfoService.updateByUserSetActiveFalse(current_user);
            userInfoService.saveNewUserInfo(userInfo);
        }else{
            userInfoService.updateUserInfo(userInfo.getAge(),userInfo.getHeight(),userInfo.getWeight(),
                    userInfo.getLifestyle(), current_user);
        }
       // userService.updateUser(userService.getUserByUsername(user.getUsername()), userInfo);

        return "redirect:/add_user_info";
    }

    @GetMapping("add_user_info/select_recent")
    public String selectSomeRecentDataForUser(Model model){
        model.addAttribute("userInfos",userInfoService);
        return "select_recent_data";
    }

    @GetMapping("select_dishes")
    public String selectDishesPage(Model model){
        model.addAttribute("select_dish", new UserDishDTO());
        model.addAttribute("dishes", dishesService.getAllDishes().getDishes());
        return "select_dishes";
    }

    @PostMapping("select_dishes")
    public String selectDishes(@ModelAttribute("select_dish") UserDishDTO userDishDTO, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User current_user = userService.getUserByUsername(user.getUsername());


        current_user.getDishes().add(dishesService.findById(userDishDTO.getDish_id()));
        userService.saveUser(current_user);
        return "redirect:/select_dishes";
    }
}