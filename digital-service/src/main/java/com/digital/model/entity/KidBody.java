package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName kid_body
 */
@TableName(value ="kid_body")
@Data
public class KidBody {
    @TableId(value = "body_id", type = IdType.AUTO)
    private Integer bodyId;
    @TableField("kid_id")
    private Long kidId;

    private BigDecimal height;

    private BigDecimal weight;
    @TableField("heartbeat_rate")
    private Integer heartbeatRate;

    private BigDecimal bmi;

}