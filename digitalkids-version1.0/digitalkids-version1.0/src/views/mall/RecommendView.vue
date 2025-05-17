<template>
  <div class="recommend-container">
    <div class="search-section">
      <div class="search-container">
        <div class="logo">
          <router-link to="/mall/recommend">
            <div class="home-icon">
              <i class="fas fa-home"></i>
            </div>
          </router-link>
        </div>
        <div class="search-bar">
          <div class="category-select" @click="toggleCategoryDropdown">
            <span>{{ selectedCategory.name }}</span>
            <i class="fas fa-angle-down"></i>
            <div class="category-dropdown" v-show="showCategoryDropdown">
              <div class="category-item" 
                   v-for="cat in searchCategories" 
                   :key="cat.id"
                   @click="selectCategory(cat)">
                {{ cat.name }}
              </div>
            </div>
          </div>

          <div class="search-input-wrapper">
            <input 
              type="text" 
              v-model="searchQuery" 
              @focus="showSearchSuggestions = true"
              @input="handleSearchInput"
              placeholder="搜索商品" />
              
            <button class="voice-search-btn" @click="startVoiceSearch" :class="{ 'recording': isRecording }" title="语音输入">
              <i class="fas" :class="isRecording ? 'fa-stop' : 'fa-microphone'"></i>
            </button>
            
            <button class="search-btn" @click="handleSearch" title="搜索">
              <i class="fas fa-search"></i>
            </button>

            <div class="search-suggestions" v-show="showSearchSuggestions" ref="suggestionsPanel">
              <div class="hot-searches">
                <h4>热门搜索</h4>
                <div class="hot-tags">
                  <span 
                    v-for="(tag, index) in hotSearches" 
                    :key="index"
                    @click="selectSearchTag(tag)"
                    :class="{ 'hot': index < 3 }">
                    <i v-if="index < 3" class="fas fa-fire"></i>
                    {{ tag }}
                  </span>
                </div>
              </div>
              
              <div class="search-history" v-if="searchHistory.length">
                <div class="history-header">
                  <h4>最近搜索</h4>
                  <span class="clear-history" @click="clearSearchHistory">
                    清空历史 <i class="fas fa-trash-alt"></i>
                  </span>
                </div>
                <div class="history-items">
                  <div 
                    v-for="(item, index) in searchHistory.slice(0, 5)" 
                    :key="index"
                    class="history-item"
                    @click="selectSearchHistory(item)">
                    <i class="fas fa-history"></i>
                    <span>{{ item }}</span>
                    <button class="remove-btn" @click.stop="removeSearchHistory(index)">
                      <i class="fas fa-times"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="user-actions">
          <div class="cart-icon">
            <router-link to="/mall/cart">
              <i class="fas fa-shopping-cart"></i>
              <span class="cart-count">{{ cartCount }}</span>
            </router-link>
          </div>
          <div class="user-icon">
            <router-link to="/mall/orders">
              <i class="fas fa-user"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <div class="carousel-section">
      <div class="carousel-container">
        <el-carousel height="400px" indicator-position="outside" arrow="always">
          <el-carousel-item v-for="(banner, index) in banners" :key="index">
            <img :src="banner.imageUrl" :alt="banner.title" class="banner-image">
            <div class="banner-content">
              <h2>{{ banner.title }}</h2>
              <p>{{ banner.description }}</p>
              <router-link :to="banner.link" class="banner-btn">立即查看</router-link>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <div class="category-nav">
      <div class="category-container">
        <div 
          v-for="(category, index) in categories" 
          :key="index" 
          class="category-item"
          @click="goToCategory(category.id)"
        >
          <div class="category-icon">
            <i :class="category.icon"></i>
          </div>
          <div class="category-name">{{ category.name }}</div>
        </div>
      </div>
    </div>

    <div class="flash-deals">
      <div class="section-header">
        <h2 class="section-title">限时特惠</h2>
        <div class="countdown">
          <span class="countdown-label">距结束</span>
          <div class="countdown-time">
            <div class="time-box">{{ countdown.hours }}</div>
            <span class="time-separator">:</span>
            <div class="time-box">{{ countdown.minutes }}</div>
            <span class="time-separator">:</span>
            <div class="time-box">{{ countdown.seconds }}</div>
          </div>
        </div>
        <router-link to="/mall/category?type=flash" class="view-more">
          查看更多 <i class="fas fa-chevron-right"></i>
        </router-link>
      </div>
      <div class="product-grid">
        <div 
          v-for="(product, index) in flashDeals" 
          :key="index" 
          class="product-card flash-deal-card"
          @click="goToProduct(product.id)"
        >
          <div class="discount-badge">{{ product.discount }}% OFF</div>
          <div class="product-image">
            <img :src="product.imageUrl" :alt="product.name">
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <div class="product-price">
              <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
              <span class="original-price">¥{{ product.originalPrice.toFixed(2) }}</span>
            </div>
            <div class="product-sales">已售 {{ product.sold }}件</div>
          </div>
        </div>
      </div>
    </div>

    <div class="hot-products">
      <div class="section-header">
        <h2 class="section-title">热门商品</h2>
        <router-link to="/mall/category?type=hot" class="view-more">
          查看更多 <i class="fas fa-chevron-right"></i>
        </router-link>
      </div>
      <div class="product-grid">
        <div 
          v-for="(product, index) in hotProducts" 
          :key="index" 
          class="product-card"
          @click="goToProduct(product.id)"
        >
          <div class="product-image">
            <img :src="product.imageUrl" :alt="product.name">
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <div class="product-price">
              <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
              <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toFixed(2) }}</span>
            </div>
            <div class="product-meta">
              <div class="product-rating">
                <i class="fas fa-star"></i>
                <span>{{ product.rating }}</span>
              </div>
              <div class="product-sales">{{ product.sold }}件已售</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="new-products">
      <div class="section-header">
        <h2 class="section-title">新品推荐</h2>
        <router-link to="/mall/category?type=new" class="view-more">
          查看更多 <i class="fas fa-chevron-right"></i>
        </router-link>
      </div>
      <div class="product-grid">
        <div 
          v-for="(product, index) in newProducts" 
          :key="index" 
          class="product-card"
          @click="goToProduct(product.id)"
        >
          <div class="new-badge">New</div>
          <div class="product-image">
            <img :src="product.imageUrl" :alt="product.name">
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <div class="product-price">
              <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
            </div>
            <div class="product-desc">{{ product.description }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <Footer />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Footer from './components/footer.vue'

const router = useRouter();
const cartCount = ref(0); 

const searchQuery = ref('');
const showSearchSuggestions = ref(false);
const showCategoryDropdown = ref(false);
const isRecording = ref(false);
const suggestionsPanel = ref<HTMLElement | null>(null);

const banners = ref([
  {
    imageUrl: '/src/assets/images/banners/儿童营养.png',
    title: '儿童营养产品专场',
    description: '精选儿童营养产品，助力孩子健康成长',
    link: '/mall/category'
  },
  {
    imageUrl: '/src/assets/images/banners/儿童玩具.png',
    title: '玩具早教大促',
    description: '多重优惠，好玩又有趣的早教玩具专场',
    link: '/mall/category'
  },
  {
    imageUrl: '/src/assets/images/banners/服饰.png',
    title: '儿童服饰焕新',
    description: '舒适透气，环保安全的儿童服饰专场',
    link: '/mall/category'
  }
]);

const categories = ref([
  { id: 'nutrition', name: '营养保健', icon: 'fas fa-apple-alt' },
  { id: 'toys', name: '玩具早教', icon: 'fas fa-puzzle-piece' },
  { id: 'clothing', name: '童装童鞋', icon: 'fas fa-tshirt' },
  { id: 'books', name: '图书绘本', icon: 'fas fa-book' },
  { id: 'maternity', name: '孕产用品', icon: 'fas fa-baby' },
  { id: 'safety', name: '安全出行', icon: 'fas fa-car' },
  { id: 'healthcare', name: '日常护理', icon: 'fas fa-pump-soap' },
  { id: 'all', name: '全部分类', icon: 'fas fa-th-large' }
]);

const countdown = ref({
  hours: '02',
  minutes: '45',
  seconds: '30'
});
let timer: number | null = null;
const flashDeals = ref([
  {
    id: 101,
    name: '儿童DHA营养补充品',
    price: 129.9,
    originalPrice: 199.9,
    discount: 35,
    imageUrl: '/src/assets/images/goods/DHA.png',
    sold: 253
  },
  {
    id: 102,
    name: '宝宝鱼肝油滴剂',
    price: 79.9,
    originalPrice: 119.9,
    discount: 33,
    imageUrl: '/src/assets/images/goods/鱼肝油.avif',
    sold: 168
  },
  {
    id: 103,
    name: '儿童钙铁锌营养包',
    price: 99.9,
    originalPrice: 159.9,
    discount: 37,
    imageUrl: '/src/assets/images/goods/GTX.jpg',
    sold: 342
  },
  {
    id: 104,
    name: '儿童益生菌粉',
    price: 69.9,
    originalPrice: 99.9,
    discount: 30,
    imageUrl: '/src/assets/images/goods/儿童益生菌粉.dpg',
    sold: 421
  }
]);

const hotProducts = ref([
  {
    id: 201,
    name: '儿童成长益智拼图',
    price: 89.9,
    originalPrice: 129.9,
    imageUrl: '/src/assets/images/goods/yzpt.jpg',
    rating: 4.8,
    sold: 1342
  },
  {
    id: 202,
    name: '宝宝软底学步鞋',
    price: 119.9,
    originalPrice: 169.9,
    imageUrl: '/src/assets/images/goods/xb.jpg',
    rating: 4.7,
    sold: 956
  },
  {
    id: 203,
    name: '儿童防蛀牙膏套装',
    price: 49.9,
    originalPrice: 69.9,
    imageUrl: '/src/assets/images/goods/yg.jpg',
    rating: 4.9,
    sold: 2156
  },
  {
    id: 204,
    name: '多功能婴儿推车',
    price: 699.9,
    originalPrice: 999.9,
    imageUrl: '/src/assets/images/goods/yec.jpg',
    rating: 4.6,
    sold: 432
  },
  {
    id: 205,
    name: '儿童智能手表',
    price: 199.9,
    originalPrice: 299.9,
    imageUrl: '/src/assets/images/goods/sb.jpg',
    rating: 4.7,
    sold: 873
  },
  {
    id: 206,
    name: '婴儿保温奶瓶',
    price: 89.9,
    imageUrl: '/src/assets/images/goods/np.jpg',
    rating: 4.8,
    sold: 1265
  }
]);

const newProducts = ref([
  {
    id: 301,
    name: '智能儿童故事机',
    price: 239.9,
    imageUrl: '/src/assets/images/goods/gsj.jpg',
    description: '智能语音交互，海量儿童故事内容'
  },
  {
    id: 302,
    name: '婴儿游泳圈',
    price: 89.9,
    imageUrl: '/src/assets/images/goods/yyq.jpg',
    description: '安全防翻，双重气囊设计'
  },
  {
    id: 303,
    name: '儿童健康监测手环',
    price: 199.9,
    imageUrl: '/src/assets/images/goods/sh.jpg',
    description: '实时监测儿童健康数据，安全定位追踪'
  },
  {
    id: 304,
    name: '宝宝防摔头盔',
    price: 99.9,
    imageUrl: '/src/assets/images/goods/tk.jpg',
    description: '轻盈透气，有效保护宝宝头部安全'
  }
]);

const searchCategories = ref([
  { id: 'all', name: '全部分类' },
  { id: 'nutrition', name: '营养保健' },
  { id: 'toys', name: '玩具早教' },
  { id: 'clothing', name: '童装童鞋' },
  { id: 'books', name: '图书绘本' },
  { id: 'maternity', name: '孕产用品' },
  { id: 'safety', name: '安全出行' },
  { id: 'healthcare', name: '日常护理' },
]);
const selectedCategory = ref(searchCategories.value[0]);

const hotSearches = ref([
  '儿童维生素D',
  '益智玩具',
  '秋季外套',
  '儿童故事书',
  '婴儿奶粉',
  '学习用品',
  '儿童益生菌',
  '防摔头盔',
]);
const searchHistory = ref<string[]>([]);
const getCartCountFromStorage = () => {
  try {
    const cartItemsStr = localStorage.getItem('cartItems');
    if (cartItemsStr) {
      const cartItems = JSON.parse(cartItemsStr);
      if (Array.isArray(cartItems)) {
        cartCount.value = cartItems.length;
      }
    }
  } catch (error) {
  }
};

const handleStorageChange = (event: StorageEvent) => {
  if (event.key === 'cartItems') {
    getCartCountFromStorage();
  }
};

onMounted(() => {
  getCartCountFromStorage();
  window.addEventListener('storage', handleStorageChange);
  const history = localStorage.getItem('searchHistory');
  if (history) {
    searchHistory.value = JSON.parse(history);
  }
  document.addEventListener('click', handleClickOutside);
  timer = window.setInterval(updateCountdown, 1000);
});

onUnmounted(() => {
  window.removeEventListener('storage', handleStorageChange);
  document.removeEventListener('click', handleClickOutside);
  if (timer) {
    clearInterval(timer);
  }
});

const handleSearchInput = () => {
  if (searchQuery.value.trim()) {
    showSearchSuggestions.value = true;
  }
};

const handleSearch = () => {
  if (!searchQuery.value.trim()) return;
  addToSearchHistory(searchQuery.value);
  router.push({
    path: '/mall/search',
    query: { 
      q: searchQuery.value,
      category: selectedCategory.value.id || 0
    }
  });
  
  showSearchSuggestions.value = false;
};

const toggleCategoryDropdown = () => {
  showCategoryDropdown.value = !showCategoryDropdown.value;
};

const selectCategory = (category: any) => {
  selectedCategory.value = category;
  showCategoryDropdown.value = false;
};

const selectSearchTag = (tag: string) => {
  searchQuery.value = tag;
  handleSearch();
};

const selectSearchHistory = (item: string) => {
  searchQuery.value = item;
  handleSearch();
};

const addToSearchHistory = (query: string) => {
  const index = searchHistory.value.indexOf(query);
  if (index > -1) {
    searchHistory.value.splice(index, 1);
  }
  searchHistory.value.unshift(query);
  if (searchHistory.value.length > 10) {
    searchHistory.value.pop();
  }
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value));
};

