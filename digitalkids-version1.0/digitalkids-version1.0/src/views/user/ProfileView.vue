<template>
  <div class="profile-container">
    <div class="profile-header">
      <div class="user-info-card">
        <div class="user-avatar-wrapper">
          <div class="user-avatar">
            <img v-if="userInfo.avatar" :src="userInfo.avatar" alt="用户头像" />
            <el-icon v-else class="default-avatar"><UserFilled /></el-icon>
          </div>
          <div class="avatar-upload">
            <el-upload
              class="avatar-uploader"
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleAvatarChange"
            >
              <button class="upload-btn">更换头像</button>
            </el-upload>
          </div>
        </div>
        <div class="user-info">
          <h1 class="username">{{ userInfo.username }}</h1>
          <div class="user-meta">
            <div class="meta-item">
              <el-icon><Iphone /></el-icon>
              <span>{{ formatPhone(userInfo.phone) || '未绑定手机' }}</span>
            </div>
            <div class="meta-item">
              <el-icon><Location /></el-icon>
              <span>{{ userInfo.location || '未设置地区' }}</span>
            </div>
          </div>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">
                {{ userInfo.babyCount || 0 }}
                <el-tooltip content="刷新宝宝数量" placement="top">
                  <el-icon class="refresh-icon" @click="fetchBabies"><Refresh /></el-icon>
                </el-tooltip>
              </div>
              <div class="stat-label">宝宝数量</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.daysCount || 0 }}</div>
              <div class="stat-label">注册天数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.checkInDays || 0 }}</div>
              <div class="stat-label">打卡天数</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <div class="settings-menu">
        <div class="menu-section">
          <div class="menu-title">个人信息</div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'info' }"
            @click="activeTab = 'info'"
          >
            <el-icon><UserFilled /></el-icon>
            <span>基本资料</span>
          </div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'contacts' }"
            @click="activeTab = 'contacts'"
          >
            <el-icon><Phone /></el-icon>
            <span>联系方式</span>
          </div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'address' }"
            @click="activeTab = 'address'"
          >
            <el-icon><Location /></el-icon>
            <span>地址管理</span>
          </div>
        </div>
        
        <div class="menu-section">
          <div class="menu-title">通知设置</div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'notifications' }"
            @click="activeTab = 'notifications'"
          >
            <el-icon><Bell /></el-icon>
            <span>消息通知</span>
          </div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'privacy' }"
            @click="activeTab = 'privacy'"
          >
            <el-icon><Lock /></el-icon>
            <span>隐私设置</span>
          </div>
        </div>
        
        <div class="menu-section">
          <div class="menu-title">偏好设置</div>
          <div 
            class="menu-item" 
            :class="{ active: activeTab === 'general' }"
            @click="activeTab = 'general'"
          >
            <el-icon><Setting /></el-icon>
            <span>通用设置</span>
          </div>
        </div>
        
        <button class="logout-button" @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          退出登录
        </button>
      </div>

      <div class="settings-content">
        <div v-if="activeTab === 'info'" class="settings-panel">
          <h2 class="panel-title">基本资料</h2>
          <el-form :model="userInfoForm" label-position="top">
            <el-form-item label="昵称">
              <el-input v-model="userInfoForm.username" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userInfoForm.gender">
                <el-radio :label="1" value="男">男</el-radio>
                <el-radio :label="2" value="女">女</el-radio>
                <el-radio :label="0" value="其他">其他</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="所在地区">
              <el-input v-model="userInfoForm.location" placeholder="请输入所在地区" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveUserInfo">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="activeTab === 'contacts'" class="settings-panel">
          <h2 class="panel-title">联系方式</h2>
          <el-form :model="contactsForm" label-position="top">
            <el-form-item label="手机号码">
              <el-input v-model="contactsForm.phone" placeholder="请输入手机号码">
                <template #append>
                  <el-button @click="sendVerificationCode">获取验证码</el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="验证码" v-if="showVerificationCode">
              <el-input v-model="contactsForm.verificationCode" placeholder="请输入验证码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveContacts">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <div v-if="activeTab === 'notifications'" class="settings-panel">
          <h2 class="panel-title">消息通知</h2>
          <div class="notification-settings">
            <div class="notification-item">
              <div class="notification-info">
                <div class="notification-title">健康提醒</div>
                <div class="notification-desc">接收宝宝健康检查提醒和建议</div>
              </div>
              <el-switch v-model="notificationSettings.health" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <div class="notification-title">成长记录</div>
                <div class="notification-desc">接收宝宝成长里程碑提醒</div>
              </div>
              <el-switch v-model="notificationSettings.growth" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <div class="notification-title">系统通知</div>
                <div class="notification-desc">接收系统更新和维护通知</div>
              </div>
              <el-switch v-model="notificationSettings.system" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <div class="notification-title">活动推送</div>
                <div class="notification-desc">接收优惠活动和新功能推送</div>
              </div>
              <el-switch v-model="notificationSettings.activity" />
            </div>
          </div>
          <div class="notification-actions">
            <el-button type="primary" @click="saveNotifications">保存设置</el-button>
          </div>
        </div>

        <div v-if="activeTab === 'privacy'" class="settings-panel">
          <h2 class="panel-title">隐私设置</h2>
        </div>
        
        <div v-if="activeTab === 'address'" class="settings-panel">
          <h2 class="panel-title">地址管理</h2>
          
          <div class="address-top-bar">
            <div class="address-count">共 {{ addressList.length }} 个地址</div>
            <el-button type="primary" @click="showAddressForm('add')">
              <el-icon><Plus /></el-icon>
              添加新地址
            </el-button>
          </div>
          
          <div v-if="loading" class="address-loading">
            <el-skeleton :rows="3" animated />
          </div>
          
          <div v-else-if="addressList.length === 0" class="no-address">
            <el-empty description="您还没有添加地址">
              <el-button type="primary" @click="showAddressForm('add')">添加地址</el-button>
            </el-empty>
          </div>
          
          <div v-else class="address-list">
            <div v-for="(address, index) in addressList" :key="index" class="address-card">
              <div class="address-card-inner">
                <div class="address-content">
                  <div class="address-main">
                    <span class="address-name">{{ address.receiverName }}</span>
                    <span class="address-phone">{{ formatPhone(address.receiverPhone) }}</span>
                  </div>
                  <div class="address-detail">
                    {{ address.receiverProvince }} {{ address.receiverCity }} {{ address.receiverDistrict }} {{ address.receiverAddress }}
                  </div>
                </div>
                <div class="address-actions">
                  <el-button type="primary" plain size="small" @click="showAddressForm('edit', address)">
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                  <el-button type="danger" plain size="small" @click="confirmDeleteAddress(address)">
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="activeTab === 'general'" class="settings-panel">
          <h2 class="panel-title">通用设置</h2>
        </div>
      </div>
    </div>

    <div class="my-babies-section">
      <div class="section-header">
        <h2 class="section-title">我的宝宝</h2>
        <el-button type="primary" @click="goToBabyManagement">
          <el-icon><Management /></el-icon>
          宝宝管理
        </el-button>
      </div>
      
      <div v-if="babies.length === 0" class="no-babies">
        <el-empty description="暂无宝宝信息">
          <el-button type="primary" @click="gotoAddBaby">添加宝宝</el-button>
        </el-empty>
      </div>
      
      <div v-else class="babies-list">
        <div v-for="baby in babies" :key="baby.id" class="baby-card">
          <div class="baby-card-inner">
            <div class="baby-avatar">
              <img v-if="baby.avatar" :src="baby.avatar" :alt="baby.name" />
              <div v-else class="default-baby-avatar">
                <span>{{ baby.name.substring(0, 1) }}</span>
              </div>
            </div>
            <div class="baby-info">
              <div class="baby-name">{{ baby.nickname }}</div>
              <div class="baby-age">{{ formatBabyAge(baby.birthday) }}</div>
              <div class="baby-birthday">
                <el-icon><Calendar /></el-icon>
                {{ formatDate(baby.birthday) }}
              </div>
            </div>
            <div class="baby-actions">
              <el-tooltip content="编辑宝宝信息" placement="top">
                <el-button type="primary" circle @click="editBaby(baby)">
                  <el-icon><Edit /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        
        <div class="add-baby-card" @click="gotoAddBaby">
          <el-icon><Plus /></el-icon>
          <span>添加宝宝</span>
        </div>
      </div>
    </div>

    <el-dialog
      v-model="addressFormVisible"
      :title="addressFormMode === 'add' ? '添加新地址' : '编辑地址'"
      width="500px"
      destroy-on-close
    >
      <el-form 
        :model="addressForm" 
        :rules="addressRules" 
        ref="addressFormRef" 
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="收件人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收件人姓名" />
        </el-form-item>
        
        <el-form-item label="手机号码" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号码" />
        </el-form-item>
        
        <el-form-item label="所在地区" required>
          <div class="region-select">
            <el-form-item prop="receiverProvince" class="inline-form-item">
              <el-select 
                v-model="addressForm.receiverProvince" 
                placeholder="省份"
                @change="handleProvinceChange"
                style="width: 100%"
              >
                <el-option 
                  v-for="item in provinces" 
                  :key="item" 
                  :label="item" 
                  :value="item" 
                />
              </el-select>
            </el-form-item>
            
            <el-form-item prop="receiverCity" class="inline-form-item">
              <el-select 
                v-model="addressForm.receiverCity" 
                placeholder="城市"
                @change="handleCityChange"
                style="width: 100%"
                :disabled="!addressForm.receiverProvince"
              >
                <el-option 
                  v-for="item in cities" 
                  :key="item" 
                  :label="item" 
                  :value="item" 
                />
              </el-select>
            </el-form-item>
            
            <el-form-item prop="receiverDistrict" class="inline-form-item">
              <el-select 
                v-model="addressForm.receiverDistrict" 
                placeholder="区/县"
                style="width: 100%"
                :disabled="!addressForm.receiverCity"
              >
                <el-option 
                  v-for="item in districts" 
                  :key="item" 
                  :label="item" 
                  :value="item" 
                />
              </el-select>
            </el-form-item>
          </div>
          <div class="selected-region" v-if="addressForm.receiverProvince || addressForm.receiverCity || addressForm.receiverDistrict">
            已选择: {{ formatSelectedRegion }}
          </div>
        </el-form-item>
        
        <el-form-item label="详细地址" prop="receiverAddress">
          <el-input 
            v-model="addressForm.receiverAddress" 
            type="textarea" 
            rows="3" 
            placeholder="请输入详细地址信息，如街道、门牌号等"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addressFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddressForm" :loading="addressFormSubmitting">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { 
  UserFilled, 
  Phone, 
  Location, 
  Bell, 
  Lock, 
  Setting, 
  SwitchButton,
  Calendar,
  Edit,
  Plus,
  Iphone,
  Management,
  Refresh,
  Delete
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import { 
  getCurrentUser, 
  updateUserInfo, 
  sendVerificationCode as apiSendVerificationCode, 
  uploadAvatar,
  getUserAddressList,
  getUserAddress,
  addUserAddress,
  updateUserAddress,
  deleteUserAddress,
  type AddressInfo
} from '@/api/user'

