package com.phm.model.vo;

import com.phm.util.StrUtil;

/**
 * 注册时所传的模型
 */
public record RegisterVo(String username,
                         String password,
                         String name,
                         boolean sex,
                         String mail) {
    /**
     * 得到加密后的密码
     */
    public String getReallyPwd() {
        return StrUtil.tranPwd(password);
    }
}
