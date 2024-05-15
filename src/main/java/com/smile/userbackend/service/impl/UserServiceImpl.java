package com.smile.userbackend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            return null;
 }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        if (RegexUtils.isAccountInvalid(userAccount)) {
            return null;
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

//       todo 记录登录态-redis/session
        return safetyUser;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,HttpServletRequest request) {

//        校验输入的信息是否合法
        if (StringUtils.isAnyBlank(userAccount, userPassword,checkPassword)) {
            return -1;    //todo 改写为异常
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8) {
            return -1;
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

    private User getSafetyUser(User originUser) {
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
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }




}




