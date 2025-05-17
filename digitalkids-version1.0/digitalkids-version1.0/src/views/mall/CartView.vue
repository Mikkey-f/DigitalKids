<template>
  <div class="cart-container">
    <SearchBar :cart-count="cartItems.length" />
    <div class="cart-main">
      <div class="cart-content">
        <h1 class="page-title">我的购物车</h1>
        <div class="empty-cart" v-if="cartItems.length === 0 && !loading">
          <div class="empty-icon">
            <i class="fas fa-shopping-cart"></i>
          </div>
          <p class="empty-text">购物车还是空的，快去选购商品吧~</p>
          <router-link to="/mall/recommend" class="shop-now-btn">去选购</router-link>
        </div>
        <div class="loading-cart" v-if="loading">
          <p class="loading-text">加载中...</p>
        </div>
        <div class="error-message" v-if="errorMsg">
          <p>{{ errorMsg }}</p>
          <button @click="loadCartItems" class="retry-btn">重试</button>
        </div>
        <div class="cart-list" v-if="cartItems.length > 0 && !loading">
          <div class="cart-header">
            <div class="col-check">
              <label class="checkbox-wrapper">
                <input type="checkbox" :checked="allSelected" @click="toggleAllItems" />
                <span class="checkmark"></span>
              </label>
              <span class="select-text" @click="toggleAllItems">全选</span>
            </div>
            <div class="col-info">商品信息</div>
            <div class="col-price">单价</div>
            <div class="col-quantity">数量</div>
            <div class="col-total">小计</div>
            <div class="col-action">操作</div>
          </div>
          <div class="store-group" v-for="(store, storeIndex) in groupedCartItems" :key="storeIndex">
            <div class="store-header">
              <div class="store-check">
                <label class="checkbox-wrapper">
                  <input 
                    type="checkbox" 
                    :checked="isStoreSelected(store.items)"
                    @change="toggleStoreSelection(store.items)"
                  />
                  <span class="checkmark"></span>
                </label>
              </div>
              <div class="store-info">
                <i class="fas fa-store"></i>
                {{ store.name }}
              </div>
            </div>

            <div class="cart-item" v-for="(item, itemIndex) in store.items" :key="itemIndex">
              <div class="col-check">
                <label class="checkbox-wrapper">
                  <input type="checkbox" v-model="item.isSelected" @change="updateItemSelection(item)" />
                  <span class="checkmark"></span>
                </label>
              </div>
              <div class="col-info">
                <div class="item-image">
                  <img v-if="!item.loading" :src="item.imageUrl" :alt="item.name" @error="handleImageError($event, item)">
                  <div v-else class="image-skeleton"></div>
                </div>
                <div class="item-details">
                  <div class="item-name">{{ item.loading ? '加载中...' : item.name }}</div>
                  <div class="item-spec">{{ item.loading ? '' : item.spec }}</div>
                </div>
              </div>
              <div class="col-price">
                <template v-if="!item.loading">¥{{ (item.price || 0).toFixed(2) }}</template>
                <div v-else class="price-skeleton"></div>
              </div>
              <div class="col-quantity">
                <div class="quantity-control">
                  <button 
                    class="quantity-btn decrease" 
                    @click="decreaseQuantity(item)"
                    :disabled="item.quantity <= 1 || item.loading"
                  >-</button>
                  <input 
                    type="number" 
                    v-model.number="item.quantity" 
                    @change="validateQuantity(item)"
                    min="1"
                    :disabled="item.loading"
                  />
                  <button 
                    class="quantity-btn increase" 
                    @click="increaseQuantity(item)"
                    :disabled="item.loading"
                  >+</button>
                </div>
              </div>
              <div class="col-total">
                <template v-if="!item.loading">¥{{ ((item.price || 0) * item.quantity).toFixed(2) }}</template>
                <div v-else class="price-skeleton"></div>
              </div>
              <div class="col-action">
                <button class="remove-btn" @click="removeItem(item)" :disabled="item.loading">删除</button>
              </div>
            </div>
          </div>
        </div>

        <div class="cart-footer" v-if="cartItems.length > 0 && !loading">
          <div class="cart-options">
            <div class="select-all">
              <label class="checkbox-wrapper">
                <input type="checkbox" :checked="allSelected" @click="toggleAllItems" />
                <span class="checkmark"></span>
              </label>
              <span class="select-text" @click="toggleAllItems">全选</span>
            </div>
            <button class="batch-delete-btn" @click="removeSelectedItems">删除选中商品</button>
          </div>

          <div class="cart-total">
            <div class="total-info">
              <p>已选商品 <span class="selected-count">{{ selectedCount }}</span> 件</p>
              <p class="total-price">
                合计：<span class="price">¥{{ totalPrice.toFixed(2) }}</span>
              </p>
              <p class="shipping-info">含运费：¥{{ shippingCost.toFixed(2) }}</p>
            </div>
            <button class="checkout-btn" @click="goToCheckout" :disabled="selectedCount === 0">
              去结算
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import SearchBar from './components/SearchBar.vue';
import { getCartList, updateCartItem, removeFromCart, selectAllCartItems, unselectAllCartItems } from '@/api/cart';
import { getProductById } from '@/api/product';

