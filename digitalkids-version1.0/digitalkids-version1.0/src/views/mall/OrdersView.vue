<template>
  <div class="orders-container">
    <SearchBar :cart-count="cartCount" />
    <div class="orders-main-container">
      <div class="orders-header">
        <h1>我的订单</h1>
      </div>
      <div class="order-tabs">
        <div 
          class="order-tab" 
          :class="{ active: activeStatus === '' }"
          @click="activeStatus = ''"
        >
          全部订单
        </div>
        <div 
          class="order-tab" 
          :class="{ active: activeStatus === 'pending_payment' }"
          @click="activeStatus = 'pending_payment'"
        >
          待付款
        </div>
        <div 
          class="order-tab" 
          :class="{ active: activeStatus === 'pending_shipping' }"
          @click="activeStatus = 'pending_shipping'"
        >
          待发货
        </div>
        <div 
          class="order-tab" 
          :class="{ active: activeStatus === 'pending_receipt' }"
          @click="activeStatus = 'pending_receipt'"
        >
          待收货
        </div>
        <div 
          class="order-tab" 
          :class="{ active: activeStatus === 'completed' }"
          @click="activeStatus = 'completed'"
        >
          已完成
        </div>
      </div>

      <div class="order-search">
        <div class="search-input">
          <input 
            type="text" 
            placeholder="商品名称/商品编号/订单号" 
            v-model="searchKeyword"
          />
          <button @click="searchOrders">搜索</button>
        </div>
        <div class="date-filter">
          <span>下单时间：</span>
          <select v-model="dateRange">
            <option value="all">全部时间</option>
            <option value="1month">近1个月</option>
            <option value="3months">近3个月</option>
            <option value="6months">近6个月</option>
          </select>
        </div>
      </div>

      <div class="orders-list" v-if="filteredOrders.length > 0">
        <div class="order-item" v-for="order in filteredOrders" :key="order.id">
          <div class="order-header">
            <div class="order-info">
              <span class="order-date">{{ order.date }}</span>
              <span class="order-number">订单号：{{ order.orderNumber }}</span>
              <span class="store-name">
                <i class="fas fa-store"></i> {{ order.store }}
              </span>
            </div>
            <div class="order-status" :class="order.status">
              {{ getStatusText(order.status) }}
            </div>
          </div>
          
          <div class="order-products">
            <div class="product-item" v-for="product in order.products" :key="product.id">
              <div class="product-img">
                <img :src="product.image" :alt="product.name" />
              </div>
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-spec">{{ product.spec }}</div>
              </div>
              <div class="product-quantity">x{{ product.quantity }}</div>
              <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-total">
              <span>共 {{ getTotalQuantity(order) }} 件商品，总计：</span>
              <span class="total-price">¥{{ getOrderTotal(order).toFixed(2) }}</span>
              <span class="shipping-fee">(含运费：¥{{ order.shippingFee.toFixed(2) }})</span>
            </div>
            
            <div class="order-actions">
              <button 
                v-if="order.status === 'pending_payment'" 
                class="pay-btn"
                @click="payOrder(order.id)"
              >
                立即付款
              </button>
              <button 
                v-if="order.status === 'pending_receipt'" 
                class="confirm-btn"
                @click="confirmReceipt(order.id)"
              >
                确认收货
              </button>
              <button 
                v-if="order.status === 'completed' || order.status === 'pending_receipt'" 
                class="review-btn"
                @click="reviewOrder(order.id)"
              >
                {{ order.hasReviewed ? '查看评价' : '去评价' }}
              </button>
              <button 
                class="detail-btn"
              >
                订单详情
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-orders" v-else>
        <div class="empty-icon">
          <i class="fas fa-file-alt"></i>
        </div>
        <p>您还没有{{ activeStatus ? getStatusText(activeStatus) : '' }}的订单</p>
        <router-link to="/mall/recommend" class="go-shopping-btn">
          去购物
        </router-link>
      </div>

      <div class="pagination" v-if="filteredOrders.length > 0">
        <button 
          class="prev-btn" 
          :disabled="currentPage === 1"
          @click="currentPage--"
        >
          上一页
        </button>
        <div class="page-numbers">
          <span 
            v-for="page in totalPages" 
            :key="page" 
            class="page-number"
            :class="{ active: currentPage === page }"
            @click="currentPage = page"
          >
            {{ page }}
          </span>
        </div>
        <button 
          class="next-btn" 
          :disabled="currentPage === totalPages"
          @click="currentPage++"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onBeforeUnmount } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import SearchBar from './components/SearchBar.vue';
