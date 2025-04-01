package com.digital.constant;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:38
 */
public class OrderStatusType {

    /**
     * 订单过期或用户取消
     */
    public static final int GAVE_UP_ORDER = 0;
    /**
     * 未支付（创建后默认）
     */
    public static final int NOT_PAY = 1;

    /**
     * 已经支付，未发货
     */
    public static final int PAYED_READY_TO_DELIVER = 2;

    /**
     * 已发货，等待接收
     */
    public static final int DELIVERED_READY_TO_GET_PRODUCT = 3;

    /**
     * 已接收，可以售后
     */
    public static final int ALREADY_DELIVERED_CAN_SHOU_HOU = 4;

    /**
     * 售后
     */
    public static final int SHOU_HOU = 5;


    /**
     * 没有支付的订单过期阈值
     */
    public static final long TIMEOUT_NOT_PAY = 7 * 24 * 60 * 60 * 1000;

    /**
     * 已经支付的订单阈值
     */
    public static final long TIMEOUT_IS_PAID = 20 * 24 * 60 * 60 * 1000;
}
