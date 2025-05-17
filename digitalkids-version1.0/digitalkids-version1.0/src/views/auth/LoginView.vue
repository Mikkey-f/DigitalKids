<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
      <div class="p-8">
        <router-link to="/" class="text-gray-500 hover:text-gray-700 mb-4 inline-block">
          <i class="fas fa-arrow-left"></i> 返回
        </router-link>
        <div class="flex justify-center mb-6">
          <div class="logo-container">
            <div class="orbit-element"></div>
            <div class="logo w-24 h-24 animate-float">
              <img src="@/assets/智塑童程.png" alt="智塑童程" class="w-full h-full object-contain" />
            </div>
          </div>
        </div>
        <h1 class="text-3xl font-bold text-gray-800 mb-2 text-center">账号登录</h1>
        <p class="text-gray-600 mb-8 text-center">使用手机号和密码登录您的账号</p>
        <form @submit.prevent="handleLogin" @keydown.enter.prevent="handleLogin" class="space-y-4">
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">手机号</label>
            <div class="flex items-center">
              <span class="prefix">+86</span>
              <div class="input-wrapper">
                <input 
                  type="tel" 
                  v-model="phone" 
                  placeholder="请输入手机号" 
                  class="w-full" 
                  required
                >
              </div>
            </div>
          </div>
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">密码</label>
            <div class="flex items-center">
              <div class="input-wrapper">
                <input 
                  :type="showPassword ? 'text' : 'password'" 
                  v-model="password" 
                  placeholder="请输入密码" 
                  class="w-full" 
                  required
                >
              </div>
              <button 
                type="button" 
                class="text-gray-500 hover:text-gray-700 ml-2" 
                @click="togglePassword"
              >
                <i :class="showPassword ? 'far fa-eye-slash' : 'far fa-eye'"></i>
              </button>
            </div>
          </div>
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">验证码</label>
            <div class="flex items-center">
              <div class="input-wrapper">
                <input 
                  type="text" 
                  v-model="verificationCode" 
                  placeholder="请输入验证码" 
                  class="w-full" 
                  required
                >
              </div>
              <button 
                type="button" 
                @click="sendVerificationCode" 
                class="text-sm text-blue-500 whitespace-nowrap ml-2 hover:underline flex-shrink-0"
                :disabled="countdown > 0"
              >
                {{ countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
              </button>
            </div>
          </div>
          <div class="flex justify-between items-center">
            <div class="flex items-center">
              <input type="checkbox" id="remember-me" v-model="rememberMe" class="mr-2">
              <label for="remember-me" class="text-sm text-gray-600">记住我</label>
            </div>
            <router-link to="/forgot-password" class="text-sm text-blue-500 hover:underline">
              忘记密码?
            </router-link>
          </div>
          <button 
            type="submit" 
            class="w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-3 rounded-full font-medium hover:opacity-90 transition"
            :disabled="loading"
          >
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        <div class="mt-6 text-center">
          <span class="text-gray-600">还没有账号?</span>
          <router-link to="/register" class="text-blue-500 font-medium hover:underline ml-2">
            立即注册
          </router-link>
        </div>
      </div>
    </div>
    <LoadingAnimation v-if="showLoading" message="登录成功，正在进入系统" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { showNotification } from '@/utils/notification';
import LoadingAnimation from '@/components/LoadingAnimation.vue';
import { login as userLogin, sendVerificationCode as apiSendVerificationCode } from '@/api/user';

const router = useRouter();
const authStore = useAuthStore();
const phone = ref('');
const password = ref('');
const verificationCode = ref('');
const rememberMe = ref(false);
const showPassword = ref(false);
const loading = ref(false);
const showLoading = ref(false);
const countdown = ref(0);

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const sendVerificationCode = async () => {
  if (!phone.value) {
    showNotification.warning('请输入手机号');
    return;
  }
  if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    showNotification.warning('请输入正确的手机号格式');
    return;
  }
  try {
    const res = await apiSendVerificationCode(phone.value);
    if (res && res.code === 1) {
      countdown.value = 60;
      const timer = setInterval(() => {
        countdown.value--;
        if (countdown.value <= 0) {
          clearInterval(timer);
        }
      }, 1000);
      showNotification.success('验证码已发送', `验证码已发送至 ${phone.value}`);
    } else {
      showNotification.error(res?.msg || '验证码发送失败');
    }
  } catch (error: any) {
    showNotification.error(error?.message || '验证码发送失败，请稍后重试');
  }
};

const handleLogin = async () => {
  if (!phone.value || !password.value || !verificationCode.value) {
    showNotification.warning('请输入手机号、密码和验证码');
    return;
  }
  loading.value = true;
  try {
    const response = await userLogin(phone.value, password.value, verificationCode.value);
    const userData = response.data;
    await authStore.setUserData(userData);
    if (rememberMe.value) {
      authStore.setRememberMe(true);
    }
    showNotification.success('登录成功', '欢迎回来');
    showLoading.value = true;
    setTimeout(() => {
      router.push('/home');
    }, 3000);
  } catch (error: any) {
    showNotification.error(error.message || '登录失败，请稍后重试');
    loading.value = false;
    return false;
  }
};
</script> 

<style scoped>
.logo-container {
  position: relative;
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo {
  background: white;
  border-radius: 8px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  z-index: 1;
  box-shadow: 0 0 20px rgba(180, 71, 153, 0.3);
}

.orbit-element {
  position: absolute;
  width: 14px;
  height: 14px;
  background: #B44799;
  border-radius: 50%;
  animation: orbit 3s linear infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

@keyframes orbit {
  0% {
    transform: rotate(0deg) translateX(73px) rotate(0deg);
  }
  100% {
    transform: rotate(360deg) translateX(73px) rotate(-360deg);
  }
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}

.orbit-element::after {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: #B44799;
  border-radius: 50%;
  filter: blur(4px);
  opacity: 0.4;
}

.card {
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  background-color: rgba(255, 255, 255, 0.85);
  border: 1px solid rgba(255, 255, 255, 0.125);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
}

.input-field {
  margin-bottom: 1rem;
  position: relative;
}

.input-field .flex {
  position: relative;
}

.input-wrapper {
  flex: 1;
}

.input-wrapper input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.prefix {
  display: inline-block;
  padding: 0 12px 0 0;
  color: #64748b;
  font-size: 14px;
}

button[type="submit"] {
  background: linear-gradient(135deg, #3B82F6 0%, #9333EA 100%);
  transition: all 0.3s ease;
}

button[type="submit"]:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);
}
</style> 