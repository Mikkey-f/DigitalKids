package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 21:04
 */
@Data
public class RespiratoryReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String vitalCapacity;
    private String respiratoryFrequency;
    private String lungAdventitiousSound;
    private String airwayPatency;
}
