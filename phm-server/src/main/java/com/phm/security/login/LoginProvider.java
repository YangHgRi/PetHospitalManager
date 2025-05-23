package com.phm.security.login;

import com.phm.security.details.ClientDetails;
import com.phm.security.details.DoctorDetails;
import com.phm.security.model.LoginVo;
import com.phm.security.model.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 想要实现分表登录
 */
@Component
public class LoginProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(LoginProvider.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ClientDetails clientDetails;
    private final DoctorDetails doctorDetails;

    public LoginProvider(ClientDetails clientDetails,
                         DoctorDetails doctorDetails) {
        this.clientDetails = clientDetails;
        this.doctorDetails = doctorDetails;
        // 让用户名未找到的异常不被转化为凭证错误异常
        setHideUserNotFoundExceptions(false);
    }

    /**
     * 校验密码有效性
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("校验密码有效性");
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("接收到的凭证为空");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("密码未与数据库匹配");
        }
    }

    /**
     * 根据用户名获取用户
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof LoginVo login) {
            log.info("角色：{}，用户名:{}", login.role(), login.username());
            if (login.role() == RoleEnum.DOCTOR) {
                return doctorDetails.loadUserByUsername(login.username());
            } else return clientDetails.loadUserByUsername(login.username());
        }
        log.warn("你好像在用传统方式登录");
        return clientDetails.loadUserByUsername(username);
    }

    /**
     * 授权持久化
     */
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        log.info("授权持久化.");
        return super.createSuccessAuthentication(principal, authentication, user);
    }
}