const router = useRouter()
const authStore = useAuthStore()
const activeTab = ref('info')
const showVerificationCode = ref(false)

const userInfo = reactive({
  id: 1,
  username: '',
  avatar: '',
  phone: '',
  location: '',
  gender: 0,
  babyCount: 0,
  daysCount: 0,
  checkInDays: 0
})

const userInfoForm = reactive({
  username: '',
  gender: 0,
  location: ''
})

const contactsForm = reactive({
  phone: '',
  verificationCode: ''
})

const notificationSettings = reactive({
  health: true,
  growth: true,
  system: false,
  activity: false
})

const babies = ref<any[]>([])

const addressList = ref<(AddressInfo & Record<string, any>)[]>([])
const loading = ref(false)
const addressFormVisible = ref(false)
const addressFormMode = ref<'add' | 'edit'>('add')
const addressFormSubmitting = ref(false)
const addressFormRef = ref()
const currentEditAddressId = ref<number | string | null>(null)
const LOCAL_STORAGE_ADDRESSES_KEY = 'userAddresses'

const addressForm = reactive<AddressInfo>({
  receiverName: '',
  receiverPhone: '',
  receiverProvince: '',
  receiverCity: '',
  receiverDistrict: '',
  receiverAddress: ''
})

const addressRules = {
  receiverName: [
    { required: true, message: '请输入收件人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符之间', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  receiverProvince: [
    { required: true, message: '请选择省份', trigger: 'change' }
  ],
  receiverCity: [
    { required: true, message: '请选择城市', trigger: 'change' }
  ],
  receiverDistrict: [
    { required: true, message: '请选择区/县', trigger: 'change' }
  ],
  receiverAddress: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符之间', trigger: 'blur' }
  ]
}

const provinces = ref(['北京市', '上海市', '广东省', '江苏省', '浙江省', '四川省', '山东省'])
const cities = ref<string[]>([])
const districts = ref<string[]>([])

const regionData: Record<string, Record<string, string[]>> = {
  '北京市': {
    '北京市': ['东城区', '西城区', '朝阳区', '海淀区', '丰台区']
  },
  '上海市': {
    '上海市': ['黄浦区', '徐汇区', '长宁区', '静安区', '普陀区']
  },
  '广东省': {
    '广州市': ['天河区', '海珠区', '越秀区', '白云区', '黄埔区'],
    '深圳市': ['福田区', '罗湖区', '南山区', '宝安区', '龙岗区']
  },
  '江苏省': {
    '南京市': ['玄武区', '秦淮区', '建邺区', '鼓楼区', '浦口区'],
    '苏州市': ['姑苏区', '吴中区', '相城区', '虎丘区', '吴江区']
  },
  '浙江省': {
    '杭州市': ['上城区', '下城区', '江干区', '拱墅区', '西湖区'],
    '宁波市': ['海曙区', '江北区', '北仑区', '镇海区', '鄞州区']
  },
  '四川省': {
    '成都市': ['锦江区', '青羊区', '金牛区', '武侯区', '成华区']
  },
  '山东省': {
    '济南市': ['历下区', '市中区', '槐荫区', '天桥区', '历城区'],
    '青岛市': ['市南区', '市北区', '黄岛区', '崂山区', '李沧区']
  }
}

const formatSelectedRegion = computed(() => {
  const province = addressForm.receiverProvince || '';
  const city = addressForm.receiverCity || '';
  const district = addressForm.receiverDistrict || '';
  return `${province} ${city} ${district}`.trim();
})

const handleProvinceChange = () => {
  addressForm.receiverCity = '';
  addressForm.receiverDistrict = '';
  if (addressForm.receiverProvince) {
    cities.value = Object.keys(regionData[addressForm.receiverProvince] || {});
  } else {
    cities.value = [];
  }
}

const handleCityChange = () => {
  addressForm.receiverDistrict = '';
  if (addressForm.receiverProvince && addressForm.receiverCity) {
    districts.value = regionData[addressForm.receiverProvince][addressForm.receiverCity] || [];
  } else {
    districts.value = [];
  }
}

const loadAddressList = async () => {
  loading.value = true
  try {
    const response = await getUserAddressList()
    if (response && response.code === 1 && response.data) {
      addressList.value = response.data.records || [];
      
      if (addressList.value.length > 0) {
        saveAddressesToLocalStorage(addressList.value);
      }
    } else {
      const localAddresses = loadAddressesFromLocalStorage();
      if (localAddresses.length > 0) {
        addressList.value = localAddresses;
        ElMessage.warning('加载最新地址列表失败，显示本地缓存的地址数据');
      } else {
        addressList.value = [];
      }
    }
  } catch (error) {
    const localAddresses = loadAddressesFromLocalStorage();
    if (localAddresses.length > 0) {
      addressList.value = localAddresses;
      ElMessage.warning('网络请求失败，显示本地缓存的地址数据');
    } else {
      addressList.value = [];
    }
  } finally {
    loading.value = false;
  }
};

const loadAddressesFromLocalStorage = (): (AddressInfo & Record<string, any>)[] => {
  try {
    const storedAddresses = localStorage.getItem(LOCAL_STORAGE_ADDRESSES_KEY)
    if (storedAddresses) {
      return JSON.parse(storedAddresses)
    }
  } catch (error) {
    console.error('从本地存储加载地址列表失败:', error)
  }
  return []
}

const showAddressForm = (action: 'add' | 'edit', address?: AddressInfo) => {
  addressFormMode.value = action
  
  if (addressFormRef.value) {
    addressFormRef.value.resetFields()
  }
  
  Object.assign(addressForm, {
    receiverName: '',
    receiverPhone: '',
    receiverProvince: '',
    receiverCity: '',
    receiverDistrict: '',
    receiverAddress: ''
  })
  
  if (action === 'edit' && address) {
    Object.assign(addressForm, {
      receiverName: address.receiverName,
      receiverPhone: address.receiverPhone,
      receiverProvince: address.receiverProvince,
      receiverCity: address.receiverCity,
      receiverDistrict: address.receiverDistrict,
      receiverAddress: address.receiverAddress
    })
    
    if (address.receiverProvince) {
      cities.value = Object.keys(regionData[address.receiverProvince] || {});
    }
    
    if (address.receiverProvince && address.receiverCity) {
      districts.value = regionData[address.receiverProvince][address.receiverCity] || [];
    }
    
    currentEditAddressId.value = address.id || null
  } else {
    currentEditAddressId.value = null
  }
  
  addressFormVisible.value = true
}

const submitAddressForm = async () => {
  if (!addressFormRef.value) return
  
  await addressFormRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      ElMessage.warning('请完善表单信息')
      return
    }
    
    addressFormSubmitting.value = true
    
    try {
      let response
      
      if (addressFormMode.value === 'add') {
        response = await addUserAddress(addressForm)
        
        if (response && response.code === 1 && response.data) {
          let newAddressId: number | string | null = null
          
          if (response.data.id) {
            newAddressId = response.data.id 
            currentEditAddressId.value = response.data.id
            
            updateLocalAddress({
              ...addressForm,
              id: response.data.id
            })
          } 
          else if (typeof response.data === 'number') {
            newAddressId = response.data
            currentEditAddressId.value = response.data
            
            updateLocalAddress({
              ...addressForm,
              id: response.data
            })
          }
        }
      } else {
        if (!currentEditAddressId.value) {
          ElMessage.error('地址ID无效')
          return
        }
        
        const updatedAddress: AddressInfo & Record<string, any> = {
          id: currentEditAddressId.value, // 获取 id
          receiverName: addressForm.receiverName,
          receiverPhone: addressForm.receiverPhone,
          receiverProvince: addressForm.receiverProvince,
          receiverCity: addressForm.receiverCity,
          receiverDistrict: addressForm.receiverDistrict,
          receiverAddress: addressForm.receiverAddress
        }
        
        if (currentEditAddressId.value !== undefined) {
          const numericId = typeof currentEditAddressId.value === 'string' 
            ? parseInt(currentEditAddressId.value, 10) 
            : currentEditAddressId.value
            
          if (isNaN(numericId)) {
            throw new Error('无效的地址ID')
          }
          
          response = await updateUserAddress(numericId, {
            receiverName: addressForm.receiverName,
            receiverPhone: addressForm.receiverPhone,
            receiverProvince: addressForm.receiverProvince,
            receiverCity: addressForm.receiverCity,
            receiverDistrict: addressForm.receiverDistrict,
            receiverAddress: addressForm.receiverAddress
          })
          
          if (response && response.code === 1) {
            updateLocalAddress(updatedAddress)
          }
        } else {
          throw new Error('地址ID不能为空')
        }
      }
      if (response && response.code === 1) {
        ElMessage.success(addressFormMode.value === 'add' ? '地址添加成功' : '地址更新成功')
        addressFormVisible.value = false
        loadAddressList()
      } else {
        ElMessage.error(response?.msg || (addressFormMode.value === 'add' ? '添加地址失败' : '更新地址失败'))
      }
    } catch (error) {
      ElMessage.error(addressFormMode.value === 'add' ? '添加地址失败，请稍后重试' : '更新地址失败，请稍后重试')
    } finally {
      addressFormSubmitting.value = false
    }
  })
}

