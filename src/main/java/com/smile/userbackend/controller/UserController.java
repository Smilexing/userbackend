package com.smile.userbackend.controller;

import com.smile.userbackend.common.BaseResponse;
import com.smile.userbackend.common.ResultUtil;
import com.smile.userbackend.model.User;
import com.smile.userbackend.model.request.UserLoginRequest;
import com.smile.userbackend.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/14 12:33
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }


        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user);

    }

}
