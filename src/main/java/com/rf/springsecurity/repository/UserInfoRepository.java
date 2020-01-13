package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.User;
import com.rf.springsecurity.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserAndActiveTrue(User user);
    List<UserInfo> findAllByUserAndActiveTrue(User user);

    @Modifying
    @Query(value = "UPDATE new_database.public.user_info set active = false where users_id = :current_user",
    nativeQuery = true)
    void updateByUserAndActiveTrue(@Param("current_user") User user);


}
