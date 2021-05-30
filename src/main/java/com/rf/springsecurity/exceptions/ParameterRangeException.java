package com.rf.springsecurity.exceptions;

public class ParameterRangeException extends RegFailedException{
    private String paramName;
    private String cause;
    private String message;
    private int value;

    public ParameterRangeException(
            String paramName,
            String cause,
            int value) {
        this.paramName = paramName;
        this.cause = cause;
        this.value = value;
    }

    public ParameterRangeException(String msg){
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message != null ? message : "Параметр " + paramName + ": " + value +  " є " + cause;
    }
}
