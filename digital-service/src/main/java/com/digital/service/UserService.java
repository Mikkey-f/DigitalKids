package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.User;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.user.LoginUserVo;
import com.digital.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:17
 */
public interface UserService extends IService<User> {

    Result registerUser(String username, String password, String phoneNumber, Object gender);

    Result<LoginUserVo> loginUser(String phoneNum, String password);

    User getLoginUser(HttpServletRequest request);

    LoginUserVo getLoginUserVO(User user);

    GetUserVo getUserVo(User user);

    List<GetUserVo> getUserVO(List<User> userList);
}