const removeSearchHistory = (index: number) => {
  searchHistory.value.splice(index, 1);
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value));
};

const clearSearchHistory = () => {
  searchHistory.value = [];
  localStorage.removeItem('searchHistory');
};

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (suggestionsPanel.value && !suggestionsPanel.value.contains(target)) {
    showSearchSuggestions.value = false;
  }
  if (!target.closest('.category-select')) {
    showCategoryDropdown.value = false;
  }
};

const startVoiceSearch = async () => {
  if (!('webkitSpeechRecognition' in window)) {
    alert('您的浏览器不支持语音识别功能');
    return;
  }

  try {
    isRecording.value = !isRecording.value;
    if (isRecording.value) {
      const SpeechRecognition = (window as any).webkitSpeechRecognition;
      const recognition = new SpeechRecognition();
      recognition.lang = 'zh-CN';
      recognition.continuous = false;
      recognition.interimResults = false;

      recognition.onresult = (event: any) => {
        const text = event.results[0][0].transcript;
        searchQuery.value = text;
        isRecording.value = false;
        // 不自动跳转，只填充搜索框
      };

      recognition.onerror = () => {
        isRecording.value = false;
      };

      recognition.onend = () => {
        isRecording.value = false;
      };

      recognition.start();
    }
  } catch (error) {
    isRecording.value = false;
  }
};

