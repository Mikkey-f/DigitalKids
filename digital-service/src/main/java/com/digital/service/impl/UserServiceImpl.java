package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.config.JwtConfig;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.RoleEnum;
import com.digital.exception.BusinessException;
import com.digital.mapper.RoleMapper;
import com.digital.mapper.RolePermissionMapper;
import com.digital.mapper.UserMapper;
import com.digital.mapper.UserRoleMapper;
import com.digital.model.entity.Role;
import com.digital.model.entity.RolePermission;
import com.digital.model.entity.User;
import com.digital.model.entity.UserRole;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.user.LoginUserVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-11  19:19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Autowired
    JwtConfig jwtConfig;

    private static final String SALT = "mikkeyf";

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result registerUser(String username, String password, String phoneNumber, Object gender) {
        if (username == null || password == null ||  phoneNumber == null || gender == null) {
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
        user.setRole(UserConstant.ADMIN_ROLE);
        user.setGender(gender);
        user.setAvatar("");
        userMapper.insert(user);
        usersQueryWrapper.clear();

        //匹配用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        //测试阶段写死管理员权限
        userRole.setRoleId((long) RoleEnum.ADMIN.getCode());
        userRoleMapper.insert(userRole);

        return Result.success();
    }

    @Override
    public Result<LoginUserVo> loginUser(String phoneNum, String password) {
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
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        loginUserVo.setToken(token);

        return Result.success(loginUserVo);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            user = customUserDetails.getUser();
            if (user == null) {
                throw new BusinessException(ResultErrorEnum.NOT_LOGIN_USER);
            }
            return user;
        } else {
            return null;
        }
    }

    @Override
    public LoginUserVo getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVo loginUserVO = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public GetUserVo getUserVo(User user) {
        if (user == null) {
            return null;
        }
        GetUserVo userVO = new GetUserVo();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<GetUserVo> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        List<GetUserVo> userVOList = new ArrayList<>();
        for (User user : userList) {
            userVOList.add(getUserVo(user));
        }
        return userVOList;
    }
}
