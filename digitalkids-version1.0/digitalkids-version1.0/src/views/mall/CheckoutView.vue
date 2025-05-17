<template>
  <div class="checkout-container">
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
            <span>全部分类</span>
            <i class="fas fa-angle-down"></i>
            <div class="category-dropdown" v-show="showCategoryDropdown">
              <div class="category-item" 
                   v-for="(cat, index) in searchCategories" 
                   :key="index"
                   @click="selectCategory(cat)">
                {{ cat.name }}
              </div>
            </div>
          </div>

          <div class="search-input-wrapper">
            <input 
              type="text" 
              v-model="searchQuery" 
              @keyup.enter="handleSearch"
              placeholder="搜索商品" />
              
            <button class="search-btn" @click="handleSearch">
            <i class="fas fa-search"></i>
          </button>
        </div>
        </div>

        <div class="user-actions">
          <div class="cart-icon">
            <router-link to="/mall/cart">
              <i class="fas fa-shopping-cart"></i>
              <span class="cart-count">{{ cartItemCount }}</span>
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

    <div class="checkout-main">
      <div class="checkout-content">
        <h1 class="page-title">确认订单</h1>
        
        <div class="section-box address-section">
          <div class="section-header">
            <h2 class="section-title">收货地址</h2>
            <button class="add-address-btn" @click="showAddressDialog = true">添加新地址</button>
          </div>
          
          <div class="address-list">
            <div v-if="addresses.length === 0" class="empty-address">
              <p>您还没有添加收货地址，请点击"添加新地址"按钮添加</p>
            </div>
            <div 
              v-else
              class="address-item active"
            >
              <div class="address-content">
                <div class="name-phone">
                  <span class="name">{{ addresses[0].receiverName }}</span>
                  <span class="phone">{{ addresses[0].receiverPhone }}</span>
                </div>
                <div class="address-detail">{{ formatFullAddress(addresses[0]) }}</div>
                <div class="address-tag">默认地址</div>
              </div>
              <div class="address-actions">
                <button class="edit-btn" @click.stop="editAddress(addresses[0], 0)">编辑</button>
                <button class="delete-btn" @click.stop="deleteAddress(addresses[0].id as number, 0)">删除</button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="section-box payment-section">
          <h2 class="section-title">支付方式</h2>
          <div class="payment-options">
            <div 
              v-for="(method, index) in paymentMethods" 
              :key="index" 
              class="payment-option"
              :class="{ active: selectedPaymentIndex === index }"
              @click="selectedPaymentIndex = index"
            >
              <div class="payment-icon">
                <i :class="method.icon"></i>
              </div>
              <div class="payment-name">{{ method.name }}</div>
            </div>
          </div>
        </div>
        
        <div class="section-box order-list-section">
          <h2 class="section-title">商品清单</h2>
          <div class="order-list">
            <div v-if="!isLoading && storeGroups.length === 0" class="empty-cart">
              <p>购物车中没有选中的商品，请返回购物车选择商品</p>
              <router-link to="/mall/cart" class="back-to-cart">返回购物车</router-link>
            </div>
            <div v-else-if="isLoading" class="loading-cart">
              <p>正在加载商品信息...</p>
            </div>
            <div class="store-group" v-else v-for="(store, storeIndex) in storeGroups" :key="storeIndex">
              <div class="store-header">
                <div class="store-name">
                  <i class="fas fa-store"></i>
                  {{ store.name }}
                </div>
              </div>
              
              <div class="product-list">
                <div 
                  class="product-item" 
                  v-for="(product, productIndex) in store.products" 
                  :key="product.productId"
                >
                  <div class="product-image">
                    <img :src="product.imageUrl" :alt="product.name">
                  </div>
                  <div class="product-info">
                    <div class="product-name">{{ product.name }}</div>
                    <div class="product-spec">{{ product.spec }}</div>
                  </div>
                  <div class="product-price">¥{{ (product.price || 0).toFixed(2) }}</div>
                  <div class="product-quantity">x{{ product.quantity }}</div>
                  <div class="product-subtotal">¥{{ ((product.price || 0) * product.quantity).toFixed(2) }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="section-box order-amount-section">
          <div class="amount-list">
            <div class="amount-item">
              <span class="amount-label">商品总价</span>
              <span class="amount-value">¥{{ totalProductPrice.toFixed(2) }}</span>
            </div>
            <div class="amount-item">
              <span class="amount-label">运费</span>
              <span class="amount-value">¥{{ shipping.toFixed(2) }}</span>
            </div>
            <div class="amount-item">
              <span class="amount-label">优惠</span>
              <span class="amount-value discount">-¥{{ discount.toFixed(2) }}</span>
            </div>
            <div class="amount-item total">
              <span class="amount-label">实付金额</span>
              <span class="amount-value">¥{{ orderTotal.toFixed(2) }}</span>
            </div>
          </div>
        </div>
        
        <div class="submit-section">
          <div class="order-summary">
            <div class="total-items">共 {{ totalItems }} 件商品</div>
            <div class="total-price">
              合计：<span class="price">¥{{ orderTotal.toFixed(2) }}</span>
            </div>
          </div>
          <button class="submit-btn" @click="submitOrder" :disabled="submitDisabled">提交订单</button>
        </div>
      </div>
    </div>

    <el-dialog
      v-model="showAddressDialog"
      :title="isEditingAddress ? '编辑收货地址' : '添加收货地址'"
      width="500px"
    >
      <el-form :model="addressForm" label-width="100px" ref="addressFormRef">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item label="所在地区">
          <div class="region-selector">
            <el-input v-model="addressForm.receiverProvince" placeholder="省份"></el-input>
            <el-input v-model="addressForm.receiverCity" placeholder="城市"></el-input>
            <el-input v-model="addressForm.receiverDistrict" placeholder="区/县"></el-input>
          </div>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input 
            v-model="addressForm.receiverAddress" 
            type="textarea" 
            placeholder="街道、门牌号等"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddressDialog = false">取消</el-button>
          <el-button type="primary" @click="saveAddress">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserAddresses, addUserAddress, updateUserAddress, deleteUserAddress, getDefaultUserAddress } from '@/api/address';
