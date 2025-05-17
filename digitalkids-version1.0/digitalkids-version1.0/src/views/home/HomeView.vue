<template>
  <div class="home-container">
    <!-- 仿Vue首页 -->
    <section class="hero-section">
      <div id="particles-background" class="particles-background" ref="particlesContainer"></div>
      <div class="container mx-auto px-4">
        <div class="hero-content">
          <div class="left-column">
            <div class="hero-badge">
              <span>创新的数字化健康管理</span>
            </div>
            <h1 class="main-title">
              <span class="gradient-text">数字儿童</span>
              <span class="subtitle">智能健康管理平台</span>
            </h1>
            <p class="description">
              以先进的数字技术赋能儿童健康管理，让每个孩子都能享有智能、精准、便捷的健康呵护。
            </p>
            <div class="cta-buttons">
              <router-link to="/health/data" class="primary-button">
                开始探索
                <i class="fas fa-arrow-right ml-2"></i>
              </router-link>
              <router-link to="/assistant" class="secondary-button">
                了解更多
              </router-link>
            </div>
            <div class="hero-stats">
              <div class="stat-item">
                <span class="stat-value">98%</span>
                <span class="stat-label">用户满意度</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat-item">
                <span class="stat-value">50+</span>
                <span class="stat-label">专业医师</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat-item">
                <span class="stat-value">24/7</span>
                <span class="stat-label">全天候服务</span>
              </div>
            </div>
          </div>
          <div class="right-column">
            <div class="hero-image">
            </div>
            <div class="floating-card card-1">
              <i class="fas fa-heartbeat"></i>
              <span>实时健康监测</span>
            </div>
            <div class="floating-card card-2">
              <i class="fas fa-brain"></i>
              <span>智能数据分析</span>
            </div>
            <div class="floating-card card-3">
              <i class="fas fa-shield-alt"></i>
              <span>隐私数据保护</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
      <div class="decoration-shape shape-1"></div>
      <div class="decoration-shape shape-2"></div>
    </section>

    <section class="features-section">
      <div class="container mx-auto px-4">
        <div class="section-header">
          <span class="eyebrow">专为儿童设计</span>
          <h2 class="section-title">多维度健康管理</h2>
          <p class="section-desc">
            集成多种功能模块，为儿童提供全方位的健康管理服务
          </p>
        </div>
        
        <div class="feature-cards">
          <div 
            v-for="feature in features" 
            :key="feature.id"
            class="feature-card"
            :class="[feature.class, 'tilt-card']"
            @mouseenter="startTiltEffect($event)"
            @mouseleave="stopTiltEffect($event)"
            @mousemove="handleTiltMove($event)"
          >
            <div class="card-icon">
              <div 
                class="lottie-container" 
                :ref="el => { if(el) lottieRefs[feature.id] = el }"
              ></div>
            </div>
            <span class="feature-tag">{{ feature.tag }}</span>
            <h3 class="card-title">{{ feature.title }}</h3>
            <p class="card-description">{{ feature.description }}</p>
            <router-link :to="feature.link" class="card-link">
              详细了解
              <i class="fas fa-chevron-right ml-1"></i>
            </router-link>
            <div class="card-border"></div>
          </div>
        </div>
      </div>

      <div class="features-decoration-1"></div>
      <div class="features-decoration-2"></div>
    </section>

    <section class="articles-section">
      <div class="container mx-auto px-4">
        <div class="section-header">
          <span class="eyebrow">精选内容</span>
          <h2 class="section-title">推荐阅读</h2>
          <p class="section-desc">
            探索专业育儿知识与健康管理技巧，助力孩子健康成长
          </p>
        </div>
        
        <recommended-articles />
      </div>
      
      <div class="articles-decoration-1"></div>
      <div class="articles-decoration-2"></div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import gsap from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import RecommendedArticles from '@/components/home/RecommendedArticles.vue';

gsap.registerPlugin(ScrollTrigger);

const lottieRefs = ref<Record<number, any>>({});
const particlesContainer = ref<HTMLElement | null>(null);

