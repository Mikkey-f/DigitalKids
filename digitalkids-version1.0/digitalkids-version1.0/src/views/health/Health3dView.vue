<template>
    <div class="health-view">
      <main class="health-main">
        <div class="main-container">
          <div class="content-area">
            <div class="health-content">
              <div class="welcome-section mb-8">
                <div class="welcome-header">
                  <h1 class="text-2xl font-bold mb-3">数字儿童健康检测系统</h1>
                  <div class="welcome-decoration"></div>
                </div>
                <p class="text-gray-600 mb-4 leading-relaxed">欢迎使用3D健康检测系统，您可以通过添加宝宝信息并点击3D模型的不同部位，查看和录入健康数据，获取智能健康建议。</p>
                <div class="mt-4 bg-blue-50 p-6 rounded-xl border border-blue-100 shadow-sm guide-container">
                  <div class="flex items-center mb-4">
                    <div class="guide-icon pulse-effect">
                      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" />
                      </svg>
                    </div>
                    <p class="font-semibold text-blue-700 ml-2 text-lg">操作指南</p>
                    <div class="flex-grow"></div>
                    <div class="hidden md:block text-xs text-blue-500 bg-blue-50 px-3 py-1 rounded-full border border-blue-200">依照步骤操作可获得最佳体验</div>
                  </div>
                  <div class="guide-steps">
                    <div class="guide-step">
                      <div class="step-number">1</div>
                      <div class="step-content">
                        <div class="step-icon">
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
                          </svg>
                        </div>
                        <div class="step-text">选择您要查看的宝宝</div>
                        <div class="step-hint">从您添加的宝宝列表中选择一个宝宝进行健康检测</div>
                      </div>
                    </div>
                    <div class="guide-step">
                      <div class="step-number">2</div>
                      <div class="step-content">
                        <div class="step-icon">
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.042 21.672L13.684 16.6m0 0l-2.51 2.225.569-9.47 5.227 7.917-3.286-.672zM12 2.25V4.5m5.834.166l-1.591 1.591M20.25 10.5H18M7.757 14.743l-1.59 1.59M6 10.5H3.75m4.007-4.243l-1.59-1.59" />
                          </svg>
                        </div>
                        <div class="step-text">点击模型的不同部位查看健康数据</div>
                        <div class="step-hint">3D模型上的高亮区域可点击，查看该部位的详细健康数据</div>
                      </div>
                    </div>
                    <div class="guide-step">
                      <div class="step-number">3</div>
                      <div class="step-content">
                        <div class="step-icon">
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10" />
                          </svg>
                        </div>
                        <div class="step-text">录入或更新身体部位信息</div>
                        <div class="step-hint">在表单中输入最新的健康数据，系统会自动分析变化趋势</div>
                      </div>
                    </div>
                    <div class="guide-step">
                      <div class="step-number">4</div>
                      <div class="step-content">
                        <div class="step-icon">
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09zM18.259 8.715L18 9.75l-.259-1.035a3.375 3.375 0 00-2.455-2.456L14.25 6l1.036-.259a3.375 3.375 0 002.455-2.456L18 2.25l.259 1.035a3.375 3.375 0 002.456 2.456L21.75 6l-1.035.259a3.375 3.375 0 00-2.456 2.456zM16.894 20.567L16.5 21.75l-.394-1.183a2.25 2.25 0 00-1.423-1.423L13.5 18.75l1.183-.394a2.25 2.25 0 001.423-1.423l.394-1.183.394 1.183a2.25 2.25 0 001.423 1.423l1.183.394-1.183.394a2.25 2.25 0 00-1.423 1.423z" />
                          </svg>
                        </div>
                        <div class="step-text">使用AI智能助手获取专业健康建议</div>
                        <div class="step-hint">基于宝宝数据分析，AI助手将提供个性化的健康建议和成长指导</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <router-view v-slot="{ Component }">
                <transition name="fade" mode="out-in">
                  <component :is="Component" />
                </transition>
              </router-view>
            </div>
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue'
  import { useRouter, useRoute } from 'vue-router'
  
  const router = useRouter()
  const route = useRoute()
  </script>
  
  <style scoped>
  .health-view {
    @apply min-h-screen bg-gradient-to-b from-gray-50 to-blue-50 flex flex-col;
  }
  
  .health-main {
    @apply flex-1 pt-8 pb-16;
    background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%233b82f6' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  }
  
  .main-container {
    @apply container mx-auto px-4 max-w-6xl;
  }
  
  .content-area {
    @apply flex-1;
  }
  
  .health-content {
    @apply bg-white rounded-2xl shadow-lg border border-gray-100 p-8 mb-6;
    box-shadow: 0 10px 30px -5px rgba(0, 0, 0, 0.1), 0 5px 15px -5px rgba(0, 0, 0, 0.05);
  }
  
  .welcome-section {
    @apply bg-gradient-to-r from-blue-50 to-indigo-50 p-6 rounded-xl border-l-4 border-blue-500;
    position: relative;
    overflow: hidden;
  }
  
  .welcome-section::before {
    content: '';
    position: absolute;
    top: -10px;
    right: -10px;
    width: 120px;
    height: 120px;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, rgba(59, 130, 246, 0) 70%);
    border-radius: 50%;
    z-index: 0;
  }
  
  .welcome-header {
    position: relative;
    display: inline-block;
  }
  
  .welcome-decoration {
    @apply bg-blue-500;
    height: 4px;
    width: 40px;
    border-radius: 2px;
    margin-bottom: 12px;
  }
  
  .guide-container {
    background-image: radial-gradient(circle at 10% 20%, rgba(242, 245, 250, 0.8) 0%, rgba(229, 235, 245, 0.9) 90%);
    position: relative;
    overflow: hidden;
  }
  
  .guide-container::before {
    content: '';
    position: absolute;
    top: -5px;
    right: -5px;
    width: 80px;
    height: 80px;
    background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
    opacity: 0.1;
    border-radius: 50%;
    z-index: 0;
  }
  
  .pulse-effect {
    animation: pulse 2s infinite;
  }
  
  @keyframes pulse {
    0% {
      transform: scale(1);
      opacity: 1;
    }
    50% {
      transform: scale(1.1);
      opacity: 0.8;
    }
    100% {
      transform: scale(1);
      opacity: 1;
    }
  }
  
  .guide-icon {
    @apply bg-blue-100 p-2 rounded-full text-blue-600;
  }
  
  .guide-steps {
    @apply space-y-4 mt-2;
  }
  
  .guide-step {
    @apply flex items-start p-2 rounded-lg transition-all duration-300 hover:bg-white hover:shadow-sm;
    border-left: 3px solid transparent;
  }
  
  .guide-step:hover {
    @apply transform translate-x-1 border-blue-500;
  }
  
  .step-number {
    @apply flex items-center justify-center w-7 h-7 rounded-full bg-blue-500 text-white font-bold text-sm mr-3;
    box-shadow: 0 2px 4px rgba(59, 130, 246, 0.3);
  }
  
  .step-content {
    @apply flex items-center flex-1;
  }
  
  .step-icon {
    @apply flex items-center justify-center w-8 h-8 rounded-full bg-blue-100 text-blue-600 mr-3;
  }
  
  .step-text {
    @apply text-blue-800 font-medium transition-all duration-300;
  }
  
  .step-hint {
    @apply hidden text-xs text-gray-500 bg-white px-3 py-1 rounded-lg ml-2;
    max-width: 0;
    overflow: hidden;
    white-space: nowrap;
    transition: all 0.3s ease;
  }
  
  .guide-step:hover .step-hint {
    @apply block ml-3;
    max-width: 300px;
    transition-delay: 0.1s;
  }
  
  .guide-step:hover .step-text {
    @apply text-blue-600 font-bold;
  }
  
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.3s ease;
  }
  
  .fade-enter-from {
    opacity: 0;
    transform: translateY(20px);
  }
  
  .fade-leave-to {
    opacity: 0;
    transform: translateY(-20px);
  }
  
  @media (max-width: 768px) {
    .health-content {
      @apply p-5;
    }
    
    .welcome-section {
      @apply p-4;
    }
  }
  </style> 