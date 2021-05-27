package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.userinfo.UserWeightAndCaloriesProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProgressRepository extends JpaRepository<UserWeightAndCaloriesProgress, Long> {

}
