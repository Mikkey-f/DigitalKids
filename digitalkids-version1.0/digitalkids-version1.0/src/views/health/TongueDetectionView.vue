<template>
  <div class="tongue-detection-page">
    <div class="page-header" v-show="step >= 1">
      <h1 class="page-title">舌苔检测系统</h1>
      <div class="page-subtitle">通过上传舌象，分析健康状况</div>
    </div>
    <div class="detection-container" v-show="step >= 2">
      <div class="detection-left">
        <div class="upload-area-container">
          <div class="upload-area">
            <div class="image-preview" v-if="previewImage">
              <img :src="previewImage" alt="舌象预览" />
              <div class="preview-overlay">
                <el-button circle class="remove-btn" @click="removeImage">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            <div class="upload-placeholder" v-else @click="triggerFileInput">
              <div class="upload-icon-container">
                <el-icon class="upload-icon"><Plus /></el-icon>
              </div>
              <div class="upload-text">点击上传舌象</div>
              <div class="upload-desc">支持JPG、PNG、JPEG格式</div>
            </div>
            <input 
              type="file" 
              ref="fileInput" 
              class="file-input" 
              accept="image/jpeg,image/png,image/jpg" 
              @change="handleFileChange"
            />
          </div>
          <el-button 
            class="upload-button" 
            type="primary" 
            @click="triggerFileInput"
            v-show="step >= 3 && !previewImage"
            :loading="uploading"
          >
            <el-icon><Upload /></el-icon>上传舌象
          </el-button>
        </div>
        
        <div class="analysis-panel" v-show="step >= 4">
          <div class="panel-header">
            <div class="panel-title">舌象分析</div>
            <div class="panel-subtitle">选择需要分析的特征</div>
          </div>
          <div class="analysis-buttons">
            <el-button 
              @click="toggleFeatureDisplay('tongueColor')" 
              :type="showTongueColor && analysisResults.tongueColor ? 'success' : 'default'"
              class="feature-button"
              :loading="pollingTongueResult && !analysisResults.tongueColor"
              :disabled="pollingTongueResult && !analysisResults.tongueColor"
            >
              <el-icon><Brush /></el-icon>
              <span>舌色分析</span>
            </el-button>
            <el-button 
              @click="toggleFeatureDisplay('coatingColor')"
              :type="showCoatingColor && analysisResults.coatingColor ? 'success' : 'default'"
              class="feature-button"
              :loading="pollingTongueResult && !analysisResults.coatingColor"
              :disabled="pollingTongueResult && !analysisResults.coatingColor"
            >
              <el-icon><ChromeFilled /></el-icon>
              <span>苔色分析</span>
            </el-button>
            <el-button 
              @click="toggleFeatureDisplay('crack')"
              :type="showCrack && analysisResults.crack ? 'success' : 'default'"
              class="feature-button"
              :loading="pollingTongueResult && !analysisResults.crack"
              :disabled="pollingTongueResult && !analysisResults.crack"
            >
              <el-icon><ScaleToOriginal /></el-icon>
              <span>裂纹舌分析</span>
            </el-button>
            <el-button 
              @click="toggleFeatureDisplay('toothMark')"
              :type="showToothMark && analysisResults.toothMark ? 'success' : 'default'"
              class="feature-button"
              :loading="pollingTongueResult && !analysisResults.toothMark"
              :disabled="pollingTongueResult && !analysisResults.toothMark"
            >
              <el-icon><Stamp /></el-icon>
              <span>齿痕舌分析</span>
            </el-button>
          </div>
          
          <el-button 
            type="primary" 
            @click="startDiagnosis" 
            class="diagnosis-button"
            :disabled="!isAnyFeatureAnalyzed || diagnosing"
            :loading="diagnosing"
          >
            <el-icon><Reading /></el-icon>开始鉴定
          </el-button>
        </div>
      </div>
      
      <div class="detection-right" v-show="step >= 5">
        <div class="doctor-container">
          <div class="doctor-image">
            <div class="doctor-placeholder" v-if="!doctorImageExists">
              <el-icon class="doctor-icon"><UserFilled /></el-icon>
              <div>医生形象</div>
            </div>
            <img v-else src="/src/assets/images/tongue/medical.jpg" alt="医生形象" />
          </div>
          <div class="doctor-status" v-if="pollingTongueResult || diagnosing">
            <div class="status-dot"></div>
            <span>{{ pollingTongueResult ? "分析进行中..." : "鉴定中..." }}</span>
          </div>
        </div>
        
        <div class="results-area">
          <transition name="fade">
            <div class="result-empty" v-if="!isAnyFeatureAnalyzed">
              <el-icon class="empty-icon"><InfoFilled /></el-icon>
              <div class="empty-text">请在左侧进行舌象分析</div>
            </div>
          </transition>
          
          <transition-group name="list" tag="div" class="result-items">
            <div class="result-item" v-if="showTongueColor && analysisResults.tongueColor" :key="'tongueColor'">
              <div class="result-header">
                <el-icon><Brush /></el-icon>
                <span class="result-label">舌色分析</span>
              </div>
              <div class="result-content">
                <span class="result-value">{{ analysisResults.tongueColor }}</span>
              </div>
            </div>
            <div class="result-item" v-if="showCoatingColor && analysisResults.coatingColor" :key="'coatingColor'">
              <div class="result-header">
                <el-icon><ChromeFilled /></el-icon>
                <span class="result-label">苔色分析</span>
              </div>
              <div class="result-content">
                <span class="result-value">{{ analysisResults.coatingColor }}</span>
              </div>
            </div>
            <div class="result-item" v-if="showCrack && analysisResults.crack" :key="'crack'">
              <div class="result-header">
                <el-icon><ScaleToOriginal /></el-icon>
                <span class="result-label">裂纹分析</span>
              </div>
              <div class="result-content">
                <span class="result-value">{{ analysisResults.crack }}</span>
              </div>
            </div>
            <div class="result-item" v-if="showToothMark && analysisResults.toothMark" :key="'toothMark'">
              <div class="result-header">
                <el-icon><Stamp /></el-icon>
                <span class="result-label">齿痕分析</span>
              </div>
              <div class="result-content">
                <span class="result-value">{{ analysisResults.toothMark }}</span>
              </div>
            </div>
          </transition-group>
          
          <transition name="fade">
            <div class="diagnosis-result" v-if="diagnosisResult">
              <div class="diagnosis-header">
                <el-icon><Reading /></el-icon>
                <h3 class="diagnosis-title">体质鉴定结果</h3>
              </div>
              <pre class="diagnosis-content pre-content" v-if="!isTyping" v-html="diagnosisResult"></pre>
              <pre class="diagnosis-content typing-content pre-content" v-else>
                <span v-html="typingDiagnosisResult"></span>
                <span class="typing-cursor"></span>
              </pre>
            </div>
          </transition>
        </div>
        
        <div class="action-buttons">
          <el-button @click="resetAnalysis" v-if="isAnyFeatureAnalyzed">
            <el-icon><RefreshRight /></el-icon>重新分析
          </el-button>
          <el-button @click="goBack">
            <el-icon><Back /></el-icon>返回
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed, watch } from 'vue';
import { 
  Plus, 
  UserFilled, 
  Upload, 
  Delete, 
  Brush, 
  ChromeFilled, 
  ScaleToOriginal, 
  Stamp, 
  Reading, 
  RefreshRight, 
  Back, 
  InfoFilled 
} from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { uploadTongueImage, getTongueResult, askQuestion, getAnswerResult, TongueResult } from '@/api/health';
import { marked } from 'marked';

