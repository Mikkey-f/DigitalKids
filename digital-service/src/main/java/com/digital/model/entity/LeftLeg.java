package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName left_leg
 */
@TableName(value ="left_leg")
@Data
public class LeftLeg {
    @TableId(value = "left_leg_id", type = IdType.AUTO)
    private Integer leftLegId;
    @TableField("kid_id")
    private Long kidId;
    @TableField("leg_length")
    private BigDecimal legLength;

}