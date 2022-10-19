package com.aitos.xenon.core.exceptions;

/**
 * 参数校验失败
 * @author hnngm
 */
public class ParamValidateInvalidException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    /**
     * 错误提示
     */
    private String message;

    public ParamValidateInvalidException(String message)
    {
        this.message = message;
    }
}