const router = useRouter();
const fileInput = ref<HTMLInputElement | null>(null);
const previewImage = ref<string | null>(null);
const step = ref(0);
const doctorImageExists = ref(false); 
const uploading = ref(false);
const uploadedFile = ref<File | null>(null);
const sessionId = ref<string | null>(null);
const analyzing = ref(false);
const analyzingTongueColor = ref(false);
const analyzingCoatingColor = ref(false);
const analyzingCrack = ref(false);
const analyzingToothMark = ref(false);
const pollingTongueResult = ref(false);
const showTongueColor = ref(true);
const showCoatingColor = ref(true);
const showCrack = ref(true);
const showToothMark = ref(true);
const diagnosing = ref(false);
const typingDiagnosisResult = ref('');
const isTyping = ref(false);
const analysisResults = ref({
  tongueColor: '',
  coatingColor: '',
  crack: '',
  toothMark: ''
});
const diagnosisResult = ref('');

const isAnyFeatureAnalyzed = computed(() => {
  return analysisResults.value.tongueColor !== '' || 
         analysisResults.value.coatingColor !== '' || 
         analysisResults.value.crack !== '' || 
         analysisResults.value.toothMark !== '';
});

onMounted(() => {
  nextStepWithDelay();
  checkDoctorImage();
});

