package com.digital.utils;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 17:33
 */
public class RedisKeyUtil {

    private static final String SPILT = ":";
    // cart:userId(赋值):cartItem(cnt,isSelected)
    private static final String PREFIX = "cart";

    public static String getUserCartKey(String productId) {
        return PREFIX + SPILT + productId;
    }
}
