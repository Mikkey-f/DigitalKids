<template>
  <nav class="nav-container">
    <div class="nav-content">
      <div class="logo-section">
        <router-link to="/home" class="logo-link">
          <img src="@/assets/智塑童程.png" alt="智塑童程" class="logo-image" />
        </router-link>
      </div>
      <div class="search-section">
        <SearchBox placeholder="搜索..." @search="handleSearch" />
      </div>
      <div class="nav-links">
        <router-link to="/home" class="nav-link" active-class="nav-link-active"
          >首页</router-link
        >
        <div
          class="dropdown-container"
          @mouseenter="showHealthMenu = true"
          @mouseleave="showHealthMenu = false"
        >
          <router-link
            to="/health"
            class="nav-link"
            active-class="nav-link-active"
          >
            健康检测
            <el-icon
              class="dropdown-icon"
              :class="{ 'rotate-180': showHealthMenu }"
              ><ArrowDown
            /></el-icon>
          </router-link>
          <div v-show="showHealthMenu" class="dropdown-menu">
            <router-link to="/health/3d" class="dropdown-item">
              <el-icon><View /></el-icon>
              3D展示
            </router-link>
            <router-link to="/health/tongue-detection" class="dropdown-item">
              <el-icon><PictureFilled /></el-icon>
              舌苔检测
            </router-link>
          </div>
        </div>
        <router-link
          to="/encyclopedia"
          class="nav-link"
          active-class="nav-link-active"
          >育儿百科</router-link
        >
        <div
          class="dropdown-container"
          @mouseenter="showMallMenu = true"
          @mouseleave="showMallMenu = false"
        >
          <router-link
            to="/mall"
            class="nav-link"
            active-class="nav-link-active"
          >
            优选商城
            <el-icon
              class="dropdown-icon"
              :class="{ 'rotate-180': showMallMenu }"
              ><ArrowDown
            /></el-icon>
          </router-link>
          <div v-show="showMallMenu" class="dropdown-menu">
            <router-link to="/mall/recommend" class="dropdown-item">
              <el-icon><Star /></el-icon>
              推荐
            </router-link>
            <router-link to="/mall/category" class="dropdown-item">
              <el-icon><Menu /></el-icon>
              分类
            </router-link>
            <router-link to="/mall/cart" class="dropdown-item">
              <el-icon><ShoppingCart /></el-icon>
              购物车
            </router-link>
            <router-link to="/mall/orders" class="dropdown-item">
              <el-icon><Document /></el-icon>
              我的订单
            </router-link>
          </div>
        </div>
        <router-link
          to="/playground"
          class="nav-link"
          active-class="nav-link-active"
          >拓展广场</router-link
        >
        <router-link v-if="!isLoggedIn" to="/login" class="login-btn">登录</router-link>
        <div
          v-else
          class="dropdown-container user-center"
          @mouseenter="showUserMenu = true"
          @mouseleave="showUserMenu = false"
        >
          <div class="user-avatar" :class="{ 'active': showUserMenu }">
            <el-icon v-if="!userAvatar"><User /></el-icon>
            <img v-else :src="userAvatar" alt="用户头像" />
          </div>
          <div v-show="showUserMenu" class="dropdown-menu user-menu">
            <router-link to="/user" class="dropdown-item">
              <el-icon><UserFilled /></el-icon>
              个人资料
            </router-link>
            <router-link to="/user/baby-management" class="dropdown-item">
              <el-icon><Management /></el-icon>
              宝宝管理
            </router-link>
            <router-link to="/user/settings" class="dropdown-item">
              <el-icon><Setting /></el-icon>
              账号设置
            </router-link>
            <router-link to="/user/messages" class="dropdown-item">
              <el-icon><Message /></el-icon>
              消息通知
              <span v-if="unreadMessages" class="badge">{{ unreadMessages }}</span>
            </router-link>
            <router-link v-if="isAdmin" to="/admin" target="_blank" class="dropdown-item admin-item">
              <el-icon><Setting /></el-icon>
              管理后台
            </router-link>
            <div class="divider"></div>
            <div @click="handleLogout" class="dropdown-item logout-item">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import SearchBox from "./SearchBox.vue";
import {
  ArrowDown,
  DataLine,
  View,
  Document,
  Timer,
  User,
  UserFilled,
  Setting,
  Message,
  SwitchButton,
  Management,
  Menu,
  ShoppingCart,
  Star,
  PictureFilled
} from "@element-plus/icons-vue";

