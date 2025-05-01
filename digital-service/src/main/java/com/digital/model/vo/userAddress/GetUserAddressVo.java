package com.digital.model.vo.userAddress;

import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/31 20:33
 */
@Data
public class GetUserAddressVo {
    private Integer id;
    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
}