const router = useRouter();

interface CartItemType {
  id?: number;
  productId?: number;
  quantity: number;
  isSelected: boolean;
  name?: string;
  spec?: string;
  price?: number;
  store?: string;
  imageUrl?: string;
  loading?: boolean; 
}

interface ProductCacheItem {
  data: any;
  timestamp: number;
}

interface ProductCache {
  [key: number]: ProductCacheItem;
}

const cartItems = ref<CartItemType[]>([]);
const loading = ref(false);
const errorMsg = ref('');
const defaultImageUrl = '/public/images/articles/疾病防御.jpg'; 
const productCache = ref<ProductCache>({});

const getProductDetails = async (productId: number, maxRetries = 3) => {
  const cachedProduct = productCache.value[productId];
  const now = new Date().getTime();
  if (cachedProduct && (now - cachedProduct.timestamp) < 3600000) { 
    return cachedProduct.data;
  }
  
  let retries = 0;
  while (retries < maxRetries) {
    try {
      const productRes = await getProductById(productId);
      if ((productRes.code === 0 || productRes.code === 1) && productRes.data) {
        productCache.value[productId] = {
          data: productRes.data,
          timestamp: now
        };
        return productRes.data;
      }
      retries++;
    } catch (error) {
      retries++;
      if (retries < maxRetries) {
        await new Promise(resolve => setTimeout(resolve, 1000 * retries));
      }
    }
  }
  return null;
};

const loadProductDetails = async (items: CartItemType[]) => {
  const batchSize = 3; 
  const productIds = items.filter(item => item.productId !== undefined)
                          .map(item => item.productId as number);
  
  for (let i = 0; i < productIds.length; i += batchSize) {
    const batch = productIds.slice(i, i + batchSize);
    await Promise.all(
      batch.map(async (productId) => {
        try {
          const product = await getProductDetails(productId);
          if (product) {
            const index = cartItems.value.findIndex(item => item.productId === productId);
            if (index !== -1) {
              const item = cartItems.value[index];
              cartItems.value[index] = {
                ...item,
                name: product.name || `商品${productId}`,
                price: typeof product.price === 'number' ? product.price : 0,
                spec: product.subtitle || '',
                imageUrl: product.mainImage || defaultImageUrl,
                store: product.categoryId ? `分类${product.categoryId}` : '商城自营',
                loading: false
              };
            }
          }
        } catch (error) {
          const index = cartItems.value.findIndex(item => item.productId === productId);
          if (index !== -1) {
            cartItems.value[index].loading = false;
          }
        }
      })
    );
  }
};

const refreshProductPrices = async () => {
  const products = cartItems.value.filter(item => item.productId !== undefined);
  
  for (const item of products) {
    try {
      const productData = await getProductDetails(item.productId!);
      if (productData && typeof productData.price === 'number') {
        if (item.price !== productData.price) {
          item.price = productData.price;
        }
      }
    } catch (error) {
    }
  }
};

