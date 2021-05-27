package com.rf.springsecurity.repository;

import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    void deleteById(Long id);
    Optional<Exercise> findById(Long id);
    List<Exercise> findAllByUser(MyUser user);
    List<Exercise> findAllByUserIsNull();
    boolean existsExerciseByNameAndUserIsNull(String name);
}
