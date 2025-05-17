<template>
  <div class="article-management">
    <div class="header">
      <el-button type="primary" @click="handleAdd">新增文章</el-button>
      <div class="mcp-assistant">
        <el-input
          v-model="mcpQuery"
          placeholder="输入问题，使用MCP协助管理文章数据"
          style="width: 300px"
          clearable
        >
          <template #suffix>
            <el-button
              class="voice-btn"
              :class="{ recording: isRecording }"
              circle
              @click="toggleVoiceRecognition"
            >
              <el-icon><Microphone /></el-icon>
            </el-button>
          </template>
        </el-input>
        <el-button type="success" @click="handleMcpAssistant">MCP协助文章数据管理</el-button>
      </div>
      <el-input
        v-model="searchQuery"
        placeholder="搜索文章标题或内容"
        style="width: 300px"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    <div class="custom-tabs">
      <div class="stage-tabs">
        <div 
          v-for="stage in stageOptions" 
          :key="stage.value"
          :class="['tab-item', { active: activeStage === stage.value }]"
          @click="setActiveStage(stage.value)"
        >
          {{ stage.label }}
        </div>
      </div>
      <div class="tab-content">
        <article-table-new
          :data="filteredArticles" 
          @edit="handleEdit"
          @delete="handleDelete"
          v-loading="loading"
        />
      </div>
    </div>
    <div class="pagination">
      <el-pagination
        :current-page="pageInfo.current"
        :page-size="pageInfo.size"
        :total="pageInfo.total"
        layout="total, prev, pager, next, jumper"
        @current-change="handlePageChange"
      />
    </div>
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle"
      width="60%"
    >
      <el-form :model="currentArticle" label-width="80px">
        <el-form-item label="文章标题" required>
          <el-input v-model="currentArticle.title" />
        </el-form-item>
        <el-form-item label="适用阶段" required>
          <el-select v-model="currentArticle.stage" placeholder="请选择适用阶段">
            <el-option 
              v-for="stage in stageOptions"
              :key="stage.value"
              :label="stage.label"
              :value="stage.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文章内容" required>
          <div class="content-tools">
            <el-input 
              v-model="aiPrompt" 
              placeholder="输入提示词，用AI生成文章内容..." 
              clearable
              style="width: 70%;"
            />
            <el-button 
              type="primary" 
              @click="generateArticleContent" 
              :loading="generating"
              :disabled="!aiPrompt.trim()"
            >
              <el-icon><ChatDotRound /></el-icon>智能生成
            </el-button>
          </div>
          <el-input 
            v-model="currentArticle.content" 
            type="textarea" 
            :rows="10" 
            placeholder="请输入文章内容..."
          />
          <div v-if="isTyping" class="typing-indicator">
            <span>AI正在生成内容</span>
            <span class="dots"><i></i><i></i><i></i></span>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
    <el-dialog 
      v-model="showMcpResponse" 
      title="MCP文章数据助手"
      width="60%"
    >
      <div class="mcp-response">
        <p v-if="!mcpResponse">正在处理您的请求...</p>
        <div v-else v-html="mcpResponse"></div>
      </div>
      <template #footer>
        <el-button @click="showMcpResponse = false">关闭</el-button>
        <el-button type="primary" @click="handleRefreshAfterMcp">刷新数据</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { Search, Plus, Microphone, ChatDotRound } from '@element-plus/icons-vue'
import ArticleTableNew from './ArticleTableNew.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { articleAPI } from '@/api/admin'
import { askQuestion, getAnswerResult } from '@/api/health'
import { marked } from 'marked'

