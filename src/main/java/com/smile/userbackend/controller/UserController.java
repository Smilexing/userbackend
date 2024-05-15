package com.smile.userbackend.controller;

import com.smile.userbackend.common.BaseResponse;
import com.smile.userbackend.common.ErrorCode;
import com.smile.userbackend.common.ResultUtil;
import com.smile.userbackend.exception.BusinessException;
import com.smile.userbackend.model.User;
import com.smile.userbackend.model.request.UserLoginRequest;
import com.smile.userbackend.model.request.UserRegisterRequest;
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
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user);
    }

//    根据用户名进行查询
//    @GetMapping("/search")
//    public BaseResponse<User> userSearch(String username,HttpServletRequest request) {
//        return ResultUtil.success()
//    }


    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword, request);
        return ResultUtil.success(result);

    }

    @PostMapping("/logout")
    public BaseResponse userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtil.success(result);
    }

    //    测试全局异常处理
    @GetMapping("/getById/{userId}")
    public BaseResponse<User> getById(@PathVariable Integer userId) {

        // 手动抛出异常
        int a = 10 / 0;
        return ResultUtil.success(userService.getById(userId));
    }


}
