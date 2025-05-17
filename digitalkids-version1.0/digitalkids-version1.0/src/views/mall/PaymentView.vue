<template>
  <div class="payment-container">
    <div class="payment-box">
      <div class="payment-header">
        <h2>订单支付</h2>
      </div>
      <div class="payment-amount">
        <span>支付金额：</span>
        <span class="amount">¥{{ amount.toFixed(2) }}</span>
      </div>
      <div class="payment-order-info">
        <p>订单号：{{ orderNumber }}</p>
        <p>支付方式：微信支付</p>
      </div>
      <div class="qrcode-container">
        <img src="/src/assets/binary.jpg" alt="支付二维码" class="qrcode-img" />
        <p class="scan-tip">请使用微信扫一扫</p>
      </div>
      <div class="payment-status">
        <div v-if="paymentStatus === 'waiting'" class="waiting-status">
          <p>等待支付中...</p>
        </div>
        <div v-else-if="paymentStatus === 'success'" class="success-status">
          <div class="success-icon">✓</div>
          <p>支付成功</p>
          <p class="redirect-tip">{{ redirectCount }}秒后返回订单页面...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const orderNumber = ref(route.params.orderNumber as string || '未知订单号');
const orderId = ref(parseInt(route.params.orderId as string) || 0);
const amount = ref(parseFloat(route.query.amount as string) || 0);
const paymentStatus = ref('waiting');
const countdown = ref(3); 
const redirectCount = ref(3); 
let countdownTimer: number | null = null;
let redirectTimer: number | null = null;

function simulatePaymentComplete() {
  paymentStatus.value = 'success';
  try {
    const orderStatusesStr = localStorage.getItem('orderPaymentStatuses');
    let orderStatuses: Record<string, string> = {};
    if (orderStatusesStr) {
      orderStatuses = JSON.parse(orderStatusesStr);
    }
    orderStatuses[orderNumber.value] = 'pending_shipping';
    localStorage.setItem('orderPaymentStatuses', JSON.stringify(orderStatuses));
    updateLocalOrderStatus(orderNumber.value, 'pending_shipping');
  } catch (error) {
  }
  startRedirectCountdown();
}

function updateLocalOrderStatus(orderNo: string, status: string) {
  try {
    const localOrdersStr = localStorage.getItem('localOrders');
    if (localOrdersStr) {
      const localOrders = JSON.parse(localOrdersStr) as Record<string, any>;
      if (localOrders[orderNo]) {
        localOrders[orderNo].status = status;
        localStorage.setItem('localOrders', JSON.stringify(localOrders));
      } else {
      }
    }
  } catch (error) {
  }
}

function startPaymentCountdown() {
  countdownTimer = window.setInterval(() => {
    countdown.value -= 1;
    if (countdown.value <= 0) {
      if (countdownTimer) clearInterval(countdownTimer);
      simulatePaymentComplete();
    }
  }, 1000);
}

function startRedirectCountdown() {
  redirectTimer = window.setInterval(() => {
    redirectCount.value -= 1;
    if (redirectCount.value <= 0) {
      if (redirectTimer) clearInterval(redirectTimer);
      router.push({
        path: '/mall/orders',
        query: { 
          paymentSuccess: 'true',
          orderId: orderId.value.toString()
        }
      });
    }
  }, 1000);
}

onMounted(() => {
  startPaymentCountdown();
});

onBeforeUnmount(() => {
  if (countdownTimer) clearInterval(countdownTimer);
  if (redirectTimer) clearInterval(redirectTimer);
});
</script>

<style scoped>
.payment-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.payment-box {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
  padding: 30px;
  text-align: center;
}

.payment-header {
  margin-bottom: 20px;
}

.payment-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
  font-weight: 500;
}

.payment-amount {
  margin-bottom: 20px;
  font-size: 16px;
}

.amount {
  font-size: 24px;
  font-weight: bold;
  color: #ff6700;
}

.payment-order-info {
  margin-bottom: 30px;
  color: #666;
  font-size: 14px;
}

.payment-order-info p {
  margin: 5px 0;
}

.qrcode-container {
  margin-bottom: 25px;
  padding: 10px;
}

.qrcode-img {
  width: 200px;
  height: 200px;
  display: block;
  margin: 0 auto 10px;
  border: 1px solid #e0e0e0;
  padding: 10px;
}

.scan-tip {
  font-size: 14px;
  color: #666;
}

.waiting-status p {
  color: #666;
  margin-bottom: 10px;
}

.countdown {
  color: #ff6700;
  font-weight: 500;
}

.success-status {
  color: #32CD32;
}

.success-icon {
  font-size: 60px;
  font-weight: bold;
  color: #32CD32;
  margin-bottom: 10px;
}

.redirect-tip {
  color: #666;
  margin-top: 10px;
  font-size: 14px;
}
</style> 