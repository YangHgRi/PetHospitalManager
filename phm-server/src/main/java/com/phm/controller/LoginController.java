package com.phm.controller;

import com.phm.common.Result;
import com.phm.mapper.ClientMapper;
import com.phm.model.entity.Client;
import com.phm.model.entity.Doctor;
import com.phm.model.vo.RegisterVo;
import com.phm.security.model.RoleEnum;
import com.phm.util.StrUtil;
import com.phm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 登录与注册模块
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private final ClientMapper clientMapper;

    @Autowired
    public LoginController(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    /**
     * 判断当前的角色进行跳转
     *
     * @return 当前登录的是管理员或用户或没有
     */
    @GetMapping
    public RoleEnum getLogin() {
        Object principal = UserUtil.getUser();
        if (principal instanceof Client) {
            return RoleEnum.CLIENT;
        } else if (principal instanceof Doctor) {
            return RoleEnum.DOCTOR;
        }
        return null;
    }


    /**
     * 登录成功的用户信息，可能在未登录的情况
     *
     * @return id，账号，名字，电话，照片
     */
    @GetMapping("/user")
    public Client getUser() {
        if (UserUtil.getUser() instanceof Client client) return clientMapper.selectById(client.getClientId());
        return null;
    }

    /**
     * 注册时检查当前账号是否存在
     *
     * @param regUse 用户名
     * @return 不存在
     */
    @GetMapping("/isExist")
    public Result isExist(String regUse) {
        if (StrUtil.isWhite(regUse)) return Result.success("空白字符");
        int nowExist = clientMapper.isExist(regUse);
        if (nowExist == 0) return Result.success("用户不存在");
        else return Result.error("该用户已存在");
    }

    /**
     * 进行注册
     *
     * @param registerVo 客户信息
     * @return 注册成功
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo) {
        if (StrUtil.isWhite(registerVo.username())) return Result.error("用户名不能为空");
        // 设置部分默认信息
        Client client = new Client();
        client.setClientUsername(registerVo.username());
        client.setClientPassword(registerVo.getReallyPwd());
        client.setClientName(registerVo.name());
        client.setClientGender(registerVo.sex());
        client.setClientTel(registerVo.mail());
        client.setClientAge(LocalDate.now());
        return Result.choice("注册", clientMapper.insert(client) > 0);
    }
}
