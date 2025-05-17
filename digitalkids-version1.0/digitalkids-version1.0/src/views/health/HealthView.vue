<template>
  <div class="health-page">
    <section class="banner-section">
      <div class="banner-content">
        <div class="banner-text">
          <h1 class="title">
            <span class="primary-text">数字化</span>
            <span class="secondary-text">健康管理</span>
          </h1>
          <p class="description">
            通过数据可视化技术，全方位展示孩子的成长轨迹，提供个性化的健康建议和发展规划
          </p>
          <div class="action-buttons">
            <button class="primary-button" @click="navigateTo('/health/3d')">
              开始体验
              <el-icon><ArrowRight /></el-icon>
            </button>
            <button class="secondary-button">
              了解更多
              <el-icon><InfoFilled /></el-icon>
            </button>
          </div>
        </div>
            
        <div class="banner-3d">
          <!-- Spline 3D模型 -->
          <div class="spline-wrapper">
            <div class="spline-box">
              <canvas ref="splineContainer" class="spline-viewer"></canvas>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <section class="features-section">
      <h2 class="section-title">健康检测</h2>
      <div class="features-grid">
        <div class="feature-card" @click="navigateTo('/health/3d')">
          <div class="feature-icon">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <h3 class="feature-title">3D模型检测</h3>
          <p class="feature-desc">通过3D模型，直观了解身体各部位健康状况</p>
        </div>
        
        <div class="feature-card" @click="navigateTo('/health/tongue-detection')">
          <div class="feature-icon">
            <el-icon><PictureFilled /></el-icon>
          </div>
          <h3 class="feature-title">舌苔检测</h3>
          <p class="feature-desc">上传舌象图片，分析舌苔特征，评估健康状况</p>
        </div>
        
        <div class="feature-card disabled">
          <div class="feature-icon">
            <el-icon><Loading /></el-icon>
          </div>
          <h3 class="feature-title">更多功能</h3>
          <p class="feature-desc">更多健康检测功能正在开发中...</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, onBeforeUnmount } from 'vue';
import { Application } from '@splinetool/runtime';
import { 
  ArrowRight, 
  InfoFilled,
  DataAnalysis,
  PictureFilled,
  Loading
} from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const splineContainer = ref<HTMLCanvasElement | null>(null);
let splineApp: any = null;

onMounted(() => {
  nextTick(() => {
    if (splineContainer.value) {
      splineApp = new Application(splineContainer.value);
      splineApp.load('/static/spline/spline008.splinecode');
    }
  });
});

onBeforeUnmount(() => {
  if (splineApp) {
    splineApp.dispose();
    splineApp = null;
  }
});

const navigateTo = (path: string) => {
  router.push(path);
};
</script>

<style scoped>
.health-page {
  width: 100%;
  background: #f5f9fc;
  color: #333;
}

.banner-section {
  padding: 80px 5% 60px;
  background: linear-gradient(135deg, #e0f2ff 0%, #fff 100%);
  overflow: hidden;
  position: relative;
}

.banner-content {
  display: flex;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  gap: 50px;
}

.banner-text {
  flex: 1;
  z-index: 2;
}

.title {
  font-size: 3.5rem;
  line-height: 1.1;
  margin-bottom: 20px;
  font-weight: 700;
}

.primary-text {
  color: #3452ff;
  display: block;
}

.secondary-text {
  color: #141520;
  display: block;
}

.description {
  font-size: 1.125rem;
  line-height: 1.6;
  color: #4a5568;
  margin-bottom: 30px;
  max-width: 90%;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.primary-button {
  background: #3452ff;
  color: white;
  border: none;
  border-radius: 50px;
  padding: 12px 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(52, 82, 255, 0.2);
  transition: all 0.3s ease;
}

.primary-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(52, 82, 255, 0.3);
}

.secondary-button {
  background: transparent;
  color: #141520;
  border: none;
  border-radius: 50px;
  padding: 12px 25px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.secondary-button:hover {
  color: #3452ff;
  background: rgba(52, 82, 255, 0.05);
}

.banner-3d {
  flex: 1;
  position: relative;
  height: 400px;
  min-width: 300px;
}

.spline-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
}

.spline-box {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  --spline-w: 300;
  --spline-h: 300;
}

.spline-viewer {
  width: 100%;
  height: 100%;
  display: block;
}

.features-section {
  padding: 60px 5%;
  max-width: 1200px;
  margin: 0 auto;
}

.section-title {
  text-align: center;
  font-size: 2rem;
  color: #141520;
  margin-bottom: 40px;
  position: relative;
}

.section-title:after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: #3452ff;
  border-radius: 2px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.feature-card.disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.feature-card.disabled:hover {
  transform: none;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.feature-icon {
  font-size: 48px;
  color: #3452ff;
  margin-bottom: 20px;
}

.feature-title {
  font-size: 1.25rem;
  color: #141520;
  margin-bottom: 10px;
}

.feature-desc {
  color: #666;
  line-height: 1.5;
}

@media (max-width: 992px) {
  .banner-content {
    flex-direction: column;
    gap: 30px;
  }
  
  .banner-text {
    text-align: center;
  }
  
  .description {
    max-width: 100%;
  }
  
  .action-buttons {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .title {
    font-size: 2.5rem;
  }
  
  .banner-section {
    padding: 60px 5% 40px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .title {
    font-size: 2rem;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 10px;
  }
  
  .primary-button, .secondary-button {
    width: 100%;
    justify-content: center;
  }
}
</style> 