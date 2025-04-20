package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName right_arm
 */
@TableName(value ="right_arm")
@Data
public class RightArm {
    @TableId(value = "right_arm_id", type = IdType.AUTO)
    private Integer rightArmId;

    private Long kidId;
    @TableField("arm_length")
    private BigDecimal armLength;

}