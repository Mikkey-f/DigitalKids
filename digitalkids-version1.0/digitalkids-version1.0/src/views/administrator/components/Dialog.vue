<template>
  <el-dialog
    v-model="visible"
    :title="dialogTitle"
    width="50%"
    top="5vh"
  >
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      加载中...
    </div>
    <div v-else class="user-detail-container">
      <div class="user-basic-info">
        <el-avatar :size="80" :src="userInfo.avatarUrl" />
        <div class="user-meta">
          <h3>{{ userInfo.nickName || '未设置昵称' }}</h3>
          <p>OpenID: {{ userInfo.openid }}</p>
        </div>
      </div>
      <el-divider />
      <div class="detail-grid">
        <div class="detail-item">
          <label>注册日期:</label>
          <div class="value">{{ formatDate(userInfo.registerDate) }}</div>
        </div>
        <div class="detail-item">
          <label>最后登录:</label>
          <div class="value">{{ formatDate(userInfo.lastLoginDate) }}</div>
        </div>
        <div class="detail-item">
          <label>登录IP:</label>
          <div class="value">{{ userInfo.lastLoginIp || '未知' }}</div>
        </div>
        <div class="detail-item">
          <label>设备类型:</label>
          <div class="value">{{ userInfo.deviceType || '未知' }}</div>
        </div>
      </div>
      <el-divider>宝宝信息</el-divider>
      <div v-if="babyInfo" class="baby-info">
        <div class="baby-item">
          <label>宝宝姓名:</label>
          <span>{{ babyInfo.name || '未设置' }}</span>
        </div>
        <div class="baby-item">
          <label>宝宝年龄:</label>
          <span>{{ babyInfo.age || '未设置' }}</span>
        </div>
        <div class="baby-item">
          <label>宝宝性别:</label>
          <span>{{ babyInfo.gender ? (babyInfo.gender === 'male' ? '男' : '女') : '未设置' }}</span>
        </div>
      </div>
      <div v-else class="no-baby">
        该用户尚未添加宝宝信息
      </div>
    </div>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import axios from 'axios'

const props = defineProps({
  modelValue: Boolean,
  dialogTitle: String,
  userId: String
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)
const loading = ref(false)
const userInfo = ref({})
const babyInfo = ref(null)

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    fetchUserDetail()
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const fetchUserDetail = async () => {
  try {
    loading.value = true
    userInfo.value = {
      nickName: '小宝贝妈妈',
      avatarUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      openid: props.userId,
      registerDate: '2023-05-10 14:30:00',
      lastLoginDate: '2023-06-15 09:20:00',
      lastLoginIp: '192.168.1.100',
      deviceType: 'iOS'
    }
    babyInfo.value = {
      name: '小小明',
      age: '3岁',
      gender: 'male'
    }
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '未知'
  const date = new Date(dateStr)
  return date.toLocaleString()
}
</script>

<style scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #666;
  
  .el-icon {
    margin-right: 10px;
    font-size: 20px;
    animation: rotating 2s linear infinite;
  }
}

.user-detail-container {
  padding: 0 20px;
}

.user-basic-info {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  
  .user-meta h3 {
    margin: 0;
    font-size: 1.5rem;
    color: #333;
  }
  
  .user-meta p {
    margin: 5px 0 0;
    color: #888;
    font-size: 0.9rem;
  }
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-top: 20px;
}

.detail-item {
  margin-bottom: 12px;
  
  label {
    display: block;
    font-weight: bold;
    color: #666;
    margin-bottom: 5px;
  }
  
  .value {
    padding: 8px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
}

.baby-info {
  .baby-item {
    margin-bottom: 12px;
    
    label {
      display: inline-block;
      width: 80px;
      font-weight: bold;
      color: #666;
    }
  }
}

.no-baby {
  text-align: center;
  color: #999;
  padding: 20px 0;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>