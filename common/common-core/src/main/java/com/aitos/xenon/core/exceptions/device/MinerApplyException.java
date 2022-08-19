package com.aitos.xenon.core.exceptions.device;

/**
 * miner apply failed
 * @author xymoc
 */
public class MinerApplyException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public MinerApplyException(String message)
    {
        super(message);
    }
}
