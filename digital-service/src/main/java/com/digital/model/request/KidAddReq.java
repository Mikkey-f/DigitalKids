package com.digital.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KidAddReq {
    private Long user_id;

    private String avatar;

    private String nickname;

    private String birthdate;

    private BigDecimal height;

    private BigDecimal weight;
}
