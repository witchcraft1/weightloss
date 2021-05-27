package com.rf.springsecurity.controller;

import com.rf.springsecurity.dto.UserDTO;
import com.rf.springsecurity.dto.UserInfoDto;
import com.rf.springsecurity.exceptions.RegFailedException;
import com.rf.springsecurity.services.UserInfoService;
import com.rf.springsecurity.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @ModelAttribute("userDto")
    public UserDTO userDto(){
        return new UserDTO();
    }

    @ModelAttribute("userInfoDto")
    public UserInfoDto userInfoDto(){
        return new UserInfoDto();
    }

    @GetMapping("/registration")
    public String registration() {
        return "reg_advanced";
    }

    @PostMapping("/registration")
    public String addUser(
            @ModelAttribute("userDto") UserDTO userDTO,
            @ModelAttribute("userInfoDto") UserInfoDto userInfoDto,
            Model model) {

        try{
            userService.saveNewUser(userDTO, userInfoDto);
        }catch (RegFailedException ex){
//            log.info(user.getLogin() + " login is already exist");
            model.addAttribute("message", ex.getMessage());
            return "reg_advanced";
        }
        return "redirect:/login";
    }
}
