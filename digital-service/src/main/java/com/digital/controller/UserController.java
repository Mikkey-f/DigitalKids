package com.digital.controller;

import com.digital.enums.ResultSuccessEnum;
import com.digital.model.request.UserRegisterReq;
import com.digital.result.Result;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:06
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户注册
     * @param userRegisterReq 用户注册请求
     * @return 返回响应
     */
    @PostMapping("/user/register")
    public Result registerUser(@RequestBody UserRegisterReq userRegisterReq) {
        Result result = userService.registerUser(userRegisterReq.getUsername(), userRegisterReq.getPassword(), userRegisterReq.getAvatar(), userRegisterReq.getPhoneNum());
        result.setMsg(ResultSuccessEnum.REGISTER_SUCCESS.getMsg());
        return result;
    }
}
