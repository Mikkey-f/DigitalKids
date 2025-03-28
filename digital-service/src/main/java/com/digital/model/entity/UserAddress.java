package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:52
 */
@Data
@TableName("user_address")
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    /**
     * 收件人名字
     */
    @TableField(value = "receiver_name")
    private String receiverName;
    /**
     * 收件人电话
     */
    @TableField(value = "receiver_phone")
    private String receiverPhone;

    /**
     * 收件人省份
     */
    @TableField(value = "receiver_province")
    private String receiverProvince;

    /**
     * 收件人市
     */
    @TableField(value = "receiver_city")
    private String receiverCity;
    /**
     * 收件人县/区
     */
    @TableField(value = "receiver_district")
    private String receiverDistrict;
    /**
     * 收件人具体地址
     */
    @TableField(value = "receiver_address")
    private String receiverAddress;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;
}
