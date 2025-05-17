<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
      <div class="p-8">
        <router-link to="/login" class="text-gray-500 hover:text-gray-700 mb-4 inline-block">
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
        
        <h1 class="text-3xl font-bold text-gray-800 mb-2 text-center">找回密码</h1>
        <p class="text-gray-600 mb-8 text-center">通过手机号找回您的密码</p>
        
        <form @submit.prevent="handleVerify" class="space-y-4">
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
                class="text-sm text-blue-500 whitespace-nowrap ml-2 hover:underline"
                :disabled="countdown > 0"
              >
                {{ countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
              </button>
            </div>
          </div>
          
          <button 
            type="submit" 
            class="w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-3 rounded-full font-medium hover:opacity-90 transition"
            :disabled="loading"
          >
            {{ loading ? '验证中...' : '验证' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotification } from '@/utils/notification';

const router = useRouter();

const phone = ref('');
const verificationCode = ref('');
const loading = ref(false);
const countdown = ref(0);

const sendVerificationCode = () => {
  if (!phone.value) {
    showNotification.warning('请输入手机号');
    return;
  }
  
  countdown.value = 60;
  
  const timer = setInterval(() => {
    countdown.value--;
    
    if (countdown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
  
  showNotification.success('验证码已发送', `验证码已发送至 ${phone.value}`);
  verificationCode.value = '1234'; 

const handleVerify = () => {
  if (!phone.value || !verificationCode.value) {
    showNotification.warning('请输入手机号和验证码');
    return;
  }
  
  loading.value = true;
  
  setTimeout(() => {
    loading.value = false;
    
    showNotification.success('验证成功');
    router.push({
      path: '/reset-password',
      query: { phone: phone.value }
    });
  }, 1000);
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

.logo-D {
  font-size: 2.5rem;
  font-weight: bold;
  color: #FF4500;  /* 橙色 */
  margin-right: -5px;
}

.logo-k {
  font-size: 2.5rem;
  font-weight: bold;
  color: #FFFFFF;
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
</style> 