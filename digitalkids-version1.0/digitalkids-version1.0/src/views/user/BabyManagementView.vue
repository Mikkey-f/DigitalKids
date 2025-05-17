<template>
  <div class="baby-management-container">
    <div class="page-header">
      <h1 class="page-title">我的宝宝</h1>
      <p class="page-description">在这里管理您宝宝的信息，记录成长点滴</p>
    </div>
    
    <div v-if="!showEditForm" class="babies-section">
      <div class="babies-header">
        <div class="section-title">宝宝列表</div>
        <el-button type="primary" @click="addNewBaby">
          <el-icon class="mr-1"><Plus /></el-icon>
          添加宝宝
        </el-button>
      </div>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="babies.length === 0" class="no-babies">
        <el-empty description="暂无宝宝信息">
          <el-button type="primary" @click="addNewBaby">添加宝宝</el-button>
        </el-empty>
      </div>
      
      <div v-else class="babies-grid">
        <div v-for="baby in babies" :key="baby.id" class="baby-card">
          <div class="baby-card-header">
            <div class="baby-avatar">
              <img v-if="baby.avatar" :src="baby.avatar" :alt="baby.nickname" />
              <div v-else class="default-baby-avatar">
                <span>{{ baby.nickname.substring(0, 1) }}</span>
              </div>
            </div>
            <div class="baby-header-info">
              <div class="baby-name">{{ baby.nickname }}</div>
              <div class="baby-gender">
                <el-icon :class="baby.gender === 1 ? 'text-blue-500' : 'text-pink-500'">
                  <Male v-if="baby.gender === 1" />
                  <Female v-else />
                </el-icon>
                <span>{{ baby.gender === 1 ? '男孩' : '女孩' }}</span>
              </div>
            </div>
          </div>
          
          <div class="baby-card-body">
            <div class="baby-info-item">
              <div class="info-label">年龄</div>
              <div class="info-value highlight">{{ formatBabyAge(baby.birthday) }}</div>
            </div>
            <div class="baby-info-item">
              <div class="info-label">生日</div>
              <div class="info-value">{{ formatDate(baby.birthday) }}</div>
            </div>
            <div class="baby-info-item">
              <div class="info-label">身高</div>
              <div class="info-value">{{ baby.height }} cm</div>
            </div>
            <div class="baby-info-item">
              <div class="info-label">体重</div>
              <div class="info-value">{{ baby.weight }} kg</div>
            </div>
          </div>
          
          <div class="baby-card-footer">
            <el-button type="primary" plain @click="editBaby(baby)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" plain @click="confirmDeleteBaby(baby)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="showEditForm" class="baby-edit-section">
      <div class="edit-header">
        <h2 class="edit-title">{{ isEditMode ? '编辑宝宝信息' : '添加宝宝' }}</h2>
        <el-button plain @click="cancelEdit">
          <el-icon><Back /></el-icon>
          返回列表
        </el-button>
      </div>
      
      <div class="edit-form-container">
        <el-form 
          :model="babyForm" 
          :rules="rules" 
          ref="babyFormRef" 
          label-position="top"
          class="baby-form"
        >
          <div class="form-avatar-section">
            <div class="avatar-upload">
              <div class="avatar-preview">
                <img 
                  v-if="babyAvatarPreview" 
                  :src="babyAvatarPreview" 
                  alt="宝宝头像" 
                  class="avatar-image"
                />
                <div v-else class="default-avatar">
                  <el-icon v-if="babyForm.nickname"><Avatar /></el-icon>
                  <span v-else-if="babyForm.nickname">{{ babyForm.nickname.substring(0, 1) }}</span>
                </div>
              </div>
              <el-upload
                class="avatar-uploader"
                action="#"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleAvatarChange"
              >
                <el-button type="primary" class="upload-btn">
                  <el-icon><Upload /></el-icon>
                  上传头像
                </el-button>
              </el-upload>
            </div>
          </div>
          
          <div class="form-content">
            <el-form-item label="宝宝昵称" prop="nickname">
              <el-input v-model="babyForm.nickname" placeholder="请输入宝宝昵称" />
            </el-form-item>
            
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="babyForm.gender">
                <el-radio :label="1">男孩</el-radio>
                <el-radio :label="0">女孩</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                v-model="babyForm.birthday"
                type="date"
                placeholder="选择出生日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                :disabled-date="disableFutureDates"
                style="width: 100%"
              />
            </el-form-item>
            
            <div class="form-row">
              <el-form-item label="身高 (cm)" prop="height">
                <el-input-number 
                  v-model="babyForm.height" 
                  :min="20" 
                  :max="200" 
                  :step="0.1" 
                  :precision="1"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              
              <el-form-item label="体重 (kg)" prop="weight">
                <el-input-number 
                  v-model="babyForm.weight" 
                  :min="1" 
                  :max="100" 
                  :step="0.1" 
                  :precision="1"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
            </div>
            
            <div class="form-actions">
              <el-button @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="saveBaby" :loading="submitting">保存</el-button>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Plus,
  Edit,
  Delete,
  Male,
  Female,
  Avatar,
  Upload,
  Back
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox} from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { addKid, updateKid, deleteKid, getKidById, uploadKidAvatar } from '@/api/kids'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const showEditForm = ref(false)
const isEditMode = ref(false)
const babyFormRef = ref<FormInstance>()
const babyAvatarPreview = ref('')
const loading = ref(false)
const submitting = ref(false)
const avatarFile = ref<File | null>(null)
const babies = ref<any[]>([])

