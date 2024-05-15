package com.smile.userbackend.common;

public enum ErrorCode {
    PARAMS_ERROR(400010, "请求参数错误"),
    ;
    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
