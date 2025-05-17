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
 * 儿童信息添加请求参数 - 严格按照API文档
 */
interface KidAddReq {
  avatar?: string;
  nickname: string;
  old: number;
  gender: number;
  birthday?: string;
}

/**
 * 儿童信息更新请求参数
 */
interface KidUpdateReq {
  id?: number;
  avatar: string;
  nickname: string;
  old: number;
  gender: number;
  birthday?: string;
}

/**
 * 儿童信息返回类型
 */
interface Kid {
  id?: number;
  userId: number;
  avatar: string;
  nickname: string;
  old: number;
  gender: number;
  createTime: string;
  updateTime: string;
}

/**
 * 创建儿童视图对象
 */
interface CreateKidVo {
  id?: number;
  userId: number;
  avatar: string;
  nickname: string;
  birthdate: string;
  old: number;
  gender: number;
  createTime: string;
  updateTime: string;
}

/**
 * 添加儿童信息
 * @param kidData 儿童信息数据
 * @returns 添加结果
 */
export const addKid = (kidData: KidAddReq): Promise<ApiResponse<Kid>> => {
  return api.post('/kids', kidData, {
    headers: {
      'Content-Type': 'application/json'
    },
    timeout: 20000 
  });
};

/**
 * 修改儿童信息
 * @param kidData 儿童信息更新数据
 * @returns 修改结果
 */
export const updateKid = (kidData: KidUpdateReq): Promise<ApiResponse<boolean>> => {
  return api.put('/kids', kidData, {
    headers: {
      'Content-Type': 'application/json'
    },
    timeout: 15000 
  });
};

/**
 * 根据ID获取儿童信息
 * @param id 儿童ID
 * @returns 儿童信息
 */
export const getKidById = (id: number): Promise<ApiResponse<CreateKidVo>> => {
  return api.get(`/kids/${id}`);
};

/**
 * 根据ID删除儿童信息
 * @param id 儿童ID
 * @returns 删除结果
 */
export const deleteKid = (id: number): Promise<ApiResponse<boolean>> => {
  return api.delete(`/kids/${id}`);
};

/**
 * 上传儿童头像
 * @param file 头像文件
 * @returns 包含头像URL的响应
 */
export const uploadKidAvatar = (file: File): Promise<ApiResponse<string>> => {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/kids/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};

/**
 * 获取儿童身体信息
 * @param kidId 儿童ID
 * @returns 身体信息
 */
export const getKidBody = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/kid_body/${kidId}`);
};

/**
 * 添加儿童身体信息
 * @param bodyData 身体信息数据
 * @returns 添加结果
 */
export const addKidBody = (bodyData: any): Promise<ApiResponse<any>> => {
  return api.post('/kid_body/add', bodyData);
};

/**
 * 更新儿童身体信息
 * @param bodyData 身体信息更新数据
 * @returns 更新结果
 */
export const updateKidBody = (bodyData: any): Promise<ApiResponse<any>> => {
  return api.put('/kid_body/update', bodyData);
};

/**
 * 获取儿童头部信息
 * @param kidId 儿童ID
 * @returns 头部信息
 */
export const getKidHead = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/kid_head/${kidId}`);
};

/**
 * 添加儿童头部信息
 * @param headData 头部信息数据
 * @returns 添加结果
 */
export const addKidHead = (headData: any): Promise<ApiResponse<any>> => {
  return api.post('/kid_head/add', headData);
};

/**
 * 更新儿童头部信息
 * @param headData 头部信息更新数据
 * @returns 更新结果
 */
export const updateKidHead = (headData: any): Promise<ApiResponse<any>> => {
  return api.put('/kid_head/update', headData);
};

/**
 * 获取儿童左臂信息
 * @param kidId 儿童ID
 * @returns 左臂信息
 */
export const getLeftArm = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/left_arm/${kidId}`);
};

/**
 * 添加儿童左臂信息
 * @param armData 左臂信息数据
 * @returns 添加结果
 */
export const addLeftArm = (armData: any): Promise<ApiResponse<any>> => {
  return api.post('/left_arm/add', armData);
};

/**
 * 更新儿童左臂信息
 * @param armData 左臂信息更新数据
 * @returns 更新结果
 */
export const updateLeftArm = (armData: any): Promise<ApiResponse<any>> => {
  return api.put('/left_arm/update', armData);
};

/**
 * 获取儿童右臂信息
 * @param kidId 儿童ID
 * @returns 右臂信息
 */
export const getRightArm = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/right_arm/${kidId}`);
};

/**
 * 添加儿童右臂信息
 * @param armData 右臂信息数据
 * @returns 添加结果
 */
export const addRightArm = (armData: any): Promise<ApiResponse<any>> => {
  return api.post('/right_arm/add', armData);
};

/**
 * 更新儿童右臂信息
 * @param armData 右臂信息更新数据
 * @returns 更新结果
 */
export const updateRightArm = (armData: any): Promise<ApiResponse<any>> => {
  return api.put('/right_arm/update', armData);
};

/**
 * 获取儿童左腿信息
 * @param kidId 儿童ID
 * @returns 左腿信息
 */
export const getLeftLeg = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/left_leg/${kidId}`);
};

/**
 * 添加儿童左腿信息
 * @param legData 左腿信息数据
 * @returns 添加结果
 */
export const addLeftLeg = (legData: any): Promise<ApiResponse<any>> => {
  return api.post('/left_leg/add', legData);
};

/**
 * 更新儿童左腿信息
 * @param legData 左腿信息更新数据
 * @returns 更新结果
 */
export const updateLeftLeg = (legData: any): Promise<ApiResponse<any>> => {
  return api.put('/left_leg/update', legData);
};

/**
 * 获取儿童右腿信息
 * @param kidId 儿童ID
 * @returns 右腿信息
 */
export const getRightLeg = (kidId: number): Promise<ApiResponse<any>> => {
  return api.get(`/right_leg/${kidId}`);
};

/**
 * 添加儿童右腿信息
 * @param legData 右腿信息数据
 * @returns 添加结果
 */
export const addRightLeg = (legData: any): Promise<ApiResponse<any>> => {
  return api.post('/right_leg/add', legData);
};

/**
 * 更新儿童右腿信息
 * @param legData 右腿信息更新数据
 * @returns 更新结果
 */
export const updateRightLeg = (legData: any): Promise<ApiResponse<any>> => {
  return api.put('/right_leg/update', legData);
}; 