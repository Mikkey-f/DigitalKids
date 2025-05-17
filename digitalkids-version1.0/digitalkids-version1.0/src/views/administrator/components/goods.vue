<template>
  <div class="goods-management">
    <div class="header">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增商品
      </el-button>
      <el-input
        v-model="searchQuery"
        placeholder="搜索商品名称或描述"
        style="width: 300px"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    <el-tabs v-model="activeCategory" class="category-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="全部商品" name="all">
        <goods-table 
          :data="getFilteredProducts('all')" 
          @edit="handleEdit"
          @delete="handleDelete"
          v-loading="loading"
        />
      </el-tab-pane>
      <el-tab-pane 
        v-for="category in categories" 
        :key="category.id" 
        :label="category.name" 
        :name="category.name"
      >
        <goods-table 
          :data="getFilteredProducts(category.name)" 
          @edit="handleEdit"
          @delete="handleDelete"
          v-loading="loading"
        />
      </el-tab-pane>
    </el-tabs>
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
      width="50%"
    >
      <el-form :model="currentProduct" label-width="100px">
        <el-form-item label="商品名称" required>
          <el-input v-model="currentProduct.name" />
        </el-form-item>
        <el-form-item label="商品分类" required>
          <el-select v-model="currentProduct.categoryId" placeholder="请选择分类">
            <el-option 
              v-for="cat in categories" 
              :key="cat.id" 
              :label="cat.name" 
              :value="cat.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品副标题">
          <el-input v-model="currentProduct.subtitle" />
        </el-form-item>
        <el-form-item label="商品详情">
          <el-input v-model="currentProduct.detail" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="商品价格" required>
          <el-input-number v-model="currentProduct.price" :min="0" :precision="2" style="width: 180px" />
          <span class="price-unit">元</span>
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="currentProduct.stock" :min="0" :precision="0" style="width: 180px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentProduct.status" placeholder="请选择状态">
            <el-option :value="1" label="在售" />
            <el-option :value="2" label="下架" />
            <el-option :value="3" label="删除" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品主图">
          <el-upload
            action="#"
            list-type="picture-card"
            :on-change="handleImageChange"
            :before-upload="beforeImageUpload"
            :auto-upload="false"
            :limit="1"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div v-if="currentProduct.mainImage" class="image-preview">
            <el-image :src="currentProduct.mainImage" fit="cover" style="width: 100px; height: 100px;" />
          </div>
        </el-form-item>
        <el-form-item label="商品子图">
          <el-upload
            action="#"
            list-type="picture-card"
            :on-change="handleSubImageChange"
            :before-upload="beforeImageUpload"
            :auto-upload="false"
            :limit="10"
            multiple
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div v-if="subImagesList.length > 0" class="sub-images-preview">
            <div v-for="(url, index) in subImagesList" :key="index" class="sub-image-item">
              <el-image
                :src="url"
                fit="cover"
                class="sub-image"
                :preview-src-list="[url]"
                preview-teleported
              />
              <div class="sub-image-actions">
                <el-button type="danger" size="small" circle @click="handleRemoveSubImage(index)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { Search, Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { productAPI } from '@/api/admin'
import { uploadAvatar } from '@/api/user'
import GoodsTable from './GoodsTable.vue'

const pageInfo = reactive({
  current: 1,
  size: 10,
  total: 0
})

const products = ref<any[]>([])
const loading = ref(false)
const searchQuery = ref('')
const imageUploading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const currentProduct = reactive({
  id: 0,
  name: '',
  categoryId: 0,
  subtitle: '',
  mainImage: '',
  subImages: '',
  detail: '',
  price: 0,
  stock: 100,
  status: 1
})

const categories = ref<any[]>([])
const activeCategory = ref('all')

