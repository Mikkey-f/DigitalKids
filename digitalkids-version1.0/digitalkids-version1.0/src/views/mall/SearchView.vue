<template>
  <div class="search-result-container">
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
            <span>{{ selectedCategory.name || '全部分类' }}</span>
            <i class="fas fa-angle-down"></i>
            <div class="category-dropdown" v-if="showCategoryDropdown">
              <div 
                class="category-item" 
                v-for="cat in searchCategories" 
                :key="cat.id"
                @click="selectCategory(cat)"
              >
                {{ cat.name }}
              </div>
            </div>
          </div>

          <div class="search-input-wrapper">
            <input 
              type="text" 
              v-model="searchQuery" 
              @keyup.enter="handleSearch"
              placeholder="请输入关键词" />
              
            <button class="search-btn" @click="handleSearch">
              <i class="fas fa-search"></i>
            </button>
          </div>
        </div>

        <div class="user-actions">
          <div class="cart-icon">
            <router-link to="/mall/cart">
              <i class="fas fa-shopping-cart"></i>
              <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
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

    <div class="filter-section">
      <div class="filter-container">
        <div class="filter-group categories">
          <div class="filter-label">分类：</div>
          <div class="filter-options">
            <div 
              class="filter-option" 
              :class="{ active: !activeCategory }"
              @click="filterByCategory(null)"
            >
              全部
            </div>
            <div 
              v-for="cat in categories" 
              :key="cat.id" 
              class="filter-option"
              :class="{ active: activeCategory === cat.id }"
              @click="filterByCategory(cat.id)"
            >
              {{ cat.name }}
            </div>
          </div>
        </div>

        <div class="filter-group sort">
          <div class="filter-label">排序：</div>
          <div class="filter-options">
            <div 
              class="filter-option" 
              :class="{ active: sortOption === 'default' }"
              @click="changeSortOption('default')"
            >
              默认
            </div>
            <div 
              class="filter-option" 
              :class="{ active: sortOption === 'sales' }"
              @click="changeSortOption('sales')"
            >
              销量优先
            </div>
            <div 
              class="filter-option price-option" 
              :class="{ 
                active: sortOption === 'priceAsc' || sortOption === 'priceDesc',
                'price-asc': sortOption === 'priceAsc',
                'price-desc': sortOption === 'priceDesc'
              }"
              @click="togglePriceSort"
            >
              价格
              <i class="fas fa-sort"></i>
            </div>
            <div 
              class="filter-option" 
              :class="{ active: sortOption === 'newest' }"
              @click="changeSortOption('newest')"
            >
              最新
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="search-results">
      <div class="results-header">
        <div class="results-count">
          共找到 <span class="highlight">{{ total }}</span> 个相关商品
        </div>
      </div>

      <div v-if="loading" class="loading-state">
        <div class="loader"></div>
        <div class="loading-text">正在加载商品...</div>
      </div>

      <div v-else-if="products.length === 0" class="empty-state">
        <div class="empty-icon">
          <i class="fas fa-search"></i>
        </div>
        <div class="empty-text">抱歉，没有找到符合条件的商品</div>
        <div class="empty-suggestions">
          <p>建议您：</p>
          <ul>
            <li>检查输入是否正确</li>
            <li>使用更简短的搜索词</li>
            <li>尝试其他分类</li>
          </ul>
        </div>
      </div>

      <div v-else class="product-grid">
        <div 
          v-for="(product, index) in products" 
          :key="product.id || product.productId || index"
          class="product-card"
          @click="goToProduct(product.id || product.productId)"
        >
          <div class="product-image">
            <img 
              :src="getProductImage(product)"
              :alt="getProductName(product)"
            >
          </div>
          <div class="product-info">
            <h3 class="product-name" v-html="getProductName(product)"></h3>
            <div class="product-price">
              <span class="current-price">¥{{ getPrice(product) }}</span>
              <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toFixed(2) }}</span>
            </div>
            <div class="product-meta">
              <div class="product-rating">
                <i class="fas fa-star"></i>
                <span>{{ product.rating || product.score || '5.0' }}</span>
              </div>
              <div class="product-sales">{{ getSales(product) }}件已售</div>
            </div>
            <div class="product-shop">{{ product.storeName || product.store || product.shopName || '官方自营' }}</div>
          </div>
        </div>
      </div>

      <div class="pagination-container" v-if="total > 0">
        <el-pagination
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { searchProducts } from '@/api/search';
import { getAllCategories } from '@/api/product';

