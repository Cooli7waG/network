package com.aitos.xenon.core.exceptions.account;

public class PayerAccountNotExistException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public PayerAccountNotExistException(String message)
    {
        super(message);
    }
}
