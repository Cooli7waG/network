package com.aitos.xenon.core.exceptions.device;

/**
 * 公钥恢复异常
 * @author xymoc
 */
public class RecoverPublicKeyException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public RecoverPublicKeyException(String message)
    {
        super(message);
    }
}
