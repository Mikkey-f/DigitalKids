<template>
    <div class="search-section">
      <div class="search-container">
        <div class="logo">
          <router-link to="/mall/recommend">
            <i class="fas fa-home"></i>
          </router-link>
        </div>
        <div class="search-bar">
          <div class="category-dropdown" @click="toggleCategoryDropdown">
            <span class="dropdown-text">{{ selectedCategory || '全部分类' }}</span>
            <i class="fas fa-angle-down"></i>
            <div class="dropdown-menu" v-show="showCategoryDropdown">
              <div 
                class="dropdown-item" 
                v-for="(category, index) in categories" 
                :key="index"
                @click="selectCategory(category)"
              >
                {{ category }}
              </div>
            </div>
          </div>
          <input 
            type="text" 
            placeholder="搜索商品" 
            v-model="searchQuery"
            @focus="showSearchSuggestions = true"
            @keyup.enter="handleSearch"
          />
          <button class="voice-btn" @click="startVoiceSearch">
            <i class="fas fa-microphone"></i>
          </button>
          <button class="search-btn" @click="handleSearch">
            <i class="fas fa-search"></i>
          </button>
          <div class="search-suggestions" v-show="showSearchSuggestions">
            <div class="suggestions-section">
              <h3 class="suggestions-title">热门搜索</h3>
              <div class="hot-tags">
                <span 
                  class="hot-tag" 
                  v-for="(tag, index) in hotSearches" 
                  :key="index"
                  @click="applySearch(tag)"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="suggestions-section" v-if="searchHistory.length > 0">
              <div class="history-header">
                <h3 class="suggestions-title">搜索历史</h3>
                <button class="clear-btn" @click.stop="clearHistory">
                  <i class="fas fa-trash-alt"></i>
                </button>
              </div>
              <div class="history-list">
                <div 
                  class="history-item" 
                  v-for="(item, index) in searchHistory" 
                  :key="index"
                  @click="applySearch(item)"
                >
                  <i class="fas fa-history"></i>
                  <span>{{ item }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="user-actions">
          <div class="cart-icon">
            <router-link to="/mall/cart">
              <i class="fas fa-shopping-cart"></i>
              <span class="cart-count" v-if="localCartCount > 0">{{ localCartCount }}</span>
            </router-link>
          </div>
          <div class="user-icon">
            <router-link to="/user/profile">
              <i class="fas fa-user"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, onUnmounted } from 'vue';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  const props = defineProps({
    cartCount: {
      type: Number,
      default: 0
    }
  });
  const searchQuery = ref('');
  const showSearchSuggestions = ref(false);
  const searchHistory = ref<string[]>([]);
  const localCartCount = ref(props.cartCount);
  const showCategoryDropdown = ref(false);
  const selectedCategory = ref('');
  const categories = ref([
    '全部分类',
    '宝宝用品',
    '婴儿电器',
    '跨境购',
    '女神专区',
    '奶粉尿裤',
    '食品生鲜',
    '日用百货',
    '营养健康',
    '零食辅食',
    '寝居家纺',
    '童装童鞋',
    '出行装备'
  ]);
  const hotSearches = ref([
    '奶粉', '纸尿裤', '奶瓶', '益生菌', '婴儿车', '儿童玩具'
  ]);
  
  const getCartCountFromStorage = () => {
    try {
      const cartItemsStr = localStorage.getItem('cartItems');
      if (cartItemsStr) {
        const cartItems = JSON.parse(cartItemsStr);
        if (Array.isArray(cartItems)) {
          localCartCount.value = cartItems.length;
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
  
  const handleSearch = () => {
    if (!searchQuery.value.trim()) return;
    if (!searchHistory.value.includes(searchQuery.value)) {
      searchHistory.value.unshift(searchQuery.value);
      if (searchHistory.value.length > 10) {
        searchHistory.value.pop();
      }
      localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value));
    }
    
    router.push({
      path: '/mall/search',
      query: { 
        q: searchQuery.value,
        category: selectedCategory.value || '全部分类'
      }
    });
    showSearchSuggestions.value = false;
  };
  const applySearch = (term: string) => {
    searchQuery.value = term;
    handleSearch();
  };
  
  const toggleCategoryDropdown = () => {
    showCategoryDropdown.value = !showCategoryDropdown.value;
  };
  
  const selectCategory = (category: string) => {
    selectedCategory.value = category === '全部分类' ? '' : category;
    showCategoryDropdown.value = false;
  };
  
  const clearHistory = () => {
    searchHistory.value = [];
    localStorage.removeItem('searchHistory');
  };
  
  const startVoiceSearch = () => {
    alert('语音搜索功能即将推出！');
  };
  
  const handleClickOutside = (event: MouseEvent) => {
    const target = event.target as HTMLElement;
    if (!target.closest('.search-bar') && !target.closest('.category-dropdown')) {
      showSearchSuggestions.value = false;
      showCategoryDropdown.value = false;
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
  });
  
  onUnmounted(() => {
    window.removeEventListener('storage', handleStorageChange);
    document.removeEventListener('click', handleClickOutside);
  });
  </script>
  
  <style scoped>
  .search-section {
    background-color: #fff;
    padding: 10px 0;
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    position: sticky;
  }
  
  .search-container {
    max-width: 1226px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    padding: 0 10px;
  }
  
  .logo {
    margin-right: 20px;
  }
  
  .logo i {
    font-size: 28px;
    color: #45b787;
  }
  
  .search-bar {
    flex: 1;
    max-width: 600px;
    display: flex;
    height: 40px;
    border: 1px solid #e0e0e0;
    border-radius: 20px;
    overflow: hidden;
    background-color: #f5f5f5;
    position: relative;
  }
  
  .category-dropdown {
    display: flex;
    align-items: center;
    padding: 0 15px;
    background-color: #f5f5f5;
    border-right: 1px solid #e0e0e0;
    cursor: pointer;
    position: relative;
    min-width: 100px;
    justify-content: center;
  }
  
  .dropdown-text {
    font-size: 14px;
    color: #666;
    margin-right: 5px;
    white-space: nowrap;
  }
  
  .dropdown-menu {
    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
    background-color: #fff;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    z-index: 101;
    max-height: 300px;
    overflow-y: auto;
  }
  
  .dropdown-item {
    padding: 8px 15px;
    font-size: 14px;
    color: #333;
    cursor: pointer;
  }
  
  .dropdown-item:hover {
    background-color: #f5f5f5;
  }
  
  .search-bar input {
    flex: 1;
    border: none;
    padding: 0 15px;
    font-size: 14px;
    background-color: transparent;
  }
  
  .search-bar input:focus {
    outline: none;
  }
  
  .voice-btn {
    width: 40px;
    height: 100%;
    background-color: transparent;
    border: none;
    color: #999;
    cursor: pointer;
    transition: color 0.3s;
  }
  
  .voice-btn:hover {
    color: #45b787;
  }
  
  .search-btn {
    width: 50px;
    height: 100%;
    background-color: #45b787;
    border: none;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .search-btn:hover {
    background-color: #3c9e75;
  }
  
  .search-suggestions {
    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
    background-color: #fff;
    border: 1px solid #e0e0e0;
    border-radius: 0 0 8px 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    padding: 15px;
    z-index: 100;
    margin-top: 5px;
  }
  
  .suggestions-title {
    font-size: 14px;
    font-weight: 600;
    color: #333;
    margin: 0 0 10px 0;
  }
  
  .suggestions-section {
    margin-bottom: 15px;
  }
  
  .suggestions-section:last-child {
    margin-bottom: 0;
  }
  
  .hot-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .hot-tag {
    display: inline-block;
    padding: 6px 12px;
    background-color: #f5f5f5;
    border-radius: 16px;
    font-size: 12px;
    color: #666;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .hot-tag:hover {
    background-color: #e0e0e0;
    color: #45b787;
  }
  
  .history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
  }
  
  .clear-btn {
    background: none;
    border: none;
    color: #999;
    font-size: 14px;
    cursor: pointer;
  }
  
  .clear-btn:hover {
    color: #ff6700;
  }
  
  .history-item {
    display: flex;
    align-items: center;
    padding: 8px 0;
    cursor: pointer;
    font-size: 14px;
    color: #666;
  }
  
  .history-item:hover {
    color: #45b787;
  }
  
  .history-item i {
    margin-right: 8px;
    font-size: 12px;
  }
  
  .user-actions {
    display: flex;
    margin-left: 20px;
  }
  
  .cart-icon, .user-icon {
    font-size: 22px;
    margin-left: 20px;
    position: relative;
  }
  
  .cart-icon a, .user-icon a {
    color: #666;
    text-decoration: none;
    display: block;
  }
  
  .cart-count {
    position: absolute;
    top: -8px;
    right: -8px;
    background-color: #ff5a5a;
    color: white;
    font-size: 12px;
    border-radius: 10px;
    min-width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    padding: 0 4px;
  }
  
  @media (max-width: 768px) {
    .category-dropdown {
      display: none;
    }
    
    .search-bar {
      max-width: none;
    }
    
    .voice-btn {
      display: none;
    }
    
    .logo {
      margin-right: 10px;
    }
    
    .user-actions {
      margin-left: 10px;
    }
  }
  
  @media (max-width: 480px) {
    .search-container {
      padding: 0 5px;
    }
    
    .search-bar {
      max-width: none;
    }
    
    .logo i {
      font-size: 24px;
    }
    
    .cart-icon, .user-icon {
      font-size: 20px;
      margin-left: 15px;
    }
  }
  </style>