package com.rf.springsecurity.entity;

public enum Portion {
    FULL_ITEM(100),
    _100_GRAM(100),
    STANDARD_PORTION(300),
    HUGE_PORTION(500),
    BIG_PORTION(400),
    VERY_BIG_PORTION(700),
    SMALL_PORTION(200),
    VERY_SMALL_PORTION(100),
    SCANTY_PORTION(50);

    private int gramsInPortion;
    Portion(int grams){
        this.gramsInPortion = grams;
    }

    public void setGramsInPortion(int newGrams){
        this.gramsInPortion = newGrams;
    }
    public int getGramsInPortion(){
        return  this.gramsInPortion;
    }
}
