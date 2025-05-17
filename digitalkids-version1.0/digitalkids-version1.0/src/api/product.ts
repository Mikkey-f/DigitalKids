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
 * 商品分类接口
 */
interface Category {
  id: number;
  parentId: number;
  name: string;
  subCategories: Category[];
}

/**
 * 商品接口
 */
interface Product {
  id: number;
  categoryId: number;
  name: string;
  subtitle: string;
  mainImage: string;
  subImages: string;
  detail: string;
  price: number;
}

/**
 * 获取所有商品分类
 * @returns 商品分类列表
 */
export const getAllCategories = (): Promise<ApiResponse<Category[]>> => {
  return api.get('/category');
};

/**
 * 添加商品分类（管理员）
 * @param categoryData 分类数据
 * @returns 添加结果
 */
export const addCategory = (categoryData: { name: string, parentId: number }): Promise<ApiResponse<any>> => {
  return api.post('/category', categoryData);
};

/**
 * 更新商品分类（管理员）
 * @param id 分类ID
 * @param categoryData 分类数据
 * @returns 更新结果
 */
export const updateCategory = (id: number, categoryData: { name: string }): Promise<ApiResponse<any>> => {
  return api.put(`/category/${id}`, categoryData);
};

/**
 * 删除商品分类（管理员）
 * @param id 分类ID
 * @returns 删除结果
 */
export const deleteCategory = (id: number): Promise<ApiResponse<any>> => {
  return api.delete(`/category/${id}`);
};

/**
 * 获取商品列表
 * @param params 分页和过滤参数
 * @returns 商品列表
 */
export const getProducts = (params: {
  pageNum?: number;
  pageSize?: number;
  categoryId?: number;
}): Promise<ApiResponse<PageData<Product>>> => {
  return api.get('/product', { params });
};

/**
 * 获取商品详情
 * @param id 商品ID
 * @returns 商品详情
 */
export const getProductById = (id: number): Promise<ApiResponse<Product>> => {
  return api.get(`/product/${id}`);
};

/**
 * 添加商品（管理员）
 * @param productData 商品数据
 * @returns 添加结果
 */
export const addProduct = (productData: {
  categoryId: number;
  name: string;
  subtitle: string;
  mainImage: string;
  subImages: string;
  detail: string;
  price: number;
}): Promise<ApiResponse<any>> => {
  return api.post('/product', productData);
};

/**
 * 更新商品（管理员）
 * @param id 商品ID
 * @param productData 商品数据
 * @returns 更新结果
 */
export const updateProduct = (id: number, productData: Partial<Product>): Promise<ApiResponse<any>> => {
  return api.put(`/product/${id}`, productData);
};

/**
 * 删除商品（管理员）
 * @param id 商品ID
 * @returns 删除结果
 */
export const deleteProduct = (id: number): Promise<ApiResponse<any>> => {
  return api.delete(`/product/${id}`);
}; 