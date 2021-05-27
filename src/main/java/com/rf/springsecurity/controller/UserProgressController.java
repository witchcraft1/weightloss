package com.rf.springsecurity.controller;

import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.services.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/progress")
public class UserProgressController {
    private final UserProgressService userProgressService;

    @Autowired
    public UserProgressController(UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    @GetMapping
    public String getProgressPage(@AuthenticationPrincipal MyUser user, Model model){
        return "progress";
    }

    @PostMapping
    public String setNewProgress(@AuthenticationPrincipal MyUser user, Model model){
        return "progress";
    }
}
