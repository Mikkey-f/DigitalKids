package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:50
 */
@Data
public class ArmReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String gripStrength;
    private String elbowRangeOfMotion;
    private String tinelSign;
    private String circumferenceDifference;
}