const nextStepWithDelay = () => {
  const incrementStep = () => {
    step.value++;
    if (step.value < 6) {
      setTimeout(incrementStep, 300);
    }
  };
  setTimeout(incrementStep, 300);
};

const checkDoctorImage = () => {
  const img = new Image();
  img.onload = () => {
    doctorImageExists.value = true;
  };
  img.onerror = () => {
    doctorImageExists.value = false;
  };
  try {
    const imgPath = new URL('/src/assets/images/tongue/medical.webp', import.meta.url).href;
    img.src = imgPath;
  } catch (e) {
    doctorImageExists.value = false;
  }
};

const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};
const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    const file = target.files[0];
    uploadedFile.value = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      if (e.target) {
        previewImage.value = e.target.result as string;
      }
    };
    reader.readAsDataURL(file);
    
    await uploadImage();
  }
};

const uploadImage = async () => {
  if (!uploadedFile.value) {
    ElMessage.warning('请选择一张图片进行上传');
    return;
  }
  
  try {
    uploading.value = true;
    const response = await uploadTongueImage(uploadedFile.value);
    if (response.code === 1) {
      let extractedId = '';
      const idMatch = response.data.match(/请求\s*ID[：:]\s*([a-f0-9-]+)/i);
      if (idMatch && idMatch[1]) {
        extractedId = idMatch[1];
        sessionId.value = extractedId;
        ElMessage.success('图片上传成功，正在分析中...');
        await pollForTongueResult();
      } else {
        ElMessage.error('无法提取有效的会话ID');
      }
    } else {
      ElMessage.error(response.msg || '图片上传失败');
    }
  } catch (error) {
    console.error('上传图片失败:', error);
    ElMessage.error('上传图片失败，请重试');
  } finally {
    uploading.value = false;
  }
};

const pollForTongueResult = async () => {
  if (!sessionId.value) {
    ElMessage.warning('未获取到会话ID');
    return;
  }
  pollingTongueResult.value = true;
  analyzing.value = true;
  try {
    const response = await getTongueResult(sessionId.value);
    if (response.code === 1 && response.data) {
      const tongueResult = response.data;

      if (tongueResult.color_result && tongueResult.coated_result && 
          (tongueResult.crack_result !== undefined) && (tongueResult.indent_result !== undefined)) {
        analysisResults.value.tongueColor = mapTongueColor(tongueResult.color_result);
        analysisResults.value.coatingColor = mapCoatingColor(tongueResult.coated_result);
        analysisResults.value.crack = mapCrackResult(tongueResult.crack_result);
        analysisResults.value.toothMark = mapToothMarkResult(tongueResult.indent_result);
        
        pollingTongueResult.value = false;
        analyzing.value = false;
        ElMessage.success('舌象分析完成');
        return;
      } else {
        setTimeout(() => pollForTongueResult(), 1000);
      }
    } else {
      setTimeout(() => pollForTongueResult(), 1000);
    }
  } catch (error) {
    setTimeout(() => pollForTongueResult(), 2000);
  }
};

const removeImage = () => {
  previewImage.value = null;
  uploadedFile.value = null;
  sessionId.value = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
  resetAnalysis();
};

const toggleFeatureDisplay = (feature: string) => {
  if (feature === 'tongueColor') showTongueColor.value = !showTongueColor.value;
  if (feature === 'coatingColor') showCoatingColor.value = !showCoatingColor.value;
  if (feature === 'crack') showCrack.value = !showCrack.value;
  if (feature === 'toothMark') showToothMark.value = !showToothMark.value;
};

const mapTongueColor = (result: string): string => {
  return result || '淡红舌'; 
};

const mapCoatingColor = (result: string): string => {
  return result || '白苔'; 
};

const mapCrackResult = (result: string): string => {
  return result ? '有裂纹' : '无裂纹';
};

