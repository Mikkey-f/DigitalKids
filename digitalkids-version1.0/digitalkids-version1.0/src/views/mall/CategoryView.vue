<template>
  <div class="category-container">
    <SearchBar :cart-count="0" />

    <div class="category-content">
      <div class="category-sidebar">
        <div class="sidebar-header">
          <h3>商品分类</h3>
        </div>
        <ul class="category-menu">
          <li 
            v-for="(category, index) in parentCategories" 
            :key="category.id"
            :class="{ active: activeParentCategory === category.id }"
            @click="selectParentCategory(category)"
          >
            <div class="category-main">
              <span>{{ category.name }}</span>
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
        </ul>
      </div>

      <div class="category-products">
        <div v-if="loading" class="loading-container">
          <div class="loading-spinner"></div>
          <p>正在加载分类...</p>
        </div>
        
        <div v-else-if="showSubcategories" class="subcategories-container">
          <div class="category-header">
            <div class="category-title">
              <h2>{{ currentParentCategoryName }}</h2>
              <div class="category-bar"></div>
            </div>
          </div>
          
          <div class="subcategories-grid">
            <div v-for="subcategory in childCategories" :key="subcategory.id" 
                class="subcategory-card"
                @click="fetchProductsByCategory(subcategory.id, subcategory.name)">
              <div class="subcategory-image">
                <img :src="getCategoryImage(subcategory.id)" :alt="subcategory.name">
              </div>
              <div class="subcategory-name">{{ subcategory.name }}</div>
            </div>
            
            <div v-if="childCategories.length === 0" class="empty-subcategories">
              <i class="fas fa-exclamation-circle"></i>
              <p>该分类下暂无子分类</p>
              <button class="view-all-btn" @click="fetchProductsByCategory(activeParentCategory, currentParentCategoryName)">
                查看所有商品
              </button>
            </div>
          </div>
        </div>
        
        <div v-else-if="showProductList" class="product-list-container">
          <div class="category-header">
            <div class="category-title">
              <h2>{{ currentCategoryName }}</h2>
              <div class="category-bar"></div>
              <div class="breadcrumb">
                <span @click="backToSubcategories">分类</span>
                <i class="fas fa-angle-right"></i>
                <span>{{ currentCategoryName }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="productLoading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>正在加载商品...</p>
          </div>
          
          <div v-else-if="products.length === 0" class="empty-state">
            <i class="fas fa-box-open empty-icon"></i>
            <p>该分类下暂无商品</p>
            <p class="empty-tips">可以尝试浏览其他分类</p>
            <button class="back-btn" @click="backToSubcategories">返回分类</button>
          </div>
          
          <div v-else class="product-grid">
            <div v-for="product in products" :key="product.id" class="product-card">
              <router-link :to="`/mall/product/${product.id}`">
                <div class="product-image">
                  <img :src="product.mainImage || '/images/articles/疾病防御.jpg'" :alt="product.name">
                </div>
                <div class="product-info">
                  <h3 class="product-name">{{ product.name }}</h3>
                  <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
                  <div class="product-subtitle">{{ product.subtitle }}</div>
                </div>
              </router-link>
            </div>
          </div>
          
          <div class="pagination" v-if="products.length > 0">
            <button 
              :disabled="currentPage <= 1" 
              @click="changePage(currentPage - 1)"
              class="page-btn prev-btn"
            >
              <i class="fas fa-chevron-left"></i> 上一页
            </button>
            
            <span class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
            
            <button 
              :disabled="currentPage >= totalPages" 
              @click="changePage(currentPage + 1)"
              class="page-btn next-btn"
            >
              下一页 <i class="fas fa-chevron-right"></i>
            </button>
          </div>
        </div>
        
        <div v-else class="welcome-container">
          <div class="welcome-content">
            <div class="welcome-banner">
              <img src="/images/shop/banner.jpg" alt="商城欢迎页" />
              <div class="banner-overlay"></div>
            </div>
            <div class="welcome-text">
              <h2>欢迎来到商品分类页</h2>
              <div class="welcome-divider"></div>
              <p>请从左侧选择商品分类，浏览健康相关产品</p>
              <div class="welcome-guide">
                <div class="guide-item">
                  <div class="guide-icon">1</div>
                  <div class="guide-text">选择分类</div>
                </div>
                <div class="guide-arrow">→</div>
                <div class="guide-item">
                  <div class="guide-icon">2</div>
                  <div class="guide-text">选择子分类</div>
                </div>
                <div class="guide-arrow">→</div>
                <div class="guide-item">
                  <div class="guide-icon">3</div>
                  <div class="guide-text">浏览商品</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue';
import SearchBar from './components/SearchBar.vue';
import { getAllCategories, getProducts } from '@/api/product';

const activeParentCategory = ref(-1);
const activeChildCategory = ref(-1);
const categories = ref<any[]>([]);
const loading = ref(true);
const viewState = ref(1);
const products = ref<any[]>([]);
const productLoading = ref(false);
const currentPage = ref(1);
const pageSize = ref(12);
const total = ref(0);
const currentCategoryId = ref<number | null>(null);
const currentCategoryName = ref('');
const currentParentCategoryName = ref('');

const categoryMappings = computed(() => {
  const mappings = {
    childToParent: new Map<number, number>(),
    parentToChildren: new Map<number, number[]>(),
    idToName: new Map<number, string>(),
    nameToIds: new Map<string, number[]>()
  };

  categories.value.forEach(category => {
    const { id, parentId, name } = category;
    mappings.idToName.set(id, name);
    if (!mappings.nameToIds.has(name)) {
      mappings.nameToIds.set(name, []);
    }
    mappings.nameToIds.get(name)?.push(id);
    if (parentId !== 0) {
      mappings.childToParent.set(id, parentId);
    }
    if (!mappings.parentToChildren.has(parentId)) {
      mappings.parentToChildren.set(parentId, []);
    }
    mappings.parentToChildren.get(parentId)?.push(id);
  });
  
  return mappings;
});
const getAllChildCategories = (parentId: number): number[] => {
  const result: number[] = [];
  const directChildren = categoryMappings.value.parentToChildren.get(parentId) || [];
  result.push(...directChildren);
  directChildren.forEach(childId => {
    result.push(...getAllChildCategories(childId));
  });
  return result;
};

const getAllParentCategories = (childId: number): number[] => {
  const result: number[] = [];
  let currentId = childId;
  
  while (true) {
    const parentId = categoryMappings.value.childToParent.get(currentId);
    if (parentId === undefined || parentId === 0) break;
    
    result.push(parentId);
    currentId = parentId;
  }
  
  return result;
};

const addCategory = (id: number, parentId: number, name: string) => {
  const existingIndex = categories.value.findIndex(c => c.id === id);
  if (existingIndex >= 0) {
    categories.value[existingIndex] = { id, parentId, name };
    console.log(`更新分类: ID=${id}, 父ID=${parentId}, 名称=${name}`);
  } else {
    categories.value.push({ id, parentId, name });
    console.log(`添加新分类: ID=${id}, 父ID=${parentId}, 名称=${name}`);
  }
};

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));
const showWelcome = computed(() => viewState.value === 1);
const showSubcategories = computed(() => viewState.value === 2);
const showProductList = computed(() => viewState.value === 3);
const parentCategories = computed(() => {
  const uniqueNames = new Map();
  return categories.value
    .filter(category => category.parentId === 0)
    .filter(category => {
      if (uniqueNames.has(category.name)) {
        return false;
      }
      uniqueNames.set(category.name, true);
      return true;
    });
});

