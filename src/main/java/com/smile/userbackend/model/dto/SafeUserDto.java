package com.smile.userbackend.model.dto;/**
 * @Author: Yeman
 * @Date: 2024-05-16-21:03
 * @Description:
 */

import lombok.Data;

/**
 * @description: TODO
 * @author Tom Smile
 * @date 2024/5/16 21:03
 * @version 1.0
 */

// 脱敏用户类
@Data
public class SafeUserDto {

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

}
