package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.dto.UsersDTO;
import com.rf.springsecurity.entity.UserInfo;
import com.rf.springsecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;


@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(), Collections.singleton(user.getRoles()));
    }

    public User getUserByUsername(String login) throws UsernameNotFoundException{
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return user;
    }

    public UsersDTO getAllUsers() {
        //TODO checking for an empty user list
        return new UsersDTO(userRepository.findAll());
    }


    public void saveNewUser (User user) throws Exception{
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
    public User findById(Long id){
        return userRepository.findById(id).get();
    }

    public void updateUser(User user, UserInfo userInfo){
//        User user3 = userRepository.fl
       /* User user2 = getUserByUsername(user.getLogin());
        userRepository.delete(user);
        user2.addUserInfo(userInfo);
        userRepository.save(user2);*/
       userRepository.updateByUserAndUserInfo(user,userInfo);
    }
}
