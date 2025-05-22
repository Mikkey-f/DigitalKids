package com.digital.model.request.kid;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KidUpdateReq implements Serializable {
    private Integer id;

    private String avatar;

    private String nickname;

    private Integer old;

    private Integer gender;

    private static final long serialVersionUID = 1L;
}
