package com.rf.springsecurity.exceptions;

public class RegFailedException extends Exception{
    @Override
    public String getMessage() {
        return "Реєстрація не вдалась через невідому причину, спробуйте знову!";
    }
}
