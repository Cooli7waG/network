package com.aitos.xenon.core.exceptions.device;

public class DeviceExistedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public DeviceExistedException(String message)
    {
        super(message);
    }
}
