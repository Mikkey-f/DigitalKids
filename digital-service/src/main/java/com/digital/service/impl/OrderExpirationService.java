package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.Order;
import com.digital.model.vo.order.OrderVo;
import com.digital.service.OrderService;
import com.digital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/1 19:33
 */
@Service
public class OrderExpirationService {
    @Autowired
    private RedisTemplate<String, OrderVo> redisOrderVoTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    public void checkExpiredOrders() {
        Set<String> orderKeys = redisOrderVoTemplate.keys("orderVo:*");
        for (String key : orderKeys) {
            if (!redisOrderVoTemplate.hasKey(key)) {
                // 键已过期，提取订单ID
                String orderNo = extractOrderIdFromKey(key);
                if (orderNo != null) {
                    String updateSql = "UPDATE order SET status = '0' WHERE order_no = ?";
                    jdbcTemplate.update(updateSql, orderNo);
                }
            }
        }
    }

    @Scheduled(fixedRate = 600000) // 每十分钟做一次redis与数据库同步
    public void redisAndMysqlSyn() {
        Set<String> orderKeys = redisOrderVoTemplate.keys("orderVo:*");
        for (String key : orderKeys) {
            String orderNo = extractOrderIdFromKey(key);
            OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(key);
            if (orderVo == null) {
                throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
            }
            QueryWrapper<Order> orderVoQueryWrapper = new QueryWrapper<>();
            orderVoQueryWrapper.eq("order_no", orderNo);
            Order order = new Order();
            order.setOrderNo(String.valueOf(orderVo.getOrderStatus()));
            orderService.update(order, orderVoQueryWrapper);
        }
    }

    private String extractOrderIdFromKey(String key) {
        // 这里根据你的键命名规则提取订单ID，格式orderVo:orderNo  orderVo
        if (key.startsWith("orderVo:")) {
            return key.substring(8);
        }
        return null;
    }
}
