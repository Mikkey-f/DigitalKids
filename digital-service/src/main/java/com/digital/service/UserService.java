package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.User;
import com.digital.model.vo.LoginUserVo;
import com.digital.result.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:17
 */
public interface UserService extends IService<User> {

    Result registerUser(String username, String password, String avatar, String phoneNumber, String role);

    Result<LoginUserVo> loginUser(String phoneNum, String password);

    User getLoginUser(HttpServletRequest request) throws Exception;
}
