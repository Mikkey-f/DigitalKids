package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName oral
 */
@TableName(value ="oral")
@Data
public class Oral implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long kidId;

    private String deciduousTeeth;

    private String permanentTeeth;

    private String decayedTeeth;

    private String gumCondition;

    private String occlusion;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}