const confirmDeleteAddress = (address: AddressInfo & Record<string, any>) => {
  if (!address.id) {
    ElMessage.error('地址ID无效')
    return
  }
  
  ElMessageBox.confirm(
    '确定要删除这个地址吗？',
    '删除地址',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      if (address.id !== undefined) {
        const numericId = typeof address.id === 'string' 
          ? parseInt(address.id, 10) 
          : address.id as number
        if (isNaN(numericId)) {
          throw new Error('无效的地址ID')
        }
        const response = await deleteUserAddress(numericId)
        if (response && response.code === 1) {
          deleteLocalAddress(address.id)
          ElMessage.success('地址已删除')
          loadAddressList()
        } else {
          ElMessage.error(response?.msg || '删除地址失败')
        }
      } else {
        throw new Error('地址ID不能为空')
      }
    } catch (error) {
      if (address.id && deleteLocalAddress(address.id)) {
        ElMessage.warning('服务器删除失败，但已从本地缓存中移除该地址')
        addressList.value = addressList.value.filter(addr => addr.id !== address.id)
      } else {
        ElMessage.error('删除地址失败，请稍后重试')
      }
    }
  }).catch(() => {
    // 取消删除啥也不做
  })
}

const initUserData = async () => {
  const token = localStorage.getItem('token');
  if (!token) {
    ElMessage.error('您未登录或登录已过期，请重新登录');
    router.push('/login');
    return;
  }
  
  if (authStore.user) {
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '加载用户数据...',
      background: 'rgba(255, 255, 255, 0.7)',
    })
    
    try {
      const response = await getCurrentUser()
      if (response && response.code === 1 && response.data) {
        const userData = response.data
        
        authStore.setUserData(userData)
        
        userInfo.id = userData.id || 1
        userInfo.username = userData.name || '数字儿童用户'
        userInfo.avatar = userData.avatar || ''
        userInfo.phone = userData.phone || ''
        userInfo.location = userData.location || ''
        userInfo.gender = userData.gender || 0
        
        userInfoForm.username = userInfo.username
        userInfoForm.gender = userInfo.gender
        userInfoForm.location = userInfo.location
        
        contactsForm.phone = userInfo.phone
      } else {
        userInfo.id = authStore.user.id || 1
        userInfo.username = authStore.user.name || '数字儿童用户'
        userInfo.avatar = authStore.user.avatar || ''
        userInfo.phone = authStore.user.phone || ''
        userInfo.location = authStore.user.location || ''
        userInfo.gender = authStore.user.gender || 0
        
        userInfoForm.username = userInfo.username
        userInfoForm.gender = userInfo.gender
        userInfo.location = userInfo.location
        
        contactsForm.phone = userInfo.phone
      }
    } catch (error: any) {
      if (error.message && error.message.includes('认证失败')) {
        ElMessage.error('登录已过期，请重新登录');
        localStorage.removeItem('token');
        router.push('/login');
        return;
      }
      
      ElMessage.error('获取用户数据失败，请刷新页面重试')
      
      userInfo.id = authStore.user.id || 1
      userInfo.username = authStore.user.name || '数字儿童用户'
      userInfo.avatar = authStore.user.avatar || ''
      userInfo.phone = authStore.user.phone || ''
      userInfo.location = authStore.user.location || ''
      userInfo.gender = authStore.user.gender || 0
      
      userInfoForm.username = userInfo.username
      userInfoForm.gender = userInfo.gender
      userInfoForm.location = userInfo.location
      
      contactsForm.phone = userInfo.phone
    } finally {
      loadingInstance.close()
    }
  }
  
  fetchBabies()
}