const mapToothMarkResult = (result: string): string => {
  return result ? '有齿痕' : '无齿痕';
};

const startDiagnosis = async () => {
  if (!isAnyFeatureAnalyzed.value) {
    ElMessage.warning('请先进行舌象分析');
    return;
  }
  diagnosing.value = true;
  try {
    let questionParts = [];
    if (showTongueColor.value && analysisResults.value.tongueColor) {
      questionParts.push(`舌色的结果是${analysisResults.value.tongueColor}`);
    }
    if (showCoatingColor.value && analysisResults.value.coatingColor) {
      questionParts.push(`苔色的结果是${analysisResults.value.coatingColor}`);
    }
    if (showCrack.value && analysisResults.value.crack) {
      questionParts.push(`裂纹的结果是${analysisResults.value.crack}`);
    }
    if (showToothMark.value && analysisResults.value.toothMark) {
      questionParts.push(`齿痕的结果是${analysisResults.value.toothMark}`);
    }
    const question = `${questionParts.join('，')}，请给我分析我的健康状况，只需要给出健康状况的结果，和简单的建议。不要跟我有互动。300个字左右。给我答案！别给我打印检索本地知识库。`;
    
    const askResponse = await askQuestion(question);
    if (askResponse.code === 1 && askResponse.data) {
      let questionSessionId = '';
      const questionIdMatch = askResponse.data.match(/请求\s*ID[：:]\s*([a-f0-9-]+)/i);
      if (questionIdMatch && questionIdMatch[1]) {
        questionSessionId = questionIdMatch[1];
        await pollForAnswer(questionSessionId);
      } else {
        ElMessage.error('无法提取有效的问题会话ID');
        diagnosing.value = false;
      }
    } else {
      ElMessage.error(askResponse.msg || '开始鉴定失败');
      diagnosing.value = false;
    }
  } catch (error) {
    ElMessage.error('开始鉴定失败，请重试');
    diagnosing.value = false;
  }
};

const simulateTypingEffect = async (fullText: string) => {
  isTyping.value = true;
  typingDiagnosisResult.value = '';
  diagnosisResult.value = '';
  const allChars = fullText.split('');
  for (let i = 0; i < allChars.length; i++) {
    typingDiagnosisResult.value += allChars[i];
    diagnosisResult.value = typingDiagnosisResult.value;
    await new Promise(resolve => setTimeout(resolve, 30));
  }
  isTyping.value = false;
};

const pollForAnswer = async (questionSessionId: string) => {
  try {
    const response = await getAnswerResult(questionSessionId);
    if (response.code === 1 && response.data) {
      const processedText = processMarkdown(response.data);
      await simulateTypingEffect(processedText);
      diagnosing.value = false;
      return;
    }
    setTimeout(() => pollForAnswer(questionSessionId), 1000);
  } catch (error) {
    setTimeout(() => pollForAnswer(questionSessionId), 2000);
  }
};

const processMarkdown = (text: string): string => {
  const cleanText = text.replace('TERMINATE', '');
  const processedText = cleanText.replace(/\\n/g, '\n');
  return marked.parse(processedText) as string;
};

const resetAnalysis = () => {
  analysisResults.value = {
    tongueColor: '',
    coatingColor: '',
    crack: '',
    toothMark: ''
  };
  diagnosisResult.value = '';
  typingDiagnosisResult.value = '';
  
  analyzing.value = false;
  analyzingTongueColor.value = false;
  analyzingCoatingColor.value = false;
  analyzingCrack.value = false;
  analyzingToothMark.value = false;
  diagnosing.value = false;
  pollingTongueResult.value = false;
  isTyping.value = false;
  
  showTongueColor.value = true;
  showCoatingColor.value = true;
  showCrack.value = true;
  showToothMark.value = true;
};

// 返回
const goBack = () => {
  router.push('/health');
};
</script>

