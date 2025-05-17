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
 * 搜索请求参数
 */
interface SearchReq {
  content: string;
}

/**
 * 按阶段搜索育儿百科请求参数
 */
interface SearchAllParEncyReq {
  stageStr: string;
}

/**
 * 商品搜索请求参数
 */
interface ProductSearchReq {
  keyword: string;
  categoryId?: number | null;
  pageReq: {
    current: number;
    pageSize: number;
    sortField?: string;
    sortOrder?: string;
  };
  [key: string]: any; 
}

/**
 * 分页数据类型
 */
interface PageData<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  orders: {
    column: string;
    asc: boolean;
  }[];
  optimizeCountSql: boolean;
  searchCount: boolean;
  optimizeJoinOfCountSql: boolean;
  countId: string;
  maxLimit: number;
}

/**
 * 搜索结果接口
 */
interface SearchVo {
  id: number;
  title: string;
  description: string;
  type: string;
}

/**
 * 通用搜索
 * @param searchData 搜索数据
 * @returns 搜索结果
 */
export const search = (searchData: SearchReq): Promise<ApiResponse<SearchVo[]>> => {
  return api.post('/search', searchData);
};

/**
 * 根据阶段搜索文章
 * @param stageData 阶段数据
 * @returns 搜索结果
 */
export const searchByStage = (stageData: SearchAllParEncyReq): Promise<ApiResponse<any[]>> => {
  return api.post('/search/stage', stageData);
};

/**
 * 商品搜索
 * @param searchParams 搜索参数
 * @returns 商品分页结果
 */
export const searchProducts = (searchParams: ProductSearchReq): Promise<ApiResponse<any>> => {
  const params = { ...searchParams };
  if (!params.pageReq) {
    params.pageReq = {
      current: 1,
      pageSize: 20
    };
  } else {
    params.pageReq.pageSize = params.pageReq.pageSize || 20;
  }
  if (params.pageReq && params.pageReq.sortOrder) {
    if (params.pageReq.sortOrder === 'asc') {
      params.pageReq.sortOrder = 'ascend';
    } else if (params.pageReq.sortOrder === 'desc') {
      params.pageReq.sortOrder = 'descend';
    }
  }
  if (!params.keyword) {
    params.keyword = '';
  }
  return api.post('/product/search', params);
};

/**
 * 智能体提问
 * @param questionData 问题数据
 * @returns 回答（HTML格式）
 */
export const askQuestion = (questionData: { content: string }): Promise<ApiResponse<string>> => {
  return api.post('/question', questionData);
};

/**
 * 用户跳转页面接口
 * @param talkData 对话数据
 * @returns 跳转结果
 */
export const talk = (talkData: { content: string }): Promise<ApiResponse<any>> => {
  return api.post('/talk', talkData);
};

/**
 * 获取智能体回答状态
 * @param sessionId 会话ID
 * @returns 状态信息
 */
export const getAnswerStatus = (sessionId: string): Promise<ApiResponse<any>> => {
  return api.get(`/status/${sessionId}`);
};

/**
 * 获取舌头检测状态
 * @param sessionId 会话ID
 * @returns 状态信息
 */
export const getTongueStatus = (sessionId: string): Promise<ApiResponse<any>> => {
  return api.get(`/status/tongue/${sessionId}`);
};

/**
 * 舌头检测
 * @param file 舌头图像文件
 * @returns 检测结果
 */
export const tongueDetection = (file: File): Promise<ApiResponse<any>> => {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/tongue', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}; 