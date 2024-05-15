package com.smile.userbackend.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author Tom Smile
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 9:47
 */
public class RegexUtils {
    public static boolean isAccountInvalid(String account) {
        return mismatch(account, RegexPatterns.ACCOUT_REGEX);
    }

    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }


}
