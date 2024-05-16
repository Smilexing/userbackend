package com.smile.userbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.userbackend.common.BaseResponse;
import com.smile.userbackend.common.ErrorCode;
import com.smile.userbackend.common.ResultUtil;
import com.smile.userbackend.constant.UserConstant;
import com.smile.userbackend.exception.BusinessException;
import com.smile.userbackend.model.User;
import com.smile.userbackend.model.request.UserLoginRequest;
import com.smile.userbackend.model.request.UserRegisterRequest;
import com.smile.userbackend.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.smile.userbackend.constant.UserConstant.ADMIN_ROLE;
import static com.smile.userbackend.constant.UserConstant.USER_LOGIN_STATE;

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

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;      //通过session就可以拿到所有的信息，完全没必要再拿到id再去查数据库
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
//        long userid = currentUser.getId();
//        User user = userService.getById(userid);
//        User safetyUser = userService.getSafetyUser(user);
        return ResultUtil.success(currentUser);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long userid, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (userid < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(userid);
        return ResultUtil.success(b);
    }

//    根据用户名进行查询
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
//        以列表的形式存放多条数据
        List<User> userList = userService.list(queryWrapper);
//        使用流处理
        List<User> resultList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

        //        不使用流处理
/*        List<User> resultList = new ArrayList<>();
        for (User user : userList) {
            User safety = userService.getSafetyUser(user);
            resultList.add(safety);
        }*/

        return ResultUtil.success(resultList);
    }

//    判断是否是管理员
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null &&user.getUserRole() == ADMIN_ROLE;
    }



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
