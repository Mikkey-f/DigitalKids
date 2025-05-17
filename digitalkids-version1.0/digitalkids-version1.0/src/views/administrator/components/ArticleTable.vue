<template>
  <div class="article-table">
    <el-table
      :data="data"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column label="内容预览" min-width="300">
        <template #default="scope">
          <div class="content-preview">{{ truncateContent(scope.row.content) }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="stage" label="适用阶段" width="120">
        <template #default="scope">
          <el-tag>{{ getStageName(scope.row.stage) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" />
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
const props = defineProps<{
  data: any[]
}>()

defineEmits(['edit', 'delete'])

const truncateContent = (content: string) => {
  if (!content) return ''
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
</script>

<style scoped>
.article-table {
  width: 100%;
}

.content-preview {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 300px;
  color: #606266;
}
</style>