const goToProduct = (productId: number) => {
  router.push(`/mall/product/${productId}`);
};

const goToCategory = (categoryId: string) => {
  router.push(`/mall/category?id=${categoryId}`);
};

const updateCountdown = () => {
  let hours = parseInt(countdown.value.hours);
  let minutes = parseInt(countdown.value.minutes);
  let seconds = parseInt(countdown.value.seconds);

  seconds--;

  if (seconds < 0) {
    seconds = 59;
    minutes--;
  }

  if (minutes < 0) {
    minutes = 59;
    hours--;
  }

  if (hours < 0) {
    hours = 23; 
  }

  countdown.value.hours = hours.toString().padStart(2, '0');
  countdown.value.minutes = minutes.toString().padStart(2, '0');
  countdown.value.seconds = seconds.toString().padStart(2, '0');
};
</script>

<style scoped>
.recommend-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin-bottom: 20px;
  background: linear-gradient(to right, #f8f9fa, #e9ecef);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid #e0e0e0;
}

.search-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.logo {
  width: 44px;
  height: 44px;
}

.home-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #41b883;
  color: white;
  border-radius: 12px;
  font-size: 22px;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(65, 184, 131, 0.3);
}

.home-icon:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(65, 184, 131, 0.4);
  background: #38a573;
}