const fetchBabies = async () => {
  try {
    const storedBabies = localStorage.getItem('userBabies')
    if (storedBabies) {
      babies.value = JSON.parse(storedBabies)
      userInfo.babyCount = babies.value.length
    } else {
      babies.value = []
      userInfo.babyCount = 0
    }
    
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      const storedUserInfo = JSON.parse(userInfoStr)
      if (storedUserInfo.babyCount !== undefined) {
        userInfo.babyCount = storedUserInfo.babyCount
      }
    }
  } catch (error) {
    babies.value = []
    userInfo.babyCount = 0
  }
}

const listenForBabyCountChanges = () => {
  window.addEventListener('storage', (event) => {
    if (event.key === 'userInfo' || event.key === 'userBabies') {
      console.log('检测到localStorage变化:', event.key)
      fetchBabies() 
    }
  })
}

const startBabyCountPolling = () => {
  const intervalId = setInterval(() => {
    fetchBabies()
  }, 30000)
  onUnmounted(() => {
    clearInterval(intervalId)
  })
}
const formatPhone = (phone: string) => {
  if (!phone) return ''
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const formatBabyAge = (birthday: string) => {
  if (!birthday) return ''
  const birthDate = new Date(birthday)
  const now = new Date()
  let years = now.getFullYear() - birthDate.getFullYear()
  let months = now.getMonth() - birthDate.getMonth()
  let days = now.getDate() - birthDate.getDate()
  if (days < 0) {
    months -= 1
    const lastMonthDate = new Date(now.getFullYear(), now.getMonth(), 0)
    days += lastMonthDate.getDate()
  }
  if (months < 0) {
    years -= 1
    months += 12
  }
  let result = ''
  if (years > 0) {
    result += `${years} 年 `
  }
  if (months > 0 || years > 0) {
    result += `${months} 月 `
  }
  result += `${days} 天`
  return result
}

const handleAvatarChange = async (file: any) => {
  const isImage = file.raw.type.startsWith('image/');
  const isLt2M = file.raw.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return;
  }
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '头像上传中...',
    background: 'rgba(255, 255, 255, 0.7)',
  });

  try {
    const reader = new FileReader();
    reader.onload = (e) => {
      userInfo.avatar = e.target?.result as string;
    };
    reader.readAsDataURL(file.raw);
    const response = await uploadAvatar(file.raw);
    let avatarUrl = '';
    try {
      if (typeof response === 'string') {
        const strResponse = response as string;
        if (strResponse.includes('http')) {
          avatarUrl = strResponse;
        }
      } 
      else if (response && typeof response === 'object') {
        const objResponse = response as any;
        if (objResponse.data && typeof objResponse.data === 'string' && objResponse.data.includes('http')) {
          avatarUrl = objResponse.data;
        }
      }
    } catch (e) {
    }
    
    if (avatarUrl) {
      userInfo.avatar = avatarUrl;
      
      if (authStore.user) {
        authStore.setUserData({
          ...authStore.user,
          avatar: avatarUrl
        });
      }
      
      try {
        const updateResponse = await updateUserInfo({
          avatar: avatarUrl
        });
      } catch (updateError) {}
      ElMessage.success('头像上传成功');
    } else {
      ElMessage.error('头像上传失败：无效的响应格式');
    }
  } catch (error: any) {
    if (error.message && error.message.includes('认证失败')) {
      ElMessage.error('登录已过期，请重新登录');
      localStorage.removeItem('token');
      router.push('/login');
      return;
    }
    ElMessage.error('头像上传失败，请稍后重试');
  } finally {
    loadingInstance.close();
  }
}

