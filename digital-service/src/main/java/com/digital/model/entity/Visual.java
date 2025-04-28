package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName visual
 */
@TableName(value ="visual")
@Data
public class Visual implements Serializable {
    private Integer id;

    @TableField(value = "kid_id")
    private Long kidId;

    @TableField(value = "left_vision")
    private String leftVision;

    @TableField(value = "right_vision")
    private String rightVision;

    @TableField(value = "left_astigmatism")
    private String leftAstigmatism;

    @TableField(value = "right_astigmatism")
    private String rightAstigmatism;

    @TableField(value = "color_vision")
    private String colorVision;

    private String recommendation;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}