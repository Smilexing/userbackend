package com.smile.userbackend.exception;

import com.smile.userbackend.common.ErrorCode;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: 自定义异常类
 * @date 2024/5/15 17:22
 */
public class BusinessException extends RuntimeException{
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public BusinessException(int code) {
        this.code = code;
    }
}
