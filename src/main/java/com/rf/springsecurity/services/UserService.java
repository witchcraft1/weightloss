package com.rf.springsecurity.services;

import com.rf.springsecurity.constants.Data;
import com.rf.springsecurity.dto.UserDTO;
import com.rf.springsecurity.dto.UserInfoDto;
import com.rf.springsecurity.entity.*;
import com.rf.springsecurity.dto.UsersDTO;
import com.rf.springsecurity.exceptions.*;
import com.rf.springsecurity.repository.DishesRepository;
import com.rf.springsecurity.repository.UserDishRepository;
import com.rf.springsecurity.repository.UserInfoRepository;
import com.rf.springsecurity.repository.UserRepository;
import com.rf.springsecurity.security.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DishesRepository dishesRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserDishRepository userDishRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, DishesRepository dishesRepository, UserInfoRepository userInfoRepository, UserDishRepository userDishRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dishesRepository = dishesRepository;
        this.userInfoRepository = userInfoRepository;
        this.userDishRepository = userDishRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(()->new UsernameNotFoundException(login));
    }

    public MyUser getUserByUsername(String login) throws UsernameNotFoundException{
       return (MyUser) loadUserByUsername(login);//TODO delete this method and use loadUserByUsername
    }

    public UsersDTO getAllUsers() {
        //TODO checking for an empty user list
        return new UsersDTO(userRepository.findAll());
    }

    private int getAge(LocalDate dob){
        return (int)ChronoUnit.YEARS.between(dob,LocalDate.now());
    }

    private String isCompleteUserOrElseGetEmptyField(UserDTO userDto){
        return !userDto.getPassword().isEmpty() ?
                (!userDto.getLogin().isEmpty() ?
                        (!userDto.getConfirmedPassword().isEmpty() ?
                                null : "confirmed password") : "login") : "password";
    }

    private String isCompleteUserInfoOrElseGetEmptyField(UserInfoDto userInfoDto){
        return userInfoDto.getWeight() != 0 ?
                (userInfoDto.getGoalWeight() != 0 ?
                        (userInfoDto.getHeight() != 0 ?
                                (userInfoDto.getMale() != null ?
                                        (userInfoDto.getDob() != null ?
                                                (userInfoDto.getLifestyle() != null ?
                                                        (userInfoDto.getExercisesPerWeek() != 0 ?
                                                                (userInfoDto.getWeightLossPerWeek() != 0.0 ?
                                                                        null : "weight loss per week") : "exercises per week") : "lifestyle") : "date of birth"): "male") : "height") : "goal weight") : "weight";
    }

    private String getMessageIfWrongRange(UserInfoDto userInfoDto){
        String message = "Parameter  %s: %d is  %s!";

        boolean isBig = false;

        if((isBig = userInfoDto.getWeight() > 300) || userInfoDto.getWeight() < 30)
            return String.format(message, "weight", userInfoDto.getWeight(), isBig ? "big" : "small");
        if((isBig = userInfoDto.getGoalWeight() > 300) || userInfoDto.getGoalWeight() < 30)
            return String.format(message, "goal weight", userInfoDto.getGoalWeight(), isBig ? "big" : "small");
        if((isBig = userInfoDto.getHeight() > 230) || userInfoDto.getHeight() < 70)
            return String.format(message, "height", userInfoDto.getHeight(), isBig ? "big" : "small");

        return null;
    }

    public void saveNewUser (UserDTO userDto, UserInfoDto userInfoDto) throws RegFailedException {
        String emptyField;
        if((emptyField = isCompleteUserInfoOrElseGetEmptyField(userInfoDto)) != null ||
           (emptyField = isCompleteUserOrElseGetEmptyField(userDto)) != null)
            throw new EmptyFieldException(emptyField);

        String msg;
        if((msg = getMessageIfWrongRange(userInfoDto)) != null)
            throw new ParameterRangeException(msg);

        Optional<MyUser> optionalMyUser = userRepository.findByLogin(userDto.getLogin());

        if(optionalMyUser.isPresent())
            throw new UserAlreadyExistsException(userDto.getLogin());
        if(!userDto.getPassword().equals(userDto.getConfirmedPassword()))
            throw new DifferentPasswordsException();

        int age = getAge(userInfoDto.getDob());
        if(age < 18)
            throw new NotAdultException(age);

        MyUser user = MyUser.builder()
                    .login(userDto.getLogin())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .role(UserRole.USER)
                    .isEnabled(true)
                    .isAccountNonExpired(true)
                    .isCredentialsNonExpired(true)
                    .isAccountNonLocked(true)
                .build();
        userRepository.save(user);

        UserInfo userInfo = UserInfo.builder()
                    .nickname(userInfoDto.getNick())
                    .weight(userInfoDto.getWeight())
                    .goalWeight(userInfoDto.getGoalWeight())
                    .height(userInfoDto.getHeight())
                    .age(age)
                    .male(Male.valueOf(userInfoDto.getMale()))
                    .lifestyle(Lifestyle.valueOf(userInfoDto.getLifestyle()))
                    .exercisesPerWeek(userInfoDto.getExercisesPerWeek())
                    .weightLossPerWeek(userInfoDto.getWeightLossPerWeek())
                    .user(user)
                .build();
        userInfoRepository.save(userInfo);
    }


    public void saveUser(MyUser user){
        userRepository.save(user);
    }
    public MyUser findById(Long id){
        return userRepository.findById(id).get();
    }

    public List<Double> calculateSumCalories(MyUser user){
//        List<Dish> dishes = dishesRepository.findAllByUsers(user);

        List<UserDish> userDishes = userDishRepository.findAllByUser(user);
        List<Dish> dishes = new ArrayList<>();
        userDishes.forEach(userDish -> {dishes.add(userDish.getDish());});

        UserInfo userInfo = userInfoRepository.findByUser(user);
        Lifestyle lifestyle = Lifestyle.PASSIVE;
        if(userInfo!=null)lifestyle = userInfo.getLifestyle();
        List<Double> values = new ArrayList<>();
        int sum = dishes.stream().mapToInt(Dish::getCalories).sum();
        values.add((double) sum);
        int calories_to_loose = lifestyle == Lifestyle.ACTIVE ? sum - Data.NON_ACTIVE_MAN_LOOSE_CALORIES *2 : sum - Data.NON_ACTIVE_MAN_LOOSE_CALORIES;
        values.add((double)calories_to_loose);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_LOW_TEMP);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_MEDIUM_TEMP);
        values.add(1.*calories_to_loose/Data.CALORIES_PER_ONE_MINUTE_HIGH_TEMP);
        return values;
    }
}
