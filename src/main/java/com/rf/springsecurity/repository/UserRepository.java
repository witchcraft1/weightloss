package com.rf.springsecurity.repository;
import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u from User u where u.login = :log  ")
    User findByLogin(@Param("log")String login);

    Optional<User> findById(Long id);

    @Modifying
    @Query(value = "UPDATE public.users u set u.userInfo = :usr_info where users_id = :currnt_user",
            nativeQuery = true)
    void updateByUserAndUserInfo(@Param("currnt_user") User user,
                                 @Param("usr_info")UserInfo usr_info);
}

