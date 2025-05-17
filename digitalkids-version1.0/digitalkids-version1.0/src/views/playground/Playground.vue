<template>
  <div class="playground-container">
    <section class="hero-section">
      <div class="container mx-auto px-4">
        <div class="hero-content">
          <div class="hero-title">
            <h1 class="main-title">
              <span class="gradient-text">拓展广场</span>
            </h1>
            <p class="description">
              探索多样化的互动体验，提升认知能力与创造力
            </p>
          </div>
        </div>
      </div>
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-shape shape-1"></div>
    </section>
    <section class="cards-section">
      <div class="container mx-auto px-4">
        <div class="cards-grid">
          <div class="feature-card tilt-card"
            @mouseenter="startTiltEffect($event)"
            @mouseleave="stopTiltEffect($event)"
            @mousemove="handleTiltMove($event)"
          >
            <div class="card-icon">
              <img src="https://img.icons8.com/color/96/000000/brain.png" alt="Brain" class="w-16 h-16">
            </div>
            <h3 class="card-title">心理测评问卷</h3>
            <p class="card-description">帮您做出正确的MBTI人格测试，发现真实的自己，提高自我认知</p>
            <router-link to="/playground/mbti" class="card-link">
              开始测试
              <i class="fas fa-chevron-right ml-1"></i>
            </router-link>
            <div class="card-border"></div>
          </div>

          <div class="feature-card tilt-card"
            @mouseenter="startTiltEffect($event)"
            @mouseleave="stopTiltEffect($event)"
            @mousemove="handleTiltMove($event)"
          >
            <div class="card-icon">
              <img src="https://img.icons8.com/color/96/000000/joystick.png" alt="Controller" class="w-16 h-16">
            </div>
            <h3 class="card-title">3D手势控制游戏</h3>
            <p class="card-description">锻炼孩子的左右手协调控制，探索几何体，提升空间感知能力</p>
            <a href="/playground/controller.html" class="card-link">
              开始游戏
              <i class="fas fa-chevron-right ml-1"></i>
            </a>
            <div class="card-border"></div>
          </div>

          <div class="feature-card tilt-card"
            @mouseenter="startTiltEffect($event)"
            @mouseleave="stopTiltEffect($event)"
            @mousemove="handleTiltMove($event)"
          >
            <div class="card-icon">
              <img src="https://img.icons8.com/color/96/000000/idea.png" alt="Idea" class="w-16 h-16">
            </div>
            <h3 class="card-title">更多精彩敬请期待</h3>
            <p class="card-description">我们正在开发更多互动体验，帮助孩子全面发展认知能力</p>
            <a href="#" class="card-link disabled">
              即将推出
              <i class="fas fa-chevron-right ml-1"></i>
            </a>
            <div class="card-border"></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const startTiltEffect = (event: MouseEvent) => {
  const target = event.currentTarget as HTMLElement;
  target.style.transition = 'transform 0.1s ease';
};

const stopTiltEffect = (event: MouseEvent) => {
  const target = event.currentTarget as HTMLElement;
  target.style.transition = 'transform 0.5s ease';
  target.style.transform = 'perspective(1000px) rotateX(0) rotateY(0)';
};

const handleTiltMove = (event: MouseEvent) => {
  const target = event.currentTarget as HTMLElement;
  const rect = target.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;
  
  const centerX = rect.width / 2;
  const centerY = rect.height / 2;
  
  const tiltX = (y - centerY) / 10;
  const tiltY = (centerX - x) / 10;
  
  target.style.transform = `perspective(1000px) rotateX(${tiltX}deg) rotateY(${tiltY}deg)`;
};

onMounted(() => {
  document.title = '拓展广场 - 数字儿童';
});
</script>

<style scoped>
.playground-container {
  min-height: 100vh;
  background-color: #f9fafb;
}

.hero-section {
  padding: 5rem 0 3rem;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
}

.hero-content {
  text-align: center;
  position: relative;
  z-index: 10;
}

.hero-title {
  margin-bottom: 2rem;
}

.main-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.gradient-text {
  background: linear-gradient(90deg, #3b82f6 0%, #8b5cf6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
}

.description {
  font-size: 1.25rem;
  color: #4b5563;
  max-width: 700px;
  margin: 0 auto;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(219,234,254,0.8) 0%, rgba(219,234,254,0) 70%);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: -50px;
}

.decoration-shape {
  position: absolute;
  background-color: rgba(219,234,254,0.4);
  border-radius: 30px;
}

.shape-1 {
  width: 100px;
  height: 100px;
  transform: rotate(45deg);
  top: 50px;
  right: 100px;
}

.cards-section {
  padding: 4rem 0;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  min-height: 320px;
  display: flex;
  flex-direction: column;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.feature-card:hover .card-border {
  opacity: 1;
}

.card-icon {
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: rgba(219, 234, 254, 0.4);
}

.card-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 1rem;
}

.card-description {
  font-size: 1rem;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  flex-grow: 1;
}

.card-link {
  display: inline-flex;
  align-items: center;
  font-weight: 600;
  color: #3b82f6;
  transition: color 0.3s ease;
  margin-top: auto;
}

.card-link:hover {
  color: #2563eb;
}

.card-link.disabled {
  color: #9ca3af;
  cursor: not-allowed;
}

.card-border {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #3b82f6 0%, #8b5cf6 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

@media (max-width: 768px) {
  .main-title {
    font-size: 2.25rem;
  }
  
  .description {
    font-size: 1rem;
  }
  
  .cards-grid {
    grid-template-columns: 1fr;
  }
}
</style>