const articles = ref<any[]>([])
const loading = ref(false)
const searchQuery = ref('')
const mcpQuery = ref('')
const isRecording = ref(false)
const mcpResponse = ref('')
const showMcpResponse = ref(false)
const aiPrompt = ref('')
const generating = ref(false)
const isTyping = ref(false)
const typingText = ref('')
const currentSessionId = ref('')
const pageInfo = reactive({
  current: 1,
  size: 100,
  total: 0
})
const stageOptions = [
  { value: -1, label: '全部文章' },
  { value: 0, label: '备孕期' },
  { value: 1, label: '孕产期管理' },
  { value: 2, label: '产褥期' },
  { value: 3, label: '产后恢复' },
  { value: 4, label: '0-1岁' },
  { value: 5, label: '1-3岁' },
  { value: 6, label: '3-6岁' },
  { value: 7, label: '6-12岁' },
  { value: 8, label: '12-18岁' }
]
const activeStage = ref(-1)
const dialogVisible = ref(false)
const dialogTitle = ref('添加文章')
const currentArticle = reactive({
  id: 0,
  title: '',
  content: '',
  stage: 0,
  userId: 0
})

const fetchArticles = async () => {
  loading.value = true
  try {
    const response = await articleAPI.getArticleListPage({
      current: pageInfo.current,
      size: pageInfo.size
    })
    if (response.data && (response.data.code === 1 || response.data.code === 0)) {
      const data = response.data.data || {}
      articles.value = data.records || []
      pageInfo.total = data.total || 0
    } else {
      articles.value = generateSampleArticles(activeStage.value)
      ElMessage.warning('加载文章数据失败，显示示例数据')
    }
  } catch (error) {
    articles.value = generateSampleArticles(activeStage.value)
    ElMessage.warning('加载文章数据失败，显示示例数据')
  } finally {
    loading.value = false
  }
}

const getStageName = (stage: number) => {
  const stageObj = stageOptions.find(item => item.value === stage)
  return stageObj ? stageObj.label : '未知阶段'
}

const filteredArticles = computed(() => {
  if (!searchQuery.value) {
    if (activeStage.value === -1) {
      return articles.value
    }
    return articles.value.filter(article => article.stage === activeStage.value)
  }
  let filtered = articles.value
  if (activeStage.value !== -1) {
    filtered = filtered.filter(article => article.stage === activeStage.value)
  }
  return filtered.filter(article => 
    article.title.includes(searchQuery.value) || 
    article.content.includes(searchQuery.value))
})

const handleSearch = () => {
}

const handlePageChange = (page: number) => {
  pageInfo.current = page
  fetchArticles()
}

const setActiveStage = (stage: number) => {
  activeStage.value = stage
  searchQuery.value = ''
  pageInfo.current = 1
  fetchArticles()
}

const handleAdd = () => {
  dialogTitle.value = '添加文章'
  Object.assign(currentArticle, {
    id: 0,
    title: '',
    content: '',
    stage: activeStage.value === -1 ? 0 : activeStage.value,
    userId: 1
  })
  dialogVisible.value = true
}

const handleEdit = async (article: any) => {
  dialogTitle.value = '编辑文章'
  try {
    const response = await articleAPI.getArticleDetail(article.id)
    if (response.data.code === 0 || response.data.code === 1) {
      const articleDetail = response.data.data || {}
      Object.assign(currentArticle, {
        id: articleDetail.id || article.id,
        title: articleDetail.title || article.title,
        content: articleDetail.content || article.content,
        stage: articleDetail.stage || article.stage,
        userId: articleDetail.userId || article.userId
      })
    } else {
      Object.assign(currentArticle, {
        id: article.id,
        title: article.title,
        content: article.content || '',
        stage: article.stage,
        userId: article.userId || 1
      })
      ElMessage.warning('获取文章详情失败')
    }
  } catch (error) {
    Object.assign(currentArticle, {
      id: article.id,
      title: article.title,
      content: article.content || '',
      stage: article.stage,
      userId: article.userId || 1
    })
    ElMessage.warning('获取文章详情失败')
  }
  dialogVisible.value = true
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确认删除该文章?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await articleAPI.deleteArticle(id)
      if (response.data.code === 0 || response.data.code === 1) {
        ElMessage.success('删除成功')
        fetchArticles()
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除文章失败')
    }
  }).catch(() => {
  })
}

