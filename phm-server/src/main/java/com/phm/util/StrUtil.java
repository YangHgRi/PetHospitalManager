package com.phm.util;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用于判空、加密等字符串操作
 */
public final class StrUtil {
    /**
     * 判断字段是否为空
     *
     * @param str 前端传来的字段
     * @return 不为空
     */
    public static boolean isWhite(@Nullable String str) {
        return str == null || str.isBlank();
    }

    /**
     * 给明文密码加盐加密
     *
     * @param pwd 明文密码
     * @return 加盐加密之后的密码
     */
    public static String tranPwd(String pwd) {
        return new BCryptPasswordEncoder().encode(pwd);
    }
}
