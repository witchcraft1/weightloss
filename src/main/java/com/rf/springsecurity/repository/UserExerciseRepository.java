package com.rf.springsecurity.repository;


import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.user_exercise.UserExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {
    List<UserExercise> findAllByUser(MyUser user);
    List<UserExercise> findAllByUserAndDate(MyUser user, LocalDate date);
    List<UserExercise> findAllByExercise(Exercise exercise);
    UserExercise findByExerciseAndUser(Exercise exercise, MyUser user);

    void deleteUserExerciseByUserAndExercise(MyUser user, Exercise exercise);
    void deleteAllByExercise( Exercise exercise);
    void deleteByIdAndUserAndExercise(Long id, MyUser user, Exercise exercise);
    List<UserExercise> findAll();

}
