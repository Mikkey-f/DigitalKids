package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName kid_head
 */
@TableName(value ="kid_head")
@Data
public class KidHead {

    @TableId(value = "head_id", type = IdType.AUTO)
    private Integer headId;
    @TableField("kid_id")
    private Long kidId;
    @TableField("hair_color")
    private String hairColor;
    @TableField("eye_color")
    private String eyeColor;
    @TableField("left_eye_degree")
    private BigDecimal leftEyeDegree;
    @TableField("right_eye_degree")
    private BigDecimal rightEyeDegree;

}