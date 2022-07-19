package com.aitos.xenon.core.exceptions.account;

public class AccountExistedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public AccountExistedException(String message)
    {
        super(message);
    }
}
