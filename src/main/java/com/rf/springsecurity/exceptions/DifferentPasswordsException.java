package com.rf.springsecurity.exceptions;

public class DifferentPasswordsException extends RegFailedException{
    @Override
    public String getMessage() {
        return "Паролі різні!";
    }
}
