package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName endocrine
 */
@TableName(value ="endocrine")
@Data
public class Endocrine implements Serializable {
    private Integer id;

    private Long kidId;

    private String thyroidFunction;

    private String growthHormone;

    private String insulin;

    private String metabolicRate;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}