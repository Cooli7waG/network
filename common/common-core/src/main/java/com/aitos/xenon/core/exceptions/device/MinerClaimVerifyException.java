package com.aitos.xenon.core.exceptions.device;

/**
 * miner apply failed
 * @author xymoc
 */
public class MinerClaimVerifyException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public MinerClaimVerifyException(String message)
    {
        super(message);
    }
}
