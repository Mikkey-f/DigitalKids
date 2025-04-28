package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:58
 */
@Data
public class OralReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String deciduousTeeth;
    private String permanentTeeth;
    private String decayedTeeth;
    private String gumCondition;
    private String occlusion;
}