const childCategories = computed(() => {
  const uniqueNames = new Map();
  return categories.value
    .filter(category => category.parentId === activeParentCategory.value)
    .filter(category => {
      if (uniqueNames.has(category.name)) {
        return false;
      }
      uniqueNames.set(category.name, true);
      return true;
    });
});

const selectParentCategory = (category: any) => {
  activeParentCategory.value = category.id;
  currentParentCategoryName.value = category.name;
  viewState.value = 2; 
};

const backToSubcategories = () => {
  viewState.value = 2;
};

const useLocalData = () => {
  categories.value = [
    { id: 1, parentId: 0, name: "卫生护理" },
    { id: 2, parentId: 0, name: "药品" },
    { id: 3, parentId: 0, name: "可穿戴健康监测设备" },
    { id: 4, parentId: 0, name: "保健产品" },
    { id: 5, parentId: 0, name: "健康医疗器械" },
    { id: 7, parentId: 2, name: "药物" },
    { id: 8, parentId: 2, name: "中药" },
    { id: 9, parentId: 2, name: "西药" },
    { id: 10, parentId: 1, name: "消毒用品" },
    { id: 11, parentId: 1, name: "洗护用品" },
    { id: 12, parentId: 5, name: "心率仪" },
    { id: 13, parentId: 5, name: "血氧仪" },
    { id: 14, parentId: 5, name: "血压计" },
    { id: 15, parentId: 5, name: "体温计" },
    { id: 16, parentId: 5, name: "听诊器" },
    { id: 17, parentId: 3, name: "智能手环" },
    { id: 18, parentId: 3, name: "智能手表" },
    { id: 19, parentId: 3, name: "婴儿监视器" },
    { id: 20, parentId: 4, name: "维生素" },
    { id: 21, parentId: 4, name: "蛋白粉" },
    { id: 22, parentId: 4, name: "鱼油" }
  ];
};

