package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName right_arm
 */
@TableName(value ="right_arm")
@Data
public class RightArm implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
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