package com.smile.userbackend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.userbackend.common.BaseResponse;
import com.smile.userbackend.common.ErrorCode;
import com.smile.userbackend.common.ResultUtil;
import com.smile.userbackend.constant.UserConstant;
import com.smile.userbackend.exception.BusinessException;
import com.smile.userbackend.mapper.UserMapper;
import com.smile.userbackend.model.User;
import com.smile.userbackend.model.dto.SafeUserDto;
import com.smile.userbackend.model.dto.UserDto;
import com.smile.userbackend.service.UserService;
import com.smile.userbackend.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.smile.userbackend.constant.UserConstant.*;


/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 14:46
 */

/*
 * @Title:
 * @Description: 测试使用验证码登录
 * @param null:
 * @return
 * @Author: Smile
 * @Date: 2024/5/16
 */

//使用手机号+验证码的形式进行登录
//    todo 后续合并手机号、密码登录和验证码登录
@RestController
@RequestMapping("/name")
@Slf4j
public class UserController1 {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //   测试写一版session实现存放code
    @PostMapping("/send")
    public BaseResponse userCode(@RequestParam String phone, HttpSession session) {
        if (StrUtil.isBlank(phone)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
//        校验手机号是否合法
        if (RegexUtils.isPhoneInvaild(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        生成验证码
        String code = RandomUtil.randomNumbers(6);
//        用session存放
//        session.setAttribute("code", code);

//        使用Redis存放-RedisTemplate
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TIME, TimeUnit.MINUTES);


        log.debug("发送验证码：{}" + code);
        return ResultUtil.success(code);

    }

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserDto userDto, HttpSession session) {
        if (userDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        校验手机号是否一致（发送验证码）
        String phone = userDto.getPhone();
//        校验输入的手机号是否合法
        if (RegexUtils.isPhoneInvaild(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        校验存在session中的code是否一致
//         Object cacheCode = session.getAttribute("code");

        // 在Redis中拿到code
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = userDto.getCode();

        if (cacheCode == null || !cacheCode.equals(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

//        验证码一致，则查找该手机号是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
//            没有就以该手机号创建
            user = (User) createUserWithPhone(phone);
        }

        // 通过UUID生成token作为登录令牌--实现登录态
        String token = UUID.randomUUID().toString(true);
        // 作为value存放，存放之前需要脱敏
        SafeUserDto safeUserDto = BeanUtil.copyProperties(user, SafeUserDto.class);

        Map<String, Object> userMap = BeanUtil.beanToMap(safeUserDto, new HashMap<>(), CopyOptions.create()
                .setIgnoreNullValue(true));

        String tokenKey = LOGIN_USER_KEY + token;

        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TIME, TimeUnit.MINUTES);

        // 查询到的数据会保留一份在session，可以通过session拿到用户信息并将其值复制到UserDto之后（不暴露user中的敏感信息）
        // session.setAttribute("user", BeanUtil.copyProperties(user, UserDto.class));
        return ResultUtil.success(user);


    }

    private Object createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUsername(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        userService.save(user);
        return user;
    }
}
