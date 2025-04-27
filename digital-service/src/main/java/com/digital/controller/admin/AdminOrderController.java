package com.digital.controller.admin;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.User;
import com.digital.model.vo.order.OrderVo;
import com.digital.result.Result;
import com.digital.service.OrderService;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 20:32
 */
@RestController
@Slf4j
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

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
}
