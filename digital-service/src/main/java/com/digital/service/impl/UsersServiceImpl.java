package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.UsersMapper;
import com.digital.model.entity.Users;
import com.digital.result.Result;
import com.digital.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:19
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    @Autowired
    UsersMapper usersMapper;

    private static final String SALT = "mikkeyf";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Result registerUser(String username, String password, String avatar, String phoneNumber) {
        if (username == null || password == null || avatar == null || phoneNumber == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }

        // 账户检验
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("name", username);
        Long l = usersMapper.selectCount(usersQueryWrapper);
        if (l > 0) {
            return  Result.error(ResultErrorEnum.ACCOUNT_IS_REPEAT.getMessage());
        }

        usersQueryWrapper.clear();
        usersQueryWrapper.eq("phone", phoneNumber);
        l = usersMapper.selectCount(usersQueryWrapper);
        if (l > 0) {
            return  Result.error(ResultErrorEnum.PHONE_NUMBER_IS_REPEAT.getMessage());
        }

        password = passwordEncoder.encode(password);
        Users user = new Users();
        user.setPassword(password);
        user.setName(username);
        user.setPhone(phoneNumber);
        user.setAvatar(avatar);
        usersMapper.insert(user);

        return Result.success();
    }
}
