package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName kid_body
 */
@TableName(value ="kid_body")
@Data
public class KidBody {
    private Integer bodyId;

    private Long kidId;

    private BigDecimal height;

    private BigDecimal weight;

    private Integer heartbeatRate;

    private BigDecimal bmi;

}