const features = ref([
  {
    id: 1,
    title: '健康档案',
    description: '智能化管理儿童健康记录，实时追踪成长轨迹，定制个性化健康计划。',
    link: '/health/data',
    class: 'feature-health',
    animationPath: '/animations/health-record.json',
    tag: '基础功能'
  },
  {
    id: 2,
    title: '在线问诊',
    description: '连接专业儿科医生，随时获取专业医疗建议，快速解答健康疑问。',
    link: '/assistant',
    class: 'feature-consult',
    animationPath: '/animations/online-doctor.json',
    tag: '专业服务'
  },
  {
    id: 3,
    title: '健康监测',
    description: '全方位监测儿童健康指标，预防胜于治疗，早期发现健康隐患。',
    link: '/health/3d',
    class: 'feature-monitor',
    animationPath: '/animations/health-monitor.json',
    tag: '实时监控'
  },
  {
    id: 4,
    title: '营养管理',
    description: '科学合理的膳食规划，均衡饮食指导，辅助孩子健康成长。',
    link: '/health/nutrition',
    class: 'feature-nutrition',
    animationPath: '/animations/nutrition.json',
    tag: '个性定制'
  }
]);

const handleScrollAnimation = () => {
  const sections = document.querySelectorAll<HTMLElement>('section:not(.hero-section)');
  
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, { threshold: 0.15 });
  
  sections.forEach(section => {
    observer.observe(section);
  });
};

onMounted(() => {
  try {
    createFallbackParticles();
    initScrollAnimations();
    handleScrollAnimation();
    features.value.forEach(feature => {
      if (lottieRefs.value[feature.id]) {
        createFeatureFallback(feature.id);
      }
    });
  } catch (error) {
  }
});

const createFeatureFallback = (featureId: number) => {
  if (!lottieRefs.value[featureId]) return;
  const iconClass = getFeatureIcon(featureId);
  lottieRefs.value[featureId].innerHTML = `
    <div class="feature-fallback-icon">
      <i class="${iconClass} fa-3x"></i>
    </div>
  `;
};

const createFallbackParticles = () => {
  if (!particlesContainer.value) return;
  particlesContainer.value.innerHTML = '';
  for (let i = 0; i < 50; i++) {
    const particle = document.createElement('div');
    particle.className = 'fallback-particle';
    const size = Math.random() * 6 + 2;
    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;
    particle.style.left = `${Math.random() * 100}%`;
    particle.style.top = `${Math.random() * 100}%`;
    
    const delay = Math.random() * 8;
    const duration = Math.random() * 8 + 12;
    particle.style.animationDelay = `${delay}s`;
    particle.style.animationDuration = `${duration}s`;
    particle.style.opacity = `${Math.random() * 0.5 + 0.1}`;
    
    particlesContainer.value.appendChild(particle);
  }
};

const getFeatureIcon = (featureId: number): string => {
  switch (featureId) {
    case 1: return 'fas fa-file-medical';
    case 2: return 'fas fa-user-md';
    case 3: return 'fas fa-heartbeat';
    case 4: return 'fas fa-utensils';
    default: return 'fas fa-star';
  }
};

const initScrollAnimations = () => {
  try {
    gsap.from('.hero-content .left-column', {
      x: -50,
      opacity: 0,
      duration: 1,
      delay: 0.2,
    });
    
    gsap.from('.hero-content .right-column', {
      x: 50,
      opacity: 0,
      duration: 1,
      delay: 0.4,
    });
  } catch (error) {
    console.error('Animation error:', error);
  }
};

const startTiltEffect = (event: MouseEvent) => {
  try {
    const card = event.currentTarget as HTMLElement;
    if (!card) return;
    card.style.transformOrigin = 'center center';
    handleTiltMove(event);
  } catch (error) {
    console.error('Tilt effect error:', error);
  }
};

const stopTiltEffect = (event: MouseEvent) => {
  try {
    const card = event.currentTarget as HTMLElement;
    if (!card) return;
    gsap.to(card, {
      rotateX: 0,
      rotateY: 0,
      duration: 0.5,
      ease: 'power2.out'
    });
  } catch (error) {
    console.error('Tilt reset error:', error);
  }
};

