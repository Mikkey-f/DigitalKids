package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:53
 */
@Data
public class HeadReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String headacheFrequency;
    private String dizziness;
    private String cognitiveTestResult;
}