import { getOrderByNo, payOrder as apiPayOrder} from '@/api/order';

const router = useRouter();
const route = useRoute();
const cartCount = ref(0);
const activeStatus = ref('');
const searchKeyword = ref('');
const dateRange = ref('all');
const currentPage = ref(1);
const itemsPerPage = 5;
const realOrders = ref<any[]>([]);

const mockOrders = ref([
  {
    id: 1001,
    date: '2023-07-22 15:30:21',
    orderNumber: '2307221530211001',
    store: '童年健康官方旗舰店',
    status: 'pending_payment',
    shippingFee: 10,
    hasReviewed: false,
    products: [
      {
        id: 101,
        name: '儿童复合维生素咀嚼片',
        spec: '60片/瓶',
        price: 128,
        quantity: 1,
        image: '/src/assets/images/goods/yw.png'
      }
    ]
  },
  {
    id: 1002,
    date: '2023-07-20 10:15:36',
    orderNumber: '2307201015361002',
    store: '营养专家母婴旗舰店',
    status: 'pending_shipping',
    shippingFee: 0,
    hasReviewed: false,
    products: [
      {
        id: 102,
        name: '儿童益生菌冲剂',
        spec: '30袋/盒',
        price: 198,
        quantity: 2,
        image: '/src/assets/images/goods/zy.jpg'
      },
      {
        id: 103,
        name: '婴幼儿鱼肝油滴剂',
        spec: '30ml',
        price: 158,
        quantity: 1,
        image: '/src/assets/images/goods/yy.jpg'
      }
    ]
  },
  {
    id: 1003,
    date: '2023-07-15 09:22:48',
    orderNumber: '2307150922481003',
    store: '童年健康官方旗舰店',
    status: 'pending_receipt',
    shippingFee: 5,
    hasReviewed: false,
    products: [
      {
        id: 104,
        name: '婴儿洗护套装',
        spec: '洗发沐浴二合一300ml+护肤霜200g',
        price: 168,
        quantity: 1,
        image: '/src/assets/images/goods/xh.jpg'
      }
    ]
  },
  {
    id: 1004,
    date: '2023-07-10 14:05:12',
    orderNumber: '2307101405121004',
    store: '环球母婴专营店',
    status: 'completed',
    shippingFee: 12,
    hasReviewed: true,
    products: [
      {
        id: 105,
        name: '儿童智力开发拼图',
        spec: '1袋装',
        price: 299,
        quantity: 1,
        image: '/src/assets/images/goods/yzpt.jpg'
      }
    ]
  },
  {
    id: 1005,
    date: '2023-07-05 16:42:30',
    orderNumber: '2307051642301005',
    store: '童年健康官方旗舰店',
    status: 'completed',
    shippingFee: 0,
    hasReviewed: false,
    products: [
      {
        id: 106,
        name: '儿童牙刷套装',
        spec: '3支装',
        price: 59.9,
        quantity: 1,
        image: '/src/assets/images/goods/yg.jpg'
      },
      {
        id: 107,
        name: '儿童防蛀牙膏',
        spec: '50g x 2',
        price: 48.8,
        quantity: 2,
        image: '/src/assets/images/goods/yg.jpg'
      }
    ]
  }
]);

const allOrders = computed(() => {
  return [...realOrders.value, ...mockOrders.value];
});

const filteredOrders = computed(() => {
  let result = allOrders.value;
  if (activeStatus.value) {
    result = result.filter(order => order.status === activeStatus.value);
  }
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    result = result.filter(order => 
      order.orderNumber.toLowerCase().includes(keyword) ||
      order.products.some((product: any) => 
        product.name.toLowerCase().includes(keyword)
      )
    );
  }
  if (dateRange.value !== 'all') {
    const now = new Date();
    let cutoffDate = new Date();
    
    if (dateRange.value === '1month') {
      cutoffDate.setMonth(now.getMonth() - 1);
    } else if (dateRange.value === '3months') {
      cutoffDate.setMonth(now.getMonth() - 3);
    } else if (dateRange.value === '6months') {
      cutoffDate.setMonth(now.getMonth() - 6);
    }
    
    result = result.filter(order => {
      const orderDate = new Date(order.date);
      return orderDate >= cutoffDate;
    });
  }
  
  return result;
});

const totalPages = computed(() => {
  return Math.ceil(filteredOrders.value.length / itemsPerPage);
});

const getStatusText = (status: string) => {
  switch (status) {
    case 'pending_payment':
      return '待付款';
    case 'pending_shipping':
      return '待发货';
    case 'pending_receipt':
      return '待收货';
    case 'completed':
      return '已完成';
    case 'cancelled':
      return '已取消';
    default:
      return '未知状态';
  }
};

