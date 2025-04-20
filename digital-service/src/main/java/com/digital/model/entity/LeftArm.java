package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName left_arm
 */
@TableName(value ="left_arm")
@Data
public class LeftArm {
    @TableId(value = "left_arm_id", type = IdType.AUTO)
    private Integer leftArmId;
    @TableField("kid_id")
    private Long kidId;
    @TableField("arm_length")
    private BigDecimal armLength;
}