const route = useRoute();
const router = useRouter();

const products = ref<any[]>([]);
const total = ref(0);
const loading = ref(true);
const cartCount = ref(0);

const categories = ref<any[]>([]);
const searchCategories = ref<any[]>([]);
const showCategoryDropdown = ref(false);
const selectedCategory = ref<any>({});
const activeCategory = ref<number | null>(null);

const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(20);
const sortOption = ref('default');
const sortField = ref('');
const sortOrder = ref('');

const fetchCategories = async () => {
  try {
    const res = await getAllCategories();
    if (res.code === 0 && res.data) {
      categories.value = res.data;
      searchCategories.value = [{ id: 0, name: '全部分类' }, ...res.data];
    }
  } catch (error) {
    ElMessage.error('获取分类数据失败');
  }
};

const searchProductList = async () => {
  loading.value = true;
  products.value = [];
  
  try {
    const params: any = {
      keyword: searchQuery.value || '',
      pageReq: {
        current: currentPage.value,
        pageSize: pageSize.value,
        sortField: sortField.value,
        sortOrder: sortOrder.value
      }
    };
    
    if (activeCategory.value) {
      params.categoryId = activeCategory.value;
    }
    
    const res = await searchProducts(params);
    
    if (res.code === 1) {
      let productList: any[] = [];
      let totalCount = 0;
      
      if (Array.isArray(res.data)) {
        productList = res.data;
        totalCount = res.data.length;
      } else if (res.data && typeof res.data === 'object') {
        if (res.data.records && Array.isArray(res.data.records)) {
          productList = res.data.records;
          totalCount = res.data.total || productList.length;
        } else {
          const possibleListProps = ['list', 'items', 'products', 'data', 'content', 'result', 'results'];
          for (const prop of possibleListProps) {
            if (res.data[prop] && Array.isArray(res.data[prop])) {
              productList = res.data[prop];
              
              const totalProps = ['total', 'totalCount', 'count', 'totalElements', 'totalItems'];
              for (const totalProp of totalProps) {
                if (res.data[totalProp] !== undefined) {
                  totalCount = res.data[totalProp];
                  break;
                }
              }
              
              if (totalCount === 0) {
                totalCount = productList.length;
              }
              
              break;
            }
          }
          
          if (productList.length === 0) {
            for (const key in res.data) {
              if (Array.isArray(res.data[key])) {
                productList = res.data[key];
                totalCount = productList.length;
                break;
              }
            }
          }
        }
        
        if (productList.length === 0 && Object.keys(res.data).length > 0) {
          if (isProductLike(res.data)) {
            productList = [res.data];
            totalCount = 1;
          }
        }
      }
      
      productList = productList.filter(item => item && typeof item === 'object');
      
      products.value = productList;
      total.value = totalCount;
    } else {
      ElMessage.error(res.msg || '搜索商品失败');
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试');
  } finally {
    loading.value = false;
  }
};

const isProductLike = (obj: any): boolean => {
  if (!obj || typeof obj !== 'object') return false;
  
  const productProps = ['id', 'productId', 'name', 'title', 'price', 'image', 'img', 'mainImage'];
  return productProps.some(prop => obj[prop] !== undefined);
};

const getProductImage = (product: any): string => {
  const imageFields = ['mainImage', 'image', 'imageUrl', 'img', 'cover', 'thumbnail', 'pic', 'picture'];
  
  for (const field of imageFields) {
    if (product[field]) {
      return product[field];
    }
  }
  
  return '';
};

const getProductName = (product: any): string => {
  const nameFields = ['name', 'title', 'productName', 'goodsName'];
  
  for (const field of nameFields) {
    if (product[field]) {
      return product[field];
    }
  }
  
  return '商品';
};

const handleSearch = () => {
  currentPage.value = 1;
  router.push({
    path: '/mall/search',
    query: {
      q: searchQuery.value,
      category: selectedCategory.value?.id || 0
    }
  });
  searchProductList();
};

const toggleCategoryDropdown = () => {
  showCategoryDropdown.value = !showCategoryDropdown.value;
};

const selectCategory = (category: any) => {
  selectedCategory.value = category;
  showCategoryDropdown.value = false;
};

const filterByCategory = (categoryId: number | null) => {
  activeCategory.value = categoryId;
  currentPage.value = 1;
  searchProductList();
};

const changeSortOption = (option: string) => {
  sortOption.value = option;
  
  switch (option) {
    case 'default':
      sortField.value = '';
      sortOrder.value = '';
      break;
    case 'sales':
      sortField.value = 'sold';
      sortOrder.value = 'desc';
      break;
    case 'newest':
      sortField.value = 'createTime';
      sortOrder.value = 'desc';
      break;
  }
  
  currentPage.value = 1;
  searchProductList();
};

const togglePriceSort = () => {
  if (sortOption.value === 'priceAsc') {
    sortOption.value = 'priceDesc';
    sortField.value = 'price';
    sortOrder.value = 'desc';
  } else {
    sortOption.value = 'priceAsc';
    sortField.value = 'price';
    sortOrder.value = 'asc';
  }
  
  currentPage.value = 1;
  searchProductList();
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  searchProductList();
};

const goToProduct = (id: number) => {
  router.push(`/mall/product/${id}`);
};

const getSearchParamsFromUrl = () => {
  const query = route.query;
  
  if (query.q) {
    searchQuery.value = query.q as string;
  }
  
  if (query.category) {
    const categoryId = parseInt(query.category as string);
    if (!isNaN(categoryId) && categoryId > 0) {
      activeCategory.value = categoryId;
      
      const category = categories.value.find(c => c.id === categoryId);
      if (category) {
        selectedCategory.value = category;
      }
    }
  }
  
  if (query.page) {
    const page = parseInt(query.page as string);
    if (!isNaN(page) && page > 0) {
      currentPage.value = page;
    }
  }
  
  if (query.sort) {
    sortOption.value = query.sort as string;
    
    switch (sortOption.value) {
      case 'priceAsc':
        sortField.value = 'price';
        sortOrder.value = 'asc';
        break;
      case 'priceDesc':
        sortField.value = 'price';
        sortOrder.value = 'desc';
        break;
      case 'sales':
        sortField.value = 'sold';
        sortOrder.value = 'desc';
        break;
      case 'newest':
        sortField.value = 'createTime';
        sortOrder.value = 'desc';
        break;
    }
  }
};

watch(
  () => route.query,
  () => {
    getSearchParamsFromUrl();
    searchProductList();
  },
  { deep: true }
);

onMounted(async () => {
  await fetchCategories();
  getSearchParamsFromUrl();
  await searchProductList();
  
  document.addEventListener('click', (e: MouseEvent) => {
    const target = e.target as HTMLElement;
    if (!target.closest('.category-select')) {
      showCategoryDropdown.value = false;
    }
  });
});

const getPrice = (product: any) => {
  const priceFields = ['price', 'currentPrice', 'salePrice', 'retailPrice', 'discountPrice'];
  
  for (const field of priceFields) {
    if (product[field] !== undefined && product[field] !== null) {
      return Number(product[field]).toFixed(2);
    }
  }
  
  return '0.00';
};

const getSales = (product: any) => {
  const salesFields = ['sold', 'sales', 'saleCount', 'sellCount', 'soldCount', 'volume'];
  
  for (const field of salesFields) {
    if (product[field] !== undefined && product[field] !== null) {
      return product[field];
    }
  }
  
  return 0;
};

const getStock = (product: any) => {
  const stockFields = ['stock', 'inventory', 'quantity', 'stockQuantity', 'availableStock', 'inventoryCount'];
  
  for (const field of stockFields) {
    if (product[field] !== undefined && product[field] !== null) {
      return product[field];
    }
  }
  
  return 0;
};
</script>

<style scoped>
.search-result-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40px;
}

