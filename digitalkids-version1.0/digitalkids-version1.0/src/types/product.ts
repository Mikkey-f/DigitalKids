/**
 * 后端商品数据接口
 */
export interface ProductVo {
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
 * API响应包装
 */
export interface ProductResponse {
  code: number;
  msg: string;
  data: ProductVo;
}

/**
 * 分页商品列表响应
 */
export interface ProductListResponse {
  code: number;
  msg: string;
  data: {
    records: ProductVo[];
    total: number;
    size: number;
    current: number;
  };
}

/**
 * 前端使用的商品数据，扩展了后端数据
 */
export interface Product extends ProductVo {
  discount?: number; 
  originalPrice?: number; 
  rating?: number;
  sold?: number;
  images?: string[];
  type?: string;
  features?: string[];
  specifications?: Array<{name: string, value: string}>;
  isPrescription?: boolean;
  ageRange?: string;
  isFavorite?: boolean;
  isNew?: boolean;
  isHot?: boolean;
  isOnSale?: boolean;
}

/**
 * 商品类别
 */
export interface Category {
  id: number;
  parentId: number;
  name: string;
  subCategories: Category[];
}

/**
 * 类别响应
 */
export interface CategoryResponse {
  code: number;
  msg: string;
  data: Category[];
}

/**
 * 商品详情页配置
 */
export interface ProductDetailConfig {
  template: string; 
  sections: string[];
  features: string[]; 
  theme: {
    primary: string;
    secondary: string;
    accent: string;
  };
}

/**
 * 商品评论类型
 */
export interface ProductComment {
  id: number;
  userId: number;
  userName: string;
  userAvatar: string;
  content: string;
  rating: number;
  images?: string[];
  createdAt: string;
  likes: number;
  reply?: {
    content: string;
    createdAt: string;
  };
} 