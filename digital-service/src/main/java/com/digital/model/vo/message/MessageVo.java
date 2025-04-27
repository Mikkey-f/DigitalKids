package com.digital.model.vo.message;

import com.digital.model.vo.user.GetUserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 20:42
 */
@Data
public class MessageVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private GetUserVo getUserVo;
    private String content;
    private Date createTime;
}