const fetchCategories = async () => {
  loading.value = true;
  try {
    const response = await getAllCategories();
    if (response.code === 0 && response.data) {
      let flatCategories: any[] = [];
      const processCategories = (categories: any[], parentId = 0) => {
        if (!categories) return;
        
        categories.forEach(category => {
          const flatCategory = {
            id: category.id,
            parentId: parentId,
            name: category.name
          };
          flatCategories.push(flatCategory);
          if (category.subCategories && category.subCategories.length > 0) {
            processCategories(category.subCategories, category.id);
          }
        });
      };
      processCategories(response.data);
      mergeWithDefaultCategories(flatCategories);
    } else {
      useLocalData();
    }
  } catch (error) {
    useLocalData();
  } finally {
    loading.value = false;
  }
};

const mergeWithDefaultCategories = (apiCategories: any[]) => {
  categories.value = apiCategories;
  const defaultCategories = [
    { id: 1, parentId: 0, name: "卫生护理" },
    { id: 2, parentId: 0, name: "药品" },
    { id: 3, parentId: 0, name: "可穿戴健康监测设备" },
    { id: 4, parentId: 0, name: "保健产品" },
    { id: 5, parentId: 0, name: "健康医疗器械" },
    { id: 7, parentId: 2, name: "药物" },
    { id: 8, parentId: 2, name: "中药" },
    { id: 9, parentId: 2, name: "西药" },
    { id: 10, parentId: 1, name: "消毒用品" },
    { id: 11, parentId: 1, name: "洗护用品" },
    { id: 12, parentId: 5, name: "心率仪" },
    { id: 13, parentId: 5, name: "血氧仪" },
    { id: 14, parentId: 5, name: "血压计" },
    { id: 15, parentId: 5, name: "体温计" },
    { id: 16, parentId: 5, name: "听诊器" },
    { id: 17, parentId: 3, name: "智能手环" },
    { id: 18, parentId: 3, name: "智能手表" },
    { id: 19, parentId: 3, name: "婴儿监视器" },
    { id: 20, parentId: 4, name: "维生素" },
    { id: 21, parentId: 4, name: "蛋白粉" },
    { id: 22, parentId: 4, name: "鱼油" }
  ];
  
  defaultCategories.forEach(defaultCategory => {
    const existsById = categories.value.some(c => c.id === defaultCategory.id);
    if (!existsById) {
      addCategory(defaultCategory.id, defaultCategory.parentId, defaultCategory.name);
    }
    const existsByParentAndName = categories.value.some(
      c => c.parentId === defaultCategory.parentId && 
           c.name === defaultCategory.name && 
           c.id !== defaultCategory.id
    );
    if (!existsByParentAndName && !existsById) {
      addCategory(defaultCategory.id, defaultCategory.parentId, defaultCategory.name);
    }
  });
};

const fetchProductsByCategory = async (categoryId: number, categoryName: string) => {
  activeChildCategory.value = categoryId;
  
  viewState.value = 3; 
  productLoading.value = true;
  currentCategoryId.value = categoryId;
  currentCategoryName.value = categoryName;
  currentPage.value = 1;
  
  try {
    const response = await getProducts({
      categoryId,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    });
    
    if (response.code === 0 && response.data) {
      products.value = response.data.records || [];
      total.value = response.data.total || 0;
    } else {
      products.value = [];
      total.value = 0;
    }
  } catch (error) {
    products.value = [];
    total.value = 0;
  } finally {
    productLoading.value = false;
  }
};

const changePage = async (page: number) => {
  if (currentCategoryId.value) {
    currentPage.value = page;
    productLoading.value = true;
    
    try {
      const response = await getProducts({
        categoryId: currentCategoryId.value,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      });
      
      if (response.code === 0 && response.data) {
        products.value = response.data.records || [];
        total.value = response.data.total || 0;
      } else {
        products.value = [];
        total.value = 0;
      }
    } catch (error) {
      console.error('获取商品列表失败:', error);
      products.value = [];
    } finally {
      productLoading.value = false;
    }
  }
};

