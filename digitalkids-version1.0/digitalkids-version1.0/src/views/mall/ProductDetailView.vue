<template>
  <div class="product-detail-container">
    <div class="search-section">
      <div class="search-container">
        <div class="logo">
          <router-link to="/mall/recommend">
            <i class="fas fa-home"></i>
          </router-link>
        </div>
        <div class="search-bar">
          <input 
            type="text" 
            v-model="searchQuery"
            placeholder="搜索商品" 
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">
            <i class="fas fa-search"></i>
          </button>
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

    <div class="detail-main-container">
      <div class="breadcrumb-section">
        <div class="breadcrumb-container">
          <router-link to="/mall/recommend">首页</router-link>
          <span class="separator"> > </span>
          <router-link to="/mall/category">分类</router-link>
          <span class="separator"> > </span>
          <span class="current">{{ product?.name || '商品详情' }}</span>
        </div>
      </div>

      <div v-if="loading" class="loading-state">
        <div class="loader"></div>
        <div class="loading-text">加载商品信息...</div>
      </div>

      <div v-else-if="!product" class="error-state">
        <div class="error-icon">
          <i class="fas fa-exclamation-circle"></i>
        </div>
        <div class="error-text">商品信息加载失败，请刷新页面重试</div>
        <button @click="fetchProductData" class="retry-btn">重新加载</button>
      </div>

      <div v-else class="product-content">
        <div class="product-info-section">
          <div class="product-gallery">
            <div class="main-image">
              <img 
                :src="product.images && product.images.length > 0 
                  ? product.images[selectedImageIndex] 
                  : product.mainImageUrl || '/public/images/articles/疾病防御.jpg'" 
                :alt="product.name" 
              />
            </div>
            <div class="thumbnail-list" v-if="product.images && product.images.length > 1">
              <div 
                v-for="(image, index) in product.images" 
                :key="index" 
                class="thumbnail"
                :class="{ active: selectedImageIndex === index }"
                @click="selectedImageIndex = index"
              >
                <img :src="image" :alt="`${product.name} 缩略图 ${index + 1}`" />
              </div>
            </div>
          </div>

          <div class="product-info">
            <div class="product-header">
              <h1 class="product-name">{{ product.name }}</h1>
              <div class="product-actions">
                <button 
                  class="favorite-btn" 
                  :class="{ active: isFavorite }"
                  @click="toggleFavorite"
                >
                  <i class="fas" :class="isFavorite ? 'fa-heart' : 'fa-heart-o'"></i>
                  <span class="action-text">{{ isFavorite ? '已收藏' : '收藏' }}</span>
                </button>
                <button 
                  class="like-btn"
                  :class="{ active: isLiked }"
                  @click="toggleLike"
                >
                  <i class="fas" :class="isLiked ? 'fa-thumbs-up' : 'fa-thumbs-o-up'"></i>
                  <span class="like-count">{{ likeCount }}</span>
                </button>
              </div>
            </div>
            
            <div class="product-brief">{{ product.subtitle }}</div>
            
            <div class="product-price-section">
              <div class="price-info">
                <div class="current-price">¥{{ product.price.toFixed(2) }}</div>
              </div>
            </div>
            
            <div class="quantity-section">
              <div class="quantity-label">数量</div>
              <div class="quantity-selector">
                <button 
                  class="quantity-btn" 
                  @click="decreaseQuantity"
                  :disabled="quantity <= 1"
                >-</button>
                <input 
                  type="number" 
                  class="quantity-input" 
                  v-model="quantity" 
                  min="1" 
                  max="99"
                  @input="validateQuantity"
                />
                <button 
                  class="quantity-btn" 
                  @click="increaseQuantity"
                  :disabled="quantity >= 99"
                >+</button>
              </div>
            </div>
            
            <div class="action-buttons">
              <button class="add-to-cart-btn" @click="addToCart">
                <i class="fas fa-shopping-cart"> 添加到购物车</i>
              </button>
              <button class="buy-now-btn" @click="buyNow">
                <i class="fas fa-bolt"></i>
                立即购买
              </button>
            </div>
          </div>
        </div>

        <div class="product-tabs">
          <div class="tab-header">
            <div 
              class="tab-item" 
              :class="{ active: activeTab === 'description' }"
              @click="activeTab = 'description'"
            >
              商品详情
            </div>
            <div 
              class="tab-item" 
              :class="{ active: activeTab === 'params' }"
              @click="activeTab = 'params'"
            >
              规格参数
            </div>
            <div 
              class="tab-item" 
              :class="{ active: activeTab === 'comments' }"
              @click="activeTab = 'comments'"
            >
              用户评价({{ product.commentVos?.length || 0 }})
            </div>
          </div>
          
          <div class="tab-content">
            <div v-show="activeTab === 'description'" class="tab-pane">
              <div class="detail-content" v-html="product.detail">
              </div>
            </div>
            
            <div v-show="activeTab === 'params'" class="tab-pane">
              <div class="params-content">
                <table class="params-table">
                  <tbody>
                    <tr>
                      <th>商品名称</th>
                      <td>{{ product.name }}</td>
                    </tr>
                    <tr>
                      <th>商品副标题</th>
                      <td>{{ product.subtitle }}</td>
                    </tr>
                    <tr>
                      <th>商品价格</th>
                      <td>¥{{ product.price.toFixed(2) }}</td>
                    </tr>
                    <tr>
                      <th>商品类别</th>
                      <td>{{ product.categoryId }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            
            <div v-show="activeTab === 'comments'" class="tab-pane">
              <div class="comments-content">
                <div class="add-comment-section">
                  <h3 class="section-title">发表评论</h3>
                  <div class="comment-input-area">
                    <textarea 
                      v-model="commentContent" 
                      placeholder="说点什么吧..." 
                      class="comment-textarea"
                    ></textarea>
                    <button class="submit-comment-btn" @click="submitComment">
                      提交评论
                    </button>
                  </div>
                </div>
                
                <h3 class="section-title">全部评论({{ product.commentVos?.length || 0 }})</h3>
                
                <div class="comments-list" v-if="product.commentVos && product.commentVos.length > 0">
                  <div 
                    v-for="comment in product.commentVos" 
                    :key="comment.id" 
                    class="comment-item"
                  >
                    <div class="comment-avatar">
                      <img :src="comment.getUserVo?.avatar || '/public/images/articles/疾病防御.jpg'" alt="评论者头像">
                    </div>
                    <div class="comment-content">
                      <div class="comment-header">
                        <span class="comment-author">{{ comment.getUserVo?.name || '匿名用户' }}</span>
                      </div>
                      <div class="comment-text">{{ comment.content }}</div>
                      <div class="comment-footer">
                        <button class="comment-action" @click="replyToComment(comment.id)">
                          <i class="fas fa-reply"></i> 回复
                        </button>
                        <button 
                          class="comment-action" 
                          :class="{ 'active': comment.likeStatus === 1 }"
                          @click="likeComment(comment)"
                        >
                          <i class="fas fa-heart"></i> 
                          <span>{{ comment.likeCount || 0 }}</span>
                        </button>
                      </div>

                      <div v-if="activeReplyId === comment.id" class="reply-box">
                        <textarea 
                          v-model="replyContent" 
                          placeholder="回复评论..." 
                          class="reply-textarea"
                        ></textarea>
                        <div class="reply-actions">
                          <button class="cancel-reply-btn" @click="activeReplyId = null">取消</button>
                          <button 
                            class="submit-reply-btn" 
                            @click="submitReply(comment.id, comment.getUserVo.id)"
                          >
                            提交
                          </button>
                        </div>
                      </div>
                      
                      <div 
                        class="sub-comments" 
                        v-if="comment.commentSonVoList && comment.commentSonVoList.length > 0"
                      >
                        <div 
                          v-for="(subComment, index) in comment.commentSonVoList" 
                          :key="index" 
                          class="sub-comment-item"
                        >
                          <div class="comment-avatar">
                            <img :src="subComment.getUserVo?.avatar || '/public/images/articles/疾病防御.jpg'" alt="回复者头像">
                          </div>
                          <div class="comment-content">
                            <div class="comment-header">
                              <span class="comment-author">{{ subComment.getUserVo?.name || '匿名用户' }}</span>
                            </div>
                            <div class="comment-text">{{ subComment.content }}</div>
                            <div class="comment-footer">
                              <button 
                                class="comment-action" 
                                :class="{ 'active': subComment.likeStatus === 1 }"
                                @click="likeComment(subComment)"
                              >
                                <i class="fas fa-heart"></i> 
                                <span>{{ subComment.likeCount || 0 }}</span>
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="no-comments" v-else>
                  <div class="no-comments-icon">
                    <i class="far fa-comment-dots"></i>
                  </div>
                  <p>暂无评论，来发表第一条评论吧</p>
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
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProductById } from '@/api/product';
import { addComment, like } from '@/api/encyclopedia';
import { showNotification } from '@/utils/notification';
import { useAuthStore } from '@/stores/auth';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const productId = computed(() => route.params.id as string);
const cartCount = ref(0);
const loading = ref(true);
const product = ref<any>(null);
const commentContent = ref('');
const activeReplyId = ref<number | null>(null);
const replyContent = ref('');
const isFavorite = ref(false);
const isLiked = ref(false);
const likeCount = ref(0);
const selectedImageIndex = ref(0);
const quantity = ref(1);
const activeTab = ref('description');
const searchQuery = ref('');

