package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName ent
 */
@TableName(value ="ent")
@Data
public class Ent implements Serializable {
    private Integer id;

    private Long kidId;

    private String leftHearing;

    private String rightHearing;

    private String sinusCondition;

    private String tonsilCondition;

    private String throatCondition;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}