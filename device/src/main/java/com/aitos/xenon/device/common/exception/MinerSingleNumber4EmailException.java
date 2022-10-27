package com.aitos.xenon.device.common.exception;

/**
 * 单个用户可申请miner量超出限制
 *
 */
public final class MinerSingleNumber4EmailException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    public MinerSingleNumber4EmailException(String message)
    {
        super(message);
    }
}
