package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName right_leg
 */
@TableName(value ="right_leg")
@Data
public class RightLeg {
    @TableId(value = "right_leg_id", type = IdType.AUTO)
    private Integer rightLegId;

    private Long kidId;
    @TableField("leg_length")
    private BigDecimal legLength;
}