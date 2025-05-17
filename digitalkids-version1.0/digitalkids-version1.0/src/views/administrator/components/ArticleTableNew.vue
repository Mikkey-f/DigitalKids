<template>
  <div class="article-table">
    <el-table
      :data="data"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column label="内容预览" min-width="320">
        <template #default="scope">
          <div class="content-preview">
            <div class="preview-text">{{ truncateContent(scope.row.content) }}</div>
            <el-button 
              type="primary" 
              link 
              size="small" 
              @click="handlePreview(scope.row)"
            >
              查看全文
            </el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="stage" label="适用阶段" width="120">
        <template #default="scope">
          <el-tag>{{ getStageName(scope.row.stage) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="150">
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
    <el-dialog
      v-model="previewVisible"
      title="文章预览"
      width="60%"
    >
      <div class="article-preview">
        <h2>{{ currentPreview.title }}</h2>
        <div class="meta">
          <span>{{ getStageName(currentPreview.stage) }}</span>
          <span>{{ formatDate(currentPreview.publishTime || currentPreview.createTime) }}</span>
        </div>
        <div class="content">{{ currentPreview.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'

const props = defineProps<{
  data: any[]
}>()

defineEmits(['edit', 'delete'])

const previewVisible = ref(false)
const currentPreview = reactive({
  id: 0,
  title: '',
  content: '',
  stage: 0,
  createTime: '',
  publishTime: '',
})

const handlePreview = (article: any) => {
  Object.assign(currentPreview, article)
  previewVisible.value = true
}

const truncateContent = (content: string) => {
  if (!content) return '暂无内容'
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

const getStageName = (stage: number) => {
  const stageMap: Record<number, string> = {
    0: '备孕期',
    1: '孕产期管理',
    2: '产褥期',
    3: '产后恢复',
    4: '0-1岁',
    5: '1-3岁',
    6: '3-6岁',
    7: '6-12岁',
    8: '12-18岁'
  }
  return stageMap[stage] || '未知阶段'
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}
</script>

<style scoped>
.article-table {
  width: 100%;
}

.content-preview {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.preview-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 250px;
  color: #606266;
}

.article-preview {
  padding: 0 20px;
}

.article-preview h2 {
  text-align: center;
  margin-bottom: 20px;
  font-size: 22px;
}

.article-preview .meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  color: #666;
  font-size: 14px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.article-preview .content {
  line-height: 1.8;
  white-space: pre-wrap;
  color: #333;
  font-size: 16px;
}
</style>