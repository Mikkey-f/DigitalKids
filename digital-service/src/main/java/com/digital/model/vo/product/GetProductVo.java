package com.digital.model.vo.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.digital.model.vo.comment.CommentVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:43
 */
@Data
public class GetProductVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private List<CommentVo> commentVos;

    private BigInteger LikeCount;

    private BigInteger LikeStatus;
}
