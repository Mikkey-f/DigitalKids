package com.digital.model.request.kid;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KidAddReq implements Serializable {

    private String avatar;

    private String nickname;

    private Integer old;

    private static final long serialVersionUID = 1L;
}
