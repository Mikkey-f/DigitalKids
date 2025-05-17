<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
      <div class="p-8">
        <div class="flex justify-center mb-8">
          <div class="logo-container">
            <div class="orbit-element"></div>
            <div class="logo w-24 h-24 animate-float">
              <img src="@/assets/智塑童程.png" alt="智塑童程" class="w-full h-full object-contain" />
            </div>
          </div>
        </div>
        
        <h1 class="text-3xl font-bold text-center text-gray-800 mb-2">数字儿童</h1>
        <p class="text-gray-600 text-center mb-8">儿童数字医疗平台</p>
        
        <div class="space-y-4">
          <router-link to="/login" class="block w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-3 rounded-full font-medium hover:opacity-90 transition text-center">
            登录账号
          </router-link>
          <router-link to="/register" class="block w-full border border-gray-300 text-gray-700 py-3 rounded-full font-medium hover:bg-gray-50 transition text-center">
            注册账号
          </router-link>
        </div>
        
        <div class="mt-8 text-center">
          <button @click="handleGuestLogin" class="text-blue-500 font-medium hover:underline">
            继续以游客身份浏览
          </button>
        </div>
      </div>
    </div>
    
    <LoadingAnimation v-if="showLoading" message="正在以游客身份登录" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router'; 
import LoadingAnimation from '@/components/LoadingAnimation.vue';

const router = useRouter();
const showLoading = ref(false);

const handleGuestLogin = () => {
  showLoading.value = true;
  
  setTimeout(() => {
    router.push('/home');
  }, 3000);
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
</style> 