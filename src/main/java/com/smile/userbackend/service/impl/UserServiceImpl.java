package com.smile.userbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.userbackend.common.ErrorCode;
import com.smile.userbackend.exception.BusinessException;
import com.smile.userbackend.model.User;
import com.smile.userbackend.service.UserService;
import com.smile.userbackend.mapper.UserMapper;
import com.smile.userbackend.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.smile.userbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Tom Smile
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-05-14 14:42:06
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private final String SALT = "smile";

    @Resource
    private UserMapper userMapper;


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
 }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (!RegexUtils.isAccountInvalid(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //        进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); //创建一个查询器
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        //        与数据库中数据进行匹配
        User user = userMapper.selectOne(queryWrapper);
//        判断-用户不存在
        if (user == null) {
            log.info("user login failed,userAccount isn't exist");
            return null;
        }

//        用户信息脱敏
        User safetyUser = getSafetyUser(user);

//       使用session记录登录态
//       todo 使用redis记录登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,HttpServletRequest request) {

//        校验输入的信息是否合法
        if (StringUtils.isAnyBlank(userAccount, userPassword,checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (!RegexUtils.isAccountInvalid(userAccount)) {
            return -1;
        }
        if (!checkPassword.equals(userPassword) ) {
            return -1;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

//        将数据存入到数据库，密码不能明文存入

        //        进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//保存到数据库（将属性赋值给对象）
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);   //this代表调用的是UserServiceImpl中的方法，不然要引入userService
        if (!saveResult) {

            return -1;
        }
        return user.getId();
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }



    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
//        脱敏对象-最后存放到数据库
//        密码需要先进行加密处理再存放
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

}




