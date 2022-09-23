package com.aitos.xenon.device.common.exception;

/**
 * miner 总量超出
 * 
 */
public final class MinerTotalNumberException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    public MinerTotalNumberException(String message)
    {
        super(message);
    }
}