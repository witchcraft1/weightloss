package com.rf.springsecurity.repository;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
    MyUser findByLogin(String login);
    Optional<MyUser> findById(Long id);
    @Modifying
    @Query(value = "UPDATE public.users u set u.userInfo = :usr_info where users_id = :currnt_user",
            nativeQuery = true)
    void updateByUserAndUserInfo(@Param("currnt_user") MyUser user,
                                 @Param("usr_info")UserInfo usr_info);
}