const handleSubmit = async () => {
  try {
    if (!currentArticle.title || !currentArticle.content) {
      ElMessage.warning('请填写必要的信息')
      return
    }
    let response
    if (currentArticle.id === 0) {
      response = await articleAPI.addArticle(currentArticle)
    } else {
      response = await articleAPI.updateArticle(currentArticle)
    }
    if (response.data.code === 0 || response.data.code === 1) {
      ElMessage.success(currentArticle.id === 0 ? '添加成功' : '更新成功')
      dialogVisible.value = false
      fetchArticles()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

let recognition: any = null

const initSpeechRecognition = () => {
  if ('webkitSpeechRecognition' in window) {
    // @ts-ignore
    recognition = new webkitSpeechRecognition()
    recognition.continuous = true
    recognition.interimResults = true
    recognition.lang = 'zh-CN'
    recognition.onresult = (event: any) => {
      const transcript = Array.from(event.results)
        .map((result: any) => result[0])
        .map((result: any) => result.transcript)
        .join('')
      mcpQuery.value = transcript
    }
    recognition.onend = () => {
      isRecording.value = false
    }
    recognition.onerror = (event: any) => {
      isRecording.value = false
      ElMessage.error(`语音识别失败: ${event.error}`)
    }
  } else {
    ElMessage.warning('您的浏览器不支持语音识别功能')
  }
}

const toggleVoiceRecognition = () => {
  if (!recognition) {
    initSpeechRecognition()
  }
  if (isRecording.value) {
    recognition.stop()
    isRecording.value = false
  } else {
    recognition.start()
    isRecording.value = true
    ElMessage.info('开始语音输入')
  }
}

const handleMcpAssistant = async () => {
  if (!mcpQuery.value.trim()) {
    ElMessage.warning('请输入问题或使用语音输入')
    return
  }
  loading.value = true
  mcpResponse.value = ''
  showMcpResponse.value = true
  try {
    const response = await fetch('/mcp-api/api/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify({
        message: mcpQuery.value
      })
    })
    const data = await response.json()
    if (data) {
      mcpResponse.value = data.data
      ElMessage.success('获取MCP响应成功')
    } else {
      mcpResponse.value = data.msg || 'MCP处理请求失败'
      ElMessage.error(data.msg || 'MCP处理请求失败')
    }
  } catch (error) {
    mcpResponse.value = '调用MCP接口失败，请确认服务已启动'
    ElMessage.error('调用MCP接口失败，请确认服务已启动')
  } finally {
    loading.value = false
  }
}

const generateArticleContent = async () => {
  if (!aiPrompt.value.trim()) {
    ElMessage.warning('请输入提示词')
    return
  }
  generating.value = true
  isTyping.value = true
  typingText.value = ''
  try {
    const stageName = getStageName(currentArticle.stage)
    const prompt = `请为我生成一篇关于"${aiPrompt.value}"的文章，适用于${stageName}阶段的读者。文章标题是"${currentArticle.title || '未命名文章'}"。回答中不要有任何引导语，直接给出正文内容。文章需要专业、严谨，字数约1000字左右。`
    const response = await askQuestion(prompt)
    if (response.code === 1 && response.data) {
      let questionSessionId = ''
      const questionIdMatch = response.data.match(/请求\s*ID[：:]\s*([a-f0-9-]+)/i)
      if (questionIdMatch && questionIdMatch[1]) {
        questionSessionId = questionIdMatch[1]
        currentSessionId.value = questionSessionId
        await pollForAnswer(questionSessionId)
      } else {
        ElMessage.error('无法提取有效的会话ID')
        generating.value = false
        isTyping.value = false
      }
    } else {
      ElMessage.error(response.msg || '生成文章内容失败')
      generating.value = false
      isTyping.value = false
    }
  } catch (error) {
    ElMessage.error('生成文章内容失败，请重试')
    generating.value = false
    isTyping.value = false
  }
}

const pollForAnswer = async (questionSessionId: string) => {
  try {
    const response = await getAnswerResult(questionSessionId)
    if (response.code === 1 && response.data) {
      const processedText = processMarkdown(response.data)
      await simulateTypingEffect(processedText)
      generating.value = false
      return
    }
    setTimeout(() => pollForAnswer(questionSessionId), 1000)
  } catch (error) {
    setTimeout(() => pollForAnswer(questionSessionId), 2000)
  }
}

