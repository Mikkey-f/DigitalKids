import api from './axios';
import { AxiosResponse } from 'axios';

/**
 * API响应结构类型
 */
export interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 舌苔检测结果接口
 */
export interface TongueResult {
  color_result: string;  // 舌色结果
  coated_result: string; // 苔色结果
  crack_result: string;  // 裂纹结果
  indent_result: string; // 齿痕结果
}

/**
 * 上传舌苔图片进行分析
 * @param file 舌苔图片文件
 * @returns 包含sessionId的响应
 */
export const uploadTongueImage = async (file: File): Promise<ApiResponse<string>> => {
  const formData = new FormData();
  formData.append('file', file);
  try {
    const response = await api.post<ApiResponse<string>>('/tongue', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response as unknown as ApiResponse<string>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 获取舌苔检测结果
 * @param sessionId 会话ID
 * @returns 舌苔检测结果
 */
export const getTongueResult = async (sessionId: string): Promise<ApiResponse<TongueResult>> => {
  try {
    const response = await api.get<ApiResponse<TongueResult>>(`/status/tongue/${sessionId}`);
    return response as unknown as ApiResponse<TongueResult>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 智能体提问接口
 * @param question 问题内容，例如舌苔分析结果拼接后的内容
 * @returns 包含sessionId的响应
 */
export const askQuestion = async (question: string): Promise<ApiResponse<string>> => {
  try {
    const response = await api.post<ApiResponse<string>>('/question', { question }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response as unknown as ApiResponse<string>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 获取智能体回答结果
 * @param sessionId 会话ID
 * @returns 智能体回答结果
 */
export const getAnswerResult = async (sessionId: string): Promise<ApiResponse<string>> => {
  try {
    const response = await api.get<ApiResponse<string>>(`/status/${sessionId}`);
    return response as unknown as ApiResponse<string>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
}; 