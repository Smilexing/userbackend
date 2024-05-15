package com.smile.userbackend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.userbackend.model.User;
import com.smile.userbackend.service.UserService;
import com.smile.userbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
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
//        与数据库中数据进行匹配
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); //创建一个查询器
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        User user = userMapper.selectOne(queryWrapper);
//        判断-用户不存在
        if (user == null) {
            log.info("user login failed,userAccount isn't exist");
            return null;
        }
        return user;
    }
}