import { getCartList } from '@/api/cart';
import { createOrder } from '@/api/order';
import { getProductById } from '@/api/product';

interface UserAddress {
  id?: number;
  userId?: number;
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverCity: string;
  receiverDistrict: string;
  receiverAddress: string;
  isDefault?: boolean;
}

interface CartItem {
  productId?: number;
  quantity: number;
  isSelected: boolean;
  name?: string;
  spec?: string;
  price?: number;
  store?: string;
  imageUrl?: string;
}

interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

interface ApiPageResponse<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
}

interface StoreGroup {
  name: string;
  products: CartItem[];
}

interface Product {
  id: number;
  categoryId: number;
  name: string;
  subtitle: string;
  mainImage: string;
  subImages: string;
  detail: string;
  price: number;
}

const router = useRouter();
const isLoading = ref(true);
const cartItemCount = ref(0);
const addresses = ref<UserAddress[]>([]);
const selectedAddressIndex = ref(0);

const showAddressDialog = ref(false);
const isEditingAddress = ref(false);
const currentEditAddressId = ref<number | null>(null);
const addressForm = ref<UserAddress>({
  receiverName: '',
  receiverPhone: '',
  receiverProvince: '',
  receiverCity: '',
  receiverDistrict: '',
  receiverAddress: '',
});

const paymentMethods = ref([
  { id: 1, name: '微信支付', icon: 'fab fa-alipay' },
  { id: 2, name: '支付宝', icon: 'fab fa-weixin' },
  { id: 3, name: '银行卡支付', icon: 'fas fa-credit-card' }
]);

const selectedPaymentIndex = ref(0);

const storeGroups = ref<StoreGroup[]>([]);

const searchQuery = ref('');
const showCategoryDropdown = ref(false);
const searchCategories = ref([
  { id: 'all', name: '全部分类' },
  { id: 'nutrition', name: '营养保健' },
  { id: 'toys', name: '玩具早教' },
  { id: 'clothing', name: '童装童鞋' },
  { id: 'books', name: '图书绘本' }
]);

const toggleCategoryDropdown = () => {
  showCategoryDropdown.value = !showCategoryDropdown.value;
};

