import api from './axios';

/**
 * API响应结构类型
 */
export interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 百科文章搜索结果类型
 */
export interface SearchVo {
  id: number;
  title: string;
  content: string;
  coverImage: string;
  summary: string;
  createTime: string;
  stage: number;
  views: number;
  likes: number;
}

/**
 * 分页请求参数类型
 */
export interface PageRequest {
  current: number;
  pageSize: number;
  sortField?: string;
  sortOrder?: string;
}

/**
 * 全局搜索参数类型
 */
export interface SearchAllParEncyReq {
  keyword: string;
  pageReq: PageRequest;
}

/**
 * 按阶段搜索参数类型
 */
export interface SearchParEncyByStageReq {
  keyword: string;
  stage: number;
  pageReq: PageRequest;
}

/**
 * 用户信息类型
 */
export interface UserVo {
  id: number;
  name: string;
  avatar: string;
  role: string;
  createTime: string;
  gender: any;
}

/**
 * 子评论类型
 */
export interface CommentSonVo {
  id: number;
  getUserVo: UserVo;
  likeCount: number;
  likeStatus: number;
  content: string;
}

/**
 * 评论类型
 */
export interface CommentVo {
  id: number;
  getUserVo: UserVo;
  content: string;
  likeCount: number;
  likeStatus: number;
  commentSonVoList: CommentSonVo[];
}

/**
 * 百科详情类型
 */
export interface EncyclopediaDetailVo {
  id: number;
  stage: number;
  userId: number;
  title: string;
  content: string;
  likeCount: number;
  likeStatus: number;
  commentVos: CommentVo[];
  createTime: string;
  updateTime: string;
  views?: number;
}

/**
 * 点赞响应类型
 */
export interface LikeVo {
  entityLikeCount: number;
  likeStatus: number;
}

/**
 * 评论请求参数类型
 */
export interface CommentAddReq {
  entityType: number; // 实体类型（1-百科, 2-商品, 3-评论）
  entityId: number; 
  targetId?: number; 
  content: string; 
}

/**
 * 收藏请求参数类型
 */
export interface FavorAddReq {
  entityType: number; 
  entityId: number; 
  userId: number; 
}

/**
 * 全局搜索百科文章
 * @param params 搜索参数
 * @returns 搜索结果
 */
export const searchEncyclopedia = async (params: SearchAllParEncyReq): Promise<ApiResponse<SearchVo[]>> => {
  try {
    const response = await api.post<ApiResponse<SearchVo[]>>('/parentingEncy/search', params, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response as unknown as ApiResponse<SearchVo[]>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 按阶段搜索百科文章
 * @param params 搜索参数，包含阶段信息
 * @returns 搜索结果
 */
export const searchEncyclopediaByStage = async (params: SearchParEncyByStageReq): Promise<ApiResponse<SearchVo[]>> => {
  try {
    const response = await api.post<ApiResponse<SearchVo[]>>('/parentingEncy/search/stage', params, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response as unknown as ApiResponse<SearchVo[]>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 获取百科详情
 * @param id 百科文章ID
 * @returns 百科详情
 */
export const getEncyclopediaDetail = async (id: number): Promise<ApiResponse<EncyclopediaDetailVo>> => {
  try {
    const response = await api.get<ApiResponse<EncyclopediaDetailVo>>(`/parentingEncyclopedia/${id}`);
    return response as unknown as ApiResponse<EncyclopediaDetailVo>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 添加评论
 * @param params 评论参数
 * @returns 添加结果
 */
export const addComment = async (params: CommentAddReq): Promise<ApiResponse<boolean>> => {
  try {
    const response = await api.post<ApiResponse<boolean>>('/comment/add', params);
    return response as unknown as ApiResponse<boolean>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 点赞
 * @param entityType 实体类型（1-百科, 2-商品, 3-评论）
 * @param entityId 实体ID
 * @param entityUserId 实体用户ID
 * @returns 点赞结果
 */
export const like = async (entityType: number, entityId: number, entityUserId: number = 0): Promise<ApiResponse<LikeVo>> => {
  try {
    const response = await api.post<ApiResponse<LikeVo>>('/like', null, {
      params: {
        entityType,
        entityId,
        entityUserId
      }
    });
    return response as unknown as ApiResponse<LikeVo>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 添加收藏
 * @param params 收藏参数
 * @returns 收藏结果
 */
export const addFavorite = async (params: FavorAddReq): Promise<ApiResponse<any>> => {
  try {
    const response = await api.post<ApiResponse<any>>('/user/favorite/add', params);
    return response as unknown as ApiResponse<any>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
};

/**
 * 取消收藏
 * @param id 收藏ID
 * @returns 取消结果
 */
export const deleteFavorite = async (id: number): Promise<ApiResponse<boolean>> => {
  try {
    const response = await api.delete<ApiResponse<boolean>>(`/user/favorite/delete/${id}`);
    return response as unknown as ApiResponse<boolean>;
  } catch (error) {
    console.error('异常:', error);
    return Promise.reject(error);
  }
}; 