package com.rf.springsecurity.exceptions;

public class EmptyFieldException extends RegFailedException{
    private String fieldName;

    public EmptyFieldException(String name){
        fieldName = name;
    }

    @Override
    public String getMessage() {
        return "Field '" + fieldName +"' is empty, please fill it";
    }
}