const beforeImageUpload = (file: File) => {
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

const handleImageChange = async (file: any) => {
  if (!file || !file.raw) return
  try {
    imageUploading.value = true
    const response = await uploadAvatar(file.raw)
    if (response) {
      currentProduct.mainImage = response
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error('图片上传失败')
    }
  } catch (error) {
    ElMessage.error('图片上传失败')
  } finally {
    imageUploading.value = false
  }
}

const handleSubImageChange = async (file: any) => {
  if (!file || !file.raw) return
  try {
    imageUploading.value = true
    const response = await uploadAvatar(file.raw)
    if (response) {
      const subImagesArray = currentProduct.subImages ? currentProduct.subImages.split(',').filter(url => url.trim() !== '') : []
      subImagesArray.push(response)
      currentProduct.subImages = subImagesArray.join(',')
      ElMessage.success('子图上传成功')
    } else {
      ElMessage.error('子图上传失败')
    }
  } catch (error) {
    ElMessage.error('子图上传失败')
  } finally {
    imageUploading.value = false
  }
}

const handleRemoveSubImage = (index: number) => {
  const subImagesArray = currentProduct.subImages.split(',')
  subImagesArray.splice(index, 1)
  currentProduct.subImages = subImagesArray.join(',')
}

const subImagesList = computed(() => {
  if (!currentProduct.subImages) return []
  return currentProduct.subImages.split(',').filter(url => url.trim() !== '')
})

const fetchProducts = async () => {
  loading.value = true
  try {
    const response = await productAPI.listProductsByPage({
      current: pageInfo.current,
      size: pageInfo.size,
      pageSize: pageInfo.size
    })
    if (response.data && response.data.code === 1) {
      const data = response.data.data || {}
      products.value = data.records || []
      pageInfo.total = data.total || 0
    } else {
      products.value = generateSampleProducts()
      ElMessage.warning('加载商品数据失败，显示示例数据')
    }
  } catch (error) {
    products.value = generateSampleProducts()
    ElMessage.warning('加载商品数据失败，显示示例数据')
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const response = await productAPI.getCategories()
    if (response.data && response.data.code === 1) {
      const data = response.data.data || []
      categories.value = data
      if (categories.value.length > 0) {
        activeCategory.value = 'all'
      }
    } else {
      categories.value = generateSampleCategories()
      activeCategory.value = 'all'
      ElMessage.warning('加载分类数据失败，显示示例数据')
    }
  } catch (error) {
    categories.value = generateSampleCategories()
    activeCategory.value = 'all'
    ElMessage.warning('加载分类数据失败，显示示例数据')
  }
}

const generateSampleCategories = () => {
  return [
    { id: 1, name: '卫生护理', parentId: 0 },
    { id: 2, name: '可穿戴设备', parentId: 0 },
    { id: 3, name: '医疗器械', parentId: 0 },
    { id: 4, name: '保健产品', parentId: 0 }
  ]
}

const generateSampleProducts = () => {
  return [
    {
      id: 1,
      name: '儿童抗菌洗手液',
      subtitle: '温和配方，不伤小手',
      price: 32.90,
      categoryId: 1,
      mainImage: 'https://example.com/product1.jpg',
      stock: 100,
      status: 1
    },
    {
      id: 2,
      name: '婴儿纸尿裤',
      subtitle: '超柔透气，防红屁股',
      price: 129.00,
      categoryId: 1,
      mainImage: 'https://example.com/product2.jpg',
      stock: 200,
      status: 1
    },
    {
      id: 3,
      name: '儿童智能手表',
      subtitle: '定位+心率监测',
      price: 299.00,
      categoryId: 2,
      mainImage: 'https://example.com/product3.jpg',
      stock: 50,
      status: 1
    },
    {
      id: 4,
      name: '电子体温计',
      subtitle: '快速精准测温',
      price: 69.90,
      categoryId: 3,
      mainImage: 'https://example.com/product4.jpg',
      stock: 150,
      status: 1
    },
    {
      id: 5,
      name: '儿童DHA营养补充剂',
      subtitle: '促进大脑发育',
      price: 168.00,
      categoryId: 4,
      mainImage: 'https://example.com/product5.jpg',
      stock: 80,
      status: 1
    }
  ]
}

const getFilteredProducts = (categoryName: string) => {
  if (categoryName === 'all') {
    if (!searchQuery.value) {
      return products.value
    }
    return products.value.filter(product => 
      product.name.includes(searchQuery.value) || 
      product.subtitle?.includes(searchQuery.value))
  }
  const category = categories.value.find(cat => cat.name === categoryName)
  if (!category) return []
  const categoryId = category.id
  if (!searchQuery.value) {
    return products.value.filter(p => p.categoryId === categoryId)
  }
  return products.value.filter(product => 
    product.categoryId === categoryId && 
    (product.name.includes(searchQuery.value) || 
     product.subtitle?.includes(searchQuery.value)))
}

const handleSearch = () => {
  products.value = [...products.value]
}

const handleTabChange = (tab: string) => {
  activeCategory.value = tab
}

const handlePageChange = (page: number) => {
  pageInfo.current = page
  fetchProducts()
}

const handleAdd = () => {
  dialogTitle.value = '新增商品'
  Object.assign(currentProduct, {
    id: 0,
    name: '',
    categoryId: 0,
    subtitle: '',
    mainImage: '',
    subImages: '',
    detail: '',
    price: 0,
    stock: 100,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (product: any) => {
  dialogTitle.value = '编辑商品'
  Object.assign(currentProduct, {
    id: product.id,
    name: product.name,
    categoryId: product.categoryId,
    subtitle: product.subtitle || '',
    mainImage: product.mainImage || '',
    subImages: product.subImages || '',
    detail: product.detail || '',
    price: product.price || 0,
    stock: product.stock || 100,
    status: product.status || 1
  })
  dialogVisible.value = true
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确认删除该商品?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await productAPI.deleteProduct(id)
      if (response.data && response.data.code === 1) {
        ElMessage.success('删除成功')
        fetchProducts()
      } else {
        ElMessage.error(response.data?.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除商品失败')
    }
  }).catch(() => {
  })
}

const handleSubmit = async () => {
  try {
    if (!currentProduct.name || !currentProduct.price) {
      ElMessage.warning('请填写必要的信息')
      return
    }
    let response
    if (currentProduct.id === 0) {
      response = await productAPI.addProduct(currentProduct)
    } else {
      response = await productAPI.updateProduct(currentProduct.id, currentProduct)
    }
    if (response.data && (response.data.code === 0 || response.data.code === 1)) {
      ElMessage.success(currentProduct.id === 0 ? '添加成功' : '更新成功')
      dialogVisible.value = false
      fetchProducts()
    } else {
      ElMessage.error(response.data?.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.goods-management {
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

.category-tabs {
  margin-top: 20px;
}

:deep(.el-tabs__header) {
  margin-bottom: 0;
}

:deep(.el-tabs__content) {
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-top: none;
  border-radius: 0 0 4px 4px;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

.image-preview {
  margin-top: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.price-unit {
  margin-left: 10px;
  color: #606266;
}

.sub-images-preview {
  margin-top: 10px;
}

.sub-image-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.sub-image {
  width: 100px;
  height: 100px;
  margin-right: 10px;
}

.sub-image-actions {
  display: flex;
  align-items: center;
}
</style>