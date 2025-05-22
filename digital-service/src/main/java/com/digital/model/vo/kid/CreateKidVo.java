package com.digital.model.vo.kid;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
*@Author: Mikkeyf
*@CreateTime: 2025-03-16  14:35
*/
@Data
public class CreateKidVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String avatar;

    private String nickname;

    private String birthdate;

    private Integer old;

    private Integer gender;

    private Date createTime;

    private Date updateTime;
}
