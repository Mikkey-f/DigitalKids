package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:54
 */
@Data
public class LegReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String lengthDifference;
    private String muscleStrength;
    private String kneeReflex;
    private String swellingDegree;
    private Long kidId;
}
