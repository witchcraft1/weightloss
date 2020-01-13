package com.rf.springsecurity.controller;

import com.rf.springsecurity.entity.Role;
import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.entity.UserInfo;
import com.rf.springsecurity.services.UserInfoService;
import com.rf.springsecurity.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegistrationController {

    private UserService userService;
    private UserInfoService userInfoService;
    @Autowired
    public RegistrationController(UserService userService,UserInfoService userInfoService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        user.setActive(true);
        user.setRoles(Role.ROLE_ADMIN);
        try{
            userService.saveNewUser(user);
            userInfoService.saveNewUserInfo(UserInfo.builder().user(user).active(true).build());
            //userInfoService.saveNewUserInfo(new UserInfo().setId());
        }catch (Exception ex){
            log.info(user.getLogin() + " login is already exist");
            model.addAttribute("message", "login is already exist");
            return "registration";
        }
        return "redirect:/login";
    }
}
