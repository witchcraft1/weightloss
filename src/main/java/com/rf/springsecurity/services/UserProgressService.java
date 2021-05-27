package com.rf.springsecurity.services;

import com.rf.springsecurity.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProgressService {
    private final UserProgressRepository userProgressRepository;

    @Autowired
    public UserProgressService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }
}