.search-bar {
  display: flex;
  width: 55%;
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #e0e0e0;
  background: white;
  transition: all 0.3s ease;
}

.search-bar:hover,
.search-bar:focus-within {
  box-shadow: 0 3px 12px rgba(65, 184, 131, 0.15);
  border-color: #41b883;
}

.category-select {
  position: relative;
  padding: 0 15px;
  height: 44px;
  line-height: 44px;
  background: #f5f5f5;
  cursor: pointer;
  user-select: none;
  z-index: 1001;
  font-weight: 500;
  color: #333;
  transition: background 0.3s;
  border-right: 1px solid #e0e0e0;
}

.category-select:hover {
  background: #ebebeb;
}

.category-select span {
  margin-right: 5px;
}

.category-select i {
  color: #41b883;
  font-size: 12px;
  transition: transform 0.3s;
}

.category-select:hover i {
  transform: translateY(2px);
}

.category-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 140px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 5px;
  z-index: 1000;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.category-item {
  padding: 10px 15px;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 1px solid #f0f0f0;
}

.category-item:last-child {
  border-bottom: none;
}

.category-item:hover {
  background: #f7f7f7;
  color: #41b883;
  padding-left: 20px;
}

.search-input-wrapper {
  flex: 1;
  position: relative;
}

.search-input-wrapper input {
  width: 100%;
  height: 44px;
  padding: 0 100px 0 15px;
  border: none;
  font-size: 15px;
  outline: none;
  background: transparent;
}

