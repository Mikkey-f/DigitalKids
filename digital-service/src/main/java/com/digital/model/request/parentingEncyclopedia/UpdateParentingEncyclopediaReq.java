package com.digital.model.request.parentingEncyclopedia;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateParentingEncyclopediaReq implements Serializable {
    private Long id;

    private Integer stage;

    private Long userId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}
