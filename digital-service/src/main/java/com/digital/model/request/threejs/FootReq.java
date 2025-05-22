package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:47
 */
@Data
public class FootReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String archStatus;
    private String halluxValgusDegree;
    private String callusStatus;
    private String gaitCycleStatus;
}