const getCategoryImage = (categoryId: number): string => {
  const defaultImages: {[key: number]: string} = {
    7: '/src/assets/images/goods/yw.png', // 药物
    8: '/src/assets/images/goods/zy.jpg', // 中药
    9: '/src/assets/images/goods/xy.jpg', // 西药
    10: '/src/assets/images/goods/xd.jpg', // 消毒用品
    11: '/src/assets/images/goods/xh.jpg', // 洗护用品
    12: '/src/assets/images/goods/xly.png', // 心率仪
    13: '/src/assets/images/goods/xyy.jpg', // 血氧仪
    14: '/src/assets/images/goods/xyyy.jpg', // 血压计
    15: '/src/assets/images/goods/twj.jpg', // 体温计
    16: '/src/assets/images/goods/tzq.jpg', // 听诊器
    17: '/src/assets/images/goods/sh.jpg', // 智能手环
    18: '/src/assets/images/goods/sb.jpg', // 智能手表
    19: '/src/assets/images/goods/js.jpg', // 婴儿监视器
    20: '/src/assets/images/goods/wss.jpg', // 维生素
    21: '/src/assets/images/goods/dbf.jpg', // 蛋白粉
    22: '/src/assets/images/goods/yy.jpg', // 鱼油
  };
  
  const parentCategoryImages: {[key: number]: string} = {
    1: '/src/assets/images/goods/xd.jpg', 
    2: '/src/assets/images/goods/xd.jpg', 
    3: '/src/assets/images/goods/xd.jpg',
    4: '/src/assets/images/goods/xd.jpg', 
    5: '/src/assets/images/goods/xd.jpg', 
  };
  
  if (defaultImages[categoryId]) {
    return defaultImages[categoryId];
  }
  
  const parentIds = getAllParentCategories(categoryId);
  for (const parentId of parentIds) {
    if (parentCategoryImages[parentId]) {
      return parentCategoryImages[parentId];
    }
  }
  
  return '';
};

onMounted(() => {
  fetchCategories();
});
</script>

<style scoped>
.category-container {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.category-content {
  display: flex;
  max-width: 1226px;
  margin: 20px auto;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  overflow: hidden;
}

.category-sidebar {
  width: 230px;
  background: linear-gradient(to bottom, #f9f9f9, #ffffff);
  border-right: 1px solid #f0f0f0;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fafafa;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

.category-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-menu li {
  position: relative;
  transition: all 0.3s;
}

.category-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.3s;
}

.category-menu li:hover .category-main {
  color: #42b983;
  background-color: #f9f9f9;
  padding-left: 25px;
}

.category-menu li.active .category-main {
  color: #42b983;
  background-color: #f0f9f5;
  font-weight: 500;
  border-left: 4px solid #42b983;
  padding-left: 16px;
}

.category-menu li.active .category-main::after {
  content: '';
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  width: 4px;
  background-color: #42b983;
  opacity: 0.3;
}

.category-main i {
  font-size: 12px;
  opacity: 0.5;
  transition: transform 0.3s;
}

.category-menu li:hover .category-main i,
.category-menu li.active .category-main i {
  opacity: 1;
  transform: translateX(3px);
}

.category-products {
  flex: 1;
  padding: 25px;
  background-color: #fff;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #666;
}

.loading-spinner {
  border: 4px solid rgba(66, 185, 131, 0.1);
  border-radius: 50%;
  border-top: 4px solid #42b983;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.subcategories-container {
  width: 100%;
}

.subcategories-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  justify-content: flex-start;
}

.subcategory-card {
  width: calc(20% - 20px);
  min-width: 150px;
  background-color: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

.subcategory-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  border-color: #42b983;
}

.subcategory-image {
  height: 140px;
  width: 100%;
  overflow: hidden;
  border: 1px solid #f0f0f0;
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  justify-content: center;
}

.subcategory-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.subcategory-card:hover .subcategory-image img {
  transform: scale(1.05);
}

.subcategory-name {
  padding: 10px;
  text-align: center;
  font-size: 14px;
  color: #333;
  font-weight: 500;
  background-color: #fff;
  border-top: 1px solid #f5f5f5;
}

.empty-subcategories {
  width: 100%;
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.empty-subcategories i {
  font-size: 40px;
  color: #ddd;
  margin-bottom: 15px;
}

.view-all-btn, .back-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.view-all-btn:hover, .back-btn:hover {
  background-color: #3aa876;
  transform: translateY(-2px);
}

.product-list-container {
  width: 100%;
}

.category-header {
  margin-bottom: 30px;
}

.category-title {
  position: relative;
}

.category-title h2 {
  font-size: 24px;
  margin: 0;
  padding-bottom: 12px;
  color: #333;
  font-weight: 500;
}

.category-bar {
  height: 3px;
  width: 50px;
  background-color: #42b983;
  margin-top: 5px;
}

.breadcrumb {
  margin-top: 10px;
  font-size: 14px;
  color: #999;
}

.breadcrumb span:first-child {
  cursor: pointer;
  color: #42b983;
}

.breadcrumb span:first-child:hover {
  text-decoration: underline;
}

.breadcrumb i {
  margin: 0 8px;
  font-size: 12px;
}

.empty-state {
  text-align: center;
  padding: 50px 30px;
  color: #999;
  background-color: #fafafa;
  border-radius: 10px;
  border: 1px dashed #ddd;
}

.empty-icon {
  font-size: 48px;
  color: #ccc;
  margin-bottom: 15px;
}

.empty-tips {
  font-size: 14px;
  color: #aaa;
  margin-top: 10px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
}

.product-card {
  background-color: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border-color: #e0e0e0;
}

.product-card a {
  display: block;
  text-decoration: none;
  color: inherit;
}

.product-image {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  padding: 10px;
  overflow: hidden;
  border-bottom: 1px solid #f0f0f0;
}

.product-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
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
  margin: 0 0 10px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 500;
}

.product-price {
  font-size: 18px;
  color: #42b983;
  font-weight: 600;
  margin-bottom: 5px;
}

.product-subtitle {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 5px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40px;
}

.page-btn {
  padding: 10px 20px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  cursor: pointer;
  border-radius: 6px;
  outline: none;
  transition: all 0.2s;
  font-size: 14px;
  display: flex;
  align-items: center;
  color: #555;
}

.page-btn i {
  margin: 0 5px;
}

.page-btn:hover:not(:disabled) {
  border-color: #42b983;
  color: #42b983;
  background-color: #f0f9f5;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
  background-color: #f5f5f5;
}

.page-info {
  margin: 0 20px;
  color: #666;
  font-size: 14px;
}

.welcome-container {
  width: 100%;
  padding: 20px;
}

.welcome-content {
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0,0,0,0.05);
}

