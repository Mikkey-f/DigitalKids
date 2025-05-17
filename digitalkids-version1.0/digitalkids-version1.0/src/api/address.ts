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
 * 用户地址接口
 */
interface UserAddress {
  id?: number;
  userId?: number;
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverCity: string;
  receiverDistrict: string;
  receiverAddress: string;
  isDefault?: boolean;
}

/**
 * 添加用户地址
 * @param addressData 地址数据
 * @returns 添加结果
 */
export const addUserAddress = (addressData: UserAddress): Promise<ApiResponse<any>> => {
  return api.post('/userAddress', addressData);
};

/**
 * 获取用户地址列表
 * @returns 地址列表
 */
export const getUserAddresses = (): Promise<ApiResponse<UserAddress[]>> => {
  return api.get('/userAddress');
};

/**
 * 获取用户默认地址
 * @returns 默认地址信息
 */
export const getDefaultUserAddress = (): Promise<ApiResponse<UserAddress>> => {
  return api.get('/userAddress/default');
};

/**
 * 获取指定地址信息
 * @param id 地址ID
 * @returns 地址信息
 */
export const getUserAddressById = (id: number): Promise<ApiResponse<UserAddress>> => {
  return api.get(`/userAddress/${id}`);
};

/**
 * 更新用户地址
 * @param id 地址ID
 * @param addressData 地址数据
 * @returns 更新结果
 */
export const updateUserAddress = (id: number, addressData: UserAddress): Promise<ApiResponse<any>> => {
  return api.put(`/userAddress/${id}`, addressData);
};

/**
 * 删除用户地址
 * @param id 地址ID
 * @returns 删除结果
 */
export const deleteUserAddress = (id: number): Promise<ApiResponse<any>> => {
  return api.delete(`/userAddress/${id}`);
}; 