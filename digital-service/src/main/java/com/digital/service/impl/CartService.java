package com.digital.service.impl;

import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.redis.CartItem;
import com.digital.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 19:44
 */
@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HashOperations<String, String, CartItem> hashOperations;


    public Result<List<CartItem>> addCart(String userCartKey, Integer productId, int quantity, boolean isSelected) {
        if (hashOperations.hasKey(userCartKey, String.valueOf(productId))) {
            CartItem cartItem = hashOperations.get(userCartKey, String.valueOf(productId));
            if (cartItem == null) {
                return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
            }
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(quantity, isSelected);
            hashOperations.put(userCartKey, String.valueOf(productId), cartItem);
        }
        return Result.success(getListOfCartItems(userCartKey));
    }

    public Result<List<CartItem>> updateCart(String userCartKey, Integer productId, int quantity, boolean isSelected) {

        if (hashOperations.hasKey(userCartKey, String.valueOf(productId))) {
            CartItem cartItem = new CartItem(quantity, isSelected);
            hashOperations.put(userCartKey, String.valueOf(productId), cartItem);
        } else {
            return Result.error(ResultErrorEnum.CART_WITH_NO_PRODUCT.getMessage());
        }

        return Result.success(getListOfCartItems(userCartKey));
    }

    public Result<List<CartItem>> deleteCart(String userCartKey, Integer productId) {

        if (!hashOperations.hasKey(userCartKey, String.valueOf(productId))) {
            return Result.error(ResultErrorEnum.CART_WITH_NO_PRODUCT.getMessage());
        }

        Long delete = hashOperations.delete(userCartKey, String.valueOf(productId));
        if (delete <= 0) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(getListOfCartItems(userCartKey));
    }

    public Result<List<CartItem>> selectedAllForCart(String userCartKey) {
        List<CartItem> listOfCartItems = getListOfCartItems(userCartKey);
        listOfCartItems.forEach(cartItem -> {
            cartItem.setIsSelected(true);
        });
        return Result.success(listOfCartItems);
    }

    public Result<List<CartItem>> unselectedAllForCart(String userCartKey) {
        List<CartItem> listOfCartItems = getListOfCartItems(userCartKey);
        listOfCartItems.forEach(cartItem -> {
            cartItem.setIsSelected(false);
        });
        return Result.success(listOfCartItems);
    }


    private List<CartItem> getListOfCartItems(String userCartKey) {
        return hashOperations.values(userCartKey);
    }

}