const handleTiltMove = (event: MouseEvent) => {
  try {
    const card = event.currentTarget as HTMLElement;
    if (!card) return;
    const rect = card.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    const centerX = rect.width / 2;
    const centerY = rect.height / 2;
    
    const tiltX = (y - centerY) / 10;
    const tiltY = (centerX - x) / 10;
    
    gsap.to(card, {
      rotateX: tiltX,
      rotateY: tiltY,
      duration: 0.5,
      ease: 'power2.out'
    });
  } catch (error) {
    console.error('Tilt move error:', error);
  }
};

onUnmounted(() => {
  try {
    if (ScrollTrigger && typeof ScrollTrigger.getAll === 'function') {
      ScrollTrigger.getAll().forEach(trigger => trigger.kill());
    }
  } catch (error) {
    console.error('Failed to clean up resources:', error);
  }
});
</script>

<style scoped>
.home-container {
  overflow-x: hidden;
}

.container {
  max-width: 1200px;
}

section {
  padding: 6rem 0;
  position: relative;
  overflow: hidden;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease, transform 0.8s ease;
}

section.visible {
  opacity: 1;
  transform: translateY(0);
}

.hero-section {
  background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
  padding: 140px 0 100px;
  position: relative;
  overflow: hidden;
  opacity: 1;
  transform: none;
  transition: none;
}

.hero-content {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  position: relative;
  z-index: 2;
}

.left-column {
  flex: 1;
  min-width: 300px;
  padding-right: 20px;
}

.right-column {
  flex: 1;
  min-width: 300px;
  display: flex;
  justify-content: center;
  position: relative;
}

.hero-image {
  max-width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.hero-badge {
  display: inline-flex;
  background: rgba(65, 184, 131, 0.1);
  color: #41B883;
  padding: 0.5rem 1rem;
  border-radius: 50px;
  font-weight: 600;
  font-size: 0.875rem;
  margin-bottom: 1.5rem;
  border: 1px solid rgba(65, 184, 131, 0.2);
}

.main-title {
  font-size: 3.5rem;
  line-height: 1.1;
  font-weight: 800;
  margin-bottom: 1.5rem;
}

.subtitle {
  font-size: 1.75rem;
  color: #4b5563;
  font-weight: 600;
  margin-top: 0.5rem;
  display: block;
}

.description {
  font-size: 1.125rem;
  color: #6b7280;
  margin-bottom: 2rem;
  max-width: 500px;
  line-height: 1.6;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 2rem;
}

.primary-button {
  background: linear-gradient(90deg, #41B883 0%, #34495E 100%);
  color: white;
  padding: 0.85rem 1.75rem;
  border-radius: 10px;
  font-weight: 600;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(65, 184, 131, 0.25);
}

.primary-button:hover {
  box-shadow: 0 6px 15px rgba(65, 184, 131, 0.4);
  transform: translateY(-3px);
}

.secondary-button {
  background: white;
  color: #41B883;
  padding: 0.85rem 1.75rem;
  border-radius: 10px;
  font-weight: 600;
  text-decoration: none;
  border: 2px solid #e5e7eb;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.secondary-button:hover {
  border-color: #41B883;
  background: rgba(65, 184, 131, 0.05);
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.08);
}

.hero-stats {
  display: flex;
  align-items: center;
  background: white;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05);
  max-width: fit-content;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 1rem;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.75rem;
  color: #6b7280;
  white-space: nowrap;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #e5e7eb;
}

/* 浮动 */
.floating-card {
  position: absolute;
  background: white;
  padding: 0.75rem 1rem;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 0.75rem;
  z-index: 3;
}

.floating-card i {
  color: #41B883;
  font-size: 1.25rem;
}

.floating-card span {
  font-weight: 600;
  font-size: 0.875rem;
  color: #4b5563;
}

.card-1 {
  top: 25%;
  right: 10%;
  animation: float 6s ease-in-out infinite;
}

.card-2 {
  top: 55%;
  left: 10%;
  animation: float 8s ease-in-out infinite 1s;
}

.card-3 {
  bottom: 20%;
  right: 15%;
  animation: float 7s ease-in-out infinite 0.5s;
}

