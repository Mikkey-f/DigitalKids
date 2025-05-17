import api from './axios';
import type { UserData } from '@/types';

/**
 * API响应结构类型
 */
interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 登录API
 * @param phone 手机号
 * @param password 密码
 * @param code 验证码
 * @returns 登录结果
 */
export const login = (phone: string, password: string, code: string): Promise<ApiResponse> => {
  const data = { phone, password, code };
  try {
    return api.post('/user/login', data, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': '' 
      }
    });
  } catch (error) {
    console.error('登录请求异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 获取当前登录用户信息
 * @returns 用户信息
 */
export const getCurrentUser = (): Promise<ApiResponse<UserData>> => {
  return api.get('/user/get/login');
};

/**
 * 退出登录
 * @returns 退出结果
 */
export const logout = (): Promise<ApiResponse> => {
  return Promise.resolve({ code: 1, msg: '退出成功', data: null });
};

/**
 * 更新用户信息
 * @param userData 用户数据
 * @returns 更新结果
 */
export const updateUserInfo = (userData: Partial<UserData>): Promise<ApiResponse> => {
  try {
    return api.post<ApiResponse>('/user/update/my', userData)
      .then(response => {
        return response.data;
      })
      .catch(error => {
        console.error('错误:', error.response || error.message || error);
        return Promise.reject(error);
      });
  } catch (error) {
    return Promise.reject(error);
  }
};

/**
 * 上传用户头像
 * @param file 头像文件
 * @returns 包含头像URL的响应
 */
export const uploadAvatar = (file: File): Promise<ApiResponse<string>> => {
  const formData = new FormData();
  formData.append('file', file);
  try {
    return api.post<ApiResponse<string>>('/user/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    .then(response => {
      return response.data;
    })
    .catch(error => {
      return Promise.reject(error);
    });
  } catch (error) {
    return Promise.reject(error);
  }
};

/**
 * 发送手机验证码
 * @param phone 手机号
 * @returns 发送结果
 */
export const sendVerificationCode = (phone: string): Promise<ApiResponse> => {
  const params = new URLSearchParams();
  params.append('phoneNum', phone);
  return api.post('/user/sendMsg', params, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': '' 
    }
  });
};

/**
 * 用户注册参数类型
 */
export interface RegisterParams {
  name: string;
  phone: string;
  password: string;
  code: string;
  gender: string;
}

/**
 * 用户注册
 * @param userData 用户注册数据
 * @returns 注册结果
 */
export const register = (userData: RegisterParams): Promise<ApiResponse> => {
  return api.post('/user/register', userData, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

// 地址信息类型
export interface AddressInfo {
  id?: number;
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverCity: string;
  receiverDistrict: string;
  receiverAddress: string;
}

// 更新地址请求类型
export interface AddressUpdateReq {
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverCity: string;
  receiverDistrict: string;
  receiverAddress: string;
}

// 分页地址列表响应类型
export interface PageAddressResponse {
  records: AddressInfo[];
  total: number;
  size: number;
  current: number;
}

/**
 * 获取用户地址列表
 * @param pageNum 页码
 * @param pageSize 每页条数
 * @returns 地址列表
 */
export const getUserAddressList = (pageNum = 1, pageSize = 10): Promise<ApiResponse<PageAddressResponse>> => {
  return api.get(`/userAddress?pageNum=${pageNum}&pageSize=${pageSize}`);
};

/**
 * 获取单个地址详情
 * @param id 地址ID
 * @returns 地址详情
 */
export const getUserAddress = (id: number): Promise<ApiResponse<AddressInfo>> => {
  return api.get(`/userAddress/${id}`);
};

/**
 * 添加用户地址
 * @param address 地址信息
 * @returns 添加结果
 */
export const addUserAddress = (address: AddressInfo): Promise<ApiResponse> => {
  return api.post('/userAddress', address);
};

/**
 * 更新用户地址
 * @param id 地址ID
 * @param address 地址信息
 * @returns 更新结果
 */
export const updateUserAddress = (id: number, address: AddressUpdateReq): Promise<ApiResponse> => {
  return api.put(`/userAddress/${id}`, address);
};

/**
 * 删除用户地址
 * @param id 地址ID
 * @returns 删除结果
 */
export const deleteUserAddress = (id: number): Promise<ApiResponse> => {
  return api.delete(`/userAddress/${id}`);
};

// 收藏数据类型
export interface FavoriteData {
  entityType: number; // 1:文章, 2:商品, 3:评论
  entityId: number;
  targetId?: number;
}

/**
 * 添加收藏
 * @param favoriteData 收藏数据
 * @returns 收藏结果
 */
export const addFavorite = (favoriteData: FavoriteData): Promise<ApiResponse> => {
  return api.post('/user/favorite/add', favoriteData);
};

/**
 * 取消收藏
 * @param id 收藏对象ID
 * @returns 取消收藏结果
 */
export const deleteFavorite = (id: number): Promise<ApiResponse> => {
  return api.delete(`/user/favorite/delete/${id}`);
};

/**
 * 获取用户收藏列表
 * @param page 页码
 * @param size 每页条数
 * @returns 收藏列表
 */
export const getUserFavorites = (page = 1, size = 10): Promise<ApiResponse<any>> => {
  return api.get(`/user/favorite/list?page=${page}&size=${size}`);
}; 