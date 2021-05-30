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
        return "Вам наразі тільки " + age + " років, реєстрація можлива з 18!";
    }
}
