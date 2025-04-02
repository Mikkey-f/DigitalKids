package com.digital.model.request.userAddress;

import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/31 20:27
 */
@Data
public class UserAddressUpdateReq {
    private String receiverName;

    private String receiverPhone;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;
}
