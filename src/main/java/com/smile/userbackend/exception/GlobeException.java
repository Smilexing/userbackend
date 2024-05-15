package com.smile.userbackend.exception;

import com.smile.userbackend.common.BaseResponse;
import com.smile.userbackend.common.ErrorCode;
import com.smile.userbackend.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 17:28
 */
@RestControllerAdvice
@Slf4j
public class GlobeException {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(),e);
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException:", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