const processMarkdown = (text: string): string => {
  const cleanText = text.replace('TERMINATE', '');
  const processedText = cleanText.replace(/\\n/g, '\n');
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = marked.parse(processedText) as string;
  return tempDiv.textContent || '';
}

const simulateTypingEffect = async (fullText: string) => {
  isTyping.value = true
  typingText.value = ''
  const allChars = fullText.split('')
  for (let i = 0; i < allChars.length; i++) {
    typingText.value += allChars[i]
    currentArticle.content = typingText.value
    await new Promise(resolve => setTimeout(resolve, 15))
  }
  isTyping.value = false
}

const handleRefreshAfterMcp = () => {
  fetchArticles()
  ElMessage.success('数据已刷新')
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

const generateSampleArticles = (stage = -1) => {
  const sampleArticles = [
    {
      id: 1,
      title: '婴儿早期发展里程碑',
      content: '婴儿在第一年会经历多个关键发展里程碑...',
      stage: 4,
      userId: 1,
      createTime: '2023-05-15 08:30:00',
      updateTime: '2023-05-15 08:30:00'
    },
    {
      id: 2,
      title: '备孕期营养指南',
      content: '备孕期间合理的营养摄入对未来宝宝的健康至关重要...',
      stage: 0,
      userId: 1,
      createTime: '2023-06-20 14:15:00',
      updateTime: '2023-06-20 14:15:00'
    },
    {
      id: 3,
      title: '幼儿语言发展技巧',
      content: '2-3岁是幼儿语言发展的关键期...',
      stage: 5,
      userId: 1,
      createTime: '2023-07-10 11:45:00',
      updateTime: '2023-07-10 11:45:00'
    },
    {
      id: 4,
      title: '产后恢复指南',
      content: '产后正确的恢复方法对妈妈的身体健康非常重要...',
      stage: 3,
      userId: 1,
      createTime: '2023-08-05 09:20:00',
      updateTime: '2023-08-05 09:20:00'
    },
    {
      id: 5,
      title: '青少年心理健康',
      content: '12-18岁是青少年心理发展的关键期，家长需要注意...',
      stage: 8,
      userId: 1,
      createTime: '2023-09-12 16:40:00',
      updateTime: '2023-09-12 16:40:00'
    }
  ]
  if (stage === -1) {
    return sampleArticles
  }
  return sampleArticles.filter(article => article.stage === stage)
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.article-management {
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

.custom-tabs {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.stage-tabs {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.tab-item {
  padding: 10px 20px;
  cursor: pointer;
  position: relative;
  font-size: 14px;
  color: #606266;
  transition: all 0.3s;
}

.tab-item:hover {
  color: #409EFF;
}

.tab-item.active {
  color: #409EFF;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #409EFF;
}

.tab-content {
  padding: 10px 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

.article-preview {
  padding: 0 20px;
}

.article-preview h1 {
  text-align: center;
  margin-bottom: 20px;
}

.article-preview .meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  color: #666;
  font-size: 14px;
}

.article-preview .content {
  line-height: 1.8;
  white-space: pre-wrap;
}

.mcp-assistant {
  display: flex;
  align-items: center;
  gap: 10px;
}

.voice-btn {
  font-size: 16px;
  color: #606266;
  transition: all 0.3s;
}

.voice-btn.recording {
  color: #409EFF;
  animation: pulse 1.5s infinite;
}

.mcp-response {
  min-height: 200px;
  max-height: 500px;
  overflow-y: auto;
  line-height: 1.8;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

.content-tools {
  display: flex;
  align-items: center;
  gap: 10px;
}

.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #409EFF;
  font-size: 14px;
  margin-top: 10px;
  background-color: #ecf5ff;
  padding: 8px;
  border-radius: 4px;
  border-left: 3px solid #409EFF;
}

.typing-indicator span {
  margin-right: 8px;
}

.dots {
  display: inline-block;
  animation: blink 1.5s infinite;
}

.dots i {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #000;
  border-radius: 50%;
  margin: 0 2px;
  animation: blink 1.5s infinite;
}

.dots i:nth-child(2) {
  animation-delay: 0.2s;
}

.dots i:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}
</style>