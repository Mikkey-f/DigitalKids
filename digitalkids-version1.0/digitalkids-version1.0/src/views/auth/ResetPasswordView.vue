<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
      <div class="p-8">
        <router-link to="/forgot-password" class="text-gray-500 hover:text-gray-700 mb-4 inline-block">
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
        
        <h1 class="text-3xl font-bold text-gray-800 mb-2 text-center">重置密码</h1>
        <p class="text-gray-600 mb-8 text-center">请设置您的新密码</p>
        
        <form @submit.prevent="handleReset" class="space-y-4">
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">新密码</label>
            <div class="flex items-center relative">
              <input 
                :type="showPassword ? 'text' : 'password'" 
                v-model="password" 
                placeholder="请输入新密码" 
                class="w-full" 
                required
              >
              <button 
                type="button" 
                @click="togglePasswordVisibility" 
                class="text-gray-500 hover:text-gray-700 absolute right-3 z-10"
              >
                <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
            </div>
          </div>
          
          <div class="input-field">
            <label class="block text-xs text-gray-500 mb-1">确认密码</label>
            <div class="flex items-center relative">
              <input 
                :type="showConfirmPassword ? 'text' : 'password'" 
                v-model="confirmPassword" 
                placeholder="请再次输入新密码" 
                class="w-full" 
                required
              >
              <button 
                type="button" 
                @click="toggleConfirmPasswordVisibility" 
                class="text-gray-500 hover:text-gray-700 absolute right-3 z-10"
              >
                <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
            </div>
          </div>
          
          <button 
            type="submit" 
            class="w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-3 rounded-full font-medium hover:opacity-90 transition"
            :disabled="loading"
          >
            {{ loading ? '重置中...' : '重置密码' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { showNotification } from '@/utils/notification';

const router = useRouter();
const route = useRoute();

const password = ref('');
const confirmPassword = ref('');
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const loading = ref(false);

onMounted(() => {
  if (!route.query.phone) {
    showNotification.warning('无效的访问路径');
    router.push('/forgot-password');
  }
});

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

const handleReset = () => {
  if (!password.value || !confirmPassword.value) {
    showNotification.warning('请输入新密码和确认密码');
    return;
  }
  
  if (password.value !== confirmPassword.value) {
    showNotification.error('两次输入的密码不一致');
    return;
  }
  
  if (password.value.length < 6) {
    showNotification.warning('密码长度不能少于6位');
    return;
  }
  
  loading.value = true;
  
  setTimeout(() => {
    loading.value = false;
    showNotification.success('密码重置成功', '请重新登录');
    router.push('/login');
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

button[type="submit"] {
  background: linear-gradient(135deg, #3B82F6 0%, #9333EA 100%);
  transition: all 0.3s ease;
}

button[type="submit"]:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);
}
</style> 