// 发送验证码
const sendVerificationCode = async () => {
  if (!contactsForm.phone) {
    ElMessage.warning('请输入手机号码')
    return
  }
  
  try {
    const response = await apiSendVerificationCode(contactsForm.phone)
    if (response && response.code === 1) {
      ElMessage.success('验证码已发送，请注意查收')
      showVerificationCode.value = true
    } else {
      ElMessage.error(response?.msg || '验证码发送失败，请稍后重试')
    }
  } catch (error) {
    console.error('发送验证码失败:', error)
    ElMessage.error('验证码发送失败，请稍后重试')
  }
}

const saveUserInfo = async () => {
  const token = localStorage.getItem('token');
  if (!token) {
    ElMessage.error('您未登录或登录已过期，请重新登录');
    router.push('/login');
    return;
  }
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '保存中...',
    background: 'rgba(255, 255, 255, 0.7)',
  })
  
  try {
    const userData = {
      name: userInfoForm.username,
      gender: userInfoForm.gender,
      location: userInfoForm.location
    }
    
    const response = await updateUserInfo(userData)
    if (JSON.stringify(response) == 'true') {
      Object.assign(userInfo, {
        username: userInfoForm.username,
        gender: userInfoForm.gender,
        location: userInfoForm.location
      })

      if (authStore.user) {
        authStore.setUserData({
          ...authStore.user,
          name: userInfoForm.username,
          gender: userInfoForm.gender,
          location: userInfoForm.location
        })
      }
      ElMessage.success('基本资料已保存')
    } else {
      ElMessage.error(response?.msg || '保存失败，请稍后重试')
    }
  } catch (error: any) {
    if (error.message && error.message.includes('认证失败')) {
      ElMessage.error('登录已过期，请重新登录');
      localStorage.removeItem('token');
      router.push('/login');
      return;
    }
    
    ElMessage.error('保存失败，请刷新页面重试')
  } finally {
    loadingInstance.close()
  }
}

