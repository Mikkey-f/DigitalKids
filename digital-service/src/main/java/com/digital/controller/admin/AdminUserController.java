package com.digital.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.mapper.UserMapper;
import com.digital.model.entity.User;
import com.digital.model.request.page.PageReq;
import com.digital.model.request.user.DeleteUserReq;
import com.digital.model.request.user.UserAddReq;
import com.digital.model.request.user.UserUpdateReq;
import com.digital.result.Result;
import com.digital.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/26 16:52
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;
    /**
     * admin直接添加用户
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Long> addUser(@RequestBody UserAddReq userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        if (!result) {
            throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
        }
        return Result.success(user.getId());
    }

    /**
     * 删除用户by admin
     * @param deleteRequest
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> deleteUser(@RequestBody DeleteUserReq deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        if (!b) {
            return Result.error(ResultErrorEnum.DB_DONT_HAVE_THIS_USER.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 更改用户by admin
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> updateUser(@RequestBody UserUpdateReq userUpdateRequest,
                                      HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
        }
        return Result.success(true);
    }

    /**
     * 仅限admin获取user
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
        }
        return Result.success(user);
    }

    /**
     * admin 获取用户分页列表
     * @param pageReq
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<User>> listUserByPage(@RequestBody PageReq pageReq) {
        long current = pageReq.getCurrent();
        long size = pageReq.getPageSize();
        Page<User> page = new Page<>(current, size);
        Page<User> userPage = userMapper.selectPage(page, null);// null 表示无查询条件，可替换为 QueryWrapper 等条件构造器

        return Result.success(userPage);
    }
}
