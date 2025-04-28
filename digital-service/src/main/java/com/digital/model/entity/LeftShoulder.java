package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName left_shoulder
 */
@TableName(value ="left_shoulder")
@Data
public class LeftShoulder implements Serializable {
    private Integer id;

    private Long kidId;

    private String rangeOfMotion;

    private String painIndex;

    private String stability;

    private String muscleStrength;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}