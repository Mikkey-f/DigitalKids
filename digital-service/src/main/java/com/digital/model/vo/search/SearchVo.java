package com.digital.model.vo.search;

import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.User;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.user.SearchUserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  21:59
 */
@Data
public class SearchVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private SearchUserVo user;
    private String title;
    private String content;
    private String mainImage;
    private Date createTime;
}
