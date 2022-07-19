package com.aitos.xenon.core.exceptions.account;

public class OwnerAccountNotExistException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public OwnerAccountNotExistException(String message)
    {
        super(message);
    }
}