const getStatusFromNumber = (statusNumber: number) => {
  switch (statusNumber) {
    case 0:
      return 'cancelled';
    case 1:
      return 'pending_payment';
    case 2:
      return 'pending_shipping'; 
    case 3:
      return 'pending_receipt'; 
    case 4:
      return 'completed'; 
    default:
      return 'pending_payment';
  }
};

const getTotalQuantity = (order: any) => {
  return order.products.reduce((sum: number, product: any) => {
    return sum + product.quantity;
  }, 0);
};

const getOrderTotal = (order: any) => {
  const productsTotal = order.products.reduce((sum: number, product: any) => {
    return sum + product.price * product.quantity;
  }, 0);
  
  return productsTotal + order.shippingFee;
};

const searchOrders = () => {
  currentPage.value = 1; 
};

const payOrder = (orderId: number) => {
  console.log('支付订单:', orderId);
  const order = allOrders.value.find(o => o.id === orderId);
  if (order) {
    const total = getOrderTotal(order);
    router.push({
      path: `/mall/payment/${orderId}/${order.orderNumber}`,
      query: {
        amount: total.toString()
      }
    });
  }
};

const confirmReceipt = (orderId: number) => {
  console.log('确认收货:', orderId);
  const order = allOrders.value.find(o => o.id === orderId);
  if (order) {
    order.status = 'completed';
  }
};

const reviewOrder = (orderId: number) => {
  console.log('评价订单:', orderId);
  const order = allOrders.value.find(o => o.id === orderId);
  if (order && !order.hasReviewed) {
    alert(`准备评价订单 ${orderId}`);
  } else {
    alert(`查看订单 ${orderId} 的评价`);
  }
};

const convertApiOrderToViewOrder = (apiOrder: any, orderId: number) => {
  const products = apiOrder.orderItemList.map((item: any, index: number) => {
    return {
      id: orderId * 100 + index,
      name: item.productName,
      spec: '标准规格', 
      price: item.price || item.Price || 0, 
      quantity: item.quantity,
      image: item.productImage
    };
  });

  const updateTime = apiOrder.paymentTime || apiOrder.createTime || new Date().toISOString();
  const formattedDate = new Date(Number(updateTime)).toLocaleString('zh-CN');
  
  const orderStatus = getOrderPaymentStatusFromStorage(apiOrder.orderNo) || getStatusFromNumber(apiOrder.status);
  
  return {
    id: orderId,
    date: formattedDate, 
    orderNumber: apiOrder.orderNo,
    store: '童年健康官方旗舰店', 
    status: orderStatus, 
    shippingFee: 0, 
    hasReviewed: false,
    products: products
  };
};

const fetchUserOrders = async () => {
  try {
    const token = localStorage.getItem('token');
    if (!token) return;
    const localOrders = loadOrdersFromLocalStorage();
    if (localOrders && localOrders.length > 0) {
      realOrders.value = localOrders;
    }
    const latestOrderNo = localStorage.getItem('latestOrderNo');
    if (latestOrderNo) {
      await fetchOrderByOrderNo(latestOrderNo, Date.now());
    }
  } catch (error) {
    const localOrders = loadOrdersFromLocalStorage();
    if (localOrders && localOrders.length > 0) {
      realOrders.value = localOrders;
    }
  }
};

const getOrderPaymentStatusFromStorage = (orderNo: string): string | null => {
  try {
    const orderStatusesStr = localStorage.getItem('orderPaymentStatuses');
    if (orderStatusesStr) {
      const orderStatuses = JSON.parse(orderStatusesStr) as Record<string, string>;
      return orderStatuses[orderNo] || null;
    }
  } catch (error) {
  }
  return null;
};

const saveOrderPaymentStatusToStorage = (orderNo: string, status: string) => {
  try {
    const orderStatusesStr = localStorage.getItem('orderPaymentStatuses');
    let orderStatuses: Record<string, string> = {};
    
    if (orderStatusesStr) {
      orderStatuses = JSON.parse(orderStatusesStr);
    }
    
    orderStatuses[orderNo] = status;
    localStorage.setItem('orderPaymentStatuses', JSON.stringify(orderStatuses));
  } catch (error) {
  }
};

const saveOrderToLocalStorage = (order: any) => {
  try {
    const localOrdersStr = localStorage.getItem('localOrders');
    let localOrders: Record<string, any> = {};
    
    if (localOrdersStr) {
      localOrders = JSON.parse(localOrdersStr);
    }
    
    localOrders[order.orderNumber] = order;
    localStorage.setItem('localOrders', JSON.stringify(localOrders));
  } catch (error) {
  }
};