/* 搜索区域样式 */
.search-section {
  background-color: #fff;
  padding: 15px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
}

.search-container {
  max-width: 1226px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
}

.logo {
  flex: 0 0 auto;
  margin-right: 20px;
}

.home-icon {
  font-size: 22px;
  color: #ff6700;
}

.search-bar {
  flex: 1;
  display: flex;
  height: 40px;
  border: 2px solid #ff6700;
  border-radius: 20px;
  overflow: hidden;
}

.category-select {
  position: relative;
  width: 110px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
  border-right: 1px solid #e0e0e0;
  cursor: pointer;
  padding: 0 10px;
}

.category-select span {
  margin-right: 5px;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.category-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;
  max-height: 300px;
  overflow-y: auto;
}

.category-item {
  padding: 8px 12px;
  font-size: 14px;
  cursor: pointer;
}

.category-item:hover {
  background-color: #f5f5f5;
}

.search-input-wrapper {
  flex: 1;
  display: flex;
  position: relative;
}

.search-input-wrapper input {
  flex: 1;
  border: none;
  outline: none;
  padding: 0 15px;
  font-size: 14px;
}

.search-btn {
  width: 50px;
  background-color: #ff6700;
  color: white;
  border: none;
  outline: none;
  cursor: pointer;
  font-size: 16px;
}

