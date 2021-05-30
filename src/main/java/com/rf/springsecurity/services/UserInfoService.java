package com.rf.springsecurity.services;

import com.rf.springsecurity.dto.PlanDto;
import com.rf.springsecurity.entity.userinfo.Lifestyle;
import com.rf.springsecurity.entity.userinfo.Male;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.userinfo.UserInfo;
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
    public UserInfo findByUser(MyUser user){
        return userInfoRepository.findByUser(user);
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

    public int calcCaloriesReduce(UserInfo userInfo){
        return (int)(calcDailyCalBudget(userInfo) *coeffReduce(userInfo));

//        return (int)(calcDailyCalBudget(userInfo) * coeff2);
    }

    private double coeffReduce(UserInfo userInfo){
        double coeff = Math.pow(1.375 / getLifeStyleCoef(userInfo.getLifestyle()), .6);

        double coeff2 = userInfo.getWeightLossPerWeek() / 0.5;

        coeff2*=0.2 * coeff;
        return coeff2;
//        return (int)(calcDailyCalBudget(userInfo) * coeff2);
    }

    public int calcDailyBudgetWhenLooseWeight(UserInfo userInfo){
        int budgetWithoutLosingWeight = calcDailyCalBudget(userInfo);
        System.out.println(budgetWithoutLosingWeight);

        double v = budgetWithoutLosingWeight * (1 - coeffReduce(userInfo));
        System.out.println(v);
        return (int) v;
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
