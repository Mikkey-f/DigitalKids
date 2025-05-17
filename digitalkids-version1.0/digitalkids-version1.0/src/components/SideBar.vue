<template>
  <div class="sidebar" :class="{ 'collapsed': isCollapsed }">
    <div class="sidebar-header">
      <div class="logo-container">
        <img v-if="!isCollapsed" src="/public/images/articles/膳食健康.jpg" alt="Logo" class="logo" />
        <img v-else src="/public/images/articles/膳食健康.jpg" alt="Logo" class="logo-small" />
      </div>
      <el-button class="toggle-btn" @click="toggleCollapse">
        <el-icon v-if="isCollapsed"><ArrowRight /></el-icon>
        <el-icon v-else><ArrowLeft /></el-icon>
      </el-button>
    </div>

    <div class="expand-sidebar-btn" title="展开更多">
      <div class="expand-btn-content">
        <el-icon><ArrowRight /></el-icon>
      </div>
      <div class="expand-btn-pulse"></div>
    </div>

    <div class="sidebar-menu">
      <router-link to="/home" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="首页">
        <el-icon><HomeFilled /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">首页</span>
        <div v-if="isCollapsed" class="icon-tooltip">首页</div>
      </router-link>

      <router-link to="/health" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="健康检测">
        <el-icon><DataLine /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">健康检测</span>
        <div v-if="isCollapsed" class="icon-tooltip">健康检测</div>
      </router-link>

      <router-link to="/health/3d" class="menu-item submenu-link" :class="{'menu-item-collapsed': isCollapsed}" title="3D展示">
        <el-icon><View /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">3D展示</span>
        <div v-if="isCollapsed" class="icon-tooltip">3D展示</div>
      </router-link>

      <router-link to="/health/tongue-detection" class="menu-item submenu-link" :class="{'menu-item-collapsed': isCollapsed}" title="舌苔检测">
        <el-icon><PictureFilled /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">舌苔检测</span>
        <div v-if="isCollapsed" class="icon-tooltip">舌苔检测</div>
      </router-link>

      <router-link to="/encyclopedia" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="育儿百科">
        <el-icon><Reading /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">育儿百科</span>
        <div v-if="isCollapsed" class="icon-tooltip">育儿百科</div>
      </router-link>

      <router-link to="/mall" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="优选商城">
        <el-icon><ShoppingCart /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">优选商城</span>
        <div v-if="isCollapsed" class="icon-tooltip">优选商城</div>
      </router-link>

      <router-link to="/mall/recommend" class="menu-item submenu-link" :class="{'menu-item-collapsed': isCollapsed}" title="推荐">
        <el-icon><Star /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">推荐</span>
        <div v-if="isCollapsed" class="icon-tooltip">推荐</div>
      </router-link>

      <router-link to="/mall/cart" class="menu-item submenu-link" :class="{'menu-item-collapsed': isCollapsed}" title="购物车">
        <el-icon><ShoppingCart /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">购物车</span>
        <div v-if="isCollapsed" class="icon-tooltip">购物车</div>
      </router-link>

      <router-link to="/playground" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="拓展广场">
        <el-icon><Monitor /></el-icon>
        <span v-show="!isCollapsed" class="menu-text">拓展广场</span>
        <div v-if="isCollapsed" class="icon-tooltip">拓展广场</div>
      </router-link>

      <template v-if="!isLoggedIn">
        <router-link to="/login" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="登录">
          <el-icon><User /></el-icon>
          <span v-show="!isCollapsed" class="menu-text">登录</span>
          <div v-if="isCollapsed" class="icon-tooltip">登录</div>
        </router-link>
      </template>
      <template v-else>
        <router-link to="/user" class="menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="个人中心">
          <el-icon><UserFilled /></el-icon>
          <span v-show="!isCollapsed" class="menu-text">个人中心</span>
          <div v-if="isCollapsed" class="icon-tooltip">个人中心</div>
        </router-link>

        <router-link to="/user/baby-management" class="menu-item submenu-link" :class="{'menu-item-collapsed': isCollapsed}" title="宝宝管理">
          <el-icon><Management /></el-icon>
          <span v-show="!isCollapsed" class="menu-text">宝宝管理</span>
          <div v-if="isCollapsed" class="icon-tooltip">宝宝管理</div>
        </router-link>

        <div @click="handleLogout" class="menu-item logout-menu-item" :class="{'menu-item-collapsed': isCollapsed}" title="退出登录">
          <el-icon><SwitchButton /></el-icon>
          <span v-show="!isCollapsed" class="menu-text">退出登录</span>
          <div v-if="isCollapsed" class="icon-tooltip">退出登录</div>
        </div>
      </template>
    </div>

    <div class="sidebar-footer">
      <div 
        class="assistant-toggle" 
        :class="{'assistant-toggle-collapsed': isCollapsed}" 
        @click="toggleAssistant"
        @mouseenter="isHoveringAssistant = true"
        @mouseleave="isHoveringAssistant = false"
      >
        <div v-if="showBubbleTip" class="bubble-tip">
          <div class="bubble-content">
            小蓝在这里启用/关闭哦
            <button class="close-bubble" @click.stop="closeBubbleTip">
              <el-icon><Close /></el-icon>
            </button>
          </div>
          <div class="bubble-arrow"></div>
        </div>
        
        <div v-if="isHoveringAssistant" class="hover-tip">
          按下即可{{ showAssistant ? '关闭' : '启用' }}小蓝哦
        </div>
        
        <el-icon :class="{'assistant-active': showAssistant, 'icon-pulse': isButtonAnimating}"><ChatRound /></el-icon>
        <span v-show="!isCollapsed" class="toggle-text">{{ showAssistant ? '隐藏小助手' : '显示小助手' }}</span>
        <div v-if="!isCollapsed" class="toggle-switch" :class="{'switch-animating': isButtonAnimating}">
          <div class="switch-track" :class="{'switch-active': showAssistant}">
            <div class="switch-thumb"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import { 
  ArrowLeft, 
  ArrowRight, 
  ArrowDown, 
  HomeFilled, 
  DataLine, 
  Reading, 
  ShoppingCart,
  ChatRound,
  Close,
  Plus,
  User,
  UserFilled,
  Setting,
  Management,
  Menu,
  Star,
  PictureFilled,
  Document,
  Message,
  SwitchButton,
  Monitor,
  View
} from '@element-plus/icons-vue';

