package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName right_arm
 */
@TableName(value ="right_arm")
@Data
public class RightArm {
    private Integer rightArmId;

    private Long kidId;

    private BigDecimal armLength;

}