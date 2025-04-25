package com.digitalkid.mcp.model.request;

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
