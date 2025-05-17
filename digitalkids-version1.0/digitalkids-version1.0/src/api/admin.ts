import axios from 'axios';
const baseURL = import.meta.env.VITE_API_URL || '';
const adminApi = axios.create({
  baseURL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});
adminApi.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

/**
 * API响应结构类型
 */
interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 分页数据类型
 */
interface PageData<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
}

/**
 * 用户信息接口
 */
interface User {
  id: number;
  name: string;
  phone: string;
  gender: string;
  role: string;
  avatar: string;
  createTime: string;
  updateTime: string;
}

/**
 * 儿童信息接口
 */
interface Kid {
  id: number;
  parentId: number;
  name: string;
  age: number;
  gender: string;
  height: number;
  weight: number;
  picture: string;
  createTime: string;
  updateTime: string;
}

/**
 * 育儿百科接口
 */
interface ParentingEncyclopedia {
  id: number;
  title: string;
  content: string;
  stage: string;
  publishTime: string;
}

/**
 * 分页请求参数
 */
interface PageReq {
  current: number;
  size: number;
  pageSize?: number;
}

/**
 * 系统概览数据类型
 */
interface SystemStats {
  userCount: number;
  productCount: number;
  articleCount: number;
}

// 用户管理接口
export const userAPI = {
  getUserList: (pageInfo: any) => {
    return adminApi.post('/api/admin/user/list/page', pageInfo);
  },
  addUser: (userData: any) => {
    return adminApi.post('/api/admin/user/add', userData);
  },
  updateUser: (userData: any) => {
    return adminApi.post('/api/admin/user/update', userData);
  },
  deleteUser: (userId: number) => {
    return adminApi.delete('/api/admin/user/delete', { data: { id: userId } });
  },
  getUserDetail: (userId: number) => {
    return adminApi.get(`/api/admin/user/get?id=${userId}`);
  }
};

// 商品管理接口
export const productAPI = {
  getProductList: (categoryId: number) => {
    return adminApi.get(`/api/product?categoryId=${categoryId}`);
  },
  listProductsByPage: (pageInfo: PageReq) => {
    return adminApi.post('/api/admin/product/list/page', pageInfo);
  },
  getCategories: () => {
    return adminApi.get('/api/category');
  },
  getProductDetail: (productId: number) => {
    return adminApi.get(`/api/product/${productId}`);
  },
  addProduct: (productData: any) => {
    return adminApi.post('/api/admin/product', productData);
  },
  updateProduct: (productId: number, productData: any) => {
    return adminApi.put(`/api/admin/product/${productId}`, productData);
  },
  deleteProduct: (productId: number) => {
    return adminApi.delete(`/api/admin/product/${productId}`);
  }
};

// 文章管理接口
export const articleAPI = {
  getArticlesByStage: (stage: number) => {
    return adminApi.get(`/api/parentingEncyclopedia/stage/${stage}`);
  },
  getArticleListPage: (pageReq: PageReq) => {
    return adminApi.get(`/api/admin/parentingEncyclopedia/list?current=${pageReq.current}&pageSize=${pageReq.size}`);
  },
  getArticleDetail: (articleId: number) => {
    return adminApi.get(`/api/parentingEncyclopedia/${articleId}`);
  },
  addArticle: (articleData: any) => {
    return adminApi.post('/api/admin/parentingEncyclopedia', articleData);
  },
  updateArticle: (articleData: any) => {
    return adminApi.put('/api/admin/parentingEncyclopedia', articleData);
  },
  deleteArticle: (articleId: number) => {
    return adminApi.delete(`/api/admin/parentingEncyclopedia/${articleId}`);
  }
};

export const statsAPI = {
  getSystemStats: () => {
    return adminApi.get('/api/admin/stats');
  }
};

export default {
  userAPI,
  productAPI,
  articleAPI,
  statsAPI
}; 