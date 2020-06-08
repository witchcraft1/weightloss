package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.Lifestyle;
import com.rf.springsecurity.entity.MyUser;
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

    public UserInfo findActiveInfoByUser(MyUser user){
        return userInfoRepository.findByUserAndActiveTrue(user);
    }

    public List<UserInfo> findAllActiveInfoByUser(MyUser user){
        return userInfoRepository.findAllByUserAndActiveTrue(user);
    }

    public void updateUserInfo(Integer age, Integer height, Integer weight, Lifestyle lifestyle, MyUser user){
        userInfoRepository.updateUserInfo(age,height,weight,lifestyle,user);
    }
}
