package com.digital.model.request.favorite;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  17:35
 */
@Data
public class FavorAddReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String targetType;

    private Integer targetId;

}
