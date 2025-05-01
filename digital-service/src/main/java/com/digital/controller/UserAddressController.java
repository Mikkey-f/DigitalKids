package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.User;
import com.digital.model.entity.UserAddress;
import com.digital.model.request.userAddress.UserAddressAddReq;
import com.digital.model.request.userAddress.UserAddressUpdateReq;
import com.digital.model.vo.userAddress.GetUserAddressVo;
import com.digital.result.Result;
import com.digital.service.UserAddressService;
import com.digital.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/31 20:18
 */
@RestController
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private UserService userService;

    /**
     * 添加人物地址信息
     * @param userAddressAddReq
     * @param request
     * @return
     */
    @PostMapping("/userAddress")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetUserAddressVo> addUserAddressByLoginUser(@RequestBody UserAddressAddReq userAddressAddReq,
                                                       HttpServletRequest request) {
        if (userAddressAddReq.getReceiverAddress() == null || userAddressAddReq.getReceiverCity() == null || userAddressAddReq.getReceiverDistrict() == null
                || userAddressAddReq.getReceiverName() == null || userAddressAddReq.getReceiverPhone() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressAddReq, userAddress);
        userAddress.setUserId(Math.toIntExact(loginUser.getId()));

        boolean save = userAddressService.save(userAddress);
        QueryWrapper<UserAddress> userAddressQueryWrapper = new QueryWrapper<>();
        userAddressQueryWrapper.eq("id", userAddress.getId());
        userAddressQueryWrapper.eq("user_id", loginUser.getId());
        userAddress = userAddressService.getOne(userAddressQueryWrapper);
        GetUserAddressVo getUserAddressVo = new GetUserAddressVo();
        BeanUtils.copyProperties(userAddress, getUserAddressVo);

        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(getUserAddressVo);
    }

    /**
     * 删除地址信息根据id
     * @param id
     * @return
     */
    @DeleteMapping("/userAddress/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> deleteUserAddressById(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        boolean removeById = userAddressService.removeById(id);
        if (!removeById) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改地址信息
     * @param id
     * @param userAddressUpdateReq
     * @param request
     * @return
     */
    @PutMapping("/userAddress/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetUserAddressVo> updateUserAddressById(@PathVariable Integer id,
                                    @RequestBody UserAddressUpdateReq userAddressUpdateReq,
                                    HttpServletRequest request) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }

        QueryWrapper<UserAddress> userAddressQueryWrapper = new QueryWrapper<>();
        userAddressQueryWrapper.eq("id", id);

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(Math.toIntExact(loginUser.getId()));
        BeanUtils.copyProperties(userAddressUpdateReq, userAddress);

        boolean update = userAddressService.update(userAddress, userAddressQueryWrapper);
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        userAddressQueryWrapper = new QueryWrapper<>();
        userAddressQueryWrapper.eq("id", id);
        userAddressQueryWrapper.eq("user_id", loginUser.getId());
        userAddress = userAddressService.getOne(userAddressQueryWrapper);
        GetUserAddressVo getUserAddressVo = new GetUserAddressVo();
        BeanUtils.copyProperties(userAddress, getUserAddressVo);

        return Result.success(getUserAddressVo);
    }

    /**
     * 得到用户地址信息根据id
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/userAddress/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetUserAddressVo> getUserAddressById(@PathVariable Integer id,
                                                       HttpServletRequest request) {
        if (id == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        QueryWrapper<UserAddress> userAddressQueryWrapper = new QueryWrapper<>();
        userAddressQueryWrapper.eq("id", id);
        userAddressQueryWrapper.eq("user_id", loginUser.getId());

        UserAddress one = userAddressService.getOne(userAddressQueryWrapper);
        GetUserAddressVo getUserAddressVo = new GetUserAddressVo();
        BeanUtils.copyProperties(one, getUserAddressVo);
        return Result.success(getUserAddressVo);
    }

    /**
     * 得到用户地址列表
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/userAddress")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Page<GetUserAddressVo>> getUserAddressList(Integer pageNum, Integer pageSize,
                                                                     HttpServletRequest request) {
        if (pageNum == null || pageSize == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }

        Page<UserAddress> userAddressPage = userAddressService.page(new Page<>(pageNum, pageSize), new QueryWrapper<UserAddress>().eq("user_id", loginUser.getId()));
        List<GetUserAddressVo> userAddressVoList = userAddressPage.getRecords().stream()
                .map(userAddress -> {
                    GetUserAddressVo getUserAddressVo = new GetUserAddressVo();
                    BeanUtils.copyProperties(userAddress, getUserAddressVo);
                    return getUserAddressVo;
                })
                .toList();
        Page<GetUserAddressVo> getUserAddressVoPage = new Page<>();
        getUserAddressVoPage.setRecords(userAddressVoList);
        return Result.success(getUserAddressVoPage);
    }
}
