package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.Order;
import com.digital.model.entity.redis.CartItem;
import com.digital.result.Result;

/**
* @author Lenovo
* @description 针对表【order】的数据库操作Service
* @createDate 2025-03-28 15:25:29
*/
public interface OrderService extends IService<Order> {

    Result addOrder(Long userId, Integer userAddressId);
}
