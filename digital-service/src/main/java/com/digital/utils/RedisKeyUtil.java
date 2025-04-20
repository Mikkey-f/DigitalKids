package com.digital.utils;

import com.google.common.base.Splitter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 17:33
 */
public class RedisKeyUtil {

    public static final String SPILT = ":";
    // cart:userId(赋值):(productId:cartItem(cnt,isSelected))
    public static final String CART_PREFIX = "cart";
    // orderItem:orderNo:(productId:oderItem(productName,productImage,Price,quantity,totalPrice))
    public static final String ORDER_ITEM_PREFIX = "orderItem";
    // userAddress:orderNo:(userAdd   adrressId:userAddressItem(receiverName,receiverP
    // hone,receiverProvince,receiverCity,receiverDistrict,receiverAddress))
    public static final String USER_ADDRESS_PREFIX = "userAddress";
    // k - order:orderNo v- orderVo
    public static final String ORDER_PREFIX = "orderVo";
    // orderCreateTime:orderNo - Date()
    public static final String ORDER_CREATE_TIME_PREFIX = "orderCreateTime";
    // likeEntity:entityType:entityId -> userId
    public static final String LIKE_ENTITY_PREFIX = "likeEntity";
    // likeUser:userId -> int
    public static final String LIKE_USER_PREFIX = "likeUser";

    public static String getLikeUserKey(String userId) {
        return LIKE_USER_PREFIX + SPILT + userId;
    }

    public static String getLikeEntityKey(String entityType, String entityId) {
        return LIKE_ENTITY_PREFIX + SPILT + entityType + SPILT + entityId;
    }

    public static String getUserCartKey(String userId) {
        return CART_PREFIX + SPILT + userId;
    }

    public static String getOrderItemKey(String orderNo) {
        return ORDER_ITEM_PREFIX + SPILT + orderNo;
    }

    public static String getUserAddressKey(String orderNo) {
        return USER_ADDRESS_PREFIX + SPILT + orderNo;
    }

    public static String getOrderKey(String orderNo) {
        return ORDER_PREFIX + SPILT + orderNo;
    }

    public static String getOrderCreateTimeKey(String orderNo) {
        return ORDER_CREATE_TIME_PREFIX + SPILT + orderNo;
    }
}
