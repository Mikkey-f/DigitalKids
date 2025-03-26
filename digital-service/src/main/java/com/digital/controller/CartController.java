package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.CartItem;
import com.digital.model.entity.User;
import com.digital.result.Result;
import com.digital.service.UserService;
import com.digital.service.impl.CartService;
import com.digital.utils.RedisKeyUtil;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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


    @PostMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addProductForCart(@PathVariable Integer productId,
                                    @RequestBody CartItem cartItem,
                                    HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }

        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.addCart(userCartKey, productId, cartItem.getQuantity(), cartItem.getIsSelected());
    }

    @PutMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateProductForCart(@PathVariable Integer productId,
                                       @RequestBody CartItem cartItem,
                                       HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.updateCart(userCartKey, productId, cartItem.getQuantity(), cartItem.getIsSelected());
    }

    @DeleteMapping("/{productId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result deleteProductForCart(@PathVariable Integer productId,
                                       HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.deleteCart(userCartKey, productId);
    }

//    @GetMapping("/{productId}")
//    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
//    public Result listProductForCart(@PathVariable Integer productId,
//                       HttpServletRequest request) {
//        User loginUser = userService.getLoginUser(request);
//        cartService
//    }

    @PutMapping("/selectedAll")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result selectedAllForCart(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.selectedAllForCart(userCartKey);
    }

    @PutMapping("/unselectedAll")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result unselectedAllForCart(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(loginUser.getId()));
        return cartService.unselectedAllForCart(userCartKey);
    }
}
