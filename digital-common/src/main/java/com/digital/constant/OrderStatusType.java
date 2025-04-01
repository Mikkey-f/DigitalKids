package com.digital.constant;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:38
 */
public class OrderStatusType {

    /**
     * 放弃订单
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
     * 订单彻底结束
     */
    public static final int ORDER_OVER = 5;

    /**
     * 订单过期或用户取消
     */
    public static final int ORDER_CANCELED = 6;

    /**
     * 订单过期阈值
     */
    public static final long TIMEOUT = 7 * 24 * 60 * 60 * 1000;
}