.voice-search-btn,
.search-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  width: 44px;
  height: 44px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.voice-search-btn {
  right: 44px;
  color: #666;
}

.voice-search-btn.recording {
  color: #41b883;
  animation: pulse 1.5s infinite;
}

.search-btn {
  right: 0;
  background: #41b883;
  color: white;
  border-radius: 0 24px 24px 0;
}

.search-btn:hover {
  background: #38a573;
}

.voice-search-btn:hover {
  color: #41b883;
}

.search-suggestions {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.12);
  padding: 18px;
  z-index: 999;
  border: 1px solid #e8e8e8;
}

.search-suggestions::before {
  content: '';
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 12px;
  background: white;
  border-top: 1px solid #e8e8e8;
  border-left: 1px solid #e8e8e8;
  transform: translateX(-50%) rotate(45deg);
}

.hot-searches h4,
.search-history h4 {
  font-size: 14px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 600;
}

.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.hot-tags span {
  padding: 6px 14px;
  background: #f5f7fa;
  border-radius: 18px;
  font-size: 13px;
  color: #555;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 5px;
}

.hot-tags span.hot {
  color: #ff4d4f;
  background: #fff1f0;
  font-weight: 500;
}

.hot-tags span.hot i {
  color: #ff4d4f;
}

.hot-tags span:hover {
  background: #41b883;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 3px 8px rgba(65, 184, 131, 0.2);
}

.hot-tags span:hover i {
  color: white;
}

.search-history {
  margin-top: 18px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.clear-history {
  font-size: 12px;
  color: #999;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 5px;
}

.clear-history:hover {
  color: #ff4d4f;
}

.history-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 10px;
}

.history-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f9f9f9;
  border-radius: 18px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.history-item:hover {
  background: #f0f0f0;
}

