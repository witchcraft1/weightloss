package com.rf.springsecurity.exceptions;

public class RegFailedException extends Exception{
    @Override
    public String getMessage() {
        return "Registration denied due to some strange reasons, pls try again";
    }
}