const loadCartItems = async () => {
  loading.value = true;
  errorMsg.value = '';
  try {
    const res = await getCartList();
    if (res.code === 0 && res.data) {
      const cartData = res.data;
      cartItems.value = cartData.map(item => ({
        ...item,
        id: item.productId,
        name: `商品${item.productId}`,
        price: 0,
        spec: '加载中...',
        imageUrl: defaultImageUrl,
        store: '商城自营',
        loading: true
      }));
      
      loadProductDetails(cartItems.value);
      
    } else if (res.code === 1 && res.data) {
      const cartData = res.data;
      cartItems.value = cartData.map(item => ({
        ...item,
        id: item.productId,
        name: `商品${item.productId}`,
        price: 0,
        spec: '加载中...',
        imageUrl: defaultImageUrl,
        store: '商城自营',
        loading: true
      }));
      
      loadProductDetails(cartItems.value);
      
    } else {
      errorMsg.value = res.msg || '获取购物车数据失败';
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
    try {
      const sampleData = [
        {
          productId: 3,
          quantity: 5,
          isSelected: true
        },
        {
          productId: 5,
          quantity: 4,
          isSelected: true
        }
      ];
      cartItems.value = sampleData.map(item => ({
        ...item,
        id: item.productId,
        name: `商品${item.productId}`,
        price: 99.99,
        spec: '示例商品',
        imageUrl: defaultImageUrl,
        store: '商城自营',
        loading: true
      }));
      loadProductDetails(cartItems.value);
      errorMsg.value = '已加载示例数据，API请求失败';
    } catch (fallbackError) {
      console.error('加载示例数据失败:', fallbackError);
    }
  } finally {
    loading.value = false;
  }
};

let refreshTimer: number | null = null;

onMounted(() => {
  loadCartItems();
  refreshTimer = window.setInterval(() => {
    refreshProductPrices();
  }, 300000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
    refreshTimer = null;
  }
});

const handleImageError = (event: Event, item: CartItemType) => {
  const imgElement = event.target as HTMLImageElement;
  imgElement.src = defaultImageUrl;
};

const updateItemSelection = async (item: CartItemType) => {
  if (item.productId === undefined) {
    errorMsg.value = '商品ID不存在，无法更新';
    return;
  }
  
  try {
    const res = await updateCartItem(item.productId, {
      quantity: item.quantity,
      isSelected: item.isSelected
    });
    if (res.code !== 0) {
      errorMsg.value = res.msg || '更新商品选中状态失败';
      item.isSelected = !item.isSelected;
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
    item.isSelected = !item.isSelected;
  }
};

const groupedCartItems = computed(() => {
  const stores = new Map();
  cartItems.value.forEach(item => {
    if (!stores.has(item.store)) {
      stores.set(item.store, {
        name: item.store,
        items: []
      });
    }
    stores.get(item.store).items.push(item);
  });
  
  return Array.from(stores.values());
});

const allSelected = computed(() => {
  return cartItems.value.length > 0 && cartItems.value.every(item => item.isSelected);
});

watch(
  () => cartItems.value.map(item => item.isSelected),
  () => {},
  { immediate: true }
);

const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.isSelected).reduce((count, item) => {
    return count + item.quantity;
  }, 0);
});

const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.isSelected)
    .reduce((total, item) => {
      return total + ((item.price || 0) * item.quantity);
    }, 0);
});

const shippingCost = computed(() => {
  return totalPrice.value >= 200 ? 0 : 10;
});

const isStoreSelected = (items: CartItemType[]) => {
  return items.length > 0 && items.every(item => item.isSelected);
};

