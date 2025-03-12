package com.digital.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @TableName users
 */
@Data
@TableName("users")
public class Users {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String password;

    private String avatar;

    private Object gender;

    private String phone;

    private String location;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}