const loadOrdersFromLocalStorage = () => {
  try {
    const localOrdersStr = localStorage.getItem('localOrders');
    if (localOrdersStr) {
      const localOrders = JSON.parse(localOrdersStr) as Record<string, any>;
      return Object.values(localOrders);
    }
  } catch (error) {
  }
  return [];
};

const fetchOrderByOrderNo = async (orderNo: string, orderId: number) => {
  if (!orderNo) return;

  try {
    const token = localStorage.getItem('token');
    if (!token) return;
    
    const responsePromise = getOrderByNo(orderNo);
    const timeoutPromise = new Promise((_, reject) => 
      setTimeout(() => reject(new Error('获取订单超时')), 5000)
    );
    
    const response = await Promise.race([responsePromise, timeoutPromise]) as any;
    
    if (response && response.code === 1 && response.data) {
      if (!response.data.orderItemList) return;
      const newOrder = convertApiOrderToViewOrder(response.data, orderId);
      realOrders.value = [];
      realOrders.value.unshift(newOrder);
      saveOrderToLocalStorage(newOrder);
    }
  } catch (error) {
  }
};

const ensureOrderPersistence = () => {
  allOrders.value.forEach(order => {
    if (order.orderNumber) {
      saveOrderToLocalStorage(order);
    }
  });
};

watch(() => route.query, (newQuery) => {
  if (newQuery.orderNo) {
    const orderNo = newQuery.orderNo as string;
    const orderId = Date.now();
    fetchOrderByOrderNo(orderNo, orderId);
    localStorage.setItem('latestOrderNo', orderNo);
  }
  
  if (newQuery.paymentSuccess === 'true' && newQuery.orderId) {
    const orderId = parseInt(newQuery.orderId as string);
    const order = allOrders.value.find(o => o.id === orderId);
    if (order && order.status === 'pending_payment') {
      order.status = 'pending_shipping';
      if (order.orderNumber) {
        saveOrderPaymentStatusToStorage(order.orderNumber, 'pending_shipping');
        saveOrderToLocalStorage(order);
      }
      
      if (order.orderNumber && order.orderNumber.length > 10) {
        const token = localStorage.getItem('token');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};
        
        apiPayOrder(order.orderNumber).catch(error => {
          console.error('支付订单API调用失败:', error);
        });
      }
      
      router.replace({ path: '/mall/orders' });
      alert('支付成功！订单状态已更新');
    }
  }
}, { immediate: true });

watch(() => route.query.orderNo, (newOrderNo) => {
  console.log('URL订单号参数变更:', newOrderNo);
  if (newOrderNo && typeof newOrderNo === 'string') {
    fetchOrderByOrderNo(newOrderNo, Date.now());
  }
});

onMounted(async () => {
  const saveInterval = setInterval(() => {
    ensureOrderPersistence();
  }, 60000);

  try {
    const localOrders = loadOrdersFromLocalStorage();
    if (localOrders && localOrders.length > 0) {
      realOrders.value = localOrders;
    }
    const token = localStorage.getItem('token');
    if (token) {
      await fetchUserOrders();
    }
    const latestOrderNo = localStorage.getItem('latestOrderNo');
    if (latestOrderNo) {
      const orderId = Date.now();
      try {
        await fetchOrderByOrderNo(latestOrderNo, orderId);
      } catch (fetchError) {
      }
    }
    if (route.query.orderNo) {
      const orderNo = route.query.orderNo as string;
      const orderId = Date.now();
      
      try {
        await fetchOrderByOrderNo(orderNo, orderId);
        localStorage.setItem('latestOrderNo', orderNo);
      } catch (urlFetchError) {
      }
    }
    if (route.query.paymentSuccess === 'true' && route.query.orderId) {
      const orderId = parseInt(route.query.orderId as string);
      const order = allOrders.value.find(o => o.id === orderId);
      if (order && order.status === 'pending_payment') {
        order.status = 'pending_shipping';
        if (order.orderNumber) {
          saveOrderPaymentStatusToStorage(order.orderNumber, 'pending_shipping');
          saveOrderToLocalStorage(order);
        }
        if (order.orderNumber && order.orderNumber.length > 10) {
          try {
            await apiPayOrder(order.orderNumber);
          } catch (payError) {
          }
        }
        
        router.replace({ path: '/mall/orders' });
        alert('支付成功！订单状态已更新');
      }
    }
    ensureOrderPersistence();
  } catch (error) {
  }
});

