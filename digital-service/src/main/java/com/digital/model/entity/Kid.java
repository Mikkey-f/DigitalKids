package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("Kid")
public class Kid {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String avatar;

    private String nickname;

    private String birthdate;

    private BigDecimal height;

    private BigDecimal weight;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
