package com.smile.userbackend.common;

public enum ErrorCode {
    SUCCESS(0, "OK"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NULL_ERROR(40001, "请求数据为空"),
    NOT_LOGIN(40100, "未登录"),
    NOT_AUTH(40101, "未认证"),
    SYSTEM_ERROR(50000, "系统错误")
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