const saveContacts = async () => {
  const token = localStorage.getItem('token');
  if (!token) {
    ElMessage.error('您未登录或登录已过期，请重新登录');
    router.push('/login');
    return;
  }
  
  if (showVerificationCode.value && !contactsForm.verificationCode) {
    ElMessage.warning('请输入验证码')
    return
  }
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '保存中...',
    background: 'rgba(255, 255, 255, 0.7)',
  })
  
  try {
    const userData = {
      phone: contactsForm.phone,
      code: showVerificationCode.value ? contactsForm.verificationCode : undefined
    }
    
    const response = await updateUserInfo(userData)
    
    if (JSON.stringify(response) == 'true') {
      userInfo.phone = contactsForm.phone
      
      if (authStore.user) {
        authStore.setUserData({
          ...authStore.user,
          phone: contactsForm.phone
        })
      }
      
      ElMessage.success('联系方式已更新')
      showVerificationCode.value = false
      contactsForm.verificationCode = ''
    } else {
      ElMessage.error(response?.msg || '保存失败，请稍后重试')
    }
  } catch (error: any) {
    if (error.message && error.message.includes('认证失败')) {
      ElMessage.error('登录已过期，请重新登录');
      localStorage.removeItem('token');
      router.push('/login');
      return;
    }
    
    ElMessage.error('保存失败，请稍后重试')
  } finally {
    loadingInstance.close()
  }
}

