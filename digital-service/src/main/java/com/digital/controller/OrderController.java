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

    /**
     * 生成订单
     * @param request
     * @param userAddressId
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> createOrder(HttpServletRequest request, Integer userAddressId) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.addOrder(loginUser.getId(), userAddressId);
    }

    /**
     * 通过订单号查看订单
     * @param orderNo
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> getOrderByOrderNo(String orderNo, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.getOrderByOrderNo(orderNo, loginUser.getId());
    }

    /**
     * 根据订单号取消订单
     * @param request
     * @param orderNo
     * @return
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result canceledOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.canceledOrderByOrderNo(orderNo);
    }

    /**
     * 支付订单
     * @param request
     * @param orderNo
     * @return
     */
    @PutMapping("/pay")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> payOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.payOrderByOrderNo(orderNo);
    }

    /**
     * 订单发货状态，限管理员
     * @param request
     * @param orderNo
     * @return
     */
    @PutMapping("/deliver")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<OrderVo> deliverOrderByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.deliverOrderByOrderNo(orderNo);
    }

    /**
     * 订单货物接收
     * @param request
     * @param orderNo
     * @return
     */
    @PutMapping("/signForDelivery")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<OrderVo> signForDeliveryByOrderNo(HttpServletRequest request, String orderNo) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.signForDeliveryByOrderNo(orderNo);
    }

    /**
     * 订单售后
     * @param request
     * @param orderNo
     * @return
     */
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
