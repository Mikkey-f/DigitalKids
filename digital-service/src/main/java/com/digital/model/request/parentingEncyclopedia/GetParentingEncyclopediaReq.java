package com.digital.model.request.parentingEncyclopedia;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetParentingEncyclopediaReq implements Serializable {
    private Integer stage;
    private static final long serialVersionUID = 1L;
}