const isCollapsed = ref(true);
const showAssistant = ref(true);
const showBubbleTip = ref(false);
const isHoveringAssistant = ref(false);
const isButtonAnimating = ref(false);
const isLoggedIn = ref(true);
const unreadMessages = ref(3);
const emit = defineEmits(['update:assistant']);

onMounted(() => {
  const bubbleClosed = localStorage.getItem('assistantBubbleClosed');
  showBubbleTip.value = bubbleClosed !== 'true';
});

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value;
};

const toggleAssistant = () => {
  showAssistant.value = !showAssistant.value;
  isButtonAnimating.value = true;
  emit('update:assistant', showAssistant.value);
  
  setTimeout(() => {
    isButtonAnimating.value = false;
  }, 300);
};

const handleLogout = () => {
  isLoggedIn.value = false;
};

const closeBubbleTip = () => {
  showBubbleTip.value = false;
  localStorage.setItem('assistantBubbleClosed', 'true');
};

watch(showAssistant, (newValue) => {
  emit('update:assistant', newValue);
});
</script>

<style scoped>
.sidebar {
  min-height: 100vh;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: width 0.3s ease;
  width: 220px; 
  position: relative;
  padding-top: 0;
  border-right: 1px solid rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.logo {
  height: 30px;
  width: auto;
  object-fit: contain;
}

.logo-small {
  width: 28px;
  height: 28px;
  object-fit: cover;
  border-radius: 6px;
}

.toggle-btn {
  padding: 6px;
  width: 28px;
  height: 28px;
  border: none;
  background: #f5f7fa;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #606266;
  cursor: pointer;
  transition: all 0.2s ease;
}

.toggle-btn:hover {
  background: #e6e8eb;
  color: #409eff;
}

.expand-sidebar-btn {
  position: absolute;
  right: -14px;
  top: 120px;
  width: 28px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.expand-btn-content {
  width: 28px;
  height: 28px;
  background: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #409eff;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  border: 1px solid rgba(64, 158, 255, 0.15);
}

.expand-btn-content:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.2);
  color: #66b1ff;
}

.expand-btn-content .el-icon {
  font-size: 14px;
  transition: transform 0.3s ease;
}

.expand-btn-content:hover .el-icon {
  transform: translateX(2px);
}

.expand-btn-pulse {
  position: absolute;
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: rgba(64, 158, 255, 0.1);
  opacity: 0;
  transform: scale(0.8);
  animation: pulse 2s infinite;
  pointer-events: none;
}

@keyframes pulse {
  0% {
    transform: scale(0.95);
    opacity: 0;
  }
  50% {
    opacity: 0.2;
  }
  100% {
    transform: scale(1.2);
    opacity: 0;
  }
}

.sidebar-menu {
  flex: 1;
  padding: 1rem 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 0.8rem 1.25rem;
  margin: 0.15rem 0.75rem;
  cursor: pointer;
  color: #606266;
  text-decoration: none;
  transition: all 0.2s ease;
  border-radius: 8px;
  position: relative;
  overflow: visible;
  z-index: 50;
}

.menu-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
}

.menu-item.active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: 500;
}