@keyframes float {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0px); }
}

.particles-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
}

.fallback-particle {
  position: absolute;
  background: #41B883;
  border-radius: 50%;
  opacity: 0.5;
  animation: float-particle 20s infinite ease-in-out;
}

@keyframes float-particle {
  0% { transform: translate(0, 0); }
  25% { transform: translate(30px, 40px); }
  50% { transform: translate(60px, 0px); }
  75% { transform: translate(20px, 60px); }
  100% { transform: translate(0, 0); }
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(65, 184, 131, 0.2) 0%, rgba(65, 184, 131, 0) 70%);
}

.decoration-shape {
  position: absolute;
  z-index: 1;
}

.shape-1 {
  top: 10%;
  right: 5%;
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, rgba(65, 184, 131, 0.05) 0%, rgba(52, 73, 94, 0.05) 100%);
  border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
}

.shape-2 {
  bottom: 10%;
  left: 5%;
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, rgba(52, 73, 94, 0.05) 0%, rgba(65, 184, 131, 0.05) 100%);
  border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: 10%;
}

.circle-2 {
  width: 500px;
  height: 500px;
  bottom: -250px;
  left: 0%;
}

.circle-3 {
  width: 400px;
  height: 400px;
  bottom: 50%;
  right: -200px;
}

.features-section {
  position: relative;
  background-color: white;
  z-index: 2;
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border: 1px solid #f3f4f6;
  transform-style: preserve-3d;
  perspective: 1000px;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border-color: rgba(65, 184, 131, 0.2);
}

.feature-tag {
  display: inline-block;
  padding: 0.3rem 0.8rem;
  background: rgba(65, 184, 131, 0.1);
  color: #41B883;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.card-icon {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
}

.lottie-container {
  width: 80px;
  height: 80px;
}

.feature-fallback-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #41B883;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  color: #111827;
}

.card-description {
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  flex-grow: 1;
}

.card-link {
  display: inline-flex;
  align-items: center;
  color: #41B883;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.2s ease;
}

.card-link:hover {
  color: #34495E;
}

.card-border {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  width: 0;
  background: linear-gradient(90deg, #41B883 0%, #34495E 100%);
  transition: width 0.3s ease;
}

.feature-card:hover .card-border {
  width: 100%;
}

.feature-card.tilt-card {
  will-change: transform;
}

.articles-section {
  position: relative;
  background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
  z-index: 2;
  padding-top: 6rem;
  padding-bottom: 6rem;
}

.articles-decoration-1 {
  position: absolute;
  top: -50px;
  right: 0;
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, rgba(65, 184, 131, 0.03) 0%, rgba(52, 73, 94, 0.03) 100%);
  border-radius: 40% 60% 60% 40% / 40% 40% 60% 60%;
  z-index: -1;
}

.articles-decoration-2 {
  position: absolute;
  bottom: -100px;
  left: 5%;
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, rgba(52, 73, 94, 0.03) 0%, rgba(65, 184, 131, 0.03) 100%);
  border-radius: 60% 40% 30% 70% / 50% 60% 40% 50%;
  z-index: -1;
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.eyebrow {
  display: inline-block;
  font-size: 0.875rem;
  font-weight: 600;
  color: #41B883;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 1rem;
  padding: 0.25rem 1rem;
  background: rgba(65, 184, 131, 0.1);
  border-radius: 50px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: #111827;
  margin-bottom: 1rem;
}

.section-desc {
  font-size: 1.125rem;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .hero-content {
    flex-direction: column;
  }
  
  .left-column,
  .right-column {
    width: 100%;
    min-width: 100%;
    padding: 0;
  }
  
  .right-column {
    margin-top: 3rem;
  }
  
  .hero-image {
    height: 300px;
  }
  
  .main-title {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1.5rem;
  }
  
  .floating-card {
    position: relative;
    margin: 1rem 0;
    top: auto;
    left: auto;
    right: auto;
    bottom: auto;
    animation: none;
  }
  
  .feature-cards {
    grid-template-columns: 1fr;
  }
  
  section {
    padding: 4rem 0;
  }
}
</style>