const fetchProductData = async () => {
  loading.value = true;
  try {
    const response = await getProductById(parseInt(productId.value));
    if (response.code === 1 && response.data) {
      product.value = response.data;
      parseProductImages();
      likeCount.value = product.value.likeCount || 0;
      isLiked.value = product.value.likeStatus === 1;
      isFavorite.value = product.value.favoriteStatus === 1;
    } else {
      showNotification.error('获取商品信息失败');
    }
  } catch (error) {
    showNotification.error('获取商品信息失败');
    product.value = null;
  } finally {
    loading.value = false;
  }
};

const parseProductImages = () => {
  if (!product.value) return;
  product.value.mainImageUrl = product.value.mainImage || '/public/images/articles/疾病防御.jpg';
  product.value.subImagesList = [];
  if (product.value.subImages) {
    product.value.subImagesList = product.value.subImages.split(',');
  }
  product.value.images = [product.value.mainImageUrl, ...product.value.subImagesList];
};

const increaseQuantity = () => {
  if (product.value && quantity.value < 99) {
    quantity.value++;
  }
};

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

const validateQuantity = () => {
  if (!product.value) return;
  quantity.value = Math.floor(quantity.value);
  
  if (quantity.value < 1) {
    quantity.value = 1;
  } else if (quantity.value > 99) {
    quantity.value = 99;
  }
};

