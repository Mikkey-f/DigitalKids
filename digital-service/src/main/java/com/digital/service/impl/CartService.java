package com.digital.service.impl;

import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.CartItem;
import com.digital.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 19:44
 */
@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, CartItem> hashOperations;

    public CartService() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public Result addCart(String userCartKey, Integer productId,  int quantity, boolean isSelected) {
        if (hashOperations.hasKey(userCartKey, productId)) {
            CartItem cartItem = hashOperations.get(userCartKey, productId);
            if (cartItem == null) {
                return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
            }
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(quantity, isSelected);
            hashOperations.put(userCartKey, String.valueOf(productId), cartItem);
        }

        return Result.success();
    }

    public Result updateCart(String userCartKey, Integer productId, int quantity, boolean isSelected) {

        if (hashOperations.hasKey(userCartKey, productId)) {
            CartItem cartItem = new CartItem(quantity, isSelected);
            hashOperations.put(userCartKey, String.valueOf(productId), cartItem);
        } else {
            return Result.error(ResultErrorEnum.CART_WITH_NO_PRODUCT.getMessage());
        }

        return Result.success();
    }

    public Result deleteCart(String userCartKey, Integer productId) {

        if (!hashOperations.hasKey(userCartKey, productId)) {
            return Result.error(ResultErrorEnum.CART_WITH_NO_PRODUCT.getMessage());
        }

        Long delete = hashOperations.delete(userCartKey, productId);
        if (delete <= 0) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }

    public Result selectedAllForCart(String userCartKey) {

    }

    public Result unselectedAllForCart(String userCartKey) {

    }

    public Result getProductForCart(String userCartKey) {

    }


}
