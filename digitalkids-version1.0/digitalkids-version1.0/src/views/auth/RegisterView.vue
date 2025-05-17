<template>
  <div class="py-8 flex items-center justify-center p-4 overflow-auto">
    <div class="card rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
      <div class="p-8">
        <router-link to="/" class="text-gray-500 hover:text-gray-700 mb-4 inline-block">
          <i class="fas fa-arrow-left"></i> 返回
        </router-link>
        <h1 class="text-3xl font-bold text-gray-800 mb-2 text-center">注册账号</h1>
        <p class="text-gray-600 mb-8 text-center">创建您的账号</p>
        <form @submit.prevent="handleRegister" class="space-y-4">
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">用户名</label>
            <div class="flex items-center">
              <div class="input-wrapper w-full">
                <input 
                  type="text" 
                  v-model="username" 
                  placeholder="请输入用户名" 
                  class="w-full" 
                  required
                >
              </div>
            </div>
          </div>
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">手机号</label>
            <div class="flex items-center">
              <span class="prefix">+86</span>
              <div class="input-wrapper w-full">
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
            <label class="block text-xs text-gray-500 mb-1">验证码</label>
            <div class="flex items-center">
              <div class="input-wrapper w-full">
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
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">性别</label>
            <div class="flex items-center space-x-4">
              <div class="flex items-center">
                <input 
                  type="radio" 
                  id="gender-male" 
                  value="男" 
                  v-model="gender" 
                  class="mr-2"
                >
                <label for="gender-male" class="text-sm text-gray-600">男</label>
              </div>
              <div class="flex items-center">
                <input 
                  type="radio" 
                  id="gender-female" 
                  value="女" 
                  v-model="gender" 
                  class="mr-2"
                >
                <label for="gender-female" class="text-sm text-gray-600">女</label>
              </div>
              <div class="flex items-center">
                <input 
                  type="radio" 
                  id="gender-other" 
                  value="其他" 
                  v-model="gender" 
                  class="mr-2"
                >
                <label for="gender-other" class="text-sm text-gray-600">其他</label>
              </div>
            </div>
          </div>
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">密码</label>
            <div class="flex items-center">
              <div class="input-wrapper w-full">
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
                class="text-gray-500 hover:text-gray-700 ml-2 flex-shrink-0" 
                @click="togglePassword"
              >
                <i :class="showPassword ? 'far fa-eye-slash' : 'far fa-eye'"></i>
              </button>
            </div>
          </div>
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">确认密码</label>
            <div class="flex items-center">
              <div class="input-wrapper w-full">
                <input 
                  :type="showConfirmPassword ? 'text' : 'password'" 
                  v-model="confirmPassword" 
                  placeholder="请再次输入密码" 
                  class="w-full" 
                  required
                >
              </div>
              <button 
                type="button" 
                class="text-gray-500 hover:text-gray-700 ml-2 flex-shrink-0" 
                @click="toggleConfirmPassword"
              >
                <i :class="showConfirmPassword ? 'far fa-eye-slash' : 'far fa-eye'"></i>
              </button>
            </div>
          </div>
          <div class="flex items-center">
            <input 
              type="checkbox" 
              id="agree-terms" 
              v-model="agreeTerms" 
              class="mr-2" 
              required
            >
            <label for="agree-terms" class="text-sm text-gray-600">
              我已阅读并同意 <a href="#" class="text-blue-500 hover:underline">用户协议</a> 和 <a href="#" class="text-blue-500 hover:underline">隐私政策</a>
            </label>
          </div>
          <button 
            type="submit" 
            class="w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-3 rounded-full font-medium hover:opacity-90 transition"
            :disabled="loading"
          >
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>
        <div class="mt-6 text-center">
          <span class="text-gray-600">已有账号?</span>
          <router-link to="/login" class="text-blue-500 font-medium hover:underline ml-2">
            立即登录
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { showNotification } from '@/utils/notification';
import { sendVerificationCode as apiSendVerificationCode, register as apiRegister } from '@/api/user';

const router = useRouter();
const authStore = useAuthStore();

const username = ref('');
const phone = ref('');
const verificationCode = ref('');
const password = ref('');
const confirmPassword = ref('');
const gender = ref('');
const agreeTerms = ref(false);
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const loading = ref(false);
const countdown = ref(0);

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const toggleConfirmPassword = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
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
    console.error('发送验证码失败:', error);
    showNotification.error(error?.message || '验证码发送失败，请稍后重试');
  }
};

const handleRegister = async () => {
  if (!username.value || !phone.value || !verificationCode.value || !password.value || !confirmPassword.value ) {
    showNotification.warning('请填写所有字段');
    return;
  }
  
  if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    showNotification.warning('请输入正确的手机号格式');
    return;
  }
  
  if (password.value !== confirmPassword.value) {
    showNotification.error('两次输入的密码不一致');
    return;
  }

  if(phone.value.length !== 11){
    showNotification.warning('手机号长度不正确');
    return;
  }
  
  if (password.value.length < 6) {
    showNotification.warning('密码长度至少为6位');
    return;
  }
  
  if (!agreeTerms.value) {
    showNotification.warning('请阅读并同意用户协议和隐私政策');
    return;
  }
  
  loading.value = true;
  
  try {
    const registerData = {
      name: username.value,
      password: password.value,
      phone: phone.value,
      gender: gender.value || '男',
      code: verificationCode.value
    };
    const res = await apiRegister(registerData);
    if (res && res.code === 1) {
      showNotification.success('注册成功', '欢迎加入');
      
      if (res.data) {
        authStore.setUserData(res.data);
      } else {
        const userData = {
          id: Math.floor(Math.random() * 1000) + 1,
          name: username.value || `数字儿童用户${phone.value.slice(-4)}`,
          avatar: '',
          gender: gender.value,
          phone: phone.value,
          location: '',
          token: res.data?.token || 'mock-token',
          role: 'user'
        };
        authStore.setUserData(userData);
      }
      
      router.push('/login');
    } else {
      showNotification.error(res?.msg || '注册失败，请稍后重试');
    }
  } catch (error: any) {
    console.error('注册失败:', error);
    showNotification.error(error?.message || '注册失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};
</script> 

<style scoped>
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