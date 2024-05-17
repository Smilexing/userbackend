package com.smile.userbackend.utils;/**
 * @Author: Yeman
 * @Date: 2024-05-17-10:30
 * @Description:
 */

import com.smile.userbackend.model.dto.UserDto;

/**
 * @description: TODO
 * @author Tom Smile
 * @date 2024/5/17 10:30
 * @version 1.0
 */

// 将信息存至threadlocal中，线程隔离
public class UserHolder {
    // 这里UserDto是除敏感信息之外的属性值
    private static final ThreadLocal<UserDto> t1 = new ThreadLocal<>();

    public static void saveUser(UserDto userDto) {
        t1.set(userDto);

    }
    public static UserDto getUser(){
        return t1.get();
    }
    public static void removeUser(){
        t1.remove();
    }
}
