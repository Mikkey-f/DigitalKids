<template>
  <div class="recommended-articles">
    <div v-if="isLoading" class="article-skeletons">
      <div v-for="i in 3" :key="`skeleton-${i}`" class="article-skeleton">
        <div class="skeleton-image"></div>
        <div class="skeleton-content">
          <div class="skeleton-badge"></div>
          <div class="skeleton-title"></div>
          <div class="skeleton-text"></div>
          <div class="skeleton-text short"></div>
          <div class="skeleton-footer"></div>
        </div>
      </div>
    </div>
    <div v-else class="articles-grid">
      <div 
        v-for="article in articles" 
        :key="article.id" 
        class="article-card"
        @mouseenter="startCardHover($event)"
        @mouseleave="endCardHover($event)"
      >
        <div class="article-image">
          <img :src="article.image" :alt="article.title" loading="lazy">
          <div class="article-badge">{{ article.category }}</div>
        </div>
        <div class="article-content">
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-description">{{ article.description }}</p>
          <div class="article-footer">
            <div class="article-meta">
              <span class="article-date">{{ formatDate(article.date) }}</span>
              <span class="article-read-time">{{ article.readTime }} 分钟阅读</span>
            </div>
            <router-link :to="article.link" class="read-more-link">
              阅读全文
              <i class="fas fa-arrow-right ml-1"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import gsap from 'gsap';

interface Article {
  id: number;
  title: string;
  description: string;
  image: string;
  category: string;
  date: string;
  readTime: number;
  link: string;
}

const isLoading = ref(true);

const articles = ref<Article[]>([
  {
    id: 1,
    title: '儿童营养均衡指南：如何制定合理的膳食计划',
    description: '科学的膳食搭配对儿童的生长发育至关重要。本文介绍如何为不同年龄段的儿童制定营养均衡的膳食计划。',
    image: 'public/images/articles/膳食健康.jpg',
    category: '营养指南',
    date: '2025-04-15',
    readTime: 5,
    link: '/articles/children-nutrition-guide'
  },
  {
    id: 2,
    title: '儿童常见疾病预防与家庭护理方法',
    description: '了解儿童常见疾病的预防措施和家庭护理方法，帮助家长正确应对孩子的健康问题。',
    image: 'public/images/articles/疾病防御.jpg',
    category: '健康护理',
    date: '2025-04-10',
    readTime: 7,
    link: '/articles/children-disease-prevention'
  },
  {
    id: 3,
    title: '数字时代：如何合理控制儿童屏幕使用时间',
    description: '在数字化时代，如何平衡科技带来的便利与对儿童健康的影响，合理控制屏幕使用时间。',
    image: 'public/images/articles/视力保护.jpg',
    category: '数字健康',
    date: '2025-04-05',
    readTime: 6,
    link: '/articles/screen-time-management'
  }
]);

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 1500);
});

const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

const startCardHover = (event: MouseEvent) => {
  const card = event.currentTarget as HTMLElement;
  gsap.to(card, {
    y: -10,
    boxShadow: '0 15px 30px rgba(0, 0, 0, 0.1)',
    duration: 0.3
  });
};

const endCardHover = (event: MouseEvent) => {
  const card = event.currentTarget as HTMLElement;
  gsap.to(card, {
    y: 0,
    boxShadow: '0 10px 20px rgba(0, 0, 0, 0.05)',
    duration: 0.3
  });
};
</script>

<style scoped>
.recommended-articles {
  width: 100%;
}
.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
  width: 100%;
}
.article-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #f3f4f6;
}
.article-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}
.article-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.article-card:hover .article-image img {
  transform: scale(1.05);
}
.article-badge {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background: #41B883;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(65, 184, 131, 0.3);
}
.article-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}
.article-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 0.75rem;
  line-height: 1.4;
}
.article-description {
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  flex-grow: 1;
}
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #f3f4f6;
  padding-top: 1rem;
}
.article-meta {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}
.article-date,
.article-read-time {
  font-size: 0.75rem;
  color: #9ca3af;
}
.read-more-link {
  color: #41B883;
  font-weight: 600;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  transition: color 0.2s ease;
}
.read-more-link:hover {
  color: #34495E;
}
.article-skeletons {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
  width: 100%;
}
.article-skeleton {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #f3f4f6;
}
.skeleton-image {
  height: 200px;
  background: linear-gradient(90deg, #f3f4f6 0%, #ebedf0 50%, #f3f4f6 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}
.skeleton-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.skeleton-badge {
  width: 80px;
  height: 20px;
  border-radius: 20px;
  background: linear-gradient(90deg, #f3f4f6 0%, #ebedf0 50%, #f3f4f6 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}
.skeleton-title {
  width: 90%;
  height: 24px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f3f4f6 0%, #ebedf0 50%, #f3f4f6 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}
.skeleton-text {
  width: 100%;
  height: 16px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f3f4f6 0%, #ebedf0 50%, #f3f4f6 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}
.skeleton-text.short {
  width: 70%;
}
.skeleton-footer {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #f3f4f6;
  width: 100%;
  height: 20px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f3f4f6 0%, #ebedf0 50%, #f3f4f6 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}
@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
@media (max-width: 768px) {
  .articles-grid,
  .article-skeletons {
    grid-template-columns: 1fr;
  }
  .article-image {
    height: 180px;
  }
}
</style> 