const showHealthMenu = ref(false);
const showUserMenu = ref(false);
const showMallMenu = ref(false);
const isLoggedIn = ref(true);
const userAvatar = ref('');
const unreadMessages = ref(3);
const isAdmin = ref(true);

const checkUserRole = () => {
  const userInfo = localStorage.getItem('userInfo');
  if (userInfo) {
    try {
      const parsedUserInfo = JSON.parse(userInfo);
      if (parsedUserInfo.role === 'admin' || 
          (parsedUserInfo.name && parsedUserInfo.name.toLowerCase().includes('admin')) ||
          (parsedUserInfo.username && parsedUserInfo.username.toLowerCase().includes('admin'))) {
        isAdmin.value = true;
      }
    } catch (error) {
      isAdmin.value = true;
    }
  } else {
    isAdmin.value = true;
  }
};

const handleSearch = (query: string) => {
  console.log("Search query:", query);
};

const handleLogout = () => {
  isLoggedIn.value = false;
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');
};

onMounted(() => {
  checkUserRole();
});
</script>

<style scoped>
.nav-container {
  @apply fixed top-0 left-0 right-0 bg-white z-50;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 64px;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.95);
}

.nav-content {
  @apply max-w-7xl mx-auto px-4 flex items-center justify-between;
  height: 100%;
}

.logo-section {
  @apply flex items-center;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-image {
  height: 80px;
  width: auto;
  object-fit: contain;
}

.search-section {
  @apply flex-1 max-w-xl mx-6;
}

.nav-links {
  @apply flex items-center space-x-6;
}

.nav-link {
  @apply text-gray-600 hover:text-blue-600 text-sm font-medium transition-colors flex items-center;
  padding: 0.5rem 0;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #3b82f6, #6366f1);
  transition: all 0.3s;
  transform: translateX(-50%);
  opacity: 0;
}

.nav-link:hover::after {
  width: 100%;
  opacity: 1;
}

.nav-link-active {
  @apply text-blue-600 font-semibold;
}

.nav-link-active::after {
  width: 100%;
  opacity: 1;
}

.login-btn {
  @apply px-5 py-2 bg-gradient-to-r from-blue-500 to-indigo-600 text-white text-sm font-medium rounded-full hover:shadow-lg transition-all;
  text-decoration: none;
  display: inline-block;
  text-align: center;
  transform: translateY(0);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.3);
}

.dropdown-container {
  @apply relative inline-block;
}

.dropdown-icon {
  @apply ml-1 w-4 h-4 transition-transform duration-200;
}

.dropdown-menu {
  @apply absolute left-0 w-48 bg-white rounded-lg shadow-lg py-2
         transform transition-all duration-200 z-50;
  top: calc(100% + 0.5rem);
  margin-top: 0;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.user-menu {
  right: 0;
  left: auto;
  width: 180px;
}

.dropdown-container::before {
  content: "";
  @apply absolute w-full;
  height: 20px;
  bottom: -20px;
}

.dropdown-item {
  @apply flex items-center px-4 py-2.5 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600
         transition-colors space-x-2 cursor-pointer;
}

.dropdown-item .el-icon {
  @apply w-4 h-4 mr-2;
}

.dropdown-menu .dropdown-item + .dropdown-item {
  @apply mt-0;
}

.dropdown-menu {
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  transition: opacity 0.3s ease, transform 0.3s ease, visibility 0.3s;
}

.dropdown-container:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.nav-link:hover {
  @apply text-blue-600;
}

.dropdown-container:hover .nav-link {
  @apply text-blue-600;
}

.user-center {
  margin-left: 6px;
}

.user-avatar {
  @apply flex items-center justify-center w-9 h-9 rounded-full bg-gradient-to-r from-blue-400 to-indigo-500 text-white cursor-pointer transition-all;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.user-avatar:hover, .user-avatar.active {
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
  transform: translateY(-2px);
}

.user-avatar img {
  @apply w-full h-full rounded-full object-cover;
}

.user-avatar .el-icon {
  @apply w-5 h-5;
}

.divider {
  height: 1px;
  background-color: rgba(0, 0, 0, 0.06);
  margin: 6px 0;
}

.badge {
  @apply ml-2 flex items-center justify-center min-w-[18px] h-[18px] text-xs text-white bg-red-500 rounded-full px-1;
}

.logout-item {
  @apply text-gray-600;
}

.logout-item:hover {
  @apply text-red-500 bg-red-50;
}
</style>
