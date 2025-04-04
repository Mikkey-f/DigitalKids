package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:36
 */
@Data
@TableName("orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 随机数，一个订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 用户
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 用户地址表id
     */
    @TableField(value = "user_address_id")
    private Integer userAddressId;

    /**
     * 订单总价
     */
    private BigDecimal payment;

    /**
     * 支付类型
     */
    @TableField(value = "payment_type")
    private Integer paymentType;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 支付时间
     */
    @TableField(value = "payment_time")
    private Date paymentTime;

    /**
     * 寄出时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 关闭时间
     */
    @TableField(value = "close_time")
    private Date closeTime;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
