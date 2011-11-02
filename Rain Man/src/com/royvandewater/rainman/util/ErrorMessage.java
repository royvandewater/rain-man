package com.royvandewater.rainman.util;

import java.io.Serializable;

public class ErrorMessage implements Serializable
{
    private static final long serialVersionUID = -7343387176150901052L;
    
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
