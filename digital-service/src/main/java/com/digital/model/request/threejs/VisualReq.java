package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:57
 */
@Data
public class VisualReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String leftVision;
    private String rightVision;
    private String leftAstigmatism;
    private String rightAstigmatism;
    private String colorVision;
    private Long kidId;
}