const getRatingClass = (score: number) => {
  if (score >= 4.8) return 'excellent';
  if (score >= 4.5) return 'good';
  if (score >= 4.0) return 'average';
  return 'below-average';
};

const addToCart = async () => {
  if (!product.value) return;
  
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    const cartItem = {
      productId: product.value.id,
      quantity: quantity.value,
      isSelected: true
    };
    const response = await fetch('/api/cart', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authStore.user?.token || ''}`
      },
      body: JSON.stringify(cartItem)
    });
    
    const result = await response.json();
    
    if (result.code === 1) {
      showNotification.success(`已将 ${quantity.value} 件商品添加到购物车`);
    } else {
      showNotification.error('添加购物车失败: ' + result.msg);
    }
  } catch (error) {
    showNotification.error('添加购物车失败');
  }
};

const buyNow = () => {
  if (!product.value) return;
  
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  router.push({
    path: '/mall/checkout',
    query: {
      products: JSON.stringify([{
        id: product.value.id,
        quantity: quantity.value
      }])
    }
  });
};

const handleSearch = () => {
  if (!searchQuery.value.trim()) return;
  
  router.push({
    path: '/mall/search',
    query: { 
      q: searchQuery.value
    }
  });
};

const toggleFavorite = async () => {
  if (!product.value) return;
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    if (!isFavorite.value) {
      const response = await fetch('/api/user/favorite/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authStore.user?.token || ''}`
        },
        body: JSON.stringify({
          entityType: 2, 
          entityId: product.value.id,
          userId: authStore.user?.id
        })
      });
      
      const result = await response.json();
      
      if (result.code === 1) {
        isFavorite.value = true;
        showNotification.success('收藏成功');
      } else {
        showNotification.error('收藏失败: ' + result.msg);
      }
    } else {
      const response = await fetch(`/api/user/favorite/delete/${product.value.id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authStore.user?.token || ''}`
        }
      });
      
      const result = await response.json();
      if (result.code === 1) {
        isFavorite.value = false;
        showNotification.success('已取消收藏');
      } else {
        showNotification.error('取消收藏失败: ' + result.msg);
      }
    }
  } catch (error) {
    showNotification.error('操作失败，请稍后重试');
  }
};

