package com.smile.userbackend.service;

import com.smile.userbackend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Tom Smile
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-14 14:42:06
*/
public interface UserService extends IService<User> {

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
    long userRegister(String userAccount, String userPassword, String checkPassword,HttpServletRequest request);

    int userLogout(HttpServletRequest request);

    User getSafetyUser(User user);
}
