package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public void saveNewExercise(Exercise exercise) throws Exception{
        exerciseRepository.save(exercise);
    }

    public void deleteExerciseById(Long id){
        exerciseRepository.deleteById(id);
    }

    public Exercise findById(Long id){
        return exerciseRepository.findById(id).get();
    }

    public List<Exercise> findAllByUser(MyUser user){
        return exerciseRepository.findAllByUser(user);
    }

    public List<Exercise> findAllByUserIsNull(){
        return exerciseRepository.findAllByUserIsNull();
    }

    public boolean existsExerciseByNameAndUserIsNull(String name){
        return exerciseRepository.existsExerciseByNameAndUserIsNull(name);
    }

}