const defaultBabyForm = {
  id: undefined as number | undefined,
  avatar: '',
  nickname: '',
  birthday: '',
  gender: 1,
  old: 0,
  height: 70,
  weight: 8
}

const babyForm = reactive({ ...defaultBabyForm })

const rules = reactive<FormRules>({
  nickname: [
    { required: true, message: '请输入宝宝昵称', trigger: 'blur' },
    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  birthday: [
    { required: false, message: '请选择出生日期', trigger: 'change' }
  ],
  height: [],
  weight: []
})

const disableFutureDates = (time: Date) => {
  return time.getTime() > Date.now()
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

const calculateAge = (birthday: string | undefined): number => {
  if (!birthday) return 0
  const birthDate = new Date(birthday)
  const today = new Date()
  const ageInMilliseconds = today.getTime() - birthDate.getTime()
  const ageInYears = ageInMilliseconds / (1000 * 60 * 60 * 24 * 365.25)
  return Math.round(ageInYears)
}

const fetchBabies = async () => {
  loading.value = true
  try {
    const storedBabies = localStorage.getItem('userBabies')
    if (storedBabies) {
      const parsedBabies = JSON.parse(storedBabies);
      babies.value = parsedBabies.filter((baby: any) => {
        const isValid = baby && baby.nickname && baby.nickname.trim() !== '';
        return isValid;
      });
      if (babies.value.length !== parsedBabies.length) {
        localStorage.setItem('userBabies', JSON.stringify(babies.value));
      }
      updateBabyCount(babies.value.length);
      
      // 检查每个宝宝是否有id
      babies.value.forEach((baby: any, index: number) => {
        console.log(`宝宝[${index}]的ID:`, baby.id);
        if (!baby.id) {
          console.warn(`宝宝[${index}] ${baby.nickname} 缺少ID!`);
        }
      });
    } else {
      babies.value = []
      updateBabyCount(0);
    }
  } catch (error) {
    ElMessage.error('获取宝宝列表失败，请稍后重试')
    babies.value = []
  } finally {
    loading.value = false
  }
}

// 头像上传处理
const handleAvatarChange = async (file: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    babyAvatarPreview.value = e.target?.result as string
  }
  reader.readAsDataURL(file.raw)
  avatarFile.value = file.raw
  try {
    ElMessage.info('正在上传头像...')
    const uploadedUrl = await uploadBabyAvatar(file.raw)
    if (uploadedUrl) {
      babyForm.avatar = uploadedUrl
      babyAvatarPreview.value = uploadedUrl
      ElMessage.success('头像上传成功')
    }
  } catch (error) {
    console.error('上传头像失败:', error)
    ElMessage.warning('头像上传失败，请稍后重试')
  }
}

// 上传宝宝头像
const uploadBabyAvatar = async (file: File): Promise<string | undefined> => {
  let avatarUrl = '';
  try {
    const response = await uploadKidAvatar(file)
    if (typeof response === 'string') {
      const strResponse = response as string;
      if (strResponse.includes('http')) {
        avatarUrl = strResponse;
        babyForm.avatar = avatarUrl;
        return avatarUrl;
      }
    } 
    else if (response && typeof response === 'object') {
      const objResponse = response as any;
      if (objResponse.data && typeof objResponse.data === 'string' && objResponse.data.includes('http')) {
        avatarUrl = objResponse.data;
        babyForm.avatar = avatarUrl;
        return avatarUrl;
      }
    }
  } catch (e) {
    console.error('处理头像URL时出错:', e);
  }
  return undefined;
}

const addNewBaby = () => {
  Object.assign(babyForm, defaultBabyForm)
  babyAvatarPreview.value = ''
  avatarFile.value = null
  isEditMode.value = false
  showEditForm.value = true
}

const editBaby = (baby: any) => {
  Object.assign(babyForm, baby)
  babyAvatarPreview.value = baby.avatar
  isEditMode.value = true
  showEditForm.value = true
}

const cancelEdit = () => {
  showEditForm.value = false
}

const saveBaby = async () => {
  if (!babyFormRef.value) return
  try {
    const valid = await babyFormRef.value.validate()
    if (valid) {
      submitting.value = true
      let avatarUrl = babyForm.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      if (avatarFile.value) {
        try {
          let retryCount = 0;
          const maxRetries = 3;
          let uploadSuccess = false;
          while (retryCount < maxRetries && !uploadSuccess) {
            try {
              const uploadedUrl = await uploadBabyAvatar(avatarFile.value);
              if (uploadedUrl) {
                avatarUrl = uploadedUrl;
                babyForm.avatar = uploadedUrl;
                uploadSuccess = true;
                await new Promise(resolve => setTimeout(resolve, 1000));
              }
            } catch (uploadError) {
              retryCount++;
              if (retryCount >= maxRetries) {
                ElMessage.warning('头像上传失败多次，将使用默认头像');
                avatarUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
                babyForm.avatar = avatarUrl;
              } else {
                await new Promise(resolve => setTimeout(resolve, 1000));
              }
            }
          }
        } catch (error) {
          ElMessage.warning('头像上传失败，将使用默认头像');
          avatarUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
          babyForm.avatar = avatarUrl;
        }
      }
      if (!avatarUrl) {
        avatarUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
        babyForm.avatar = avatarUrl;
      }
      await new Promise(resolve => setTimeout(resolve, 1000));
      if (isEditMode.value) {
        const updateData = {
          id: babyForm.id!,
          nickname: babyForm.nickname,
          avatar: avatarUrl,
          old: calculateAge(babyForm.birthday),
          gender: babyForm.gender
        }
        try {
          const response = await updateKid(updateData)
          if (response && response.code === 1 && response.data) {
            ElMessage.success('宝宝信息已更新')
            const index = babies.value.findIndex(b => b.id === babyForm.id)
            if (index !== -1) {
              babies.value[index] = { ...babyForm }
              localStorage.setItem('userBabies', JSON.stringify(babies.value))
            }
            showEditForm.value = false
            fetchBabies()
          } else {
            ElMessage.error(response?.msg || '更新失败，请稍后重试')
          }
        } catch (error) {
          ElMessage.error('更新失败，请稍后重试')
        }
      } else {
        const addData = {
          nickname: babyForm.nickname,
          avatar: avatarUrl,
          old: calculateAge(babyForm.birthday),
          gender: babyForm.gender
        }
        try {
          let retryCount = 0;
          const maxRetries = 2;
          let addSuccess = false;
          while (retryCount < maxRetries && !addSuccess) {
            try {
              const token = localStorage.getItem('token');
              if (!token) {
                throw new Error('未找到认证令牌，请重新登录');
              }
              const response = await addKid(addData);
              if (response && response.code === 1) {
                let newBabyData = response.data;
                if (typeof newBabyData === 'number') {
                  const kidId = newBabyData;
                  newBabyData = {
                    id: kidId,
                    userId: 0,
                    nickname: babyForm.nickname,
                    avatar: avatarUrl,
                    gender: babyForm.gender,
                    old: calculateAge(babyForm.birthday),
                    createTime: new Date().toISOString(),
                    updateTime: new Date().toISOString()
                  };
                }
                ElMessage.success('宝宝添加成功');
                const newBabyWithUI = {
                  id: newBabyData.id,
                  nickname: newBabyData.nickname || babyForm.nickname,
                  avatar: newBabyData.avatar || babyForm.avatar,
                  birthday: babyForm.birthday,
                  gender: newBabyData.gender || babyForm.gender,
                  height: babyForm.height,
                  weight: babyForm.weight
                };
                babies.value.push(newBabyWithUI);
                localStorage.setItem('userBabies', JSON.stringify(babies.value));
                showEditForm.value = false;
                fetchBabies();
                addSuccess = true;
                updateBabyCount(babies.value.length + 1);
              } else {
                retryCount++;
                if (retryCount >= maxRetries) {
                  ElMessage.error(response?.msg || '添加失败，请稍后重试');
                } else {
                  await new Promise(resolve => setTimeout(resolve, 1000));
                }
              }
            } catch (addError) {
              retryCount++;
              if (retryCount >= maxRetries) {
                ElMessage.error('添加失败，请稍后重试');
              } else {
                await new Promise(resolve => setTimeout(resolve, 1000));
              }
            }
          }
        } catch (error) {
          ElMessage.error('添加失败，请稍后重试')
        }
      }
    } else {
      ElMessage.warning('请完成必填项')
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const confirmDeleteBaby = (baby: any) => {
  ElMessageBox.confirm(
    `确定要删除 ${baby.nickname} 的信息吗？此操作不可恢复。`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const response = await deleteKid(baby.id)
      if (response && response.code === 1 && response.data) {
        const index = babies.value.findIndex(b => b.id === baby.id)
        if (index !== -1) {
          babies.value.splice(index, 1)
          localStorage.setItem('userBabies', JSON.stringify(babies.value))
          updateBabyCount(babies.value.length);
        }
        ElMessage.success('宝宝信息已删除')
      } else {
        ElMessage.error(response?.msg || '删除失败，请稍后重试')
      }
    } catch (error) {
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {})
}

const updateBabyCount = (count: number) => {
  try {
    const userInfoStr = localStorage.getItem('userInfo');
    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr);
      userInfo.babyCount = count;
      localStorage.setItem('userInfo', JSON.stringify(userInfo));
    }
  } catch (error) {
  }
}

const checkAndFixBabyData = () => {
  const storedBabies = localStorage.getItem('userBabies');
  if (!storedBabies) {
    return;
  }
  try {
    const babiesArray = JSON.parse(storedBabies);
    let needsUpdate = false;
    babiesArray.forEach((baby: any, index: number) => {
      if (!baby.id) {
        baby.id = -(index + 1);
        needsUpdate = true;
      }
    });
    if (needsUpdate) {
      localStorage.setItem('userBabies', JSON.stringify(babiesArray));
    }
    updateBabyCount(babiesArray.length);
  } catch (error) {
  }
}

const clearDefaultBabyData = () => {
  const storedBabies = localStorage.getItem('userBabies');
  if (!storedBabies) {
    return;
  }
  try {
    const babiesArray = JSON.parse(storedBabies);
    const filteredBabies = babiesArray.filter((baby: any) => {
      const isDefault = 
        (!baby.avatar || baby.avatar.includes('elemecdn.com')) &&
        (!baby.id || baby.id < 0) &&
        (baby.nickname.includes('示例') || 
         baby.nickname.includes('默认') || 
         baby.nickname.includes('测试'));
      return !isDefault;
    });
    if (filteredBabies.length !== babiesArray.length) {
      localStorage.setItem('userBabies', JSON.stringify(filteredBabies));
      updateBabyCount(filteredBabies.length);
    }
  } catch (error) {
  }
}

const removeDefaultCard = () => {
  const storedBabies = localStorage.getItem('userBabies');
  if (!storedBabies) return;
  const babiesArray = JSON.parse(storedBabies);
  const filteredBabies = babiesArray.filter((baby: any) => {
    const isDefaultCard = 
      baby.gender === 1 &&
      (baby.height === 70 || baby.height === '70 cm') &&
      (baby.weight === 10 || baby.weight === '10 kg') &&
      (!baby.avatar || baby.avatar.includes('elemecdn.com'));
    return !isDefaultCard;
  });
}

onMounted(() => {
  removeDefaultCard();
  clearDefaultBabyData();
  checkAndFixBabyData();
  fetchBabies();
  const action = route.query.action as string
  const id = route.query.id ? Number(route.query.id) : null
  if (action === 'add') {
    addNewBaby()
  } else if (action === 'edit' && id) {
    const baby = babies.value.find(b => b.id === id)
    if (baby) {
      editBaby(baby)
    } else {
      getKidById(id)
        .then(response => {
          if (response && response.code === 1 && response.data) {
            const kidData = response.data
            const formattedBaby = {
              id: kidData.id,
              nickname: kidData.nickname || '未命名宝宝',
              avatar: kidData.avatar || '',
              birthday: kidData.birthdate || '',
              gender: kidData.gender || 1,
              height: 70, 
              weight: 10  
            }
            editBaby(formattedBaby)
          } else {
            ElMessage.warning('未找到宝宝信息')
            router.push('/user/baby-management')
          }
        })
        .catch(error => {
          ElMessage.error('获取宝宝详情失败')
          router.push('/user/baby-management')
        })
    }
  }
})
</script>

<style scoped>
.baby-management-container {
  @apply max-w-7xl mx-auto px-4 py-6 space-y-8;
}

.page-header {
  @apply mb-8;
}

.page-title {
  @apply text-2xl font-bold text-gray-800 mb-2;
}

.page-description {
  @apply text-gray-600;
}

.babies-section {
  @apply bg-white rounded-xl shadow-md p-6;
}

.babies-header {
  @apply flex justify-between items-center mb-6 pb-3 border-b border-gray-100;
}

.section-title {
  @apply text-xl font-bold text-gray-800;
}

.babies-grid {
  @apply grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6;
}

.baby-card {
  @apply bg-white rounded-xl overflow-hidden shadow-md border border-gray-100 transition-all hover:shadow-lg;
}

.baby-card-header {
  @apply flex items-center gap-4 p-4 border-b border-gray-50 bg-gradient-to-r from-blue-50 to-indigo-50;
}

.baby-avatar {
  @apply w-16 h-16 rounded-full overflow-hidden flex-shrink-0 border-2 border-white shadow-sm;
}

.baby-avatar img {
  @apply w-full h-full object-cover;
}

.default-baby-avatar {
  @apply w-full h-full bg-gradient-to-r from-blue-400 to-indigo-500 flex items-center justify-center text-white text-2xl font-bold;
}

.baby-header-info {
  @apply flex-1;
}

.baby-name {
  @apply font-bold text-lg text-gray-800 mb-1;
}

.baby-gender {
  @apply flex items-center text-sm;
}

.baby-gender .el-icon {
  @apply mr-1;
}

.baby-card-body {
  @apply p-4 grid grid-cols-2 gap-4;
}

.baby-info-item {
  @apply flex flex-col;
}

.info-label {
  @apply text-xs text-gray-500 mb-1;
}

.info-value {
  @apply text-sm text-gray-800;
}

.info-value.highlight {
  @apply text-blue-600 font-medium;
}

.baby-card-footer {
  @apply flex justify-between p-4 border-t border-gray-50 bg-gray-50;
}

.no-babies {
  @apply py-12 text-center;
}

.baby-edit-section {
  @apply bg-white rounded-xl shadow-md p-6;
}

.edit-header {
  @apply flex justify-between items-center mb-6 pb-3 border-b border-gray-100;
}

.edit-title {
  @apply text-xl font-bold text-gray-800;
}

.edit-form-container {
  @apply max-w-3xl mx-auto;
}

.baby-form {
  @apply flex flex-col md:flex-row gap-8;
}

.form-avatar-section {
  @apply flex flex-col items-center mb-6 md:mb-0;
}

.avatar-upload {
  @apply text-center;
}

.avatar-preview {
  @apply w-32 h-32 rounded-full overflow-hidden mb-4 mx-auto flex items-center justify-center bg-gray-100 border-2 border-gray-200;
}

.avatar-image {
  @apply w-full h-full object-cover;
}

.default-avatar {
  @apply w-full h-full flex items-center justify-center bg-gradient-to-r from-blue-400 to-indigo-500 text-white text-4xl;
}

.upload-btn {
  @apply w-full;
}

.form-content {
  @apply flex-1;
}

.form-row {
  @apply grid grid-cols-1 md:grid-cols-2 gap-4;
}

.form-actions {
  @apply flex justify-end gap-2 mt-6;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .baby-form {
    @apply flex-col;
  }
  
  .form-avatar-section {
    @apply mb-6;
  }
}

.loading-container {
  @apply py-8;
}
</style> 