<style scoped>
.tongue-detection-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  color: #333;
  min-height: 100vh;
  background: linear-gradient(to bottom, #f5f9ff 0%, #ffffff 100%);
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  animation: fadeInDown 0.8s ease;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #3452ff;
  margin-bottom: 8px;
  background: linear-gradient(90deg, #3452ff, #5e7cff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 2px 10px rgba(52, 82, 255, 0.1);
}

.page-subtitle {
  font-size: 1.1rem;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

.detection-container {
  display: flex;
  gap: 40px;
  position: relative;
  z-index: 1;
}

.detection-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 30px;
  animation: fadeInLeft 0.8s ease;
}

.upload-area-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.upload-area {
  width: 100%;
  max-width: 420px;
  margin: 0 auto;
}

.image-preview, 
.upload-placeholder {
  width: 100%;
  aspect-ratio: 4/3;
  border-radius: 16px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  box-shadow: 0 10px 25px rgba(52, 82, 255, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.upload-placeholder {
  background-color: #f0f4ff;
  border: 2px dashed #bdc8ff;
  cursor: pointer;
}

.upload-placeholder:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(52, 82, 255, 0.12);
  border-color: #3452ff;
}

.image-preview {
  background-color: #f0f4ff;
  border: none;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.preview-overlay {
  position: absolute;
  top: 10px;
  right: 10px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-preview:hover .preview-overlay {
  opacity: 1;
}

.remove-btn {
  background-color: rgba(255, 255, 255, 0.9);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.upload-icon-container {
  width: 80px;
  height: 80px;
  background-color: rgba(52, 82, 255, 0.1);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.upload-placeholder:hover .upload-icon-container {
  background-color: rgba(52, 82, 255, 0.2);
  transform: scale(1.05);
}

.upload-icon {
  font-size: 32px;
  color: #3452ff;
}

.upload-text {
  font-size: 1.1rem;
  font-weight: 600;
  color: #3452ff;
  margin-bottom: 8px;
}

.upload-desc {
  font-size: 0.9rem;
  color: #6b7280;
}

.file-input {
  display: none;
}

.upload-button {
  min-width: 160px;
  height: 44px;
  font-size: 1rem;
  border-radius: 22px;
  font-weight: 600;
  background: linear-gradient(90deg, #3452ff, #5e7cff);
  border: none;
  box-shadow: 0 10px 20px rgba(52, 82, 255, 0.15);
  transition: all 0.3s ease;
}

.upload-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 25px rgba(52, 82, 255, 0.25);
}

.upload-button:active {
  transform: translateY(0);
}

.upload-button .el-icon {
  margin-right: 8px;
}

.analysis-panel {
  background-color: white;
  border-radius: 20px;
  padding: 25px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  animation: fadeIn 0.8s ease;
}

.panel-header {
  margin-bottom: 20px;
}

.panel-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #3452ff;
  margin-bottom: 5px;
}

.panel-subtitle {
  font-size: 0.9rem;
  color: #6b7280;
}

.analysis-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 25px;
  justify-items: center;
  align-items: stretch;
}

.feature-button {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
  width: 100%;
  text-align: center;
}

.feature-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.05);
}

.feature-button .el-icon {
  margin-right: 8px;
  font-size: 18px;
  flex-shrink: 0;
}

.feature-button span {
  white-space: nowrap;
}

.diagnosis-button {
  width: 100%;
  height: 48px;
  font-size: 1rem;
  border-radius: 12px;
  font-weight: 600;
  background: linear-gradient(90deg, #3452ff, #5e7cff);
  border: none;
  box-shadow: 0 10px 20px rgba(52, 82, 255, 0.15);
  transition: all 0.3s ease;
  margin-top: 10px;
}

.diagnosis-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 15px 25px rgba(52, 82, 255, 0.25);
}

.diagnosis-button:disabled {
  background: linear-gradient(90deg, #b0b9e3, #c9cfed);
  box-shadow: none;
  cursor: not-allowed;
}

.diagnosis-button .el-icon {
  margin-right: 8px;
}

.detection-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 25px;
  animation: fadeInRight 0.8s ease;
}

.doctor-container {
  position: relative;
}

.doctor-image {
  width: 100%;
  aspect-ratio: 16/9;
  max-height: 240px;
  border-radius: 20px;
  overflow: hidden;
  background-color: #f0f4ff;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
}

.doctor-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.doctor-placeholder {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f0f4ff 0%, #e6eeff 100%);
}

.doctor-icon {
  font-size: 64px;
  color: #a0aec0;
  margin-bottom: 10px;
}

.doctor-status {
  position: absolute;
  bottom: 15px;
  left: 15px;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 8px 15px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: #3452ff;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  animation: pulse 2s infinite;
}

.status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #4caf50;
  animation: blink 1.5s infinite;
}

.results-area {
  background-color: white;
  border-radius: 20px;
  padding: 25px;
  min-height: 280px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  position: relative;
}

.result-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 25px;
}

