package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.config.JwtConfig;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.UserMapper;
import com.digital.model.entity.User;
import com.digital.model.vo.LoginUerVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:19
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtConfig jwtConfig;

    private static final String SALT = "mikkeyf";

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result registerUser(String username, String password, String avatar, String phoneNumber) {
        if (username == null || password == null || avatar == null || phoneNumber == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }

        // 账户检验
        QueryWrapper<User> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("name", username);
        Long l = userMapper.selectCount(usersQueryWrapper);
        if (l > 0) {
            return  Result.error(ResultErrorEnum.ACCOUNT_IS_REPEAT.getMessage());
        }

        usersQueryWrapper.clear();
        usersQueryWrapper.eq("phone", phoneNumber);
        l = userMapper.selectCount(usersQueryWrapper);
        if (l > 0) {
            return  Result.error(ResultErrorEnum.PHONE_NUMBER_IS_REPEAT.getMessage());
        }

        password = passwordEncoder.encode(password);
        User user = new User();
        user.setPassword(password);
        user.setName(username);
        user.setPhone(phoneNumber);
        user.setAvatar(avatar);
        userMapper.insert(user);

        return Result.success();
    }

    @Override
    public Result<LoginUerVo> loginUser(String phoneNum, String password) {
        if (phoneNum == null || password == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }

        if (phoneNum.length() < 4 || password.length() < 4) {
            return Result.error(ResultErrorEnum.PARAM_INPUT_TOO_SHORT.getMessage());
        }

        QueryWrapper<User> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("phone", phoneNum);

        User dbUser = userMapper.selectOne(usersQueryWrapper);
        if (dbUser == null) {
            return Result.error(ResultErrorEnum.THIS_USER_NOT_REGISTER.getMessage());
        }

        if (!passwordEncoder.matches(password, dbUser.getPassword())) {
            return Result.error(ResultErrorEnum.PASSWORD_ERROR.getMessage());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNum, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = customUserDetails.getUser();

        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return Result.error(ResultErrorEnum.THIS_USER_NOT_REGISTER.getMessage());
        }

        String token = jwtConfig.createToken(phoneNum);
        LoginUerVo loginUerVo = new LoginUerVo();
        BeanUtils.copyProperties(user, loginUerVo);
        loginUerVo.setToken(token);

        return Result.success(loginUerVo);
    }
}
