package com.aitos.xenon.device.common.exception.handler;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.exceptions.account.AccountExistedException;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.exceptions.account.PayerAccountNotEnoughException;
import com.aitos.xenon.core.exceptions.account.PayerAccountNotExistException;
import com.aitos.xenon.core.exceptions.device.DeviceExistedException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.common.exception.MinerSingleNumber4EmailException;
import com.aitos.xenon.device.common.exception.MinerSingleNumberException;
import com.aitos.xenon.device.common.exception.MinerTotalNumberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DeviceGlobalExceptionHandler {

    /**
     *
     */
    @ExceptionHandler(DeviceExistedException.class)
    public Result handleDeviceExistedException(DeviceExistedException e,
                                               HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_DEVICE_ID_EXISTED);
    }

    /**
     * payer 账户不存在
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(PayerAccountNotExistException.class)
    public Result handlePayerAccountNotExistException(PayerAccountNotExistException e,
                                                      HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_PAYER_ACCOUNT_NOT_EXIST);
    }

    /**
     * owner 账户不存在
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(OwnerAccountNotExistException.class)
    public Result handleOwnerAccountNotExistException(OwnerAccountNotExistException e,
                                                      HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_OWNER_ACCOUNT_NOT_EXIST);
    }

    /**
     * payer 账户额度不够
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(PayerAccountNotEnoughException.class)
    public Result handlePayerAccountNotEnoughException(PayerAccountNotEnoughException e,
                                                       HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_PAYER_ACCOUNT_NOT_ENOUGH);
    }

    /**
     * miner 总量超出 限制
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MinerTotalNumberException.class)
    public Result handleMinerTotalNumberException(MinerTotalNumberException e,
                                                  HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_DEVICE_GMAE_MINER_TOTAL_NUMBER);
    }

    /**
     * 单个用户可申请miner量超出限制
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MinerSingleNumberException.class)
    public Result handleMinerSingleNumberException(MinerSingleNumberException e,
                                                   HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_DEVICE_GMAE_MINER_SINGLE_NUMBER);
    }

    /**
     * 单个邮箱可申请miner量超出限制
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MinerSingleNumber4EmailException.class)
    public Result handleMinerSingleNumber4EmailException(MinerSingleNumber4EmailException e, HttpServletRequest request) {
        return Result.failed(ApiStatus.BUSINESS_DEVICE_GMAE_MINER_SINGLE_NUMBER_FOR_EMAIL);
    }
}
