package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:49
 */
@Data
public class BodyReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String scoliosisStatus;
    private String coreStrength;
    private String bodyFatPercentage;
    private String flexibility;
}
