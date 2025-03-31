package com.digital.model.vo.order;

import com.digital.model.entity.redis.OrderItem;
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
    private GetUserAddressVo getUserAddressVo;
    private String orderNo;
    private Integer orderStatus;
    private Date createTime;
    private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
}