.empty-icon {
  font-size: 48px;
  color: #e0e7ff;
  margin-bottom: 15px;
}

.empty-text {
  color: #a0aec0;
  font-size: 1rem;
}

.result-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.result-item {
  background-color: #f8faff;
  border-radius: 12px;
  padding: 15px;
  transition: all 0.3s ease;
}

.result-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.03);
}

.result-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.result-header .el-icon {
  font-size: 18px;
  color: #3452ff;
  margin-right: 8px;
}

.result-label {
  font-weight: 600;
  color: #3452ff;
  font-size: 1rem;
}

.result-content {
  padding-left: 26px;
}

.result-value {
  color: #374151;
  font-size: 1rem;
}

.diagnosis-result {
  background: linear-gradient(to right, #f0f4ff, #e6eeff);
  border-radius: 12px;
  padding: 20px;
  margin-top: auto;
  border-left: 4px solid #3452ff;
}

.diagnosis-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.diagnosis-header .el-icon {
  font-size: 20px;
  color: #3452ff;
  margin-right: 10px;
}

.diagnosis-title {
  font-size: 1.1rem;
  color: #3452ff;
  font-weight: 600;
  margin: 0;
}

.diagnosis-content {
  white-space: pre-line;
  line-height: 1.6;
  color: #4b5563;
  padding-left: 30px;
}

.pre-content {
  white-space: pre-wrap;  
  font-family: inherit;   
  margin: 0;
  padding: 0;
  background: transparent;
  border: none;
  overflow-wrap: break-word;
}

.diagnosis-content :deep(h3) {
  font-size: 1.2rem;
  font-weight: 600;
  color: #3452ff;
  margin: 15px 0 10px 0;
}

.diagnosis-content :deep(ul), 
.diagnosis-content :deep(ol) {
  padding-left: 20px;
  margin: 8px 0;
}

.diagnosis-content :deep(li) {
  margin-bottom: 5px;
}

.diagnosis-content :deep(p) {
  margin: 10px 0;
}

.diagnosis-content :deep(strong) {
  font-weight: 600;
  color: #374151;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.action-buttons .el-button {
  min-width: 110px;
  height: 40px;
  border-radius: 20px;
}

.action-buttons .el-button .el-icon {
  margin-right: 5px;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes fadeInDown {
  from { 
    opacity: 0; 
    transform: translateY(-20px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

@keyframes fadeInLeft {
  from { 
    opacity: 0; 
    transform: translateX(-20px); 
  }
  to { 
    opacity: 1; 
    transform: translateX(0); 
  }
}

@keyframes fadeInRight {
  from { 
    opacity: 0; 
    transform: translateX(20px); 
  }
  to { 
    opacity: 1; 
    transform: translateX(0); 
  }
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(52, 82, 255, 0.2); }
  70% { box-shadow: 0 0 0 10px rgba(52, 82, 255, 0); }
  100% { box-shadow: 0 0 0 0 rgba(52, 82, 255, 0); }
}

@keyframes blink {
  0% { opacity: 0.4; }
  50% { opacity: 1; }
  100% { opacity: 0.4; }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

@media (max-width: 1080px) {
  .detection-container {
    gap: 30px;
  }
}

@media (max-width: 992px) {
  .detection-container {
    flex-direction: column;
  }
  
  .detection-left, 
  .detection-right {
    width: 100%;
  }
  
  .upload-area {
    max-width: 100%;
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .analysis-buttons {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 1.8rem;
  }
  
  .tongue-detection-page {
    padding: 30px 15px;
  }
  
  .upload-area-container {
    gap: 15px;
  }
  
  .panel-title {
    font-size: 1.2rem;
  }
  
  .action-buttons {
    justify-content: center;
  }
}

.typing-content {
  position: relative;
}

.typing-cursor {
  display: inline-block;
  width: 2px;
  height: 18px;
  background-color: #3452ff;
  margin-left: 2px;
  vertical-align: middle;
  animation: cursor-blink 0.8s infinite;
}

@keyframes cursor-blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}
</style> 