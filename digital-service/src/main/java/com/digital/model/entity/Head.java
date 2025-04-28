package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName head
 */
@TableName(value ="head")
@Data
public class Head implements Serializable {
    private Integer id;

    private Long kidId;

    private String headacheFrequency;

    private String dizziness;

    private String traumaHistory;

    private String cognitiveTestResult;

    private String recommendation;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}