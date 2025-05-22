package com.digital.model.request.parentingEncyclopedia;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddParentingEncyclopediaReq implements Serializable {

    private Integer stage;

    private Long userId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}
