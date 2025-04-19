package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName left_leg
 */
@TableName(value ="left_leg")
@Data
public class LeftLeg {
    private Integer leftLegId;

    private Long kidId;

    private BigDecimal legLength;

}