const selectCategory = (category: any) => {
  showCategoryDropdown.value = false;
};

const handleSearch = () => {
  if (!searchQuery.value.trim()) return;
  router.push({
    path: '/mall/search',
    query: { q: searchQuery.value }
  });
};

const loadDefaultAddress = async () => {
  try {
    const token = localStorage.getItem('token');
    if (!token) {
      ElMessage.warning('请先登录');
      router.push('/login');
      return;
    }

    const response = await getDefaultUserAddress();
    if (response.data && response.code === 1) {
      if (response.data) {
        addresses.value = [response.data];
        selectedAddressIndex.value = 0; 
      } else {
        addresses.value = [];
        ElMessage.warning('您尚未设置默认地址，请添加新地址');
      }
    } else {
      ElMessage.error('获取地址信息失败');
    }
  } catch (error) {
    console.error('获取默认地址出错:', error);
    ElMessage.error('获取地址信息失败');
  }
};

const getProductDetail = async (productId: number): Promise<Product | null> => {
  try {
    const response = await getProductById(productId);
    if (response.code === 1 && response.data) {
      return response.data;
    } else {
      return null;
    }
  } catch (error) {
    return null;
  }
};

const loadCartItems = async () => {
  isLoading.value = true;
  try {
    const token = localStorage.getItem('token');
    if (!token) {
      isLoading.value = false;
      return;
    }

    const response = await getCartList();
    if (response.data && response.code === 1) {
      const selectedItems = response.data.filter(item => item.isSelected);
      cartItemCount.value = response.data.length;
      const groupedByStore: Record<string, StoreGroup> = {};
      const productPromises = selectedItems.map(async (item) => {
        if (item.productId && (!item.name || !item.price)) {
          const productDetail = await getProductDetail(item.productId);
          if (productDetail) {
            item.name = productDetail.name;
            item.price = productDetail.price;
            item.imageUrl = productDetail.mainImage;
          }
        }
        
        const storeName = item.store || '儿童品牌商店';
        if (!groupedByStore[storeName]) {
          groupedByStore[storeName] = {
            name: storeName,
            products: []
          };
        }
        groupedByStore[storeName].products.push(item);
      });
      
      await Promise.all(productPromises);
      
      storeGroups.value = Object.values(groupedByStore);
    } else {
      ElMessage.error('获取购物车商品失败');
    }
  } catch (error) {
    ElMessage.error('获取购物车商品失败');
  } finally {
    isLoading.value = false;
  }
};

const formatFullAddress = (address: UserAddress): string => {
  return `${address.receiverProvince} ${address.receiverCity} ${address.receiverDistrict} ${address.receiverAddress}`;
};

const editAddress = (address: UserAddress, index: number): void => {
  isEditingAddress.value = true;
  currentEditAddressId.value = address.id || null;
  addressForm.value = { ...address };
  showAddressDialog.value = true;
};

