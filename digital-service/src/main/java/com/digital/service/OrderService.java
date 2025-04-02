package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.Order;
import com.digital.model.entity.redis.CartItem;
import com.digital.model.vo.order.OrderVo;
import com.digital.result.Result;

/**
* @author Lenovo
* @description 针对表【order】的数据库操作Service
* @createDate 2025-03-28 15:25:29
*/
public interface OrderService extends IService<Order> {

    Result<OrderVo> addOrder(Long userId, Integer userAddressId);

    Result<OrderVo> getOrderByOrderNo(String orderNo, Long userId, Integer userAddressId);

    Result canceledOrderByOrderNo(String orderNo);

    Result<OrderVo> deliverOrderByOrderNo(String orderNo);

    Result<OrderVo> afterSaleOrderByOrderNO(String orderNo);

    Result<OrderVo> signForDeliveryByOrderNo(String orderNo);

    Result<OrderVo> payOrderByOrderNo(String orderNo);
}
