package com.rf.springsecurity.exceptions;

public class UserAlreadyExistsException extends RegFailedException{
    private String login;
    public UserAlreadyExistsException(String login){
        this.login = login;
    }

    @Override
    public String getMessage() {
        return "User with login " + login + " already exists";
    }
}
