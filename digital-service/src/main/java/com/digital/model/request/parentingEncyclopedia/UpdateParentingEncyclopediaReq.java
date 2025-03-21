package com.digital.model.request.parentingEncyclopedia;

import lombok.Data;

@Data
public class UpdateParentingEncyclopediaReq {
    private Long id;

    private Integer stage;

    private Long userId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}
