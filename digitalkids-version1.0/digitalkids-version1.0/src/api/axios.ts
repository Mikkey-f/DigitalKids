import axios from 'axios';
import router from '@/router';
import { ElMessage } from 'element-plus';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    const noAuthUrls = ['/user/login', '/user/sendMsg', '/user/register'];
    const isNoAuthUrl = noAuthUrls.some(url => config.url?.includes(url));
    
    if (isNoAuthUrl) {
      if (config.headers && 'Authorization' in config.headers) {
        delete config.headers.Authorization;
      }
    } else {
      const token = localStorage.getItem('token');
      if (token) {
        if (!config.headers) {
          config.headers = {};
        }
        if (config.headers.Authorization) {
          console.log('自定义Authorization头:', config.headers.Authorization);
        } else {
          config.headers.Authorization = `Bearer ${token}`;
          console.log('Authorization头:', `Bearer ${token}`);
        }
      } else {
        console.warn('未找到token');
      }
    }    
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    if (response.data && response.data.code === 0) {
      const errorMsg = response.data.msg || '请求失败';
      if (errorMsg.includes('认证失败')) {
        ElMessage.error('登录已过期，请重新登录');
        localStorage.removeItem('token'); 
        router.push('/login');
      }
      return Promise.reject(new Error(errorMsg));
    }
    return response.data;
  },
  error => {
    ElMessage.error('网络请求异常，请稍后再试');
    return Promise.reject(error);
  }
);
export default api; 