.menu-item-collapsed {
  padding: 0.8rem;
  justify-content: center;
  margin: 0.15rem 0.3rem;
}

.menu-text {
  margin-left: 12px;
  font-size: 0.95rem;
}

.submenu-link {
  margin-left: 1.5rem;
  font-size: 0.9rem;
  padding: 0.65rem 1.25rem;
}

.submenu-link.menu-item-collapsed {
  margin-left: 0.3rem;
}

.icon-tooltip {
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  font-size: 0.75rem;
  padding: 4px 8px;
  border-radius: 4px;
  white-space: nowrap;
  margin-left: 10px;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
  z-index: 100;
}

.icon-tooltip::before {
  content: '';
  position: absolute;
  left: -4px;
  top: 50%;
  transform: translateY(-50%) rotate(45deg);
  width: 8px;
  height: 8px;
  background: rgba(0, 0, 0, 0.8);
}

.menu-item:hover .icon-tooltip {
  opacity: 1;
}

.menu-badge {
  background-color: #f56c6c;
  color: white;
  font-size: 0.7rem;
  font-weight: bold;
  height: 16px;
  min-width: 16px;
  line-height: 16px;
  text-align: center;
  border-radius: 8px;
  padding: 0 5px;
  margin-left: auto;
}

.collapsed-badge {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #f56c6c;
  color: white;
  font-size: 0.6rem;
  font-weight: bold;
  height: 14px;
  min-width: 14px;
  line-height: 14px;
  text-align: center;
  border-radius: 7px;
  padding: 0 3px;
  transform: translate(30%, -30%);
}

.el-icon {
  font-size: 1.05rem;
}

.logout-menu-item {
  color: #606266;
}

.logout-menu-item:hover {
  color: #f56c6c;
  background-color: #fef0f0;
}

.sidebar-footer {
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  padding: 0.75rem;
}

.assistant-toggle {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  background-color: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  transform: translateY(0);
}

.assistant-toggle:hover {
  background-color: #ecf5ff;
}

.assistant-toggle:active {
  transform: scale(0.97);
}

.assistant-toggle-collapsed {
  justify-content: center;
  padding: 0.75rem 0.5rem;
}

.assistant-active {
  color: #409eff;
}

.icon-pulse {
  animation: iconPulse 0.3s ease;
}

@keyframes iconPulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.35); }
  100% { transform: scale(1); }
}

.toggle-text {
  margin-left: 10px;
  font-size: 0.9rem;
  color: #606266;
  flex: 1;
  transition: color 0.3s ease;
}

.toggle-switch {
  width: 36px;
  min-width: 36px;
  position: relative;
}

.switch-animating::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 20px;
  height: 20px;
  transform: translate(-50%, -50%) scale(0);
  background: radial-gradient(circle, rgba(64,158,255,0.3) 0%, rgba(255,255,255,0) 70%);
  border-radius: 50%;
  z-index: 0;
  animation: rippleEffect 0.4s ease-out;
}

@keyframes rippleEffect {
  0% { transform: translate(-50%, -50%) scale(0); opacity: 1; }
  100% { transform: translate(-50%, -50%) scale(3); opacity: 0; }
}

.switch-track {
  width: 36px;
  height: 18px;
  border-radius: 10px;
  background-color: #dcdfe6;
  position: relative;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.switch-active {
  background-color: #409eff;
}

.switch-thumb {
  position: absolute;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background-color: white;
  top: 2px;
  left: 2px;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.switch-active .switch-thumb {
  left: 20px;
}

.bubble-tip {
  position: absolute;
  top: 0;
  right: -190px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  padding: 8px 12px;
  font-size: 0.85rem;
  color: #606266;
  width: 170px;
  z-index: 100;
  animation: slideIn 0.5s ease;
}

@keyframes slideIn {
  0% { transform: translateX(10px); opacity: 0; }
  100% { transform: translateX(0); opacity: 1; }
}

.bubble-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.close-bubble {
  background: none;
  border: none;
  color: #909399;
  cursor: pointer;
  padding: 2px;
  margin-left: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-bubble:hover {
  color: #f56c6c;
}

.bubble-arrow {
  position: absolute;
  width: 10px;
  height: 10px;
  background: white;
  left: -5px;
  top: 12px;
  transform: rotate(45deg);
  box-shadow: -2px 2px 4px rgba(0, 0, 0, 0.07);
}

.hover-tip {
  position: absolute;
  top: 0;
  right: -140px;
  background: #303133;
  color: white;
  font-size: 0.8rem;
  padding: 5px 10px;
  border-radius: 4px;
  white-space: nowrap;
  z-index: 100;
  opacity: 0.9;
}

.hover-tip::after {
  content: '';
  position: absolute;
  left: -4px;
  top: 10px;
  transform: rotate(45deg);
  width: 8px;
  height: 8px;
  background: #303133;
}
</style> 