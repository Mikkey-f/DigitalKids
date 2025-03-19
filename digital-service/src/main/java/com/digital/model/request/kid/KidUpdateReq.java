package com.digital.model.request.kid;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KidUpdateReq {
    private Long id;

    private String avatar;

    private String nickname;

    private String birthdate;

    private BigDecimal height;

    private BigDecimal weight;
    private static final long serialVersionUID = 1L;
}
