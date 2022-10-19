package com.aitos.xenon.core.exceptions;

/**
 * 业务异常
 * 
 * @author ruoyi
 */
public final class SignatureVerificationFailedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public SignatureVerificationFailedException()
    {
    }

    public SignatureVerificationFailedException(String message)
    {
        this.message = message;
    }

    public SignatureVerificationFailedException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public SignatureVerificationFailedException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public SignatureVerificationFailedException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}