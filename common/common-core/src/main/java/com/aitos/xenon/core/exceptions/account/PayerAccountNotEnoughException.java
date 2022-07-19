package com.aitos.xenon.core.exceptions.account;

public class PayerAccountNotEnoughException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public PayerAccountNotEnoughException(String message)
    {
        super(message);
    }
}