const toggleLike = async () => {
  if (!product.value) return;
  
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    const response = await like(2, product.value.id); // 2表示商品
    
    if (response.code === 1 && response.data) {
      likeCount.value = response.data.entityLikeCount;
      isLiked.value = response.data.likeStatus === 1;
    } else {
      showNotification.error('操作失败: ' + response.msg);
    }
  } catch (error) {
    showNotification.error('操作失败，请稍后重试');
  }
};

const likeComment = async (comment: any) => {
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    const response = await like(3, comment.id, comment.getUserVo.id); // 3表示评论
    
    if (response.code === 1 && response.data) {
      comment.likeCount = response.data.entityLikeCount;
      comment.likeStatus = response.data.likeStatus;
    } else {
      showNotification.error('操作失败: ' + response.msg);
    }
  } catch (error) {
    showNotification.error('操作失败，请稍后重试');
  }
};

const submitComment = async () => {
  if (!product.value || !commentContent.value.trim()) return;
  
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    const response = await addComment({
      entityType: 2, 
      entityId: product.value.id,
      content: commentContent.value.trim(),
      targetId: 0
    });
    
    if (response.code === 1) {
      showNotification.success('评论成功');
      commentContent.value = '';
      fetchProductData(); 
    } else {
      showNotification.error('评论失败: ' + response.msg);
    }
  } catch (error) {
    showNotification.error('评论失败，请稍后重试');
  }
};

const replyToComment = (commentId: number) => {
  if (activeReplyId.value === commentId) {
    activeReplyId.value = null;
  } else {
    activeReplyId.value = commentId;
    replyContent.value = '';
  }
};

// 提交回复
const submitReply = async (commentId: number, userId: number) => {
  if (!product.value || !replyContent.value.trim()) return;
  
  if (!authStore.isAuthenticated) {
    showNotification.warning('请先登录');
    router.push('/login');
    return;
  }
  
  try {
    const response = await addComment({
      entityType: 3, 
      entityId: commentId,
      targetId: userId,
      content: replyContent.value.trim()
    });
    
    if (response.code === 1) {
      showNotification.success('回复成功');
      replyContent.value = '';
      activeReplyId.value = null;
      fetchProductData(); 
    } else {
      showNotification.error('回复失败: ' + response.msg);
    }
  } catch (error) {
    showNotification.error('回复失败，请稍后重试');
  }
};

onMounted(async () => {
  await fetchProductData();
});
</script>

<style scoped>
.product-detail-container {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-section {
  background-color: #fff;
  padding: 10px 0;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
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
  margin-right: 30px;
  font-size: 22px;
}

.logo a {
  color: #333;
  text-decoration: none;
}

.logo a:hover {
  color: #ff6700;
}

.search-bar {
  flex: 1;
  max-width: 600px;
  display: flex;
  height: 40px;
  border: 1px solid #e0e0e0;
}

.search-bar input {
  flex: 1;
  border: none;
  padding: 0 15px;
  font-size: 14px;
}

.search-bar input:focus {
  outline: none;
}

.search-btn {
  width: 52px;
  height: 100%;
  background-color: #ff6700;
  border: none;
  color: white;
  cursor: pointer;
}

.user-actions {
  display: flex;
  margin-left: 20px;
}

.cart-icon, .user-icon {
  font-size: 18px;
  margin-left: 20px;
  position: relative;
  cursor: pointer;
}

.cart-icon a, .user-icon a {
  color: #333;
  text-decoration: none;
}

.cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #ff6700;
  color: white;
  font-size: 12px;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  line-height: 16px;
  text-align: center;
}

.detail-main-container {
  max-width: 1226px;
  margin: 0 auto;
  padding: 20px 10px;
}

.breadcrumb-section {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.breadcrumb-container {
  max-width: 1226px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding: 0 10px;
}

.breadcrumb-container a {
  color: #666;
  text-decoration: none;
}

.breadcrumb-container a:hover {
  color: #ff6700;
}

.breadcrumb-container .separator {
  margin: 0 8px;
  color: #999;
}

.breadcrumb-container .current {
  color: #333;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  text-align: center;
}

.loader {
  font-size: 40px;
  color: #ff6700;
  margin-bottom: 16px;
}

.loading-text {
  font-size: 16px;
  color: #666;
}

.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  padding: 60px 0;
  text-align: center;
}

