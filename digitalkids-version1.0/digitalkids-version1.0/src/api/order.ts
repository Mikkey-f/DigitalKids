import api from './axios';

/**
 * API响应结构类型
 */
interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 订单项目接口
 */
interface OrderItem {
  productName: string;
  productImage: string;
  Price: number;
  quantity: number;
  totalPrice: number;
}

/**
 * 用户地址接口
 */
interface UserAddressItem {
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverCity: string;
  receiverDistrict: string;
  receiverAddress: string;
}

/**
 * 订单接口
 */
interface OrderVo {
  orderItemList: OrderItem[];
  payment: number;
  userAddressItem: UserAddressItem;
  userId: number;
  orderNo: string;
  orderStatus: number;
  createTime: string;
  paymentTime: string;
  sendTime: string;
  endTime: string;
  closeTime: string;
}

/**
 * 生成订单
 * @param userAddressId 用户地址ID
 * @returns 订单信息
 */
export const createOrder = (userAddressId: number): Promise<ApiResponse<OrderVo>> => {
  return api.post('/order/add', null, {
    params: { userAddressId }
  });
};

/**
 * 通过订单号查看订单
 * @param orderNo 订单号
 * @returns 订单信息
 */
export const getOrderByNo = async (orderNo: string): Promise<ApiResponse<OrderVo>> => {
  if (!orderNo || orderNo.trim() === '') {
    return {
      code: 0,
      msg: '订单号不能为空',
      data: null as any
    };
  }
  const token = localStorage.getItem('token');
  const headers: Record<string, string> = {};
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  try {
    try {
      const testResponse = await fetch(`/api/order/get?orderNo=${orderNo}`, {
        headers: headers
      });
      const responseText = await testResponse.text();
      console.log('测试请求响应:', responseText);
    } catch (testError) {
      console.error('测试请求失败:', testError);
    }
    const response = await api.get('/order/get', {
      params: { orderNo },
      headers
    });
    
    return response;
  } catch (error) {
    return {
      code: 0,
      msg: '获取订单失败: ' + (error instanceof Error ? error.message : String(error)),
      data: null as any
    };
  } finally {
    console.log('End');
  }
};

/**
 * 获取当前用户的所有订单
 * @returns 当前用户的订单列表
 */
export const getUserOrders = (): Promise<ApiResponse<OrderVo[]>> => {
  return api.get('/order/list');
};

/**
 * 根据订单号取消订单
 * @param orderNo 订单号
 * @returns 取消结果
 */
export const cancelOrder = (orderNo: string): Promise<ApiResponse<any>> => {
  return api.delete('/order/delete', {
    params: { orderNo }
  });
};

/**
 * 支付订单
 * @param orderNo 订单号
 * @returns 支付结果
 */
export const payOrder = (orderNo: string): Promise<ApiResponse<any>> => {
  return api.put('/order/pay', null, {
    params: { orderNo }
  });
};

/**
 * 订单发货状态（管理员）
 * @param orderNo 订单号
 * @returns 发货结果
 */
export const deliverOrder = (orderNo: string): Promise<ApiResponse<any>> => {
  return api.put('/order/deliver', null, {
    params: { orderNo }
  });
};

/**
 * 确认收货
 * @param orderNo 订单号
 * @returns 确认结果
 */
export const confirmReceipt = (orderNo: string): Promise<ApiResponse<any>> => {
  return api.put('/order/signForDelivery', null, {
    params: { orderNo }
  });
};

/**
 * 申请售后
 * @param orderNo 订单号
 * @returns 申请结果
 */
export const applyAfterSale = (orderNo: string): Promise<ApiResponse<any>> => {
  return api.put('/order/afterSale', null, {
    params: { orderNo }
  });
}; 