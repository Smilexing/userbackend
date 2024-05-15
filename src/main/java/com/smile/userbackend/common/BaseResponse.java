package com.smile.userbackend.common;

import lombok.Data;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 8:46
 */

@Data
//通用返回类--封装返回值结果
public class BaseResponse<T> {
    private int code;

    private T data;
    private String message;

    public BaseResponse(int code,  T data,String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, String message) {
        this(code, null, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

}
