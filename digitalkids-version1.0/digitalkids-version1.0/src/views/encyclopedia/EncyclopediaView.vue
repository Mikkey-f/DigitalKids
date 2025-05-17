<template>
  <div class="encyclopedia-container">
    <section class="search-section">
      <div class="container">
        <div class="search-header">
          <h1>育儿百科</h1>
          <p>专业的儿童健康知识库，助力家长科学育儿</p>
        </div>
        
        <div class="centered-search">
          <div class="search-bar">
            <div class="filter-dropdown">
              <select class="filter-select" v-model="searchType">
                <option value="content">内容</option>
                <option value="title">标题</option>
              </select>
            </div>
            <input 
              type="text" 
              class="search-input" 
              placeholder="请输入关键词搜索育儿知识..." 
              v-model="searchKeyword"
              @keyup.enter="performSearch"
            />
            <button class="search-button" @click="performSearch">
              <i class="fas fa-search"></i>
              搜索
            </button>
          </div>
          
          <div class="sort-options">
            <span>排序: </span>
            <select class="sort-select" v-model="sortOption">
              <option value="hot">热度</option>
              <option value="latest">最新</option>
              <option value="recommend">推荐</option>
            </select>
          </div>
        </div>
        
        <div class="age-categories-wrapper">
          <div class="age-categories">
            <div 
              v-for="category in ageCategories" 
              :key="category.id" 
              class="age-category"
              :class="{ 'active': activeCategory === category.id }"
              @click="setActiveCategory(category.id)"
            >
              {{ category.name }}
            </div>
          </div>
          <div class="nav-controls">
            <button class="nav-arrow prev" @click="scrollCategories('left')">
              <i class="fas fa-chevron-left"></i>
            </button>
            <button class="nav-arrow next" @click="scrollCategories('right')">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>
        </div>
      </div>
    </section>
    
    <div class="main-content">
      <div class="container">
        <div class="content-layout">
          <section class="articles-section">
            <div class="loading-container" v-if="isInitialLoading">
              <div class="spinner">
                <i class="fas fa-spinner fa-spin"></i>
              </div>
              <p>正在加载数据...</p>
            </div>

            <div class="no-results" v-else-if="displayedArticles.length === 0">
              <div class="no-results-icon">
                <i class="fas fa-search"></i>
              </div>
              <h3>未找到相关结果</h3>
              <p>请尝试使用其他关键词，或浏览我们的热门话题</p>
            </div>

            <div class="articles-grid" v-else>
              <div 
                v-for="article in displayedArticles" 
                :key="article.id" 
                class="article-card"
                @click="viewArticle(article.id)"
              >
                <div class="article-image">
                  <img :src="article.coverImage || '/public/images/articles/点击查看.webp'" :alt="article.title" />
                  <div class="article-overlay">
                    <div class="overlay-content">
                      <span class="read-more">阅读全文</span>
                      <i class="fas fa-arrow-right"></i>
                    </div>
                  </div>
                  <div class="article-category" v-if="article.category">{{ article.category }}</div>
                </div>
                <div class="article-content">
                  <h3 class="article-title">{{ article.title }}</h3>
                  <p class="article-excerpt" v-html="article.excerpt || article.content"></p>
                  <div class="article-meta">
                    <span class="article-date"><i class="far fa-calendar-alt"></i> {{ article.date || article.createTime }}</span>
                    <div class="article-stats">
                      <span class="article-views"><i class="fas fa-eye"></i> {{ article.views || '0' }}</span>
                      <span class="article-likes"><i class="fas fa-heart"></i> {{ article.likes || '0' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="load-more" v-if="hasMoreArticles && !isLoading && displayedArticles.length > 0">
              <button class="load-more-button" @click="loadMoreArticles">
                <span v-if="!isLoading">加载更多</span>
                <span v-else class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> 加载中...</span>
              </button>
            </div>
          </section>
          
          <aside class="sidebar">
            <div class="sidebar-card hot-topics">
              <h3 class="sidebar-title">
                <i class="fas fa-fire"></i> 热门话题
              </h3>
              <ul class="topic-list">
                <li v-for="(topic, index) in hotTopics" :key="index" class="topic-item">
                  <a href="#" class="topic-link" @click.prevent="setSearchKeyword(topic.name)">
                    <span class="topic-rank" :class="{'top-rank': index < 3}">{{ index + 1 }}</span>
                    <span class="topic-name">{{ topic.name }}</span>
                    <span class="topic-heat">{{ topic.heat }}</span>
                  </a>
                </li>
              </ul>
            </div>
            
            <div class="sidebar-card recommended-reading">
              <h3 class="sidebar-title">
                <i class="fas fa-star"></i> 推荐阅读
              </h3>
              <div class="recommended-list">
                <div v-for="(rec, index) in recommendedArticles" :key="index" class="recommended-item">
                  <div class="rec-image">
                    <img :src="rec.image" :alt="rec.title">
                  </div>
                  <div class="rec-info">
                    <h4 class="rec-title">{{ rec.title }}</h4>
                    <p class="rec-desc" v-html="rec.desc"></p>
                  </div>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { searchEncyclopedia, searchEncyclopediaByStage, type SearchAllParEncyReq, type SearchParEncyByStageReq, type SearchVo } from '@/api/encyclopedia';
import { useRouter } from 'vue-router';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '';
const router = useRouter();
const searchKeyword = ref('');
const searchType = ref('content');
const sortOption = ref('hot');
const isInitialLoading = ref(false);

const ageCategories = ref([
  { id: 'all', name: '全部', stage: 0 },
  { id: 'pregnancy-prep', name: '备孕期', stage: 0 },
  { id: 'pregnancy', name: '孕产期管理', stage: 1 },
  { id: 'puerperium', name: '产褥期', stage: 2 },
  { id: 'postpartum', name: '产后恢复', stage: 3 },
  { id: 'baby-0-1', name: '0-1岁宝宝', stage: 4 },
  { id: 'baby-1-2', name: '1-2岁宝宝', stage: 5 },
  { id: 'baby-2-3', name: '2-3岁宝宝', stage: 6 },
  { id: 'child-3-5', name: '3-5岁宝宝', stage: 7 },
  { id: 'child-5-10', name: '5-10岁宝宝', stage: 8 },
  { id: 'teen-10-15', name: '10-15岁宝宝', stage: 9 }
]);

const activeCategory = ref('all');
const getCurrentStage = () => {
  if (activeCategory.value === 'all') {
    return -1; // 使用-1表示全部分类
  }
  const category = ageCategories.value.find(cat => cat.id === activeCategory.value);
  return category ? category.stage : -1;
};

const setActiveCategory = (categoryId: string) => {
  activeCategory.value = categoryId;
  currentPage.value = 1;
  displayedArticles.value = [];
  searchByStage();
};

const allArticles = ref([
  {
    id: 1,
    title: '0~3岁婴幼儿营养膳食指南',
    excerpt: '科学合理的营养膳食是婴幼儿健康成长的基础，本文为您详细解析0~3岁婴幼儿的营养需求与膳食搭配。',
    coverImage: '/public/images/articles/膳食健康.jpg',
    category: '0~3岁',
    date: '2025-04-15',
    views: 1560,
    likes: 328,
    ageGroup: '0-3',
    stage: 4 
  },
  {
    id: 2,
    title: '幼儿视力保护与用眼卫生',
    excerpt: '随着电子产品的普及，幼儿视力问题日益凸显。如何科学保护孩子的视力健康，预防近视发生？',
    coverImage: '/public/images/articles/视力.jpg',
    category: '3~6岁',
    date: '2025-04-13',
    views: 2340,
    likes: 456,
    ageGroup: '3-6',
    stage: 7 
  },
  {
    id: 3,
    title: '宝宝辅食添加全指南',
    excerpt: '何时开始添加辅食？如何科学添加？这些问题都是新手父母关心的。本文将为您详细解答。',
    coverImage: '/public/images/articles/辅食.jpg',
    category: '0~1岁',
    date: '2025-04-12',
    views: 1890,
    likes: 287,
    ageGroup: '0-1',
    stage: 4 
  },
  {
    id: 4,
    title: '孕期营养与体重管理',
    excerpt: '孕期的营养摄入直接关系到胎儿发育和孕妇健康，如何科学规划孕期饮食？',
    coverImage: '/public/images/articles/疾病防御.jpg',
    category: '孕产期',
    date: '2025-04-08',
    views: 3210,
    likes: 542,
    ageGroup: 'pregnancy',
    stage: 1 
  },
  {
    id: 5,
    title: '产后恢复与乳房护理',
    excerpt: '产后如何科学恢复体态？哺乳期乳房应该如何护理？这些都是新妈妈们关心的问题。',
    coverImage: '/public/images/articles/产后恢复.webp',
    category: '产后恢复',
    date: '2025-04-05',
    views: 1740,
    likes: 398,
    ageGroup: 'postpartum',
    stage: 3 
  }
]);

const hotTopics = ref([
  { name: '儿童近视防控', heat: '12.5万' },
  { name: '婴幼儿辅食添加', heat: '9.8万' },
  { name: '青少年心理健康', heat: '8.6万' },
  { name: '儿童免疫力提升', heat: '7.2万' },
  { name: '宝宝入园适应', heat: '6.9万' },
  { name: '儿童肥胖防治', heat: '6.3万' },
  { name: '家庭亲子沟通', heat: '5.7万' },
  { name: '儿童阅读习惯培养', heat: '5.2万' },
  { name: '青春期性教育', heat: '4.9万' },
  { name: '儿童口腔健康', heat: '4.5万' }
]);

const recommendedArticles = ref([
  { 
    title: '家长必读：儿童成长关键期指南', 
    desc: '不同年龄段孩子的成长特点与家长指导方法', 
    image: '/public/images/articles/儿童成长关键期指南.jpg' 
  },
  { 
    title: '科学育儿：破解育儿误区100问', 
    desc: '专家解答常见育儿困惑，纠正错误认知', 
    image: '/public/images/articles/误区.webp' 
  },
  { 
    title: '儿童营养餐搭配全攻略', 
    desc: '平衡膳食，科学配餐，促进健康成长', 
    image: '/public/images/articles/营.webp' 
  }
]);

const articlesPerPage = 6;
const currentPage = ref(1);
const isLoading = ref(false);
const displayedArticles = ref<any[]>([]);
const hasMoreArticles = ref(true);
const totalCount = ref(0);

const setSearchKeyword = (keyword: string) => {
  searchKeyword.value = keyword;
  performSearch();
};

const performSearch = () => {
  currentPage.value = 1;
  displayedArticles.value = [];
  
  if (activeCategory.value === 'all') {
    searchAllArticles();
  } else {
    searchByStage();
  }
};

const searchByStage = async () => {
  if (isLoading.value) return;
  
  isLoading.value = true;
  isInitialLoading.value = currentPage.value === 1;
  
  try {
    const stage = getCurrentStage();
    if (stage === -1) {
      await searchAllArticles();
      return;
    }
    const searchParams: SearchParEncyByStageReq = {
      keyword: searchKeyword.value,
      stage: stage,
      pageReq: {
        current: currentPage.value,
        pageSize: articlesPerPage,
        sortField: sortOption.value === 'latest' ? 'createTime' : 'views',
        sortOrder: sortOption.value === 'latest' ? 'descend' : 'descend'
      }
    };
    
    const response = await searchEncyclopediaByStage(searchParams);
    
    if (response.code === 1) {
      const searchResults = response.data || [];
      if (currentPage.value === 1) {
        displayedArticles.value = searchResults;
      } else {
        displayedArticles.value = [...displayedArticles.value, ...searchResults];
      }
      
      hasMoreArticles.value = searchResults.length === articlesPerPage;
    } else {
      console.error('请求失败:', response.msg);
    }
  } catch (error: any) {
    if (currentPage.value === 1) {
      useLocalData();
    }
  } finally {
    isLoading.value = false;
    isInitialLoading.value = false;
  }
};

const searchAllArticles = async () => {
  if (isLoading.value) return;
  isLoading.value = true;
  isInitialLoading.value = currentPage.value === 1;
  try {
    const searchParams: SearchAllParEncyReq = {
      keyword: searchKeyword.value,
      pageReq: {
        current: currentPage.value,
        pageSize: articlesPerPage,
        sortField: sortOption.value === 'latest' ? 'createTime' : 'views',
        sortOrder: sortOption.value === 'latest' ? 'descend' : 'descend'
      }
    };
    
    const response = await searchEncyclopedia(searchParams);

    
    if (response.code === 1) {
      const searchResults = response.data || [];
      if (currentPage.value === 1) {
        displayedArticles.value = searchResults;
      } else {
        displayedArticles.value = [...displayedArticles.value, ...searchResults];
      }
      hasMoreArticles.value = searchResults.length === articlesPerPage;
    } else {
      console.error('搜索请求失败:', response.msg);
    }
  } catch (error: any) {
    console.error('搜索请求出错:', error);
    if (currentPage.value === 1) {
      useLocalData();
    }
  } finally {
    isLoading.value = false;
    isInitialLoading.value = false;
  }
};

const useLocalData = () => {
  if (activeCategory.value === 'all') {
    const filteredArticles = searchKeyword.value
      ? allArticles.value.filter(article => 
          article.title.includes(searchKeyword.value) || 
          article.excerpt.includes(searchKeyword.value))
      : allArticles.value;
    
    displayedArticles.value = filteredArticles.slice(0, currentPage.value * articlesPerPage);
    hasMoreArticles.value = displayedArticles.value.length < filteredArticles.length;
  } else {
    const stage = getCurrentStage();
    const filteredArticles = allArticles.value.filter(article => 
      article.stage === stage && 
      (searchKeyword.value 
        ? article.title.includes(searchKeyword.value) || article.excerpt.includes(searchKeyword.value)
        : true)
    );
    
    displayedArticles.value = filteredArticles.slice(0, currentPage.value * articlesPerPage);
    hasMoreArticles.value = displayedArticles.value.length < filteredArticles.length;
  }
};

const loadMoreArticles = () => {
  if (isLoading.value) return;
  currentPage.value++;
  if (activeCategory.value === 'all') {
    searchAllArticles();
  } else {
    searchByStage();
  }
};

const viewArticle = (articleId: number) => {
  router.push({ name: 'EncyclopediaArticle', params: { id: articleId } });
};

const setupInfiniteScroll = () => {
  window.addEventListener('scroll', () => {
    if (hasMoreArticles.value && !isLoading.value) {
      const scrollHeight = document.documentElement.scrollHeight;
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      const clientHeight = document.documentElement.clientHeight;
      if (scrollTop + clientHeight >= scrollHeight - 200) {
        loadMoreArticles();
      }
    }
  });
};

watch(sortOption, () => {
  currentPage.value = 1;
  displayedArticles.value = [];
  if (activeCategory.value === 'all') {
    searchAllArticles();
  } else {
    searchByStage();
  }
});

onMounted(() => {
  searchAllArticles();
  setupInfiniteScroll();
});

const scrollCategories = (direction: 'left' | 'right') => {
  const container = document.querySelector('.age-categories');
  if (container) {
    const scrollAmount = direction === 'left' ? -300 : 300;
    container.scrollBy({ left: scrollAmount, behavior: 'smooth' });
  }
};
</script>

<style scoped>
/* 基础样式 */
.encyclopedia-container {
  background-color: #f9fafb;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

section {
  padding: 3rem 0;
}

.search-section {
  background: linear-gradient(135deg, rgba(255,255,255,0.9) 30%, rgba(65, 184, 131, 0.15) 100%);
  padding-bottom: 0;
  background-image: url('/src/assets/banner3.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

.search-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(255,255,255,0.92) 30%, rgba(255,255,255,0.85) 100%);
  z-index: 0;
}

.search-section .container {
  position: relative;
  z-index: 1;
}

.search-header {
  text-align: center;
  margin-bottom: 2rem;
}

.search-header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 0.5rem;
}

.search-header p {
  font-size: 1.125rem;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

.centered-search {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-bottom: 2rem;
  width: 100%;
}

.search-bar {
  display: flex;
  align-items: center;
  width: 80%;
  max-width: 800px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 1rem;
  transition: all 0.3s ease;
}

.search-bar:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.filter-dropdown {
  background: #f3f4f6;
  padding: 0 1rem;
  height: 54px;
  display: flex;
  align-items: center;
  border-right: 1px solid #e5e7eb;
}

.filter-select {
  background: transparent;
  border: none;
  color: #374151;
  font-weight: 500;
  cursor: pointer;
  outline: none;
  height: 100%;
}

.search-input {
  flex: 1;
  height: 54px;
  padding: 0 1.5rem;
  border: none;
  outline: none;
  font-size: 1.05rem;
}

.search-button {
  background: #41B883;
  color: white;
  height: 54px;
  padding: 0 1.75rem;
  border: none;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
}

.search-button:hover {
  background: #34a873;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.sort-options span {
  color: #4b5563;
  font-weight: 500;
}

.sort-select {
  background: white;
  border: 1px solid #e5e7eb;
  padding: 0.5rem;
  border-radius: 6px;
  color: #374151;
  font-weight: 500;
  cursor: pointer;
  outline: none;
  transition: all 0.2s ease;
}

.sort-select:hover {
  border-color: #41B883;
}

.age-categories-wrapper {
  position: relative;
  width: 100%;
  padding: 0 40px;
  margin-bottom: 1rem;
}

.age-categories {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem 0;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  scroll-behavior: smooth;
}

.age-categories::-webkit-scrollbar {
  display: none;
}

.age-category {
  padding: 0.65rem 1.5rem;
  background: #f3f4f6;
  border-radius: 30px;
  color: #4b5563;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
  border: 1px solid rgba(0,0,0,0.03);
}

.age-category:hover {
  background: #e5e7eb;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.08);
}

.age-category.active {
  background: #41B883;
  color: white;
  font-weight: 600;
  box-shadow: 0 4px 10px rgba(65, 184, 131, 0.3);
  border: 1px solid rgba(65, 184, 131, 0.2);
}

.nav-controls {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-arrow {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  pointer-events: auto;
  color: #4b5563;
}

.nav-arrow:hover {
  background: #f9fafb;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  color: #41B883;
}

.nav-arrow.prev {
  margin-left: 5px;
}

.nav-arrow.next {
  margin-right: 5px;
}

.main-content {
  padding: 3rem 0;
}

.content-layout {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 2rem;
}

.articles-section {
  padding-top: 0;
  min-height: 400px;
  position: relative;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 0;
  width: 100%;
}

.spinner {
  font-size: 2rem;
  color: #41B883;
  margin-bottom: 1rem;
}

.loading-container p {
  font-size: 1rem;
  color: #6b7280;
}

.no-results {
  text-align: center;
  padding: 3rem 0;
}

.no-results-icon {
  font-size: 3rem;
  color: #d1d5db;
  margin-bottom: 1rem;
}

.no-results h3 {
  font-size: 1.5rem;
  color: #374151;
  margin-bottom: 0.5rem;
}

.no-results p {
  font-size: 1rem;
  color: #6b7280;
  max-width: 400px;
  margin: 0 auto;
}

.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.article-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  transition: all 0.4s ease;
  cursor: pointer;
  border: 1px solid rgba(0,0,0,0.03);
}

.article-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.article-image {
  height: 220px;
  position: relative;
  overflow: hidden;
}

.article-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.8s ease;
}

.article-card:hover .article-image img {
  transform: scale(1.1);
}

.article-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  opacity: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.4s ease;
}

.article-card:hover .article-overlay {
  opacity: 1;
}

.overlay-content {
  background: rgba(65, 184, 131, 0.9);
  color: white;
  padding: 0.75rem 1.25rem;
  border-radius: 30px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transform: translateY(20px);
  transition: all 0.4s 0.1s ease;
}

.article-card:hover .overlay-content {
  transform: translateY(0);
}

.read-more {
  font-size: 0.9rem;
}

.article-category {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background: rgba(65, 184, 131, 0.95);
  color: white;
  padding: 0.35rem 1rem;
  border-radius: 30px;
  font-size: 0.75rem;
  font-weight: 600;
  box-shadow: 0 2px 10px rgba(65, 184, 131, 0.3);
  backdrop-filter: blur(2px);
  z-index: 10;
}

.article-content {
  padding: 1.75rem;
}

.article-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  color: #111827;
  line-height: 1.3;
  transition: color 0.3s ease;
}