.error-icon {
  font-size: 60px;
  color: #ff9800;
  margin-bottom: 16px;
}

.error-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
}

.retry-btn {
  display: inline-block;
  background: #ff6700;
  color: white;
  padding: 8px 24px;
  border-radius: 4px;
  text-decoration: none;
  transition: background 0.3s;
}

.retry-btn:hover {
  background: #f25807;
}

.product-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  overflow: hidden;
}

.product-info-section {
  display: flex;
  padding: 30px;
}

.product-gallery {
  width: 400px;
  margin-right: 30px;
}

.main-image {
  width: 400px;
  height: 400px;
  overflow: hidden;
  border: 1px solid #f0f0f0;
  margin-bottom: 10px;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border: 1px solid #f0f0f0;
  cursor: pointer;
  opacity: 0.7;
  transition: all 0.3s;
}

.thumbnail:hover, .thumbnail.active {
  border-color: #ff6700;
  opacity: 1;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 24px;
  font-weight: 500;
  margin: 0 0 10px;
  color: #333;
}

.product-brief {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
}

.product-price-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
  background: #fff2f2;
  padding: 15px;
  border-radius: 4px;
}

.price-info {
  display: flex;
  align-items: baseline;
}

.current-price {
  font-size: 28px;
  font-weight: 500;
  color: #ff6700;
  margin-right: 10px;
}

.stock-info {
  font-size: 14px;
  color: #666;
}

.quantity-section {
  margin-bottom: 30px;
}

.quantity-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.quantity-selector {
  display: flex;
  align-items: center;
}

