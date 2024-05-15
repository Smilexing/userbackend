package com.smile.userbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: 用户登录请求体
 * @date 2024/5/14 12:36
 */
@Data
public class UserLoginRequest implements Serializable {
//    用户账号
    private String userAccount;
//    用户密码
    private String userPassword;
}
