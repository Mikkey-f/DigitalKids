package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Order;
import com.digital.service.OrderService;
import com.digital.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【order】的数据库操作Service实现
* @createDate 2025-03-28 15:25:29
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




