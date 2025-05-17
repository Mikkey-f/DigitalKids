import { createRouter, createWebHistory } from 'vue-router'; 
import { useAuthStore } from '@/stores/auth';
import HomeView from '../views/home/HomeView.vue'
import EncyclopediaView from '../views/encyclopedia/EncyclopediaView.vue'
import HealthView from '../views/health/HealthView.vue'
import PlaygroundView from '../views/playground/Playground.vue'
import AdminDashboard from '../views/administrator/ad_main.vue'

const routes = [ // 路由配置
  {
    path: '/',
    name: 'Guest',
    component: () => import('@/views/auth/GuestView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('@/views/auth/ForgotPasswordView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: () => import('@/views/auth/ResetPasswordView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  },
  {
    path: '/encyclopedia',
    name: 'encyclopedia',
    component: EncyclopediaView
  },
  {
    path: '/health',
    name: 'health',
    component: HealthView
  },
  {
    path: '/health/tongue-detection',
    name: 'tongueDetection',
    component: () => import('@/views/health/TongueDetectionView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/health/3d',
    name: 'health3d',
    component: () => import('@/views/health/Health3dView.vue'),
    children: [
      {
        path: '',
        component: () => import('@/views/health/model/HealthModelView.vue')
      }
    ]
  },
  // 育儿百科路由
  {
    path: '/encyclopedia',
    name: 'Encyclopedia',
    component: () => import('@/views/encyclopedia/EncyclopediaView.vue'),
    meta: { requiresAuth: false },
  },
  // 育儿百科文章详情页
  {
    path: '/encyclopedia/article/:id',
    name: 'EncyclopediaArticle',
    component: () => import('@/views/encyclopedia/EncyclopediaArticleView.vue'),
    meta: { requiresAuth: false },
    props: true
  },
  // 商城相关路由
  {
    path: '/mall',
    name: 'Mall',
    component: () => import('@/views/mall/MallView.vue'),
    meta: { requiresAuth: false },
    redirect: '/mall/recommend',
    children: [
      {
        path: 'recommend', 
        name: 'MallRecommend',
        component: () => import('@/views/mall/RecommendView.vue'),
        meta: { requiresAuth: false },
      },
      {
        path: 'category',
        name: 'MallCategory',
        component: () => import('@/views/mall/CategoryView.vue'),
        meta: { requiresAuth: false },
      },
      {
        path: 'search',
        name: 'MallSearch',
        component: () => import('@/views/mall/SearchView.vue'),
        meta: { requiresAuth: false },
      },
      {
        path: 'cart',
        name: 'MallCart',
        component: () => import('@/views/mall/CartView.vue'),
        meta: { requiresAuth: false },
      },
      {
        path: 'orders',
        name: 'MallOrders',
        component: () => import('@/views/mall/OrdersView.vue'),
        meta: { requiresAuth: false },
      },
      {
        path: 'payment/:orderId/:orderNumber',
        name: 'MallPayment',
        component: () => import('@/views/mall/PaymentView.vue'),
        meta: { requiresAuth: false },
        props: true
      },
      {
        path: 'product/:id',
        name: 'MallProductDetail',
        component: () => import('@/views/mall/ProductDetailView.vue'),
        meta: { requiresAuth: false },
        props: true
      },
      {
        path: 'checkout',
        name: 'MallCheckout',
        component: () => import('@/views/mall/CheckoutView.vue'),
        meta: { requiresAuth: false },
      }
    ]
  },
  // 个人中心相关路由
  {
    path: '/user',
    name: 'User',
    component: () => import('@/views/user/ProfileView.vue'),
    meta: { requiresAuth: false },
  },
  // 宝宝管理路由
  {
    path: '/user/baby-management',
    name: 'BabyManagement',
    component: () => import('@/views/user/BabyManagementView.vue'),
    meta: { requiresAuth: false },
  },
  // 商品详情路由
  {
    path: '/product/detail/:id',
    name: 'ProductDetail',
    component: () => import('@/views/mall/ProductDetailView.vue'),
    meta: { requiresAuth: false },
    props: true
  },
  // 百科详情路由
  {
    path: '/encyclopedia/detail/:id',
    name: 'EncyclopediaDetail',
    component: () => import('@/views/encyclopedia/EncyclopediaArticleView.vue'),
    meta: { requiresAuth: false },
    props: true
  },
  {
    path: '/playground',
    name: 'playground',
    component: PlaygroundView,
    meta: { requiresAuth: false },
  },
  {
    path: '/playground/mbti',
    name: 'MBTI',
    component: () => import('@/views/playground/MBTI.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/playground/controller',
    name: 'Controller',
    meta: { requiresAuth: false },
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: {
      requiresAuth: true,
      title: '后台管理',
      openInNewTab: true
    }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// 导航守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router;