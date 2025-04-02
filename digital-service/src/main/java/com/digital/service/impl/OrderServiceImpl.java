package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.constant.OrderPayMentType;
import com.digital.constant.OrderStatusType;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.Order;
import com.digital.model.entity.Product;
import com.digital.model.entity.User;
import com.digital.model.entity.UserAddress;
import com.digital.model.entity.redis.CartItem;
import com.digital.model.entity.redis.OrderItem;
import com.digital.model.entity.redis.UserAddressItem;
import com.digital.model.vo.order.OrderVo;
import com.digital.model.vo.userAddress.GetUserAddressVo;
import com.digital.result.Result;
import com.digital.service.OrderService;
import com.digital.mapper.OrderMapper;
import com.digital.service.ProductService;
import com.digital.service.UserAddressService;
import com.digital.service.UserService;
import com.digital.utils.RedisKeyUtil;
import lombok.val;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【order】的数据库操作Service实现
* @createDate 2025-03-28 15:25:29
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, Date> redisTemplateForOrderCreateTime;

    @Autowired
    private RedisTemplate<String, OrderVo> redisOrderVoTemplate;

    @Autowired
    private HashOperations<String, String, CartItem> hashOperationsForCartItem;

    @Autowired
    private HashOperations<String, String, OrderItem> hashOperationsForOrderItem;

    @Autowired
    private HashOperations<String, String, UserAddressItem> hashOperationsForUserAddress;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result<OrderVo> addOrder(Long userId, Integer userAddressId) {
        String userCartKey = RedisKeyUtil.getUserCartKey(String.valueOf(userId));
        Map<String, CartItem> entries = hashOperationsForCartItem.entries(userCartKey);
        if (entries.isEmpty()) {
            return Result.error(ResultErrorEnum.CART_IS_NULL.getMessage());
        }

        Order order = new Order();
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        order.setOrderNo(orderNo);
        Map<String, CartItem> cartItemMap = entries.entrySet().stream()
                .filter(entry -> entry.getValue().getIsSelected())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String orderItemKey = RedisKeyUtil.getOrderItemKey(orderNo);
        final BigDecimal[] bigDecimal = {new BigDecimal("0")};

        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                for (Map.Entry<String, CartItem> cartItemEntry : cartItemMap.entrySet()) {
                    String productId = cartItemEntry.getKey();
                    Product product = productService.getById(productId);
                    CartItem cartItem = cartItemEntry.getValue();
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setProductName(product.getName());
                    orderItem.setPrice(product.getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    bigDecimal[0] = bigDecimal[0].add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    hashOperationsForOrderItem.put(orderItemKey, productId, orderItem);
                    hashOperationsForCartItem.delete(userCartKey, productId);
                }
                UserAddress userAddress = userAddressService.getById(userAddressId);
                order.setUserAddressId(userAddressId);
                String userAddressKey = RedisKeyUtil.getUserAddressKey(orderNo);
                UserAddressItem userAddressItem = new UserAddressItem();
                BeanUtils.copyProperties(userAddress, userAddressItem);
                userAddressItem.setId(String.valueOf(userAddress.getId()));
                hashOperationsForUserAddress.put(userAddressKey, String.valueOf(userAddressId), userAddressItem);

                order.setUserId(Math.toIntExact(userId));
                order.setPaymentType(OrderPayMentType.NOT_SELECTED_PAYMENT);
                order.setStatus(OrderStatusType.NOT_PAY);
                order.setPayment(bigDecimal[0]);
                order.setCreateTime(new Date());

                orderMapper.insert(order);
                String orderCreateTimeKey = RedisKeyUtil.getOrderCreateTimeKey(orderNo);
                redisTemplateForOrderCreateTime.opsForValue().set(orderCreateTimeKey, new Date());
                return operations.exec();
            }
        });
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        redisOrderVoTemplate.opsForValue().set(orderKey, getOrderVo(order, userId));
        if (!redisResults.contains(null)) {
            return Result.success(getOrderVo(order, userId));
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }

    @Override
    public Result<OrderVo> getOrderByOrderNo(String orderNo, Long userId) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        if (orderVo != null) {
            return Result.success(orderVo);
        } else {
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("order_no", orderNo);
            Order order = orderMapper.selectOne(orderQueryWrapper);
            if (order == null) {
                return Result.error(ResultErrorEnum.NOT_HAVE_THIS_ORDER.getMessage());
            }
            OrderVo currentOrderVo = new OrderVo();
            currentOrderVo.setOrderNo(order.getOrderNo());
            currentOrderVo.setPayment(order.getPayment());
            currentOrderVo.setOrderItemList(getOrderItemList(orderNo));
            currentOrderVo.setStatus(order.getStatus());
            currentOrderVo.setUserAddressItem(getUserAddressItem(orderNo, String.valueOf(order.getUserAddressId())));
            currentOrderVo.setUserId(Math.toIntExact(userId));
            currentOrderVo.setCreateTime(order.getCreateTime());
            currentOrderVo.setPaymentTime(order.getPaymentTime());
            currentOrderVo.setSendTime(order.getSendTime());
            currentOrderVo.setEndTime(order.getEndTime());
            currentOrderVo.setCloseTime(order.getCloseTime());
            return Result.success(currentOrderVo);
        }
    }

    @Override
    public Result canceledOrderByOrderNo(String orderNo) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        String orderCreateTimeKey = RedisKeyUtil.getOrderCreateTimeKey(orderNo);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                if (!orderVo.getStatus().equals(OrderStatusType.NOT_PAY)) {
                    throw new BusinessException(ResultErrorEnum.ORDER_TYPE_ERROR);
                }
                redisTemplateForOrderCreateTime.delete(orderCreateTimeKey);
                orderVo.setCloseTime(new Date());
                orderVo.setStatus(OrderStatusType.GAVE_UP_ORDER);
                redisOrderVoTemplate.opsForValue().set(orderKey, orderVo);
                return operations.exec();
            }
        });
        if (!redisResults.contains(null)) {
            return Result.success(orderVo);
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }

    /**
     * 发货
     * @param orderNo
     * @return
     */
    @Override
    public Result<OrderVo> deliverOrderByOrderNo(String orderNo) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                if (!orderVo.getStatus().equals(OrderStatusType.PAYED_READY_TO_DELIVER)) {
                    throw new BusinessException(ResultErrorEnum.ORDER_TYPE_ERROR);
                }
                orderVo.setStatus(OrderStatusType.DELIVERED_READY_TO_GET_PRODUCT);
                orderVo.setSendTime(new Date());
                redisOrderVoTemplate.opsForValue().set(orderKey, orderVo);
                return operations.exec();
            }
        });

        if (!redisResults.contains(null)) {
            return Result.success(orderVo);
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }

    /**
     * 用户售后
     * @param orderNo
     * @return
     */
    @Override
    public Result<OrderVo> afterSaleOrderByOrderNO(String orderNo) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                if (!orderVo.getStatus().equals(OrderStatusType.ALREADY_DELIVERED_CAN_SHOU_HOU)) {
                    throw new BusinessException(ResultErrorEnum.ORDER_TYPE_ERROR);
                }
                orderVo.setStatus(OrderStatusType.SHOU_HOU);
                redisOrderVoTemplate.opsForValue().set(orderKey, orderVo);
                return operations.exec();
            }
        });
        if (!redisResults.contains(null)) {
            return Result.success(orderVo);
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }

    /**
     * 用户接收
     * @param orderNo
     * @return
     */
    @Override
    public Result<OrderVo> signForDeliveryByOrderNo(String orderNo) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                if (!orderVo.getStatus().equals(OrderStatusType.DELIVERED_READY_TO_GET_PRODUCT)) {
                    throw new BusinessException(ResultErrorEnum.ORDER_TYPE_ERROR);
                }
                orderVo.setStatus(OrderStatusType.ALREADY_DELIVERED_CAN_SHOU_HOU);
                orderVo.setEndTime(new Date());
                redisOrderVoTemplate.opsForValue().set(orderKey, orderVo);
                return operations.exec();
            }
        });
        if (!redisResults.contains(null)) {
            return Result.success(orderVo);
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }

    /**
     * 支付order
     * @param orderNo
     * @return
     */
    @Override
    public Result<OrderVo> payOrderByOrderNo(String orderNo) {
        String orderKey = RedisKeyUtil.getOrderKey(orderNo);
        String orderCreateTimeKey = RedisKeyUtil.getOrderCreateTimeKey(orderNo);
        redisTemplateForOrderCreateTime.delete(orderCreateTimeKey);
        OrderVo orderVo = redisOrderVoTemplate.opsForValue().get(orderKey);
        List<Object> redisResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                if (!orderVo.getStatus().equals(OrderStatusType.NOT_PAY)) {
                    throw new BusinessException(ResultErrorEnum.ORDER_TYPE_ERROR);
                }
                //需要调用支付系统
                orderVo.setStatus(OrderStatusType.PAYED_READY_TO_DELIVER);
                orderVo.setPaymentTime(new Date());
                redisOrderVoTemplate.opsForValue().set(orderKey, orderVo);
                redisTemplateForOrderCreateTime.delete(orderCreateTimeKey);
                return operations.exec();
            }
        });

        if (!redisResults.contains(null)) {
            return Result.success(orderVo);
        } else {
            return Result.error(ResultErrorEnum.REDIS_TRANSACTION_ERROR.getMessage());
        }
    }


    private OrderVo getOrderVo(Order order, Long userId) {
        String orderItemKey = RedisKeyUtil.getOrderItemKey(order.getOrderNo());
        String userAddressKey = RedisKeyUtil.getUserAddressKey(order.getOrderNo());
        OrderVo orderVo = new OrderVo();
        Map<String, OrderItem> entries = hashOperationsForOrderItem.entries(orderItemKey);
        List<OrderItem> list = entries.values().stream().toList();
        orderVo.setOrderItemList(list);
        UserAddressItem userAddressItem = hashOperationsForUserAddress.get(userAddressKey, String.valueOf(order.getUserAddressId()));
        if (userAddressItem == null) {
            throw new BusinessException(ResultErrorEnum.USER_ADDRESS_IS_NULL);
        }
        orderVo.setUserAddressItem(userAddressItem);
        orderVo.setUserId(Math.toIntExact(userId));
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }

    private List<OrderItem> getOrderItemList(String orderNo) {
        String orderItemKey = RedisKeyUtil.getOrderItemKey(orderNo);
        return hashOperationsForOrderItem.entries(orderItemKey).values().stream().toList();
    }

    private UserAddressItem getUserAddressItem(String orderNo, String userAddressId) {
        String userAddressKey = RedisKeyUtil.getUserAddressKey(orderNo);
        return hashOperationsForUserAddress.get(userAddressKey, userAddressId);
    }
}




