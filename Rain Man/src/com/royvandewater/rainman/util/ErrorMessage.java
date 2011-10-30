package com.royvandewater.rainman.util;

public class ErrorMessage
{
    private Exception exception;
    private String message;
    
    public ErrorMessage(Exception exception, String message)
    {
        this.exception = exception;
        this.message = message;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public String getMessage() {
        return message;
    }
}
