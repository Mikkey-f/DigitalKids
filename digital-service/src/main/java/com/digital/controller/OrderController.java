package com.digital.controller;

import com.digital.model.entity.redis.CartItem;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:32
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @RequestMapping("/add")
    public String createOrder(@RequestBody CartItem cartItem) {

    }
}
