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
 * 购物车项目接口
 */
interface CartItem {
  productId?: number;
  quantity: number;
  isSelected: boolean;
  name?: string;
  spec?: string;
  price?: number;
  store?: string;
  imageUrl?: string;
}

/**
 * 获取购物车列表
 * @returns 购物车列表数据
 */
export const getCartList = (): Promise<ApiResponse<CartItem[]>> => {
  return api.get('/cart/list');
};

/**
 * 向购物车添加商品
 * @param productId 商品ID
 * @param cartData 购物车数据
 * @returns 更新后的购物车列表
 */
export const addToCart = (productId: number, cartData: CartItem): Promise<ApiResponse<CartItem[]>> => {
  return api.post('/cart', {
    productId: productId,
    quantity: cartData.quantity,
    isSelected: cartData.isSelected
  });
};

/**
 * 更新购物车商品
 * @param productId 商品ID
 * @param cartData 购物车数据
 * @returns 更新后的购物车列表
 */
export const updateCartItem = (productId: number, cartData: CartItem): Promise<ApiResponse<CartItem[]>> => {
  return api.put('/cart', {
    productId: productId,
    quantity: cartData.quantity,
    isSelected: cartData.isSelected
  });
};

/**
 * 从购物车删除商品
 * @param productId 商品ID
 * @returns 更新后的购物车列表
 */
export const removeFromCart = (productId: number): Promise<ApiResponse<CartItem[]>> => {
  return api.delete(`/cart/${productId}`);
};

/**
 * 购物车商品全选
 * @returns 更新后的购物车列表
 */
export const selectAllCartItems = (): Promise<ApiResponse<CartItem[]>> => {
  return api.put('/cart/selectedAll');
};

/**
 * 购物车商品全不选
 * @returns 更新后的购物车列表
 */
export const unselectAllCartItems = (): Promise<ApiResponse<CartItem[]>> => {
  return api.put('/cart/unselectedAll');
}; 