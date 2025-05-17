<template>
  <div class="goods-table">
    <el-table
      :data="data"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="商品图片" width="120">
        <template #default="scope">
          <el-image
            :src="scope.row.mainImage"
            fit="cover"
            style="width: 60px; height: 60px"
            :preview-src-list="[scope.row.mainImage]"
          >
            <template #error>
              <div class="image-placeholder">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" min-width="180" />
      <el-table-column prop="subtitle" label="描述" min-width="200" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="scope">
          <span>￥{{ parseFloat(scope.row.price).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            size="small" 
            @click="$emit('edit', scope.row)"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="$emit('delete', scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import { Picture } from '@element-plus/icons-vue'

const props = defineProps<{
  data: any[]
}>()

defineEmits(['edit', 'delete'])
</script>

<style scoped>
.goods-table {
  width: 100%;
}

.image-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 60px;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 20px;
  border-radius: 4px;
}
</style>