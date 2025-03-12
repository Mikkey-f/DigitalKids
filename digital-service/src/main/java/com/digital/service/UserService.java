package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.User;
import com.digital.result.Result;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:17
 */
public interface UserService extends IService<User> {

    Result registerUser(String username, String password, String avatar, String phoneNumber);

    Result loginUser(String phoneNum, String password);
}