.user-actions {
  display: flex;
  margin-left: 20px;
}

.cart-icon, .user-icon {
  font-size: 20px;
  margin-left: 15px;
  position: relative;
  color: #333;
}

.cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #ff6700;
  color: white;
  font-size: 12px;
  min-width: 16px;
  height: 16px;
  text-align: center;
  line-height: 16px;
  border-radius: 8px;
  padding: 0 4px;
}

.filter-section {
  background-color: #fff;
  padding: 10px 0;
  margin-top: 1px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.filter-container {
  max-width: 1226px;
  margin: 0 auto;
  padding: 0 15px;
}

.filter-group {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.filter-label {
  width: 70px;
  color: #666;
  font-size: 14px;
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  flex: 1;
}

.filter-option {
  padding: 5px 15px;
  margin-right: 10px;
  margin-bottom: 5px;
  background-color: #f5f5f5;
  border-radius: 15px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-option:hover {
  color: #ff6700;
}

.filter-option.active {
  background-color: #ff6700;
  color: white;
}

.price-option {
  display: flex;
  align-items: center;
}

.price-option i {
  margin-left: 5px;
}

.price-option.price-asc i:before {
  content: "\f0d8";
}

.price-option.price-desc i:before {
  content: "\f0d7";
}

.search-results {
  max-width: 1226px;
  margin: 20px auto;
  padding: 0 15px;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.results-count {
  font-size: 14px;
  color: #666;
}

.highlight {
  color: #ff6700;
  font-weight: bold;
  margin: 0 5px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 15px;
}

.product-card {
  background-color: #fff;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image {
  height: 0;
  padding-top: 100%;
  position: relative;
  overflow: hidden;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 14px;
  margin: 0 0 8px;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-price {
  margin-bottom: 8px;
}

.current-price {
  color: #ff6700;
  font-size: 16px;
  font-weight: bold;
}

.original-price {
  color: #999;
  font-size: 12px;
  text-decoration: line-through;
  margin-left: 5px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
  margin-bottom: 5px;
}

.product-rating {
  display: flex;
  align-items: center;
}

.product-rating i {
  color: #ff6700;
  margin-right: 3px;
}

.product-shop {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
}

.loader {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #ff6700;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  color: #666;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  text-align: center;
}

.empty-icon {
  font-size: 40px;
  color: #ddd;
  margin-bottom: 20px;
}

.empty-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.empty-suggestions {
  font-size: 14px;
  color: #999;
  text-align: left;
}

.empty-suggestions p {
  margin-bottom: 5px;
}

.empty-suggestions ul {
  padding-left: 20px;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 992px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .category-select {
    width: 80px;
  }
}

@media (max-width: 576px) {
  .product-grid {
    grid-template-columns: repeat(1, 1fr);
  }
  
  .category-select {
    display: none;
  }
}
</style> 