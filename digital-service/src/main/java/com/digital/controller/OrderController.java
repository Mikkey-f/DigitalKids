package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Order;
import com.digital.model.entity.User;
import com.digital.model.vo.order.OrderVo;
import com.digital.result.Result;
import com.digital.service.OrderService;
import com.digital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:32
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> createOrder(HttpServletRequest request, Integer userAddressId) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.addOrder(loginUser.getId(), userAddressId);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> getOrderByOrderNo(String orderNo,Integer userAddressId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.getOrderByOrderNo(orderNo, loginUser.getId(), userAddressId);
    }

    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result canceledOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.canceledOrderByOrderNo(orderNo);
    }

    @PutMapping("/pay")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> payOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }

    }

    @PutMapping("/deliver")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<OrderVo> deliverOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.deliverOrderByOrderNo(orderNo);
    }

    @PutMapping("/signForDelivery")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> signForDeliveryByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.signForDeliveryByOrderNo(orderNo);
    }

    @PutMapping("/afterSale")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> afterSaleOrderByOrderNO(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.afterSaleOrderByOrderNO(orderNo);
    }
}