.quantity-btn {
  width: 36px;
  height: 36px;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.quantity-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.quantity-selector input {
  width: 60px;
  height: 36px;
  border: 1px solid #e0e0e0;
  border-left: none;
  border-right: none;
  text-align: center;
  font-size: 14px;
}

.quantity-selector input::-webkit-inner-spin-button,
.quantity-selector input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.action-buttons {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.add-to-cart-btn, .buy-now-btn {
  height: 50px;
  padding: 0 30px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.add-to-cart-btn {
  background: #fff2f0;
  color: #ff6700;
  border: 1px solid #ff6700;
}

.add-to-cart-btn:hover {
  background: #fff0eb;
}

.buy-now-btn {
  background: #ff6700;
  color: white;
  border: none;
}

.buy-now-btn:hover {
  background: #f25807;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.favorite-btn, .like-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 6px 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background: white;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.favorite-btn:hover, .like-btn:hover {
  background: #f5f5f5;
}

.favorite-btn.active {
  color: #ff6700;
  border-color: #ff6700;
  background: #fff0eb;
}

.like-btn.active {
  color: #ff6700;
  border-color: #ff6700;
  background: #fff0eb;
}

.add-comment-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 15px;
  color: #333;
}

.comment-input-area {
  display: flex;
  flex-direction: column;
}

.comment-textarea, .reply-textarea {
  width: 100%;
  min-height: 80px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 10px;
}

.submit-comment-btn {
  align-self: flex-end;
  padding: 8px 16px;
  background: #ff6700;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.submit-comment-btn:hover {
  background: #f25807;
}

.comments-list {
  margin-bottom: 30px;
}

.comment-item {
  display: flex;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.comment-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 500;
  color: #333;
}

.comment-text {
  line-height: 1.6;
  color: #333;
  margin-bottom: 8px;
}

.comment-footer {
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
}

.comment-action {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 3px 8px;
  border: none;
  background: none;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.comment-action:hover {
  color: #ff6700;
}

.comment-action.active {
  color: #ff6700;
}

.reply-box {
  margin: 10px 0;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 4px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.cancel-reply-btn, .submit-reply-btn {
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-reply-btn {
  background: white;
  border: 1px solid #e0e0e0;
  color: #666;
}

.cancel-reply-btn:hover {
  background: #f5f5f5;
}

.submit-reply-btn {
  background: #ff6700;
  border: none;
  color: white;
}

.submit-reply-btn:hover {
  background: #f25807;
}

.sub-comments {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #f0f0f0;
}

.sub-comment-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px dashed #f0f0f0;
}

.sub-comment-item:last-child {
  border-bottom: none;
}

.no-comments {
  text-align: center;
  padding: 30px 0;
  color: #999;
}

.no-comments-icon {
  font-size: 50px;
  margin-bottom: 10px;
  color: #e0e0e0;
}

@keyframes heart-pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.favorite-btn.active i, .like-btn.active i {
  animation: heart-pulse 0.3s ease-in-out;
}

.product-tabs {
  margin-top: 30px;
  border-top: 1px solid #f0f0f0;
}

.tab-header {
  display: flex;
  background: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
}

.tab-item {
  padding: 15px 30px;
  font-size: 16px;
  cursor: pointer;
  position: relative;
  color: #666;
  transition: all 0.3s;
}

.tab-item:hover {
  color: #ff6700;
}

.tab-item.active {
  color: #ff6700;
  font-weight: 500;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: #ff6700;
}

.tab-content {
  padding: 30px;
}

.tab-pane {
  min-height: 400px;
}

.detail-content {
  color: #333;
}

.section-title:first-child {
  margin-top: 0;
}

.detail-description p {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 15px;
}

.detail-images {
  margin-top: 20px;
}

.detail-images img {
  max-width: 100%;
  margin-bottom: 20px;
  display: block;
}

.product-features {
  margin-top: 20px;
}

.feature-item {
  display: flex;
  margin-bottom: 20px;
}

.feature-icon {
  color: #52c41a;
  font-size: 18px;
  margin-right: 15px;
  margin-top: 3px;
}

.feature-text h3 {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 8px;
  color: #333;
}

.feature-text p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.usage-instructions p,
.precautions p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}

.params-content {
  padding: 15px 0;
}

.params-table {
  width: 100%;
  border-collapse: collapse;
}

.params-table th, 
.params-table td {
  padding: 12px 15px;
  text-align: left;
  font-size: 14px;
  border-bottom: 1px solid #f0f0f0;
}

.params-table th {
  width: 150px;
  color: #666;
  background: #f9f9f9;
}

.params-table td {
  color: #333;
}

.comments-content {
  padding: 15px 0;
}

.reviews-summary {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
}

.overall-rating {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-right: 30px;
  border-right: 1px solid #f0f0f0;
  margin-right: 30px;
}

.rating-number {
  font-size: 36px;
  font-weight: 500;
  color: #ff6700;
}

.rating-text {
  font-size: 14px;
  color: #666;
  margin: 5px 0 10px;
}

.rating-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.rating-tags .tag {
  padding: 6px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
}

.rating-tags .tag:hover,
.rating-tags .tag.active {
  color: #ff6700;
  border-color: #ff6700;
  background: #fff7f5;
}

.review-list {
  margin-bottom: 30px;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.reviewer-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 10px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.reviewer-name {
  font-size: 14px;
  color: #666;
}

.review-rating {
  color: #ff6700;
}

.review-content p {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin: 0 0 15px;
}

.review-specs {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.review-specs span {
  margin-right: 15px;
}

.review-time {
  font-size: 12px;
  color: #999;
}

.recommended-section {
  margin-top: 40px;
  padding: 30px;
  background: #f9f9f9;
  border-radius: 8px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  transition: all 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.product-card .product-img {
  width: 100%;
  height: 0;
  padding-bottom: 100%;
  position: relative;
}

.product-card .product-img img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-card .product-info {
  padding: 15px;
}

.product-card .product-name {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 8px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-card .product-brief {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-card .product-price {
  display: flex;
  align-items: baseline;
}

.product-card .current-price {
  font-size: 16px;
  font-weight: 500;
  color: #ff6700;
  margin-right: 8px;
}

.product-card .original-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
}

.prev-btn, .next-btn {
  padding: 6px 12px;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.prev-btn:hover, .next-btn:hover {
  background: #f5f5f5;
}

.prev-btn:disabled, .next-btn:disabled {
  color: #ccc;
  background: #f5f5f5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  margin: 0 10px;
}

.page-number {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  margin: 0 5px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.page-number:hover {
  background: #f5f5f5;
}

.page-number.active {
  background: #ff6700;
  color: white;
}

@media (max-width: 992px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .tab-header {
    flex-wrap: wrap;
  }
  
  .tab-item {
    padding: 12px 15px;
    font-size: 14px;
  }
  
  .tab-content {
    padding: 15px;
  }
  
  .reviews-summary {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .overall-rating {
    padding-right: 0;
    border-right: none;
    margin-right: 0;
    margin-bottom: 15px;
    width: 100%;
  }
}

@media (max-width: 576px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style> 