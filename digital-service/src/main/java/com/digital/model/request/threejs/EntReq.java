package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 21:02
 */
@Data
public class EntReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String leftHearing;
    private String rightHearing;
    private String sinusCondition;
    private String throatCondition;
    private Long kidId;
}
