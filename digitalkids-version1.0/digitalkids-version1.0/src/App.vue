<template>
  <div class="app-container">
    <template v-if="!isAuthPage">
      <NavBar class="nav-bar" />
      
      <div class="content-container">
        <SideBar class="side-bar" @update:assistant="updateAssistantVisibility" />
        
        <div class="main-content">
          <router-view></router-view> 
        </div>
      </div>
    </template>
    
    <template v-else>
      <router-view></router-view>
    </template>
    <Live2DAssistant v-if="!isAuthPage && showAssistant" />
    <!-- <MouseFollower /> -->
  </div>
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent, ref } from 'vue';
import { useRoute } from 'vue-router';
import NavBar from '@/components/NavBar.vue';
import Live2DAssistant from '@/components/assistant/Live2DAssistant.vue';
import MouseFollower from '@/components/MouseFollower.vue';

const showAssistant = ref(true);

const updateAssistantVisibility = (value: boolean) => {
  showAssistant.value = value;
};

const SideBar = defineAsyncComponent(() => 
  import('@/components/SideBar.vue')
);
const route = useRoute();

// 判断当前路由是否为认证相关页面
const isAuthPage = computed(() => {
  // 数组囊括所有认证界面
  const authRoutes = ['/login', '/register', '/forgot-password', '/reset-password', '/'];
  return authRoutes.includes(route.path);
});
</script>

<style>
@import './styles/main.scss'; 

body {
  margin: 0;
  min-height: 100vh;
  background: linear-gradient(
    125deg,
    #2a0845,
    #4a00e0,
    #1e3c72,
    #2a5298
  );
  background-size: 400% 400%;
  overflow: auto;
  position: relative;
}

.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.nav-bar {
  width: 100%;
}

.content-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.side-bar {
  width: 240px;
  transition: width 0.3s;
  z-index: 40;
}

.side-bar.collapsed {
  width: 64px;
}

.main-content {
  flex: 1;
  overflow: auto;
  padding: 20px;
  margin-top: 56px; /* NavBar的高度 */
}
</style>