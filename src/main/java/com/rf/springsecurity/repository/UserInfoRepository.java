package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.userinfo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUser(MyUser user);
    List<UserInfo> findAllByUser(MyUser user);

   /*@Transactional
   @Modifying
    @Query(value = "update UserInfo u set u.age = :age, u.height = :height, u.weight = :weight," +
            "u.lifestyle = :style where u.user = :user")
    void updateUserInfo(@Param("age") Integer age, @Param("height")Integer height,
                        @Param("weight")Integer weight, @Param("style") Lifestyle lifestyle,
                          @Param("user") MyUser user);*/
}