.history-item i {
  color: #999;
  margin-right: 8px;
  font-size: 12px;
}

.history-item span {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 13px;
  color: #666;
}

.remove-btn {
  background: none;
  border: none;
  color: #ccc;
  cursor: pointer;
  padding: 2px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.2s;
}

.history-item:hover .remove-btn {
  opacity: 1;
}

.remove-btn:hover {
  color: #ff4d4f;
  background: #fff1f0;
}

.user-actions {
  display: flex;
  gap: 20px;
}

.cart-icon, .user-icon {
  position: relative;
  font-size: 22px;
  color: #333;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 50%;
}

.cart-icon:hover, .user-icon:hover {
  background: rgba(65, 184, 131, 0.1);
  color: #41b883;
  transform: translateY(-2px);
}

.cart-count {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #f56c6c;
  color: white;
  border-radius: 10px;
  min-width: 18px;
  height: 18px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(245, 108, 108, 0.4);
}

@keyframes pulse {
  0% {
    transform: translateY(-50%) scale(1);
  }
  50% {
    transform: translateY(-50%) scale(1.1);
  }
  100% {
    transform: translateY(-50%) scale(1);
  }
}

.carousel-section {
  margin-bottom: 30px;
}

.carousel-container {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-content {
  position: absolute;
  bottom: 40px;
  left: 40px;
  color: white;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.banner-content h2 {
  font-size: 28px;
  margin-bottom: 10px;
}

.banner-content p {
  font-size: 16px;
  margin-bottom: 15px;
  max-width: 60%;
}

.banner-btn {
  display: inline-block;
  padding: 8px 16px;
  background: #41b883;
  color: white;
  border-radius: 20px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.banner-btn:hover {
  background: #349d70;
  transform: translateY(-2px);
}

.category-nav {
  margin-bottom: 30px;
}

.category-container {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 15px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 15px 5px;
  border-radius: 8px;
  transition: all 0.3s;
}

.category-item:hover {
  background: #f5f7fa;
  transform: translateY(-2px);
}

.category-icon {
  width: 50px;
  height: 50px;
  background: #ecf5ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  font-size: 24px;
  color: #41b883;
  transition: all 0.3s;
}

.category-item:hover .category-icon {
  background: #d6eaff;
}

.category-name {
  font-size: 14px;
  color: #333;
}

.flash-deals, .hot-products, .new-products {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin: 0;
  margin-right: auto;
}

.view-more {
  color: #41b883;
  text-decoration: none;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.view-more i {
  margin-left: 5px;
  transition: transform 0.3s;
}

.view-more:hover i {
  transform: translateX(3px);
}

.countdown {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.countdown-label {
  font-size: 14px;
  color: #666;
  margin-right: 10px;
}

.countdown-time {
  display: flex;
  align-items: center;
}

.time-box {
  width: 30px;
  height: 30px;
  background: #333;
  color: white;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.time-separator {
  margin: 0 5px;
  color: #333;
  font-weight: 600;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background: white;
  transition: all 0.3s;
  cursor: pointer;
  position: relative;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.3;
  height: 42px;
}

.product-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 8px;
}

.current-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
  margin-right: 8px;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 13px;
}

.product-rating {
  color: #ff9e21;
  display: flex;
  align-items: center;
}

.product-rating i {
  margin-right: 3px;
}

.product-desc {
  font-size: 13px;
  color: #666;
  margin-top: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 38px;
}

.flash-deal-card {
  border: 1px solid #ffecb3;
}

.discount-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #f56c6c;
  color: white;
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 600;
}

.product-sales {
  font-size: 13px;
  color: #999;
}

.new-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: #41b883;
  color: white;
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 600;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
    gap: 15px;
    padding: 10px;
  }
  
  .search-bar {
    width: 100%;
  }
  
  .category-select {
    display: none;
  }
  
  .search-input-wrapper input {
    border-radius: 20px;
    border-left: 1px solid #e0e0e0;
  }
  
  .history-items {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
  
  .search-container {
    flex-direction: column;
    gap: 10px;
  }
  
  .search-bar {
    width: 100%;
  }
  
  .category-container {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style> 