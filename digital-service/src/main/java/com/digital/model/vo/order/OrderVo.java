package com.digital.model.vo.order;

import com.digital.model.entity.redis.OrderItem;
import com.digital.model.entity.redis.UserAddressItem;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.userAddress.GetUserAddressVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/31 21:37
 */
@Data
public class OrderVo {
    private List<OrderItem> orderItemList;
    private BigDecimal payment;
    private UserAddressItem userAddressItem;
    private Integer userId;
    private String orderNo;
    private Integer status;
    private Date createTime;
    private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
}
