<template>
  <div class="user-management">
    <div class="header">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加用户
      </el-button>
      <el-input
        v-model="searchQuery"
        placeholder="搜索用户名或手机号"
        style="width: 300px"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    <el-table
      v-loading="loading"
      :data="users"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="用户名" width="120" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="scope">
          <el-tag 
            :type="scope.row.role === 'admin' ? 'danger' : 'success'"
          >
            {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="头像" width="100">
        <template #default="scope">
          <el-avatar :size="32" :src="scope.row.avatar" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button 
            size="small" 
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button 
            size="small" 
            type="danger" 
            @click="handleDelete(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        :current-page="pageInfo.current"
        :page-size="pageInfo.size"
        :total="pageInfo.total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle"
      width="40%"
    >
      <el-form :model="currentUser" label-width="100px">
        <el-form-item label="用户名" required>
          <el-input v-model="currentUser.name" />
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="currentUser.phone" />
        </el-form-item>
        <el-form-item label="密码" :required="currentUser.id === 0">
          <el-input v-model="currentUser.password" type="password" />
          <span class="note" v-if="currentUser.id !== 0">留空表示不修改密码</span>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="currentUser.gender" placeholder="请选择性别">
            <el-option 
              v-for="item in genderOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="currentUser.role" placeholder="请选择角色">
            <el-option 
              v-for="item in roles" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            action="#"
            list-type="picture-card"
            :on-change="handleAvatarChange"
            :before-upload="beforeAvatarUpload"
            :auto-upload="false"
            :limit="1"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div v-if="currentUser.avatar" class="avatar-preview">
            <el-avatar :size="100" :src="currentUser.avatar" />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userAPI } from '@/api/admin'
import { uploadAvatar } from '@/api/user'

const pageInfo = reactive({
  current: 1,
  size: 10,
  total: 0
})

const users = ref<any[]>([])
const loading = ref(false)
const searchQuery = ref('')
const avatarUploading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('添加用户')
const currentUser = reactive({
  id: 0,
  name: '',
  phone: '',
  gender: '男',
  password: '',
  avatar: '',
  role: 'user'
})

const roles = [
  { value: 'user', label: '普通用户' },
  { value: 'admin', label: '管理员' }
]

const genderOptions = [
  { value: '男', label: '男' },
  { value: '女', label: '女' },
  { value: '其他', label: '其他' }
]

const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('上传文件只能是图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isImage && isLt2M
}

const handleAvatarChange = async (file: any) => {
  if (!file || !file.raw) return
  try {
    avatarUploading.value = true
    const response = await uploadAvatar(file.raw)
    if (response) {
      currentUser.avatar = response
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error('头像上传失败')
    }
  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    avatarUploading.value = false
  }
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await userAPI.getUserList({
      current: pageInfo.current,
      size: pageInfo.size
    })
    if (response.data.code === 1) {
      const data = response.data.data || {}
      users.value = data.records || []
      pageInfo.total = data.total || 0
    } else {
      users.value = generateSampleUsers()
      ElMessage.warning('加载用户数据失败，显示示例数据')
    }
  } catch (error) {
    users.value = generateSampleUsers()
    ElMessage.warning('加载用户数据失败，显示示例数据')
  } finally {
    loading.value = false
  }
}

const generateSampleUsers = () => {
  return [
    {
      id: 1,
      name: '张三',
      phone: '13800138000',
      gender: '男',
      role: 'user',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createTime: '2025-05-01 10:00:00'
    },
    {
      id: 2,
      name: '李四',
      phone: '13900139000',
      gender: '女',
      role: 'user',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createTime: '2025-05-02 14:30:00'
    },
    {
      id: 3,
      name: '王五',
      phone: '13700137000',
      gender: '男',
      role: 'admin',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createTime: '2025-05-03 09:15:00'
    }
  ]
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    users.value = users.value.filter(user => 
      user.name.includes(searchQuery.value) || 
      user.phone.includes(searchQuery.value))
  } else {
    fetchUsers()
  }
}

const handleAdd = () => {
  dialogTitle.value = '添加用户'
  Object.assign(currentUser, {
    id: 0,
    name: '',
    phone: '',
    gender: '男',
    password: '',
    avatar: '',
    role: 'user'
  })
  dialogVisible.value = true
}

const handleEdit = (user: any) => {
  dialogTitle.value = '编辑用户'
  Object.assign(currentUser, {
    id: user.id,
    name: user.name,
    phone: user.phone,
    gender: user.gender,
    password: '',
    avatar: user.avatar,
    role: user.role
  })
  dialogVisible.value = true
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确认删除该用户?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await userAPI.deleteUser(id)
      if (response.data.code === 0 || response.data.code === 1) {
        ElMessage.success('删除成功')
        fetchUsers()
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除用户失败')
    }
  }).catch(() => {
  })
}

const handleSubmit = async () => {
  try {
    if (!currentUser.name || !currentUser.phone) {
      ElMessage.warning('请填写必要的信息')
      return
    }
    if (currentUser.id === 0 && !currentUser.password) {
      ElMessage.warning('新增用户必须设置密码')
      return
    }
    let response
    if (currentUser.id === 0) {
      response = await userAPI.addUser(currentUser)
    } else {
      response = await userAPI.updateUser(currentUser)
    }
    if (response.data.code === 0 || response.data.code === 1) {
      ElMessage.success(currentUser.id === 0 ? '添加成功' : '更新成功')
      dialogVisible.value = false
      fetchUsers()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handlePageChange = (page: number) => {
  pageInfo.current = page
  fetchUsers()
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.note {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}

.avatar-preview {
  margin-top: 10px;
  display: flex;
  justify-content: center;
}

:deep(.el-table .el-table__header-wrapper) {
  background-color: #f5f7fa;
}

:deep(.el-table .el-table__row:hover) {
  background-color: #f5f7fa;
}

:deep(.el-pagination) {
  justify-content: flex-end;
}
</style>