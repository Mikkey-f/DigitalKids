package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName right_leg
 */
@TableName(value ="right_leg")
@Data
public class RightLeg {
    private Integer rightLegId;

    private Long kidId;

    private BigDecimal legLength;
}