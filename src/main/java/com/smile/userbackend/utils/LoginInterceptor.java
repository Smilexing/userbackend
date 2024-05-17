package com.smile.userbackend.utils;/**
 * @Author: Yeman
 * @Date: 2024-05-17-10:27
 * @Description:
 */

import com.smile.userbackend.model.dto.UserDto;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/17 10:27
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 拿到session中的用户信息
        Object user = session.getAttribute("user");
        // 用户不存在，则进行拦截
        if (user == null) {
            response.setStatus(401);
            return false;
        }
        UserHolder.saveUser((UserDto) user);
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    //     移除用户
        UserHolder.removeUser();
    }
}
