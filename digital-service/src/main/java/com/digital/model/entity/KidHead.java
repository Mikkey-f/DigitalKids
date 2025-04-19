package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @TableName kid_head
 */
@TableName(value ="kid_head")
@Data
public class KidHead {

    private Integer headId;

    private Long kidId;

    private String hairColor;

    private String eyeColor;

    private BigDecimal leftEyeDegree;

    private BigDecimal rightEyeDegree;

}