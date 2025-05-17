import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { UserData } from '@/types';

export const useAuthStore = defineStore('auth', () => {
  const storedUser = localStorage.getItem('user');
  const user = ref<UserData | null>(storedUser ? JSON.parse(storedUser) : null);
  const rememberMe = ref(localStorage.getItem('rememberMe') === 'true');
  const isAuthenticated = computed(() => !!user.value);
  
  const login = (phone: string, password: string) => {
    return new Promise<boolean>((resolve) => {
      setTimeout(() => {
        if (phone === '123' && password === '123456') {
          user.value = {
            id: 1,
            name: '数字儿童',
            avatar: '',
            gender: '男',
            phone,
            location: '',
            token: 'shane',
            role: 'user'
          };
          if (user.value.token) {
            localStorage.setItem('token', user.value.token);
            localStorage.setItem('user', JSON.stringify(user.value));
          }
          resolve(true);
        } else {
          resolve(false);
        }
      }, 1000);
    });
  };
  
  const register = (phone: string, password: string) => {
    return new Promise<boolean>((resolve) => {
      setTimeout(() => {
        user.value = {
          id: Math.floor(Math.random() * 1000) + 1,
          name: `数字儿童用户${phone.slice(-4)}`,
          avatar: '',
          gender: '男',
          phone,
          location: '',
          token: 'shane',
          role: 'user'
        };
        if (user.value.token) {
          localStorage.setItem('token', user.value.token);
          localStorage.setItem('user', JSON.stringify(user.value));
        }
        resolve(true);
      }, 1000);
    });
  };
  
  const logout = () => {
    user.value = null;
    rememberMe.value = false;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userInfo');
    localStorage.removeItem('rememberMe');
  };
  
  const setUserData = (userData: UserData) => {
    userData.role = 'admin';
    user.value = userData;
    if (userData && userData.token) {
      localStorage.setItem('token', userData.token);
    }
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('userInfo', JSON.stringify({...userData, role: 'admin'}));
    return true;
  };
  
  const setRememberMe = (value: boolean) => {
    rememberMe.value = value;
    localStorage.setItem('rememberMe', value.toString());
  };
  
  return {
    user,
    rememberMe,
    isAuthenticated,
    login,
    register,
    logout,
    setUserData,
    setRememberMe,
  };
});