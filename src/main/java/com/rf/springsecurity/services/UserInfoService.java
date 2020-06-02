package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.Dish;
import com.rf.springsecurity.entity.Lifestyle;
import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.entity.UserInfo;
import com.rf.springsecurity.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    UserInfoRepository userInfoRepository;
    @Autowired
    UserInfoService(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    public void saveNewUserInfo (UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public List<UserInfo> getAllUserInfos(){
        return userInfoRepository.findAll();
    }

    public UserInfo findActiveInfoByUser(User user){
        return userInfoRepository.findByUserAndActiveTrue(user);
    }

    public List<UserInfo> findAllActiveInfoByUser(User user){
        return userInfoRepository.findAllByUserAndActiveTrue(user);
    }

    public void updateUserInfo(Integer age, Integer height, Integer weight, Lifestyle lifestyle, User user){
        userInfoRepository.updateUserInfo(age,height,weight,lifestyle,user);
    }
}
