package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName left_arm
 */
@TableName(value ="left_arm")
@Data
public class LeftArm {
    private Integer leftArmId;

    private Long kidId;

    private BigDecimal armLength;
}