const deleteAddress = async (id: number, index: number): Promise<void> => {
  try {
    await ElMessageBox.confirm('确定要删除这个地址吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    
    const response = await deleteUserAddress(id);
    if (response.code === 1) {
      ElMessage.success('地址删除成功');
      addresses.value.splice(index, 1);
      
      if (index === selectedAddressIndex.value) {
        selectedAddressIndex.value = addresses.value.length > 0 ? 0 : -1;
      } else if (index < selectedAddressIndex.value) {
        selectedAddressIndex.value--;
      }
      
      if (addresses.value.length === 0) {
        ElMessage.warning('您已删除所有地址，请添加新地址');
      }
    } else {
      ElMessage.error('地址删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除地址出错:', error);
      ElMessage.error('地址删除失败');
    }
  }
};

const saveAddress = async () => {
  if (!addressForm.value.receiverName || !addressForm.value.receiverPhone) {
    ElMessage.warning('请填写收货人和手机号');
    return;
  }
  
  if (!addressForm.value.receiverProvince || !addressForm.value.receiverCity || !addressForm.value.receiverDistrict) {
    ElMessage.warning('请填写完整的省市区信息');
    return;
  }
  
  if (!addressForm.value.receiverAddress) {
    ElMessage.warning('请填写详细地址');
    return;
  }
  
  try {
    let response;
    if (isEditingAddress.value && currentEditAddressId.value) {
      response = await updateUserAddress(currentEditAddressId.value, addressForm.value);
    } else {
      response = await addUserAddress(addressForm.value);
    }
    
    if (response.code === 1) {
      ElMessage.success(isEditingAddress.value ? '地址更新成功' : '地址添加成功');
      showAddressDialog.value = false;
      
      if (!isEditingAddress.value) {
        ElMessage.info('新地址已添加，重新加载默认地址');
      }
      
      await loadDefaultAddress();
    } else {
      ElMessage.error(isEditingAddress.value ? '地址更新失败' : '地址添加失败');
    }
  } catch (error) {
    ElMessage.error('保存地址失败');
  }
};

const totalProductPrice = computed(() => {
  let total = 0;
  storeGroups.value.forEach(store => {
    store.products.forEach((product: CartItem) => {
      total += (product.price ?? 0) * product.quantity;
    });
  });
  return total;
});

const shipping = ref(0);

const discount = ref(20);

const orderTotal = computed(() => {
  return totalProductPrice.value + shipping.value - discount.value;
});

const totalItems = computed(() => {
  let count = 0;
  storeGroups.value.forEach(store => {
    store.products.forEach((product: CartItem) => {
      count += product.quantity;
    });
  });
  return count;
});

const submitDisabled = computed(() => {
  return isLoading.value || storeGroups.value.length === 0 || selectedAddressIndex.value < 0 || addresses.value.length === 0;
});

const submitOrder = async () => {
  if (submitDisabled.value) {
    ElMessage.warning('请先选择商品和收货地址');
    return;
  }

  try {
    const selectedAddress = addresses.value[selectedAddressIndex.value];
    
    if (!selectedAddress || !selectedAddress.id) {
      ElMessage.warning('请选择有效的收货地址');
      return;
    }
    
    const response = await createOrder(selectedAddress.id);
    
    if (response.code === 1) {
      ElMessage.success('订单提交成功');
      localStorage.setItem('latestOrderNo', response.data.orderNo);
      router.push({
        path: '/mall/orders',
        query: { orderNo: response.data.orderNo }
      });
    } else {
      ElMessage.error('订单提交失败: ' + response.msg);
    }
  } catch (error) {
    ElMessage.error('订单提交失败，请稍后重试');
  }
};

onMounted(async () => {
  const token = localStorage.getItem('token');
  if (!token) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  await loadDefaultAddress();
  await loadCartItems();
  
  document.addEventListener('click', (e) => {
    if (showCategoryDropdown.value) {
      showCategoryDropdown.value = false;
    }
  });
});

watch(showAddressDialog, (newVal) => {
  if (!newVal) {
    isEditingAddress.value = false;
    currentEditAddressId.value = null;
    addressForm.value = {
      receiverName: '',
      receiverPhone: '',
      receiverProvince: '',
      receiverCity: '',
      receiverDistrict: '',
      receiverAddress: '',
      isDefault: false
    };
  }
});
</script>

<style scoped>
.checkout-container {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-section {
  margin-bottom: 20px;
  background: linear-gradient(to right, #f8f9fa, #e9ecef);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
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
  padding: 0 50px 0 15px;
  border: none;
  font-size: 15px;
  outline: none;
  background: transparent;
}

.search-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: 0;
  background: #41b883;
  color: white;
  border: none;
  width: 44px;
  height: 44px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 24px 24px 0;
}

.search-btn:hover {
  background: #38a573;
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

.checkout-main {
  max-width: 1226px;
  margin: 20px auto;
  padding: 0 10px;
}

.checkout-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 30px;
  color: #333;
}

.section-box {
  margin-bottom: 30px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
}

.add-address-btn {
  padding: 6px 12px;
  background-color: #fff;
  border: 1px solid #ff6700;
  color: #ff6700;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.add-address-btn:hover {
  background-color: #ff6700;
  color: #fff;
}

.address-list {
  padding: 20px;
}

.empty-address {
  text-align: center;
  padding: 20px;
  color: #999;
}

.address-item {
  display: flex;
  justify-content: space-between;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.address-item:last-child {
  margin-bottom: 0;
}

.address-item.active {
  border-color: #ff6700;
  background-color: #fff8f0;
}

.address-content {
  flex: 1;
}

.name-phone {
  margin-bottom: 8px;
}

.name {
  font-weight: 600;
  margin-right: 10px;
}

.phone {
  color: #666;
}

.address-detail {
  color: #666;
  line-height: 1.5;
}

.address-tag {
  display: inline-block;
  padding: 2px 6px;
  background-color: #ff6700;
  color: white;
  border-radius: 2px;
  font-size: 12px;
  margin-top: 8px;
}

.address-actions {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 15px;
}

.edit-btn, .delete-btn {
  padding: 4px 8px;
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 13px;
  text-align: center;
}

.edit-btn:hover, .delete-btn:hover {
  color: #ff6700;
}

.payment-options {
  display: flex;
  padding: 20px;
}

.payment-option {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-right: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.payment-option.active {
  border-color: #ff6700;
  background-color: #fff8f0;
}

.payment-icon {
  font-size: 24px;
  margin-right: 10px;
}

.payment-icon i {
  color: #ff6700;
}

.order-list {
  padding: 0;
}

.empty-cart, .loading-cart {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.back-to-cart {
  display: inline-block;
  margin-top: 10px;
  color: #ff6700;
  text-decoration: none;
}

.store-group {
  border-bottom: 1px solid #f0f0f0;
}

.store-group:last-child {
  border-bottom: none;
}

.store-header {
  padding: 15px 20px;
  background-color: #fafafa;
}

.store-name {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #333;
}

.store-name i {
  margin-right: 8px;
  color: #ff6700;
}

.product-list {
  padding: 0 20px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f5f5f5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-image {
  width: 80px;
  height: 80px;
  margin-right: 20px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 16px;
  margin-bottom: 8px;
}

.product-spec {
  font-size: 14px;
  color: #999;
}

.product-price {
  width: 100px;
  text-align: center;
  color: #666;
}

.product-quantity {
  width: 60px;
  text-align: center;
  color: #666;
}

.product-subtotal {
  width: 100px;
  text-align: right;
  font-weight: 600;
  color: #ff6700;
}

.amount-list {
  padding: 20px;
}

.amount-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.amount-item:last-child {
  margin-bottom: 0;
}

.amount-label {
  color: #666;
}

.amount-value {
  font-weight: 600;
}

.discount {
  color: #ff6700;
}

.amount-item.total {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.amount-item.total .amount-label {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.amount-item.total .amount-value {
  font-size: 20px;
  color: #ff6700;
}

.submit-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 30px;
}

.order-summary {
  margin-right: 20px;
  text-align: right;
}

.total-items {
  color: #666;
  margin-bottom: 5px;
}

.total-price {
  font-size: 16px;
  color: #333;
}

.price {
  font-size: 20px;
  font-weight: 600;
  color: #ff6700;
}

.submit-btn {
  padding: 12px 30px;
  background-color: #ff6700;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #f25807;
}

.submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.region-selector {
  display: flex;
  gap: 10px;
}

.region-selector .el-input {
  flex: 1;
}

@media (max-width: 768px) {
  .checkout-content {
    padding: 20px 15px;
  }
  
  .payment-options {
    flex-wrap: wrap;
  }
  
  .payment-option {
    margin-bottom: 10px;
    width: calc(50% - 10px);
    margin-right: 10px;
    padding: 10px;
  }
  
  .product-item {
    flex-wrap: wrap;
  }
  
  .product-image {
    margin-right: 10px;
  }
  
  .product-info {
    width: calc(100% - 90px);
    margin-bottom: 10px;
  }
  
  .product-price, .product-quantity, .product-subtotal {
    width: 33.333%;
    text-align: center;
  }
  
  .product-subtotal {
    text-align: right;
  }
  
  .submit-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .order-summary {
    margin-right: 0;
    margin-bottom: 15px;
    text-align: left;
  }
  
  .submit-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .payment-option {
    width: 100%;
    margin-right: 0;
  }
}
</style> 