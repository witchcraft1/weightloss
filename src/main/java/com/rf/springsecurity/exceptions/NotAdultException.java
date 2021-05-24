package com.rf.springsecurity.exceptions;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NotAdultException extends RegFailedException{
    private int age;

    public NotAdultException(int age){
        this.age = age;
    }

    @Override
    public String getMessage() {
        return "You are only " + age + " years old, 18y.o. restriction";
    }
}
