package com.smile.userbackend.utils;/**
 * @Author: Yeman
 * @Date: 2024-05-17-10:15
 * @Description:
 */

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author Tom Smile
 * @date 2024/5/17 10:15
 * @version 1.0
 */

// 测试发送短信

@RestController
@CrossOrigin("http://localhost:633242")
public class SendCode {
    /**
     * @param targetPhone 用户手机号
     * @return
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @GetMapping("/getCode")
    @ResponseBody
    public String phone(@RequestParam("targetPhone") String targetPhone) {
        // 发送前先看下我们是否已经缓存了验证码
        String yzm = redisTemplate.opsForValue().get("yzm");
        if (yzm == null) {
            //生成六位数验证码
            int authNum = new Random().nextInt(899999) + 100000;
            String authCode = String.valueOf(authNum);
            // 不存在我们发送验证码给用户
            SMSUtils.sendMessage(targetPhone,authCode);
            redisTemplate.opsForValue().set("yzm", authCode, 1, TimeUnit.MINUTES);
            return "发送成功";
        }
        return "请勿重复发送验证码";
    }

}
