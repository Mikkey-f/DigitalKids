package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.redis.CartItem;
import com.digital.model.entity.User;
import com.digital.result.Result;
import com.digital.service.UserService;
import com.digital.service.impl.CartService;
import com.digital.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 19:29
 */
@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    /**
     * 给购物车加入商品
     * @param productId
     * @param cartItem
     * @param request
     * @return
     */
    @PostMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<CartItem>> addProductForCart(@PathVariable Integer productId,
                                    @RequestBody CartItem cartItem,
                                    HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }

        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.addCart(userCartKey, productId, cartItem.getQuantity(), cartItem.getIsSelected());
    }

    /**
     * 更新购物车商品
     * @param productId
     * @param cartItem
     * @param request
     * @return
     */
    @PutMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<CartItem>> updateProductForCart(@PathVariable Integer productId,
                                       @RequestBody CartItem cartItem,
                                       HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.updateCart(userCartKey, productId, cartItem.getQuantity(), cartItem.getIsSelected());
    }

    /**
     * 删除购物车商品
     * @param productId
     * @param request
     * @return
     */
    @DeleteMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<CartItem>> deleteProductForCart(@PathVariable Integer productId,
                                                       HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.deleteCart(userCartKey, productId);
    }

    /**
     * 购物车商品全选
     * @param request
     * @return
     */
    @PutMapping("/selectedAll")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<CartItem>> selectedAllForCart(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.selectedAllForCart(userCartKey);
    }

    /**
     * 购物车商品全不选
     * @param request
     * @return
     */
    @PutMapping("/unselectedAll")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<CartItem>> unselectedAllForCart(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.unselectedAllForCart(userCartKey);
    }
}
