package com.digital.utils;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 17:33
 */
public class RedisKeyUtil {

    private static final String SPILT = ":";
    // cart:userId(赋值):(productId:cartItem(cnt,isSelected))
    private static final String CART_PREFIX = "cart";
    // orderItem:orderNo:(productId:oderItem(productName,productImage,Price,quantity,totalPrice))
    private static final String ORDER_ITEM_PREFIX = "orderItem";
    // userAddress:orderNo:(userAddressId:userAddressItem(receiverName,receiverP
    // hone,receiverProvince,receiverCity,receiverDistrict,receiverAddress))
    private static final String USER_ADDRESS_PREFIX = "userAddress";
    // order:orderNo:orderVo
    private static final String ORDER_PREFIX = "orderVo";

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
}