const toggleStoreSelection = async (items: CartItemType[]) => {
  const newState = !isStoreSelected(items);
  items.forEach(item => {
    item.isSelected = newState;
  });
  try {
    for (const item of items) {
      if (item.productId === undefined) continue;
      
      await updateCartItem(item.productId, {
        quantity: item.quantity,
        isSelected: newState
      });
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
  }
};

const increaseQuantity = async (item: CartItemType) => {
  if (item.productId === undefined) {
    errorMsg.value = '商品ID不存在，无法更新';
    return;
  }
  
  try {
    const newQuantity = item.quantity + 1;
    item.quantity = newQuantity;
    const res = await updateCartItem(item.productId, {
      quantity: newQuantity,
      isSelected: item.isSelected
    });
    if (res.code !== 1) {
      item.quantity = newQuantity - 1;
      errorMsg.value = res.msg || '更新商品数量失败';
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
    item.quantity -= 1;
  }
};

const decreaseQuantity = async (item: CartItemType) => {
  if (item.productId === undefined) {
    errorMsg.value = '商品ID不存在，无法更新';
    return;
  }
  
  if (item.quantity > 1) {
    try {
      const newQuantity = item.quantity - 1;
      item.quantity = newQuantity;
      const res = await updateCartItem(item.productId, {
        quantity: newQuantity,
        isSelected: item.isSelected
      });
      
      if (res.code !== 1) {
        item.quantity = newQuantity + 1;
        errorMsg.value = res.msg || '更新商品数量失败';
      }
    } catch (error) {
      errorMsg.value = '网络错误，请稍后重试';
      item.quantity += 1;
    }
  }
};

const validateQuantity = async (item: CartItemType) => {
  if (item.productId === undefined) {
    errorMsg.value = '商品ID不存在，无法更新';
    return;
  }
  
  const originalQuantity = item.quantity;
  
  if (isNaN(item.quantity) || item.quantity < 1) {
    item.quantity = 1;
  } else {
    item.quantity = Math.floor(item.quantity);
  }
  
  if (originalQuantity === item.quantity) return;
  
  try {
    const res = await updateCartItem(item.productId, {
      quantity: item.quantity,
      isSelected: item.isSelected
    });
    
    if (res.code !== 0) {
      errorMsg.value = res.msg || '更新商品数量失败';
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
  }
};

// 移除商品
const removeItem = async (item: CartItemType) => {
  if (item.productId === undefined) {
    errorMsg.value = '商品ID不存在，无法删除';
    return;
  }
  
  try {
    const index = cartItems.value.findIndex(i => i.productId === item.productId);
    if (index !== -1) {
      const removedItem = cartItems.value.splice(index, 1)[0];
      const res = await removeFromCart(item.productId);
      if (res.code !== 0) {
        cartItems.value.splice(index, 0, removedItem);
        errorMsg.value = res.msg || '删除商品失败';
      }
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
    await loadCartItems();
  }
};

const removeSelectedItems = async () => {
  if (selectedCount.value === 0) {
    errorMsg.value = '请至少选择一件商品';
    return;
  }
  if (confirm('确定要删除选中的商品吗？')) {
    const selectedItems = cartItems.value.filter(item => item.isSelected);
    cartItems.value = cartItems.value.filter(item => !item.isSelected);
    
    try {
      for (const item of selectedItems) {
        if (item.productId !== undefined) {
          await removeFromCart(item.productId);
        }
      }
    } catch (error) {
      errorMsg.value = '网络错误，请稍后重试';
      await loadCartItems();
    }
  }
};

const goToCheckout = async () => {
  if (selectedCount.value === 0) {
    alert('请至少选择一件商品');
    return;
  }
  try {
    if (selectedCount.value > 0 && !allSelected.value) {
      await unselectAllCartItems();
      for (const item of cartItems.value.filter(i => i.isSelected)) {
        if (item.productId !== undefined) {
          await updateCartItem(item.productId, {
            quantity: item.quantity,
            isSelected: true
          });
        }
      }
    }
    router.push('/mall/checkout');
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
  }
};

const toggleAllItems = async () => {
  const newState = !allSelected.value;
  try {
    cartItems.value.forEach(item => {
      item.isSelected = newState;
    });
    const res = newState ? await selectAllCartItems() : await unselectAllCartItems();
    if (res.code !== 0) {
      errorMsg.value = res.msg || (newState ? '全选商品失败' : '取消全选失败');
      await loadCartItems();
    }
  } catch (error) {
    errorMsg.value = '网络错误，请稍后重试';
    await loadCartItems();
  }
};
</script>

<style scoped>
.cart-container {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.cart-main {
  max-width: 1226px;
  margin: 20px auto;
  padding: 0 10px;
}

.cart-content {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  font-weight: 400;
  color: #333;
}

.empty-cart {
  text-align: center;
  padding: 60px 0;
}

.empty-icon {
  font-size: 60px;
  color: #e0e0e0;
  margin-bottom: 20px;
}

.empty-text {
  color: #999;
  margin-bottom: 20px;
}

.shop-now-btn {
  display: inline-block;
  background-color: #ff6700;
  color: white;
  padding: 10px 30px;
  border-radius: 4px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.shop-now-btn:hover {
  background-color: #f25807;
}

.loading-cart, .error-message {
  text-align: center;
  padding: 40px 0;
}

.loading-text {
  color: #666;
}

.error-message p {
  color: #f56c6c;
  margin-bottom: 10px;
}

.retry-btn {
  background-color: #ff6700;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.cart-header {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fafafa;
  font-size: 14px;
  color: #666;
}

.col-check {
  width: 60px;
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}

.col-info {
  flex: 3;
}

.col-price, .col-quantity, .col-total {
  flex: 1;
  text-align: center;
}

.col-action {
  width: 100px;
  text-align: center;
}

.store-group {
  margin-bottom: 15px;
}

.store-header {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  background-color: #f5f5f5;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.store-check {
  width: 60px;
}

.store-info {
  display: flex;
  align-items: center;
  font-weight: 500;
}

.store-info i {
  margin-right: 8px;
  color: #ff6700;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.item-image {
  width: 80px;
  height: 80px;
  margin-right: 15px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.item-details {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 8px;
}

.item-spec {
  font-size: 12px;
  color: #999;
}

.quantity-control {
  display: inline-flex;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.quantity-btn {
  width: 30px;
  height: 30px;
  border: none;
  background-color: #f5f5f5;
  color: #666;
  font-size: 16px;
  cursor: pointer;
}

.quantity-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.quantity-control input {
  width: 40px;
  border: none;
  border-left: 1px solid #e0e0e0;
  border-right: 1px solid #e0e0e0;
  text-align: center;
  font-size: 14px;
  padding: 0;
}

.quantity-control input:focus {
  outline: none;
}

.remove-btn {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
}

.remove-btn:hover {
  color: #ff6700;
  text-decoration: underline;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.cart-options {
  display: flex;
  align-items: center;
}

.select-all {
  display: flex;
  align-items: center;
  width: 60px;
  margin-right: 20px;
  padding: 0;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  user-select: none;
}

.select-text {
  cursor: pointer;
}

.batch-delete-btn {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  font-size: 14px;
}

.batch-delete-btn:hover {
  color: #ff6700;
  text-decoration: underline;
}

.cart-total {
  display: flex;
  align-items: center;
}

.total-info {
  text-align: right;
  margin-right: 20px;
}

.selected-count {
  color: #ff6700;
  font-weight: 700;
}

.total-price {
  margin: 5px 0;
}

.price {
  color: #ff6700;
  font-size: 24px;
  font-weight: 700;
}

.shipping-info {
  font-size: 12px;
  color: #999;
}

.checkout-btn {
  width: 150px;
  height: 50px;
  background-color: #ff6700;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.checkout-btn:hover {
  background-color: #f25807;
}

.checkout-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.checkbox-wrapper {
  position: relative;
  display: inline-block;
  width: 18px;
  height: 18px;
  margin-right: 8px;
}

.checkbox-wrapper input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 18px;
  width: 18px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 2px;
}

.checkbox-wrapper:hover input ~ .checkmark {
  border-color: #ccc;
}

.checkbox-wrapper input:checked ~ .checkmark {
  background-color: #ff6700;
  border-color: #ff6700;
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

.checkbox-wrapper input:checked ~ .checkmark:after {
  display: block;
}

.checkbox-wrapper .checkmark:after {
  left: 6px;
  top: 2px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

@media (max-width: 768px) {
  .cart-header, .cart-item {
    flex-wrap: wrap;
  }
  
  .col-check {
    width: 10%;
  }
  
  .col-info {
    width: 90%;
    flex: none;
    margin-bottom: 10px;
  }
  
  .col-price, .col-quantity, .col-total {
    flex: 1;
  }
  
  .col-action {
    width: 100%;
    text-align: right;
    margin-top: 10px;
  }
  
  .cart-footer {
    flex-direction: column;
  }
  
  .cart-options {
    margin-bottom: 15px;
    width: 100%;
  }
  
  .cart-total {
    width: 100%;
    justify-content: space-between;
  }
  
  .total-info {
    text-align: left;
  }
}

.image-skeleton {
  width: 80px;
  height: 80px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 4px;
}

.price-skeleton {
  width: 60px;
  height: 20px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 2px;
  margin: 0 auto;
}

@keyframes shimmer {
  0% { background-position: -200% 0; }
  100% { background-position: 200% 0; }
}
</style> 