.welcome-banner {
  position: relative;
  width: 100%;
  height: 250px;
  overflow: hidden;
}

.welcome-banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 5s ease;
}

.welcome-container:hover .welcome-banner img {
  transform: scale(1.1);
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.4));
}

.welcome-text {
  text-align: center;
  padding: 30px 20px 40px;
}

.welcome-text h2 {
  font-size: 28px;
  margin-bottom: 15px;
  color: #333;
  font-weight: 500;
}

.welcome-divider {
  width: 60px;
  height: 3px;
  background-color: #42b983;
  margin: 0 auto 20px;
}

.welcome-text p {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.welcome-guide {
  display: flex;
  justify-content: center;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
  flex-wrap: wrap;
}

.guide-item {
  text-align: center;
  padding: 10px 15px;
}

.guide-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #42b983;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  margin: 0 auto 10px;
}

.guide-text {
  font-size: 14px;
  color: #666;
}

.guide-arrow {
  color: #bbb;
  font-size: 20px;
  margin: 0 10px;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .subcategory-card {
    width: calc(25% - 20px);
  }
}

@media (max-width: 900px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .subcategory-card {
    width: calc(33.33% - 20px);
  }
  
  .welcome-guide {
    flex-direction: column;
  }
  
  .guide-arrow {
    transform: rotate(90deg);
    margin: 5px 0;
  }
}

@media (max-width: 768px) {
  .category-content {
    flex-direction: column;
  }
  
  .category-sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .category-menu {
    display: flex;
    flex-wrap: wrap;
    padding: 10px;
  }
  
  .category-main {
    padding: 8px 15px;
    margin: 5px;
    border: 1px solid #f0f0f0;
    border-radius: 20px;
  }
  
  .category-menu li.active .category-main {
    border-left: none;
    background-color: #42b983;
    color: white;
  }
  
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .pagination {
    flex-direction: column;
    gap: 10px;
  }
  
  .welcome-banner {
    height: 180px;
  }
  
  .subcategory-card {
    width: calc(50% - 15px);
  }
}

@media (max-width: 480px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
  
  .welcome-text h2 {
    font-size: 22px;
  }
  
  .subcategory-card {
    width: 100%;
    min-width: auto;
  }
}
</style> 