<template>
  <div 
    class="glass-card rounded-2xl shadow-2xl overflow-hidden"
    :class="[className]" 
  > <!-- 使用类名数组（确保为空的安全性）-->
    <!-- 允许自定义内容的插入 -->
    <slot></slot>
  </div>
</template>
  
<script setup lang="ts">
  defineProps<{
    className?: string // 自定义类名
  }>();
</script>
  
<style scoped>
.glass-card {
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  background-color: rgba(255, 255, 255, 0.85);
  border: 1px solid rgba(255, 255, 255, 0.125);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
  transition: all 0.3s ease;
}

.glass-card:hover {
  backdrop-filter: blur(20px) saturate(200%);
  -webkit-backdrop-filter: blur(20px) saturate(200%);
  transform: translateY(-2px);
  box-shadow: 0 12px 40px 0 rgba(31, 38, 135, 0.1);
}
</style>