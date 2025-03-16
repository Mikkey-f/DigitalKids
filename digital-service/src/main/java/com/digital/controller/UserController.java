package com.digital.controller;

import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ResultSuccessEnum;
import com.digital.model.request.UserLoginReq;
import com.digital.model.request.UserRegisterReq;
import com.digital.model.vo.LoginUserVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:06
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户注册
     * @param userRegisterReq 用户注册请求
     * @return 返回响应
     */
    @PostMapping("/register")
    public Result registerUser(@RequestBody UserRegisterReq userRegisterReq) {
        Result result = userService.registerUser(userRegisterReq.getUsername(), userRegisterReq.getPassword(), userRegisterReq.getAvatar(), userRegisterReq.getPhoneNum(), userRegisterReq.getRole());
        result.setMsg(ResultSuccessEnum.REGISTER_SUCCESS.getMsg());
        return result;
    }

    /**
     * 登录
     * @param userLoginReq
     * @return
     */
    @PostMapping("/login")
    public Result<LoginUserVo> loginUser(@RequestBody UserLoginReq userLoginReq) {
        if (userLoginReq == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }
        String phoneNum = userLoginReq.getPhoneNum();
        String userPassword = userLoginReq.getUserPassword();
        if (StringUtils.isAnyBlank(phoneNum, userPassword)) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        return userService.loginUser(phoneNum, userPassword);
    }

    
}
