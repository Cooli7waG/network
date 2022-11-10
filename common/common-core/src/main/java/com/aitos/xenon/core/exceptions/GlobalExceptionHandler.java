package com.aitos.xenon.core.exceptions;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.exceptions.device.MinerApplyException;
import com.aitos.xenon.core.exceptions.device.MinerClaimVerifyException;
import com.aitos.xenon.core.exceptions.device.RecoverPublicKeyException;
import com.aitos.xenon.core.model.Result;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                      HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.failed(ApiStatus.HTTP_REQUEST_METHOD_NOT_SUPPORTED);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.failed(ApiStatus.ERROR);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR);
    }

    /**
     * 参数类型
     */
    @ExceptionHandler(InvalidFormatException.class)
    public Result handleBindException(InvalidFormatException e) {
        log.error(e.getMessage(), e);
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR);
    }

    /**
     * 参数类型
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleBindException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR, message);
    }


    /**
     * 服务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e) {
        log.error("handleServiceException:{}", e);
        return Result.failed(ApiStatus.ERROR);
    }


    /**
     * 校验异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR, String.join(",", msgList));
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(value = ParamValidateInvalidException.class)
    public Result paramValidateInvalidExceptionHandler(ParamValidateInvalidException ex) {
        return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR, ex.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.failed(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(RecoverPublicKeyException.class)
    public Result handleException(RecoverPublicKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.failed(e.getMessage());
    }

    /**
     * MinerClaimVerifyException
     */
    @ExceptionHandler(MinerClaimVerifyException.class)
    public Result handleMinerClaimVerifyException(MinerClaimVerifyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.failed(e.getMessage());
    }

    /**
     * MinerClaimVerifyException
     */
    @ExceptionHandler(MinerApplyException.class)
    public Result handleMinerApplyException(MinerApplyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.failed(e.getMessage());
    }
}

