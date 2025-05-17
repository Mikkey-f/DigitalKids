/**
 * 用户数据类型
 */
export interface UserData {
  id: number;
  name: string;
  avatar: string;
  gender: any;
  phone: string;
  location: string;
  token: string;
  role: string;
}

/**
 * API响应类型
 */
export interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 登录请求参数
 */
export interface LoginRequest {
  phone: string;
  password: string;
  code?: string;
}

/**
 * 分页请求参数
 */
export interface PageRequest {
  pageNum: number;
  pageSize: number;
  [key: string]: any;
}

/**
 * 分页响应类型
 */
export interface PageResponse<T> {
  total: number;
  list: T[];
  pageNum: number;
  pageSize: number;
} 