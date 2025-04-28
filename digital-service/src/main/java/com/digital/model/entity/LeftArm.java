package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName left_arm
 */
@TableName(value ="left_arm")
@Data
public class LeftArm implements Serializable {
    private Integer id;

    private Long kidId;

    private String gripStrength;

    private String elbowRangeOfMotion;

    private String tinelSign;

    private String circumferenceDifference;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}