.article-card:hover .article-title {
  color: #41B883;
}

.article-excerpt {
  font-size: 0.9rem;
  color: #6b7280;
  margin-bottom: 1.5rem;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid #f3f4f6;
  padding-top: 1rem;
}

.article-date {
  font-size: 0.8rem;
  color: #9ca3af;
  display: flex;
  align-items: center;
  gap: 0.35rem;
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.article-views,
.article-likes {
  font-size: 0.8rem;
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 0.35rem;
  transition: color 0.3s ease;
}

.article-card:hover .article-views,
.article-card:hover .article-likes {
  color: #41B883;
}

.load-more {
  text-align: center;
  margin-top: 2rem;
}

.load-more-button {
  background: white;
  color: #41B883;
  padding: 0.85rem 2rem;
  border-radius: 8px;
  font-weight: 600;
  border: 2px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.load-more-button:hover {
  border-color: #41B883;
  background: rgba(65, 184, 131, 0.05);
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.08);
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.sidebar-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.sidebar-title {
  padding: 1rem 1.5rem;
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
  border-bottom: 1px solid #f3f4f6;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sidebar-title i {
  color: #41B883;
}

.topic-list {
  padding: 1rem 0;
}

.topic-item {
  list-style: none;
}

.topic-link {
  padding: 0.75rem 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: background-color 0.2s ease;
  text-decoration: none;
}

.topic-link:hover {
  background-color: #f9fafb;
}

.topic-rank {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #e5e7eb;
  color: #6b7280;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: 600;
}

.topic-rank.top-rank {
  background-color: #41B883;
  color: white;
}

.topic-name {
  flex: 1;
  font-size: 0.9375rem;
  color: #374151;
}

.topic-heat {
  font-size: 0.8125rem;
  color: #9ca3af;
}

.recommended-list {
  padding: 1rem 1.5rem;
}

.recommended-item {
  display: flex;
  gap: 1rem;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f3f4f6;
  cursor: pointer;
}

.recommended-item:last-child {
  border-bottom: none;
}

.rec-image {
  flex-shrink: 0;
  width: 80px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
}

.rec-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.recommended-item:hover .rec-image img {
  transform: scale(1.05);
}

.rec-info {
  flex: 1;
}

.rec-title {
  font-size: 0.9375rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 0.25rem;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.rec-desc {
  font-size: 0.8125rem;
  color: #6b7280;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.qrcode-card {
  padding-bottom: 1.5rem;
}

.qrcode-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.qrcode-image {
  width: 180px;
  height: 180px;
  margin-bottom: 1rem;
}

.qrcode-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qrcode-text {
  font-size: 0.9375rem;
  color: #4b5563;
}

@media (max-width: 1024px) {
  .centered-search {
    width: 100%;
  }
  
  .search-bar {
    width: 90%;
  }
  
  .content-layout {
    grid-template-columns: 1fr;
  }
  
  .sidebar {
    order: -1;
    margin-bottom: 2rem;
  }
}

@media (max-width: 768px) {
  .articles-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }

  .search-header h1 {
    font-size: 2rem;
  }
  
  .search-bar {
    width: 95%;
  }
  
  .recommended-list {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1rem;
  }
  
  .recommended-item {
    flex-direction: column;
    border-bottom: none;
  }
  
  .rec-image {
    width: 100%;
    height: 120px;
  }
}

@media (max-width: 640px) {
  .search-bar {
    width: 100%;
  }
  
  .recommended-list {
    grid-template-columns: 1fr;
  }
  
  .recommended-item {
    flex-direction: row;
    border-bottom: 1px solid #f3f4f6;
  }
  
  .rec-image {
    width: 80px;
    height: 60px;
  }
}

@media (max-width: 576px) {
  .articles-grid {
    grid-template-columns: 1fr;
  }

  .search-header h1 {
    font-size: 1.75rem;
  }

  .search-bar {
    flex-direction: column;
    height: auto;
    width: 100%;
  }

  .filter-dropdown {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #e5e7eb;
    justify-content: center;
  }

  .search-input,
  .search-button {
    width: 100%;
    height: 50px;
  }
  
  .age-categories {
    justify-content: flex-start;
    padding-bottom: 1rem;
  }
}
</style> 