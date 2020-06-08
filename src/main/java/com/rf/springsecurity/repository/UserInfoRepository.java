package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.Lifestyle;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserAndActiveTrue(MyUser user);
    List<UserInfo> findAllByUserAndActiveTrue(MyUser user);

   @Transactional
   @Modifying
    @Query(value = "update UserInfo u set u.age = :age, u.height = :height, u.weight = :weight," +
            "u.lifestyle = :style where u.user = :user")
    void updateUserInfo(@Param("age") Integer age, @Param("height")Integer height,
                        @Param("weight")Integer weight, @Param("style") Lifestyle lifestyle,
                          @Param("user") MyUser user);
}