const saveNotifications = () => {
  ElMessage.success('通知设置已保存')
}

const goToBabyManagement = () => {
  router.push('/user/baby-management')
}

const gotoAddBaby = () => {
  router.push('/user/baby-management?action=add')
}

const editBaby = (baby: any) => {
  router.push(`/user/baby-management?action=edit&id=${baby.id}`)
}

const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '退出提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    authStore.logout()
    ElMessage.success('已成功退出登录')
    router.push('/login')
  }).catch(() => {})
}

const saveAddressesToLocalStorage = (addresses: (AddressInfo & Record<string, any>)[]) => {
  try {
    localStorage.setItem(LOCAL_STORAGE_ADDRESSES_KEY, JSON.stringify(addresses))
  } catch (error) {
    console.error('保存地址列表到本地存储失败:', error)
  }
}

const getAddressById = (id: number | string): (AddressInfo & Record<string, any>) | null => {
  const addresses = loadAddressesFromLocalStorage()
  const address = addresses.find(addr => addr.id === id)
  return address || null
}

const updateLocalAddress = (updatedAddress: AddressInfo & Record<string, any>) => {
  if (!updatedAddress.id) return
  
  const addresses = loadAddressesFromLocalStorage()
  const index = addresses.findIndex(addr => addr.id === updatedAddress.id)
  if (index !== -1) {
    addresses[index] = updatedAddress
    saveAddressesToLocalStorage(addresses)
  } else {
    addresses.push(updatedAddress)
    saveAddressesToLocalStorage(addresses)
  }
}
const deleteLocalAddress = (id: number | string) => {
  const addresses = loadAddressesFromLocalStorage()
  const filteredAddresses = addresses.filter(addr => addr.id !== id)
  if (filteredAddresses.length !== addresses.length) {
    saveAddressesToLocalStorage(filteredAddresses)
    return true
  }
  return false
}
onMounted(() => {
  initUserData()
  listenForBabyCountChanges()
  startBabyCountPolling()
  loadAddressList()
})
</script>

<style scoped>
.profile-container {
  @apply max-w-7xl mx-auto px-4 py-6 space-y-6;
}

.profile-header {
  @apply w-full rounded-xl overflow-hidden shadow-md bg-white;
}

.user-info-card {
  @apply flex flex-col sm:flex-row items-start sm:items-center p-6 relative;
}

.user-avatar-wrapper {
  @apply flex flex-col items-center mr-0 sm:mr-8 mb-4 sm:mb-0;
}

.user-avatar {
  @apply w-24 h-24 rounded-full bg-gradient-to-r from-blue-400 to-indigo-500 flex items-center justify-center overflow-hidden shadow-lg mb-2;
}

.user-avatar img {
  @apply w-full h-full object-cover;
}

.default-avatar {
  @apply text-white text-4xl;
}

.avatar-upload .upload-btn {
  @apply text-sm text-blue-600 hover:text-blue-700 cursor-pointer bg-transparent border-none;
}

.user-info {
  @apply flex-1;
}

.username {
  @apply text-2xl font-bold text-gray-800 mb-3;
}

.user-meta {
  @apply flex flex-wrap mb-4;
}

.meta-item {
  @apply flex items-center text-sm text-gray-600 mr-6 mb-2;
}

.meta-item .el-icon {
  @apply mr-1.5;
}

.user-stats {
  @apply flex border-t border-gray-100 pt-4;
}

.stat-item {
  @apply mr-8 text-center;
}

.stat-value {
  @apply text-xl font-semibold text-blue-600 flex items-center justify-center;
}

.refresh-icon {
  @apply ml-1 text-sm cursor-pointer text-blue-400 hover:text-blue-600 transition-colors;
}

