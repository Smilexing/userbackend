package com.smile.userbackend.constant;

public interface UserConstant {
    String USER_LOGIN_STATE = "user_login_state";
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 登录验证码
     */

    public static final String LOGIN_CODE_KEY = "login:code";

    /**
     * 验证码时效
     */
    public static final Long LOGIN_CODE_TIME = 30L;

    /**
     * tokenkey
     */
    public static final String LOGIN_USER_KEY = "login:key";

    /**
     * tokenkey-时效
     */
    public static final Long LOGIN_USER_TIME = 30L;
    /**
     * 默认用户姓名
     */

    public static final String USER_NICK_NAME_PREFIX = "user_";
}
