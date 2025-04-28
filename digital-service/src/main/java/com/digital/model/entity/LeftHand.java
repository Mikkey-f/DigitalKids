package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName left_hand
 */
@TableName(value ="left_hand")
@Data
public class LeftHand implements Serializable {
    private Integer id;

    private Long kidId;

    private String flexibility;

    private String jointSwelling;

    private String twoPointDiscrimination;

    private String nailBedCirculation;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}