onBeforeUnmount(() => {
  ensureOrderPersistence();
});

window.addEventListener('beforeunload', () => {
  ensureOrderPersistence();
});
</script>

<style scoped>
.orders-container {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.orders-main-container {
  max-width: 1226px;
  margin: 0 auto;
  padding: 20px 10px;
}

.orders-header {
  margin-bottom: 20px;
}

.orders-header h1 {
  font-size: 24px;
  font-weight: 400;
  color: #333;
  margin: 0;
}

.order-tabs {
  display: flex;
  background: #fff;
  border-radius: 8px 8px 0 0;
  margin-bottom: 2px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.order-tab {
  padding: 15px 20px;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
  font-size: 14px;
  text-align: center;
  flex: 1;
  position: relative;
}

.order-tab:hover {
  color: #ff6700;
}

.order-tab.active {
  color: #ff6700;
  font-weight: 500;
}

.order-tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #ff6700;
}

.order-search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 15px 20px;
  margin-bottom: 15px;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.search-input {
  display: flex;
}

.search-input input {
  width: 300px;
  height: 34px;
  border: 1px solid #e0e0e0;
  border-radius: 4px 0 0 4px;
  padding: 0 10px;
  font-size: 14px;
}

.search-input button {
  height: 34px;
  padding: 0 15px;
  background: #ff6700;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
}

.date-filter {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 14px;
}

.date-filter select {
  margin-left: 8px;
  padding: 6px 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
}

.orders-list {
  margin-bottom: 20px;
}

.order-item {
  background: #fff;
  border-radius: 8px;
  margin-bottom: 15px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.order-info {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 13px;
}

.order-date, .order-number {
  margin-right: 20px;
}

.store-name {
  color: #333;
  font-weight: 500;
}

.store-name i {
  margin-right: 5px;
  color: #ff6700;
}

.order-status {
  font-size: 14px;
  font-weight: 500;
}

.order-status.pending_payment {
  color: #ff6700;
}

.order-status.pending_shipping {
  color: #7B68EE;
}

.order-status.pending_receipt {
  color: #32CD32;
}

.order-status.completed {
  color: #999;
}

.order-status.cancelled {
  color: #999;
}

.order-products {
  padding: 0 20px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f5f5f5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-img {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  flex-shrink: 0;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.product-spec {
  color: #999;
  font-size: 12px;
}

.product-quantity {
  width: 60px;
  text-align: center;
  color: #666;
  font-size: 14px;
}

.product-price {
  width: 100px;
  text-align: right;
  color: #666;
  font-size: 14px;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #fafafa;
  border-top: 1px solid #f0f0f0;
}

.order-total {
  color: #666;
  font-size: 14px;
}

.total-price {
  color: #ff6700;
  font-weight: 700;
  font-size: 16px;
  margin: 0 5px;
}

.shipping-fee {
  color: #999;
  font-size: 12px;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.order-actions button {
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.pay-btn {
  background: #ff6700;
  color: white;
  border: none;
}

.pay-btn:hover {
  background: #f25807;
}

.confirm-btn {
  background: #32CD32;
  color: white;
  border: none;
}

.confirm-btn:hover {
  background: #2db82d;
}

.review-btn {
  background: white;
  color: #7B68EE;
  border: 1px solid #7B68EE;
}

.review-btn:hover {
  background: #f5f0ff;
}

.detail-btn {
  background: white;
  color: #666;
  border: 1px solid #e0e0e0;
}

.detail-btn:hover {
  background: #f5f5f5;
}

.empty-orders {
  text-align: center;
  padding: 60px 0;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
}

.empty-icon {
  font-size: 60px;
  color: #e0e0e0;
  margin-bottom: 20px;
}

.empty-orders p {
  color: #999;
  margin-bottom: 20px;
}

.go-shopping-btn {
  display: inline-block;
  background: #ff6700;
  color: white;
  padding: 8px 20px;
  border-radius: 4px;
  text-decoration: none;
  transition: background 0.3s;
}

.go-shopping-btn:hover {
  background: #f25807;
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

@media (max-width: 768px) {
  .order-header, .order-footer {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .order-status, .order-actions {
    margin-top: 10px;
  }
  
  .product-item {
    flex-wrap: wrap;
  }
  
  .product-info {
    width: 100%;
    margin-top: 10px;
  }
  
  .product-quantity, .product-price {
    margin-top: 10px;
  }
  
  .order-search {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .date-filter {
    margin-top: 10px;
  }
  
  .search-input input {
    width: 100%;
  }
}
</style> 