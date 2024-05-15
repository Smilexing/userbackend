package com.smile.userbackend.model.request;

import lombok.Data;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 10:16
 */
@Data
public class UserRegisterRequest {
    //    用户账号
    private String userAccount;
    //    用户密码
    private String userPassword;
    private String checkPassword;
}
