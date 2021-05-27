package com.rf.springsecurity.entity.exercise;

public enum ExerciseMode {
    SlOW(1), MEDIUM(1.2f), FAST(1.5f), VERY_FAST(1.8f);

    private float caloriesMultCoeff;

    ExerciseMode(float caloriesMultCoeff){
        this.caloriesMultCoeff = caloriesMultCoeff;
    }

    public float getCaloriesMultCoeff() {
        return caloriesMultCoeff;
    }
}
