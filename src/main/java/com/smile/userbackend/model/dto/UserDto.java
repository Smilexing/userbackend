package com.smile.userbackend.model.dto;

import lombok.Data;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 14:58
 */
@Data
public class UserDto {
    private String phone;
    private String userPassword;
    private String code;
}
