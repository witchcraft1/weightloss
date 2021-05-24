package com.rf.springsecurity.services;

import com.rf.springsecurity.dto.PlanDto;
import com.rf.springsecurity.entity.Lifestyle;
import com.rf.springsecurity.entity.Male;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.UserInfo;
import com.rf.springsecurity.repository.UserInfoRepository;
import com.rf.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    UserInfoService(UserInfoRepository userInfoRepository,
                    UserRepository userRepository){
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    public void saveNewUserInfo (UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public List<UserInfo> getAllUserInfos(){
        return userInfoRepository.findAll();
    }

    public UserInfo findActiveInfoByUser(MyUser user){
        return userInfoRepository.findByUser(user);
    }

    public List<UserInfo> findAllActiveInfoByUser(MyUser user){
        return userInfoRepository.findAllByUser(user);
    }

    //@Transactional -> userInfo = userInfoRepo.findByUser or byId -> userInfo.age(newAge).height(newHeight) -> that's all need
    @Transactional
    public void updateUserInfo(UserInfo userInfo, MyUser user){
        UserInfo info = userInfoRepository.findByUser(user);
        info.setAge(userInfo.getAge());
        info.setWeight(userInfo.getWeight());
        info.setGoalWeight(userInfo.getGoalWeight());
        info.setHeight(userInfo.getHeight());
        info.setNickname(userInfo.getNickname());
        info.setMale(userInfo.getMale());
        info.setLifestyle(userInfo.getLifestyle());
        info.setExercisesPerWeek(userInfo.getExercisesPerWeek());
        info.setWeightLossPerWeek(userInfo.getWeightLossPerWeek());
//        userInfoRepository.updateUserInfo(age,height,weight,lifestyle,user);
    }

    private double getLifeStyleCoef(Lifestyle ls){
        return ls == Lifestyle.PASSIVE ? 1.2 :
               ls == Lifestyle.SEDENTARY ? 1.375 :
               ls == Lifestyle.ACTIVE ? 1.55 :
                       1.725;//               ls == Lifestyle.VERY_ACTIVE ? 1.725 : 0;
    }

    private int calcDailyCalBudget(UserInfo userInfo){
        return (int) ((10*userInfo.getWeight()
                      + 6.25 * userInfo.getHeight()
                      - 5*userInfo.getAge()
                      + (userInfo.getMale() == Male.MALE ? 5: -161))
                      * getLifeStyleCoef(userInfo.getLifestyle()));
    }

    public PlanDto getPlan(MyUser user){
        UserInfo info = userInfoRepository.findByUser(user);

        float totalWeightLoss = info.getWeight() - info.getGoalWeight();
        int daysForGoal = (int)Math.ceil(totalWeightLoss/info.getWeightLossPerWeek()*7);

        return PlanDto.builder()
                    .dailyCalorieBudget(calcDailyCalBudget(info))
                    .totalWeightLoss(totalWeightLoss)
                    .weeklyWeightLoss(info.getWeightLossPerWeek())
                    .goalDate(LocalDate.now().plusDays(daysForGoal).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();
    }
}