.stat-label {
  @apply text-xs text-gray-500;
}

.profile-content {
  @apply flex flex-col md:flex-row gap-6;
}

.settings-menu {
  @apply w-full md:w-64 bg-white rounded-xl shadow-md p-4 flex flex-col;
}

.menu-section {
  @apply mb-6;
}

.menu-title {
  @apply text-xs text-gray-500 font-medium uppercase tracking-wider mb-2 px-3;
}

.menu-item {
  @apply flex items-center p-3 rounded-lg text-gray-700 hover:bg-blue-50 hover:text-blue-600 cursor-pointer transition-colors mb-1;
}

.menu-item .el-icon {
  @apply mr-3 text-lg;
}

.menu-item.active {
  @apply bg-blue-50 text-blue-600 font-medium;
}

.logout-button {
  @apply mt-auto p-3 flex items-center justify-center bg-gray-100 hover:bg-red-50 text-gray-700 hover:text-red-600 rounded-lg transition-colors;
}

.logout-button .el-icon {
  @apply mr-2;
}

.settings-content {
  @apply flex-1 bg-white rounded-xl shadow-md;
}

.settings-panel {
  @apply p-6;
}

.panel-title {
  @apply text-xl font-bold text-gray-800 mb-6 pb-3 border-b border-gray-100;
}

.my-babies-section {
  @apply bg-white rounded-xl shadow-md p-6;
}

.section-header {
  @apply flex justify-between items-center mb-6 pb-3 border-b border-gray-100;
}

.section-title {
  @apply text-xl font-bold text-gray-800;
}

.babies-list {
  @apply grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4;
}

.baby-card {
  @apply bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-shadow;
}

.baby-card-inner {
  @apply p-4 flex items-center;
}

.baby-avatar {
  @apply w-16 h-16 rounded-full overflow-hidden mr-4 flex-shrink-0 border-2 border-white shadow-sm;
}

.baby-avatar img {
  @apply w-full h-full object-cover;
}

.default-baby-avatar {
  @apply w-full h-full bg-gradient-to-r from-blue-400 to-indigo-500 flex items-center justify-center text-white text-2xl font-bold;
}

.baby-info {
  @apply flex-1;
}

.baby-name {
  @apply font-bold text-lg text-gray-800 mb-1;
}

.baby-age {
  @apply text-sm text-blue-600 font-medium mb-1;
}

.baby-birthday {
  @apply text-xs text-gray-500 flex items-center;
}

.baby-birthday .el-icon {
  @apply mr-1;
}

.baby-actions {
  @apply flex-shrink-0;
}

.add-baby-card {
  @apply flex flex-col items-center justify-center p-6 h-full bg-gray-50 hover:bg-gray-100 rounded-xl border-2 border-dashed border-gray-200 cursor-pointer transition-colors text-gray-500 hover:text-blue-600;
  min-height: 120px;
}

.add-baby-card .el-icon {
  @apply text-3xl mb-2;
}

.no-babies {
  @apply py-12;
}

.notification-settings {
  @apply space-y-4;
}

.notification-item {
  @apply flex justify-between items-center p-4 border border-gray-100 rounded-lg;
}

.notification-title {
  @apply font-medium text-gray-800 mb-1;
}

.notification-desc {
  @apply text-xs text-gray-500;
}

.notification-actions {
  @apply mt-6;
}

.address-top-bar {
  @apply flex justify-between items-center mb-4;
}

.address-count {
  @apply text-sm text-gray-500;
}

.address-loading {
  @apply py-4;
}

.no-address {
  @apply py-12;
}

.address-list {
  @apply space-y-4;
}

.address-card {
  @apply bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-shadow;
}

.address-card-inner {
  @apply p-4 flex flex-col sm:flex-row;
}

.address-content {
  @apply flex-1 mb-3 sm:mb-0;
}

.address-main {
  @apply flex items-center mb-1;
}

.address-name {
  @apply font-bold text-gray-800 mr-2;
}

.address-phone {
  @apply text-sm text-blue-600;
}

.address-detail {
  @apply text-sm text-gray-600;
}

.address-actions {
  @apply flex space-x-2 sm:justify-end;
}

.region-select {
  @apply flex justify-between gap-4;
}

.inline-form-item {
  @apply mb-0 flex-1;
}

.inline-form-item :deep(.el-form-item__error) {
  @apply mt-1;
}

.inline-form-item :deep(.el-form-item__label) {
  @apply hidden;
}

.selected-region {
  @apply mt-2 text-sm text-blue-600 bg-blue-50 px-3 py-1.5 rounded-md;
}

@media (max-width: 768px) {
  .settings-menu {
    @apply mb-4;
  }
  
  .user-info-card {
    @apply flex-col items-center text-center;
  }
  
  .user-meta {
    @apply justify-center;
  }
  
  .address-card-inner {
    @apply flex-col;
  }
  
  .address-actions {
    @apply mt-3;
  }
}
</style> 