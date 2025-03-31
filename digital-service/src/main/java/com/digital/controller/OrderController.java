package com.digital.controller;

import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.User;
import com.digital.model.entity.redis.CartItem;
import com.digital.result.Result;
import com.digital.service.OrderService;
import com.digital.service.UserService;
import com.digital.utils.RedisKeyUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/add")
    public Result createOrder(HttpServletRequest request, Integer userAddressId) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        return orderService.addOrder(loginUser.getId(), userAddressId);
    }
}
