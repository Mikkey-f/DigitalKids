package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 11:52
 */
@Data
@TableName("user_address")
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress{
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    /**
     * 收件人名字
     */
    @TableField("receiver_name")
    private String receiverName;
    /**
     * 收件人电话
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 收件人省份
     */
    @TableField("receiver_province")
    private String receiverProvince;

    /**
     * 收件人市
     */
    @TableField("receiver_city")
    private String receiverCity;
    /**
     * 收件人县/区
     */
    @TableField("receiver_district")
    private String receiverDistrict;
    /**
     * 收件人具体地址
     */
    @TableField("receiver_address")
    private String receiverAddress;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
