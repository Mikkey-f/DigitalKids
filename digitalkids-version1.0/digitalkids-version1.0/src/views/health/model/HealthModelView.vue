<template>
  <div class="health-model-view">
    <h1 class="text-2xl font-bold mb-4">3D数字儿童展示</h1>
    <p class="text-gray-600 mb-6">通过点击3D模型的不同部位，可以查看孩子身体各部位的健康状况信息</p>
    
    <div class="baby-selection mb-4">
      <h3 class="text-lg font-medium mb-2">选择宝宝</h3>
      <div class="flex space-x-4">
        <div 
          v-for="baby in kidsList" 
          :key="baby.id"
          class="baby-option cursor-pointer p-3 rounded-lg border transition-all duration-200"
          :class="{ 'bg-blue-50 border-blue-300': baby.isSelected, 'border-gray-200 hover:border-blue-200': !baby.isSelected }"
          @click="selectKid(baby)"
        >
          <img
            :src="baby.avatar || '/avatar-default.png'"
            :alt="baby.nickname"
            class="baby-avatar w-12 h-12 object-cover rounded-full mb-2"
          />
          <div class="text-center">
            <div class="baby-name font-medium">{{ baby.nickname }}</div>
            <div class="baby-age text-sm text-gray-500">{{ baby.old || calculateAge(baby.birthday) }}岁</div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="view-tabs mb-6">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="3D身体模型" name="model">
          <div class="body-model-container">
            <div class="model-viewer">
              <div class="model-canvas" ref="modelCanvas">
                <div v-if="modelLoading" class="model-loading">
                  <div class="loading-spinner"></div>
                  <p class="loading-text">模型加载中，请稍候...</p>
                </div>
                
                <div v-if="modelLoadError" class="model-error">
                  <el-icon class="error-icon"><WarningFilled /></el-icon>
                  <p class="error-text">模型加载失败</p>
                  <el-button size="small" type="primary" @click="retryLoadModel">重试</el-button>
                </div>
              </div>
              <div v-if="zoomFeedback" class="zoom-feedback">
                {{ zoomFeedback }}
              </div>
              <div class="model-controls">
                <el-button-group>
                  <el-button @click="rotateModel('left')">
                    <el-icon><ArrowLeft /></el-icon>
                  </el-button>
                  <el-button @click="rotateModel('right')">
                    <el-icon><ArrowRight /></el-icon>
                  </el-button>
                  <el-button @click="zoomModel('in')">
                    <el-icon><ZoomIn /></el-icon>
                  </el-button>
                  <el-button @click="zoomModel('out')">
                    <el-icon><ZoomOut /></el-icon>
                  </el-button>
                  <el-button @click="centerModel()">
                    <el-icon><Position /></el-icon>
                  </el-button>
                </el-button-group>
              </div>
              </div>
            </div>
            
          <div class="body-parts-forms mt-8">
            <h3 class="text-lg font-semibold mb-4">身体部位数据管理</h3>
            
            <div class="form-section-group mb-8">
              <h3 class="text-base font-semibold mb-4 pb-2 border-b border-gray-200">头部和躯干</h3>
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-blue-500 rounded-full"></span>
                    头部数据
                  </h4>
                  <el-form ref="headFormRef" :model="headForm" label-position="top" :rules="formRules.headForm">
                    <el-form-item label="头痛频率" prop="headacheFrequency">
                      <el-input v-model="headForm.headacheFrequency" placeholder="例如：每周1-2次"></el-input>
                    </el-form-item>
                    <el-form-item label="眩晕情况" prop="dizziness">
                      <el-input v-model="headForm.dizziness" placeholder="例如：偶尔、无"></el-input>
                    </el-form-item>
                    <el-form-item label="外伤史" prop="traumaHistory">
                      <el-input v-model="headForm.traumaHistory" placeholder="例如：无、轻微碰撞"></el-input>
                    </el-form-item>
                    <el-form-item label="认知测试结果" prop="cognitiveTestResult">
                      <el-input v-model="headForm.cognitiveTestResult" placeholder="例如：正常、良好"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-green-500 rounded-full"></span>
                    躯干数据
                  </h4>
                  <el-form ref="bodyFormRef" :model="bodyForm" label-position="top" :rules="formRules.bodyForm">
                    <el-form-item label="脊柱侧弯程度" prop="scoliosisDegree">
                      <el-input v-model="bodyForm.scoliosisDegree" placeholder="例如：5°"></el-input>
                    </el-form-item>
                    <el-form-item label="核心肌力" prop="coreStrength">
                      <el-input v-model="bodyForm.coreStrength" placeholder="例如：3级"></el-input>
                    </el-form-item>
                    <el-form-item label="体脂百分比" prop="bodyFatPercentage">
                      <el-input v-model="bodyForm.bodyFatPercentage" placeholder="例如：22%"></el-input>
                    </el-form-item>
                    <el-form-item label="柔韧性" prop="flexibility">
                      <el-input v-model="bodyForm.flexibility" placeholder="例如：前屈+5cm"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
              
            <div class="form-section-group mb-8">
              <h3 class="text-base font-semibold mb-4 pb-2 border-b border-gray-200">肩部</h3>
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-blue-500 rounded-full"></span>
                    左肩数据
                  </h4>
                  <el-form ref="leftShoulderFormRef" :model="leftShoulderForm" label-position="top">
                    <el-form-item label="活动范围">
                      <el-input v-model="leftShoulderForm.activityRange" placeholder="例如：受限（外展90°）"></el-input>
                    </el-form-item>
                    <el-form-item label="疼痛指数">
                      <el-input v-model="leftShoulderForm.painIndex" placeholder="例如：2级"></el-input>
                    </el-form-item>
                    <el-form-item label="稳定性">
                      <el-input v-model="leftShoulderForm.stability" placeholder="例如：轻度松弛"></el-input>
                    </el-form-item>
                    <el-form-item label="肌力">
                      <el-input v-model="leftShoulderForm.muscleStrength" placeholder="例如：4级"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-green-500 rounded-full"></span>
                    右肩数据
                  </h4>
                  <el-form ref="rightShoulderFormRef" :model="rightShoulderForm" label-position="top">
                    <el-form-item label="活动范围">
                      <el-input v-model="rightShoulderForm.activityRange" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="疼痛指数">
                      <el-input v-model="rightShoulderForm.painIndex" placeholder="例如：1级（轻微）"></el-input>
                    </el-form-item>
                    <el-form-item label="稳定性">
                      <el-input v-model="rightShoulderForm.stability" placeholder="例如：良好"></el-input>
                    </el-form-item>
                    <el-form-item label="肌力">
                      <el-input v-model="rightShoulderForm.muscleStrength" placeholder="例如：5级"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
              
            <div class="form-section-group mb-8">
              <h3 class="text-base font-semibold mb-4 pb-2 border-b border-gray-200">上肢</h3>
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-blue-500 rounded-full"></span>
                    左臂数据
                  </h4>
                  <el-form ref="leftArmFormRef" :model="leftArmForm" label-position="top">
                    <el-form-item label="握力">
                      <el-input v-model="leftArmForm.gripStrength" placeholder="例如：40kg"></el-input>
                    </el-form-item>
                    <el-form-item label="肘关节活动度">
                      <el-input v-model="leftArmForm.elbowActivity" placeholder="例如：0-135°"></el-input>
                    </el-form-item>
                    <el-form-item label="Tinel征">
                      <el-input v-model="leftArmForm.tinelSign" placeholder="例如：阴性"></el-input>
                    </el-form-item>
                    <el-form-item label="周径差异">
                      <el-input v-model="leftArmForm.circumferenceDifference" placeholder="例如：较右侧细1cm"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-green-500 rounded-full"></span>
                    右臂数据
                  </h4>
                  <el-form ref="rightArmFormRef" :model="rightArmForm" label-position="top">
                    <el-form-item label="握力">
                      <el-input v-model="rightArmForm.gripStrength" placeholder="例如：45kg"></el-input>
                    </el-form-item>
                    <el-form-item label="肘关节活动度">
                      <el-input v-model="rightArmForm.elbowActivity" placeholder="例如：0-140°"></el-input>
                    </el-form-item>
                    <el-form-item label="Tinel征">
                      <el-input v-model="rightArmForm.tinelSign" placeholder="例如：阴性"></el-input>
                    </el-form-item>
                    <el-form-item label="周径差异">
                      <el-input v-model="rightArmForm.circumferenceDifference" placeholder="例如：较左侧粗1cm"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
              
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-purple-500 rounded-full"></span>
                    左手数据
                  </h4>
                  <el-form ref="leftHandFormRef" :model="leftHandForm" label-position="top">
                    <el-form-item label="手指活动度">
                      <el-input v-model="leftHandForm.fingerMobility" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="精细动作">
                      <el-input v-model="leftHandForm.fineMotorSkills" placeholder="例如：需改进"></el-input>
                    </el-form-item>
                    <el-form-item label="手腕灵活性">
                      <el-input v-model="leftHandForm.wristFlexibility" placeholder="例如：轻度受限"></el-input>
                    </el-form-item>
                    <el-form-item label="指甲状态">
                      <el-input v-model="leftHandForm.nailCondition" placeholder="例如：正常"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-yellow-500 rounded-full"></span>
                    右手数据
                  </h4>
                  <el-form ref="rightHandFormRef" :model="rightHandForm" label-position="top">
                    <el-form-item label="手指活动度">
                      <el-input v-model="rightHandForm.fingerMobility" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="精细动作">
                      <el-input v-model="rightHandForm.fineMotorSkills" placeholder="例如：良好"></el-input>
                    </el-form-item>
                    <el-form-item label="手腕灵活性">
                      <el-input v-model="rightHandForm.wristFlexibility" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="指甲状态">
                      <el-input v-model="rightHandForm.nailCondition" placeholder="例如：正常"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
              
            <div class="form-section-group mb-8">
              <h3 class="text-base font-semibold mb-4 pb-2 border-b border-gray-200">下肢</h3>
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-blue-500 rounded-full"></span>
                    左腿数据
                  </h4>
                  <el-form ref="leftLegFormRef" :model="leftLegForm" label-position="top">
                    <el-form-item label="膝关节稳定性">
                      <el-input v-model="leftLegForm.kneeStability" placeholder="例如：轻度不稳"></el-input>
                    </el-form-item>
                    <el-form-item label="髋关节活动度">
                      <el-input v-model="leftLegForm.hipMobility" placeholder="例如：正常范围"></el-input>
                    </el-form-item>
                    <el-form-item label="腿部肌力">
                      <el-input v-model="leftLegForm.legStrength" placeholder="例如：4级"></el-input>
                    </el-form-item>
                    <el-form-item label="Q角">
                      <el-input v-model="leftLegForm.qAngle" placeholder="例如：12°"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-base font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-green-500 rounded-full"></span>
                    右腿数据
                  </h4>
                  <el-form ref="rightLegFormRef" :model="rightLegForm" label-position="top">
                    <el-form-item label="膝关节稳定性">
                      <el-input v-model="rightLegForm.kneeStability" placeholder="例如：良好"></el-input>
                    </el-form-item>
                    <el-form-item label="髋关节活动度">
                      <el-input v-model="rightLegForm.hipMobility" placeholder="例如：正常范围"></el-input>
                    </el-form-item>
                    <el-form-item label="腿部肌力">
                      <el-input v-model="rightLegForm.legStrength" placeholder="例如：4级"></el-input>
                    </el-form-item>
                    <el-form-item label="Q角">
                      <el-input v-model="rightLegForm.qAngle" placeholder="例如：15°"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
              
              <div class="form-group grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                <div class="form-section">
                  <h4 class="text-md font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-purple-500 rounded-full"></span>
                    左脚数据
                  </h4>
                  <el-form ref="leftFootFormRef" :model="leftFootForm" label-position="top">
                    <el-form-item label="足弓">
                      <el-input v-model="leftFootForm.arch" placeholder="例如：平足（1级）"></el-input>
                    </el-form-item>
                    <el-form-item label="踝关节活动度">
                      <el-input v-model="leftFootForm.ankleMobility" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="足部旋转">
                      <el-input v-model="leftFootForm.footRotation" placeholder="例如：外翻5°"></el-input>
                    </el-form-item>
                    <el-form-item label="平衡能力">
                      <el-input v-model="leftFootForm.balance" placeholder="例如：单腿站立8秒"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
                
                <div class="form-section">
                  <h4 class="text-md font-medium mb-4 flex items-center">
                    <span class="mr-2 w-3 h-3 inline-block bg-yellow-500 rounded-full"></span>
                    右脚数据
                  </h4>
                  <el-form ref="rightFootFormRef" :model="rightFootForm" label-position="top">
                    <el-form-item label="足弓">
                      <el-input v-model="rightFootForm.arch" placeholder="例如：正常"></el-input>
                    </el-form-item>
                    <el-form-item label="踝关节活动度">
                      <el-input v-model="rightFootForm.ankleMobility" placeholder="例如：轻度受限"></el-input>
                    </el-form-item>
                    <el-form-item label="足部旋转">
                      <el-input v-model="rightFootForm.footRotation" placeholder="例如：内翻3°"></el-input>
                    </el-form-item>
                    <el-form-item label="平衡能力">
                      <el-input v-model="rightFootForm.balance" placeholder="例如：单腿站立10秒"></el-input>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
            
            <div class="form-actions mt-6 flex justify-end">
              <el-button type="primary" @click="submitHealthData" :loading="submittingForm" :disabled="submittingForm">
                {{ submitButtonText }}
              </el-button>
            </div>
          </div>
          
          <div v-if="showHealthInfo" class="health-info-panel">
            <div class="info-header">
              <h3 class="info-title">{{ selectedPartInfo.name }} 详细数据</h3>
              <el-button @click="closeHealthInfo" circle>
                <el-icon><Close /></el-icon>
              </el-button>
            </div>
            
            <div class="info-content">
              <div v-for="(value, key) in selectedPartInfo.data" :key="key" class="info-item">
                <div class="info-label">{{ key }}</div>
                <div class="info-value">{{ value }}</div>
              </div>
            </div>
            
            <div class="info-footer" v-if="selectedPartInfo.recommendation">
              <div class="recommendation-title">健康建议</div>
              <div class="recommendation-content">{{ selectedPartInfo.recommendation }}</div>
            </div>
          </div>

          <div v-if="showModelInfo" class="health-info-panel model-info-panel" :style="{
            position: 'fixed',
            left: `${clickPosition.x + 20}px`,
            top: `${clickPosition.y - 200}px`,
            zIndex: 9999,
            maxWidth: '350px',
            maxHeight: '500px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            overflow: 'auto'
          }">
            <div class="info-header">
              <h3 class="info-title">{{ getPartName(currentModelPart) }} 详细数据</h3>
              <el-button @click="closeModelInfo" circle size="small" type="text">
                <el-icon><Close /></el-icon>
              </el-button>
            </div>
            
            <div class="info-content">
              <div v-if="Object.keys(modelPartInfo[currentModelPart]?.data || {}).length === 0" class="empty-data">
                <el-empty description="暂无数据" :image-size="80">
                  <template #description>
                    <p class="text-gray-500">该部位暂无数据，可在表单中添加</p>
                  </template>
                </el-empty>
              </div>
              <div v-else class="data-grid">
                <div v-for="(value, key) in modelPartInfo[currentModelPart]?.data" :key="key" class="info-item">
                  <div class="info-label">{{ key }}</div>
                  <div class="info-value">{{ value }}</div>
                </div>
              </div>
            </div>

            <div class="info-footer">
              <div v-if="modelPartInfo[currentModelPart]?.recommendation" class="mb-3">
                <div class="recommendation-title">健康建议</div>
                <div class="recommendation-content">
                  {{ modelPartInfo[currentModelPart]?.recommendation }}
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div v-if="showHoverCard" class="hover-card" :style="{
      position: 'fixed',
      left: `${hoverPosition.x + 15}px`,
      top: `${hoverPosition.y - 120}px`,
      zIndex: 999
    }">
      <div class="hover-card-content">
        <div class="hover-card-title">{{ hoverPartInfo.name }}</div>
        <div v-if="Object.keys(hoverPartInfo.data).length > 0" class="hover-card-data">
          <div v-for="(value, key) in hoverPartInfo.data" :key="key" class="hover-data-item">
            <span class="hover-data-label">{{ key }}:</span>
            <span class="hover-data-value">{{ value }}</span>
          </div>
          <div v-if="hoverPartInfo.recommendation" class="hover-recommendation">
            <div class="hover-section-title">健康建议</div>
            <div class="hover-recommendation-text">{{ hoverPartInfo.recommendation }}</div>
          </div>
        </div>
        <div v-else class="hover-card-empty">点击查看详情</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, onBeforeUnmount, reactive, watch, nextTick } from 'vue'
import { 
  ArrowLeft, ArrowRight, ZoomIn, ZoomOut, Close, Position,
  View, MagicStick, Compass, Notification, User, Link, ScaleToOriginal, Histogram, WarningFilled
} from '@element-plus/icons-vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js'
import axios from 'axios'
import { ElMessage, ElMessageBox, FormRules } from 'element-plus'

const activeTab = ref('model')
const kidsList = ref<any[]>([])
const selectedKidId = ref(0)

const submittedParts = reactive({
  head: false,
  body: false,
  leftShoulder: false,
  rightShoulder: false,
  leftArm: false,
  rightArm: false,
  leftHand: false,
  rightHand: false,
  leftLeg: false,
  rightLeg: false,
  leftFoot: false,
  rightFoot: false
})

const showAiResponse = ref(false)
const aiResponseContent = ref('')
const loadingAiResponse = ref(false)
const streamComplete = ref(false)
const aiSessionId = ref('')

const submittingForm = ref(false)

const submitButtonText = computed(() => {
  if (submittingForm.value) {
    return '正在提交...'
  }
  
  const allSubmitted = Object.values(submittedParts).every(status => status)
  return allSubmitted ? '更新身体健康数据' : '提交身体健康数据'
})

const zoomFeedback = ref('')
const zoomLimits = ref({
  min: 0.9,
  max: 2.4
})

const headFormRef = ref(null)
const bodyFormRef = ref(null)
const leftShoulderFormRef = ref(null)
const rightShoulderFormRef = ref(null)
const leftArmFormRef = ref(null)
const rightArmFormRef = ref(null)
const leftHandFormRef = ref(null)
const rightHandFormRef = ref(null)
const leftLegFormRef = ref(null)
const rightLegFormRef = ref(null)
const leftFootFormRef = ref(null)
const rightFootFormRef = ref(null)

const formRules = {
  headForm: {
    headacheFrequency: [
      { required: true, message: '请输入头痛频率', trigger: 'blur' }
    ],
    dizziness: [
      { required: true, message: '请输入眩晕情况', trigger: 'blur' }
    ]
  },
  bodyForm: {
    scoliosisDegree: [
      { required: true, message: '请输入脊柱侧弯程度', trigger: 'blur' }
    ],
    coreStrength: [
      { required: true, message: '请输入核心肌力', trigger: 'blur' }
    ]
  }
} as Record<string, FormRules>

const headForm = reactive({
  kidId: 0,
  headacheFrequency: '每周1-2次',
  dizziness: '偶尔',
  traumaHistory: '无',
  cognitiveTestResult: '正常'
})

const bodyForm = reactive({
  kidId: 0,
  scoliosisDegree: '5°',
  coreStrength: '3级',
  bodyFatPercentage: '22%',
  flexibility: '前屈+5cm'
})

const leftShoulderForm = reactive({
  kidId: 0,
  activityRange: '受限（外展90°）',
  painIndex: '2级',
  stability: '轻度松弛',
  muscleStrength: '4级'
})

const rightShoulderForm = reactive({
  kidId: 0,
  activityRange: '正常',
  painIndex: '1级（轻微）',
  stability: '良好',
  muscleStrength: '5级'
})

const leftArmForm = reactive({
  kidId: 0,
  gripStrength: '40kg',
  elbowActivity: '0-135°',
  tinelSign: '阴性',
  circumferenceDifference: '较右侧细1cm'
})

const rightArmForm = reactive({
  kidId: 0,
  gripStrength: '45kg',
  elbowActivity: '0-140°',
  tinelSign: '阴性',
  circumferenceDifference: '较左侧粗1cm'
})

const leftHandForm = reactive({
  kidId: 0,
  fingerMobility: '正常',
  fineMotorSkills: '需改进',
  wristFlexibility: '轻度受限',
  nailCondition: '正常'
})

const rightHandForm = reactive({
  kidId: 0,
  fingerMobility: '正常',
  fineMotorSkills: '良好',
  wristFlexibility: '正常',
  nailCondition: '正常'
})

const leftLegForm = reactive({
  kidId: 0,
  kneeStability: '轻度不稳',
  hipMobility: '正常范围',
  legStrength: '4级',
  qAngle: '12°'
})

const rightLegForm = reactive({
  kidId: 0,
  kneeStability: '良好',
  hipMobility: '正常范围',
  legStrength: '4级',
  qAngle: '15°'
})

const leftFootForm = reactive({
  kidId: 0,
  arch: '平足（1级）',
  ankleMobility: '正常',
  footRotation: '外翻5°',
  balance: '单腿站立8秒'
})

const rightFootForm = reactive({
  kidId: 0,
  arch: '正常',
  ankleMobility: '轻度受限',
  footRotation: '内翻3°',
  balance: '单腿站立10秒'
})

const submitHealthData = async () => {
  if (!selectedKidId.value) {
    ElMessageBox.confirm('您还没有选择宝宝，请先选择一个宝宝或者创建新宝宝', '提示', {
      confirmButtonText: '选择宝宝',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      ElMessage.info('请从上方选择一个宝宝')
    }).catch(() => {
      ElMessage.info('已取消提交')
    })
    return
  }
  
  try {
    submittingForm.value = true
    updateFormKidIds()
    const errors: string[] = []
    const submitPromises = [
      submitPartData('/api/threeJs/add/head', {
        kidId: headForm.kidId,
        headacheFrequency: headForm.headacheFrequency,
        dizziness: headForm.dizziness,
        traumaHistory: headForm.traumaHistory,
        cognitiveTestResult: headForm.cognitiveTestResult
      })
        .then(() => submittedParts.head = true)
        .catch(error => errors.push(`头部: ${error.message}`)),
      submitPartData('/api/threeJs/add/body', {
        kidId: bodyForm.kidId,
        scoliosisDegree: bodyForm.scoliosisDegree,
        coreStrength: bodyForm.coreStrength,
        bodyFatPercentage: bodyForm.bodyFatPercentage,
        flexibility: bodyForm.flexibility
      })
        .then(() => submittedParts.body = true)
        .catch(error => errors.push(`躯干: ${error.message}`)),
      submitPartData('/api/threeJs/add/leftShoulder', {
        kidId: leftShoulderForm.kidId,
        rangeOfMotion: leftShoulderForm.activityRange,
        painIndex: leftShoulderForm.painIndex,
        stability: leftShoulderForm.stability,
        muscleStrength: leftShoulderForm.muscleStrength
      })
        .then(() => submittedParts.leftShoulder = true)
        .catch(error => errors.push(`左肩: ${error.message}`)),
      submitPartData('/api/threeJs/add/rightShoulder', {
        kidId: rightShoulderForm.kidId,
        rangeOfMotion: rightShoulderForm.activityRange,
        painIndex: rightShoulderForm.painIndex,
        stability: rightShoulderForm.stability,
        muscleStrength: rightShoulderForm.muscleStrength
      })
        .then(() => submittedParts.rightShoulder = true)
        .catch(error => errors.push(`右肩: ${error.message}`)),
      submitPartData('/api/threeJs/add/leftArm', {
        kidId: leftArmForm.kidId,
        gripStrength: leftArmForm.gripStrength,
        elbowRangeOfMotion: leftArmForm.elbowActivity,
        tinelSign: leftArmForm.tinelSign,
        circumferenceDifference: leftArmForm.circumferenceDifference
      })
        .then(() => submittedParts.leftArm = true)
        .catch(error => errors.push(`左臂: ${error.message}`)),
      submitPartData('/api/threeJs/add/rightArm', {
        kidId: rightArmForm.kidId,
        gripStrength: rightArmForm.gripStrength,
        elbowRangeOfMotion: rightArmForm.elbowActivity,
        tinelSign: rightArmForm.tinelSign,
        circumferenceDifference: rightArmForm.circumferenceDifference
      })
        .then(() => submittedParts.rightArm = true)
        .catch(error => errors.push(`右臂: ${error.message}`)),
      submitPartData('/api/threeJs/add/leftHand', {
        kidId: leftHandForm.kidId,
        flexibility: leftHandForm.fingerMobility,
        jointSwelling: leftHandForm.wristFlexibility,
        twoPointDiscrimination: leftHandForm.fineMotorSkills,
        nailBedCirculation: leftHandForm.nailCondition
      })
        .then(() => submittedParts.leftHand = true)
        .catch(error => errors.push(`左手: ${error.message}`)),
      submitPartData('/api/threeJs/add/rightHand', {
        kidId: rightHandForm.kidId,
        flexibility: rightHandForm.fingerMobility,
        jointSwelling: rightHandForm.wristFlexibility,
        twoPointDiscrimination: rightHandForm.fineMotorSkills,
        nailBedCirculation: rightHandForm.nailCondition
      })
        .then(() => submittedParts.rightHand = true)
        .catch(error => errors.push(`右手: ${error.message}`)),
      submitPartData('/api/threeJs/add/leftLeg', {
        kidId: leftLegForm.kidId,
        lengthDifference: leftLegForm.kneeStability,
        muscleStrength: leftLegForm.legStrength,
        kneeReflex: leftLegForm.hipMobility,
        swellingDegree: leftLegForm.qAngle
      })
        .then(() => submittedParts.leftLeg = true)
        .catch(error => errors.push(`左腿: ${error.message}`)),
      submitPartData('/api/threeJs/add/rightLeg', {
        kidId: rightLegForm.kidId,
        lengthDifference: rightLegForm.kneeStability,
        muscleStrength: rightLegForm.legStrength,
        kneeReflex: rightLegForm.hipMobility,
        swellingDegree: rightLegForm.qAngle
      })
        .then(() => submittedParts.rightLeg = true)
        .catch(error => errors.push(`右腿: ${error.message}`)),
      submitPartData('/api/threeJs/add/leftFoot', {
        kidId: leftFootForm.kidId,
        archStatus: leftFootForm.arch,
        halluxValgusDegree: leftFootForm.footRotation,
        callusStatus: leftFootForm.ankleMobility,
        gaitCycleStatus: leftFootForm.balance
      })
        .then(() => submittedParts.leftFoot = true)
        .catch(error => errors.push(`左脚: ${error.message}`)),
      submitPartData('/api/threeJs/add/rightFoot', {
        kidId: rightFootForm.kidId,
        archStatus: rightFootForm.arch,
        halluxValgusDegree: rightFootForm.footRotation,
        callusStatus: rightFootForm.ankleMobility,
        gaitCycleStatus: rightFootForm.balance
      })
        .then(() => submittedParts.rightFoot = true)
        .catch(error => errors.push(`右脚: ${error.message}`))
    ]
    await Promise.allSettled(submitPromises)
    if (errors.length === 0) {
      ElMessage.success('健康数据提交成功')
    } else {
      const successCount = submitPromises.length - errors.length
      if (successCount > 0) {
        ElMessage({
          message: `部分数据提交成功(${successCount}/${submitPromises.length})，部分提交失败`,
          type: 'warning'
        })
        console.warn('提交失败的部位:', errors)
      } else {
        ElMessage.error('所有数据提交失败，请稍后再试')
        console.error('提交错误:', errors)
      }
    }
    if (selectedKidId.value) {
      fetchHealthData(selectedKidId.value)
    }
  } catch (error) {
    console.error('提交健康数据失败:', error)
    ElMessage.error('提交健康数据失败，请稍后再试')
  } finally {
    submittingForm.value = false
  }
}
const updateFormKidIds = () => {
  headForm.kidId = selectedKidId.value
  bodyForm.kidId = selectedKidId.value
  leftShoulderForm.kidId = selectedKidId.value
  rightShoulderForm.kidId = selectedKidId.value
  leftArmForm.kidId = selectedKidId.value
  rightArmForm.kidId = selectedKidId.value
  leftHandForm.kidId = selectedKidId.value
  rightHandForm.kidId = selectedKidId.value
  leftLegForm.kidId = selectedKidId.value
  rightLegForm.kidId = selectedKidId.value
  leftFootForm.kidId = selectedKidId.value
  rightFootForm.kidId = selectedKidId.value
}
const submitPartData = async (url: string, data: any) => {
  try {
    const token = localStorage.getItem('token') || ''
    const headers = {
      'Authorization': `Bearer ${token}`
    }
    let response = await axios.post(url, data, { headers })
    if (response.data && response.data.code === 1) {
      return response.data
    } else if (response.data && response.data.msg === "信息已经添加不能再次添加") {
      const updateUrl = url.replace('/add/', '/update/')
      response = await axios.put(updateUrl, data, { headers })
      if (response.data && response.data.code === 1) {
        return response.data
      } else {
        throw new Error(`更新API返回错误: ${response.data?.msg || '未知错误'}`)
      }
    } else {
      throw new Error(`API返回错误: ${response.data?.msg || '未知错误'}`)
    }
  } catch (error) {
    console.error(`提交 ${url} 失败:`, error)
    throw error
  }
}

const modelCanvas = ref<HTMLElement | null>(null)
const growthCanvas = ref<HTMLElement | null>(null)
const selectedPart = ref('')
const showHealthInfo = ref(false)
const showModelInfo = ref(false)
const currentModelPart = ref('')
const modelPartInfo = ref<Record<string, any>>({})
let scene: THREE.Scene | null = null
let camera: THREE.PerspectiveCamera | null = null
let renderer: THREE.WebGLRenderer | null = null
let controls: OrbitControls | null = null
let model: THREE.Object3D | null = null
let animationFrameId: number | null = null
let modelCenter: THREE.Vector3 | null = null
let modelSize: THREE.Vector3 | null = null

interface PartInfo {
  name: string;
  description: string;
  icon: string;
  data: Record<string, string>;
  recommendation: string;
}

interface ModelPartInfoMap {
  [key: string]: PartInfo;
}

const defaultModelPartInfo: ModelPartInfoMap = {
  'left_hand': {
    name: '左手',
    description: '手部精细功能',
    icon: 'Hand',
    data: {
      '灵活性': '轻度受限',
      '指关节肿胀': 'PIP关节',
      '两点辨别觉': '5mm',
      '甲床循环': '稍慢'
    },
    recommendation: '建议睡前热敷后做手指体操，避免冷水刺激。'
  },
  'right_hand': {
    name: '右手',
    description: '手部精细功能',
    icon: 'Hand',
    data: {
      '灵活性': '正常',
      '指关节肿胀': '无',
      '两点辨别觉': '3mm',
      '甲床循环': '良好'
    },
    recommendation: '长时间写字时使用握笔器缓解疲劳。'
  },
  'left_foot': {
    name: '左脚',
    description: '足部生物力学',
    icon: 'Foot',
    data: {
      '足弓': '轻度扁平',
      '拇外翻': '15°',
      '胼胝体': '足跟部',
      '步态周期': '推进期缩短'
    },
    recommendation: '建议定制矫形鞋垫，每日做抓毛巾练习。'
  },
  'right_foot': {
    name: '右脚',
    description: '足部生物力学',
    icon: 'Foot',
    data: {
      '足弓': '正常',
      '拇外翻': '10°',
      '胼胝体': '无',
      '步态周期': '对称'
    },
    recommendation: '选择足弓支撑鞋，避免尖头鞋。'
  },
  'body': {
    name: '躯干',
    description: '脊柱与核心肌群',
    icon: 'Torso',
    data: {
      '脊柱侧弯': '5°',
      '核心肌力': '3级',
      '体脂率': '22%',
      '柔韧性': '前屈+5cm'
    },
    recommendation: '建议普拉提训练每周3次，坐姿保持腰部支撑。'
  },
  'left_arm': {
    name: '左臂',
    description: '上肢功能状态',
    icon: 'Arm',
    data: {
      '握力': '40kg',
      '肘活动度': '0-135°',
      'Tinel征': '阴性',
      '周长差': '较右侧细1cm'
    },
    recommendation: '建议进行对称性力量训练，提重物时双侧均衡使用。'
  },
  'right_arm': {
    name: '右臂',
    description: '上肢功能状态',
    icon: 'Arm',
    data: {
      '握力': '45kg',
      '肘活动度': '0-140°',
      'Tinel征': '阴性',
      '周长差': '无'
    },
    recommendation: '办公时每1小时做手臂伸展运动。'
  },
  'head_1': {
    name: '头部',
    description: '神经系统与感官功能',
    icon: 'Head',
    data: {
      '头痛频率': '每周1次',
      '眩晕': '无',
      '外伤史': '无',
      '认知测试': '正常'
    },
    recommendation: '建议保持7小时睡眠，午间闭目休息15分钟。'
  },
  'head_2': {
    name: '头部',
    description: '神经系统与感官功能',
    icon: 'Head',
    data: {
      '头痛频率': '每周1次',
      '眩晕': '无',
      '外伤史': '无',
      '认知测试': '正常'
    },
    recommendation: '建议保持7小时睡眠，午间闭目休息15分钟。'
  },
  'head_3': {
    name: '头部',
    description: '神经系统与感官功能',
    icon: 'Head',
    data: {
      '头痛频率': '每周1次',
      '眩晕': '无',
      '外伤史': '无',
      '认知测试': '正常'
    },
    recommendation: '建议保持7小时睡眠，午间闭目休息15分钟。'
  },
  'left_leg_1': {
    name: '左腿',
    description: '下肢肌力与关节功能',
    icon: 'Leg',
    data: {
      '长度差': '0.5cm（左短）',
      '肌力': '4+级',
      '膝反射': '+',
      '肿胀度': '轻度'
    },
    recommendation: '建议定制鞋垫矫正，避免长时间站立。'
  },
  'right_leg_1': {
    name: '右腿',
    description: '下肢肌力与关节功能',
    icon: 'Leg',
    data: {
      '长度差': '0cm',
      '肌力': '5级',
      '膝反射': '++',
      '肿胀度': '无'
    },
    recommendation: '保持每日深蹲练习，建议跑步前做好动态拉伸。'
  },
  'left_leg_2': {
    name: '左腿',
    description: '下肢肌力与关节功能',
    icon: 'Leg',
    data: {
      '长度差': '0.5cm（左短）',
      '肌力': '4+级',
      '膝反射': '+',
      '肿胀度': '轻度'
    },
    recommendation: '建议定制鞋垫矫正，避免长时间站立。'
  },
  'right_leg_2': {
    name: '右腿',
    description: '下肢肌力与关节功能',
    icon: 'Leg',
    data: {
      '长度差': '0cm',
      '肌力': '5级',
      '膝反射': '++',
      '肿胀度': '无'
    },
    recommendation: '保持每日深蹲练习，建议跑步前做好动态拉伸。'
  },
  'left_shoulder': {
     name: '左肩',
    description: '肩关节活动度与疼痛情况',
    icon: 'Shoulder',
    data: {
      '活动范围': '受限（外展90°）',
      '疼痛指数': '2级',
      '稳定性': '轻度松弛',
      '肌力': '4级'
    },
    recommendation: '建议物理治疗改善关节活动度，热敷后做爬墙训练。'
  },
  'right_shoulder': {
     name: '右肩',
    description: '肩关节活动度与疼痛情况',
    icon: 'Shoulder',
    data: {
      '活动范围': '正常',
      '疼痛指数': '1级（轻微）',
      '稳定性': '良好',
      '肌力': '5级'
    },
    recommendation: '建议每日做肩部环绕运动10分钟，避免单侧背包过重。'
  }
}
const timelineValue = ref(4) 
const growthRecords = [
  { 
    age: '1岁', 
    height: 75, 
    weight: 10, 
    head: 46, 
    chest: 48,
    heightPercentile: '65%',
    weightPercentile: '60%',
    headPercentile: '55%',
    chestPercentile: '60%'
  },
  { 
    age: '2岁', 
    height: 86, 
    weight: 12.5, 
    head: 49, 
    chest: 51,
    heightPercentile: '70%',
    weightPercentile: '65%',
    headPercentile: '60%',
    chestPercentile: '65%'
  },
  { 
    age: '3岁', 
    height: 95, 
    weight: 14.2, 
    head: 50, 
    chest: 53,
    heightPercentile: '75%',
    weightPercentile: '70%',
    headPercentile: '65%',
    chestPercentile: '70%'
  },
  { 
    age: '4岁', 
    height: 103, 
    weight: 16.5, 
    head: 51, 
    chest: 55,
    heightPercentile: '80%',
    weightPercentile: '75%',
    headPercentile: '70%',
    chestPercentile: '75%'
  },
  { 
    age: '5岁', 
    height: 110, 
    weight: 18.5, 
    head: 52, 
    chest: 57,
    heightPercentile: '80%',
    weightPercentile: '75%',
    headPercentile: '70%',
    chestPercentile: '75%'
  },
  { 
    age: '6岁', 
    height: 116, 
    weight: 21, 
    head: 53, 
    chest: 59,
    heightPercentile: '82%',
    weightPercentile: '78%',
    headPercentile: '75%',
    chestPercentile: '80%'
  },
  { 
    age: '7岁', 
    height: 122, 
    weight: 23.5, 
    head: 53.5, 
    chest: 62,
    heightPercentile: '85%',
    weightPercentile: '80%',
    headPercentile: '75%',
    chestPercentile: '82%'
  }
]
const growthMetrics = [
  { id: 'height', title: '身高', unit: 'cm', icon: 'ScaleToOriginal' },
  { id: 'weight', title: '体重', unit: 'kg', icon: 'Histogram' },
  { id: 'head', title: '头围', unit: 'cm', icon: 'User' },
  { id: 'chest', title: '胸围', unit: 'cm', icon: 'Link' }
]
const currentGrowthRecord = computed(() => {
  return growthRecords[timelineValue.value]
})
const selectedPartInfo = computed(() => {
  return { 
    name: selectedPart.value ? getPartName(selectedPart.value) : '', 
    data: {}, 
    recommendation: '' 
  }
})
const fetchKidsList = async () => {
  try {
    const userBabies = localStorage.getItem('userBabies')
    if (userBabies) {
      const babies = JSON.parse(userBabies)
      kidsList.value = babies.map((baby: any, index: number) => ({
        ...baby,
        isSelected: index === 0 
      }))
      if (kidsList.value.length > 0) {
        selectKid(kidsList.value[0])
      } else {
        ElMessage.warning('没有找到宝宝数据，请先添加宝宝')
      }
    } else {
      kidsList.value = []
      ElMessage.warning('没有找到宝宝数据，请先添加宝宝')
    }
  } catch (error) {
    console.error('获取宝宝列表失败:', error)
    ElMessage.error('获取宝宝列表失败')
  }
}
const calculateAge = (birthdayStr: string): number => {
  if (!birthdayStr) return 0
  
  try {
    const birthday = new Date(birthdayStr)
    const today = new Date()
    let age = today.getFullYear() - birthday.getFullYear()
    const monthDiff = today.getMonth() - birthday.getMonth()
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthday.getDate())) {
      age--
    }
    
    return age >= 0 ? age : 0
  } catch (error) {
    console.error('计算年龄出错:', error)
    return 0
  }
}

const selectKid = (kid: any) => {
  selectedKidId.value = kid.id
  kidsList.value = kidsList.value.map(item => ({
    ...item,
    isSelected: item.id === kid.id
  }))
  const age = kid.old || calculateAge(kid.birthday)
  let gender = 1 // 默认为男
  if (typeof kid.gender === 'number') {
    gender = kid.gender
  } else if (typeof kid.gender === 'string') {
    gender = kid.gender.toLowerCase() === 'female' || kid.gender === '2' || kid.gender === '女' ? 2 : 1
  }
  
  loadModelByAgeAndGender(age, gender)
  fetchHealthData(kid.id)
}

const loadModelByAgeAndGender = (age: number, gender: number) => {
  let modelPath = '' // 默认模型
  if (age >= 0 && age <= 3) {
    modelPath = '/3Dkid/婴儿.glb'
  } else if (age >= 4 && age <= 10) {
    modelPath = gender === 1 ? '/3Dkid/男孩.glb' : '/3Dkid/女孩.glb'
  } else {
    modelPath = gender === 1 ? '/3Dkid/男青.glb' : '/3Dkid/女青.glb'
  }

  loadModel(modelPath)
}

const fetchHealthData = async (kidId: number) => {
  try {
    const parts = [
      { name: 'head', modelKeys: ['head_1', 'head_2', 'head_3'] },
      { name: 'body', modelKeys: ['body'] },
      { name: 'leftShoulder', modelKeys: ['left_shoulder'] },
      { name: 'rightShoulder', modelKeys: ['right_shoulder'] },
      { name: 'leftArm', modelKeys: ['left_arm'] },
      { name: 'rightArm', modelKeys: ['right_arm'] },
      { name: 'leftHand', modelKeys: ['left_hand'] },
      { name: 'rightHand', modelKeys: ['right_hand'] },
      { name: 'leftLeg', modelKeys: ['left_leg_1', 'left_leg_2'] },
      { name: 'rightLeg', modelKeys: ['right_leg_1', 'right_leg_2'] },
      { name: 'leftFoot', modelKeys: ['left_foot'] },
      { name: 'rightFoot', modelKeys: ['right_foot'] }
    ];
    const token = localStorage.getItem('token') || ''
    const headers = {
      'Authorization': `Bearer ${token}`
    }
    await Promise.all(parts.map(async (part) => {
      try {
        const response = await axios.get(`/api/threeJs/get/${part.name}/${kidId}`, { headers });
        if (response.data && response.data.code === 1) {
          part.modelKeys.forEach(key => {
            const updatedInfo = { ...modelPartInfo.value };
            if (updatedInfo[key]) {
              updatedInfo[key] = {
                ...updatedInfo[key],
                data: response.data.data.data || updatedInfo[key].data,
                recommendation: response.data.data.recommendation || ""
              };
            }
            modelPartInfo.value = updatedInfo;
          });
          
          submittedParts[part.name as keyof typeof submittedParts] = true;
        }
      } catch (error) {
        console.error(`获取${part.name}数据失败:`, error);
      }
    }));
  } catch (error) {
    console.error('获取健康数据失败:', error)
  }
}

const modelLoading = ref(false)
const modelLoadError = ref(false)
let modelLoadTimeout: NodeJS.Timeout | null = null

const clickPosition = ref({ x: 0, y: 0 })
const modelInfoPosition = computed(() => {
  return {
    position: 'absolute',
    left: `${clickPosition.value.x + 20}px`,
    top: `${clickPosition.value.y - 200}px`
  }
})

const typingAiResponse = ref('')
const isTyping = ref(false)
const hoveredPart = ref('')
const showHoverCard = ref(false)
const hoverPosition = ref({ x: 0, y: 0 })
const hoverPartInfo = ref<any>({
  name: '',
  data: {},
  recommendation: ''
})
const hoverTimeout = ref<NodeJS.Timeout | null>(null)

const initModelInteraction = (modelObj: THREE.Object3D) => {
  if (!modelCanvas.value || !renderer || !camera) return
  const canvas = renderer.domElement
  // @ts-ignore
  const oldClickListener = canvas._clickListener
  // @ts-ignore
  const oldMoveListener = canvas._moveListener
  if (oldClickListener) {
    canvas.removeEventListener('click', oldClickListener)
  }
  if (oldMoveListener) {
    canvas.removeEventListener('mousemove', oldMoveListener)
  }
  
  const clickListener = (event: MouseEvent) => {
    if (isModelRotating.value) return
    const raycaster = new THREE.Raycaster()
    const mouse = new THREE.Vector2()
    const rect = renderer!.domElement.getBoundingClientRect()

    mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1
    mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1
    raycaster.setFromCamera(mouse, camera!)
    const intersects = raycaster.intersectObject(modelObj, true)

    if (intersects.length > 0) {
      const selectedObject = intersects[0].object
      clickPosition.value = { 
        x: event.clientX, 
        y: event.clientY 
      }
      let partId = selectedObject.name || ''
      if (!partId) {
        let parent = selectedObject.parent
        while (parent && !partId) {
          partId = parent.name || ''
          parent = parent.parent
        }
      }
      if (partId) {
        highlightBodyPart(selectedObject)
        showModelPart(partId)
      } else {
        console.log('未能识别点击的部位')
      }
    }
  }

  const mouseMoveListener = (event: MouseEvent) => {
    if (isModelRotating.value) return
    const raycaster = new THREE.Raycaster()
    const mouse = new THREE.Vector2()
    const rect = renderer!.domElement.getBoundingClientRect()

    mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1
    mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1
    raycaster.setFromCamera(mouse, camera!)
    const intersects = raycaster.intersectObject(modelObj, true)
    
    if (intersects.length > 0) {
      const hoveredObject = intersects[0].object
      let partId = hoveredObject.name || ''
      if (!partId) {
        let parent = hoveredObject.parent
        while (parent && !partId) {
          partId = parent.name || ''
          parent = parent.parent
        }
      }
      
      if (partId) {
        if (hoverTimeout.value) {
          clearTimeout(hoverTimeout.value)
        }
        hoverTimeout.value = setTimeout(() => {
          hoveredPart.value = partId
          hoverPosition.value = { x: event.clientX, y: event.clientY }
          showHoverCard.value = true
          fetchHoverInfo(partId)
        }, 200)
      }
    } else {
      if (hoverTimeout.value) {
        clearTimeout(hoverTimeout.value)
      }
      hoverTimeout.value = setTimeout(() => {
        showHoverCard.value = false
      }, 100)
    }
  }
  // @ts-ignore
  canvas._clickListener = clickListener
  // @ts-ignore
  canvas._moveListener = mouseMoveListener
  canvas.addEventListener('click', clickListener)
  canvas.addEventListener('mousemove', mouseMoveListener)
}

const fetchHoverInfo = async (part: string) => {
  const partName = getPartName(part)
  hoverPartInfo.value = {
    name: partName,
    data: {},
    recommendation: ''
  }
  if (modelPartInfo.value[part]) {
    hoverPartInfo.value.data = modelPartInfo.value[part].data || {}
    hoverPartInfo.value.recommendation = modelPartInfo.value[part].recommendation || ''
  }
}

const highlightBodyPart = (part: THREE.Object3D | null) => {
  if (model) {
    model.traverse((object: any) => {
      if (object.isMesh && object.material) {
        if (object._originalMaterial) {
          object.material = object._originalMaterial.clone()
          delete object._originalMaterial
        }
      }
    })
    
    if (part) {
      part.traverse((object: any) => {
        if (object.isMesh && object.material) {
          if (!object._originalMaterial) {
            object._originalMaterial = object.material.clone()
          }
          const highlightMaterial = object.material.clone()
          highlightMaterial.emissive = new THREE.Color(0x555555)
          highlightMaterial.emissiveIntensity = 0.3
          object.material = highlightMaterial
        }
      })
    }
  }
}

const closeModelInfo = () => {
  showModelInfo.value = false
  showAiResponse.value = false
  typingAiResponse.value = ''
  if (controls) controls.enabled = true
}

const closeHealthInfo = () => {
  showHealthInfo.value = false
  if (controls) controls.enabled = true
}

// 实现打字机效果
const simulateTypingEffect = async (fullText: string) => {
  isTyping.value = true
  typingAiResponse.value = ''
  aiResponseContent.value = ''
  const allChars = fullText.split('')
  for (let i = 0; i < allChars.length; i++) {
    typingAiResponse.value += allChars[i]
    aiResponseContent.value = typingAiResponse.value
    await new Promise(resolve => setTimeout(resolve, 30))
  }
  
  isTyping.value = false
}

const onWindowResize = () => {
  if (!camera || !renderer || !modelCanvas.value) return
  camera.aspect = modelCanvas.value.clientWidth / modelCanvas.value.clientHeight
  camera.updateProjectionMatrix()
  renderer.setSize(modelCanvas.value.clientWidth, modelCanvas.value.clientHeight)
}

const isModelRotating = ref(false)
const rotationTimeout = ref<NodeJS.Timeout | null>(null)

const rotateModel = (direction: 'left' | 'right') => {
  if (!model) return
  isModelRotating.value = true
  if (rotationTimeout.value) {
    clearTimeout(rotationTimeout.value)
  }
  const rotationSpeed = 0.3
  if (direction === 'left') {
    model.rotation.y -= rotationSpeed
  } else {
    model.rotation.y += rotationSpeed
  }

  rotationTimeout.value = setTimeout(() => {
    isModelRotating.value = false
  }, 500) // 500ms后认为旋转停止
}

// 缩放模型
const zoomModel = (direction: 'in' | 'out') => {
   if (!camera || !controls) return
  const zoomSpeed = 0.2  
  const newZ = direction === 'in'
    ? camera.position.z - zoomSpeed
    : camera.position.z + zoomSpeed

  if (direction === 'in' && newZ <= zoomLimits.value.min) {
    showZoomFeedback('模型已放大到最大')
    controls.target.set(0, 0, 0)
    return
  }
  if (direction === 'out' && newZ >= zoomLimits.value.max) {
    showZoomFeedback('模型已缩小到最小')
    controls.target.set(0, 0, 0)
    return
  }

  camera.position.z = newZ
  controls.update()
}

const showZoomFeedback = (message: string) => {
  zoomFeedback.value = message
  setTimeout(() => {
    zoomFeedback.value = ''
  }, 1000)
}

const centerModel = () => {
  if (!camera || !controls || !model || !modelSize) return
  model.rotation.set(0, 0, 0)
  const maxDim = Math.max(modelSize.x, modelSize.y, modelSize.z)
  const fov = camera.fov * (Math.PI / 180)
  let cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2))) * 1.5
  camera.position.set(0, 0, cameraZ)
  controls.target.set(0, 0, 0)
  controls.update()
}

const animate = () => {
  if (!scene || !camera || !renderer || !controls) return
  animationFrameId = requestAnimationFrame(animate)
  controls!.update()
  renderer!.render(scene!, camera!)
}

const getCurrentMetricValue = (metricId: string) => {
  return (currentGrowthRecord.value as any)[metricId]
}

const getCurrentMetricPercentile = (metricId: string) => {
  return (currentGrowthRecord.value as any)[`${metricId}Percentile`]
}

const updateGrowthRecord = () => {
  console.log(`更新到年龄: ${currentGrowthRecord.value.age}`)
}

const retryLoadModel = () => {
  if (!selectedKidId.value) {
    ElMessage.warning('请先选择宝宝')
    return
  }
  
  const kid = kidsList.value.find(k => k.id === selectedKidId.value)
  if (kid) {
    modelLoadError.value = false
    loadModelByAgeAndGender(kid.old, kid.gender || 'male')
  } else {
    ElMessage.error('无法获取宝宝信息')
  }
}

const initThreeJS = () => {
  if (!modelCanvas.value) return
  scene = new THREE.Scene()
  scene.background = new THREE.Color(0xf0f0f0)
  const aspect = modelCanvas.value.clientWidth / modelCanvas.value.clientHeight
  camera = new THREE.PerspectiveCamera(45, aspect, 0.1, 1000)
  camera.position.set(0, 0, 5)
  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(modelCanvas.value.clientWidth, modelCanvas.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  renderer.shadowMap.enabled = true
  while (modelCanvas.value.firstChild) {
    modelCanvas.value.removeChild(modelCanvas.value.firstChild)
  }
  modelCanvas.value.appendChild(renderer.domElement)
  controls = new OrbitControls(camera, renderer.domElement)
  controls.enableDamping = true
  controls.dampingFactor = 0.25
  controls.screenSpacePanning = false
  controls.maxPolarAngle = Math.PI / 1.5
  controls.minDistance = zoomLimits.value.min
  controls.maxDistance = zoomLimits.value.max
  
  // 添加光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  scene.add(ambientLight)
  
  // 添加方向光源
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(2, 4, 1)
  directionalLight.castShadow = true
  scene.add(directionalLight)
  const softLight1 = new THREE.DirectionalLight(0xffffff, 0.4)
  softLight1.position.set(3, 5, 3)
  softLight1.castShadow = false
  scene.add(softLight1)

  const softLight2 = new THREE.DirectionalLight(0xffffff, 0.5)
  softLight2.position.set(-3, 5, -3)
  softLight2.castShadow = false
  scene.add(softLight2)
  
  window.addEventListener('resize', onWindowResize)
  if (selectedKidId.value) {
    const kid = kidsList.value.find(k => k.id === selectedKidId.value)
    if (kid) {
      loadModelByAgeAndGender(kid.old, kid.gender || 'male')
    } else {
    }
  } else {
  }
}

onMounted(() => {
  modelPartInfo.value = defaultModelPartInfo;
  initThreeJS()
  fetchKidsList()
  setTimeout(() => {
    const selectedKid = kidsList.value.find(kid => kid.isSelected)
    if (selectedKid) {
      fetchHealthData(selectedKid.id)
    }
  }, 500) // 延迟一点以确保已加载
})

onBeforeUnmount(() => {
  if (rotationTimeout.value) {
    clearTimeout(rotationTimeout.value)
  }
  if (hoverTimeout.value) {
    clearTimeout(hoverTimeout.value)
  }
  
  if (animationFrameId !== null) {
    cancelAnimationFrame(animationFrameId)
  }
  
  window.removeEventListener('resize', onWindowResize)
  if (scene) {
    scene.clear()
  }
  if (renderer) {
    renderer.dispose()
  }
  if (controls) {
    controls.dispose()
  }
})

const loadModel = (modelPath : any) => {
  modelLoading.value = true
  modelLoadError.value = false
  if (modelLoadTimeout) {
    clearTimeout(modelLoadTimeout)
  }
  
  modelLoadTimeout = setTimeout(() => {
    if (modelLoading.value) {
      modelLoading.value = false
      modelLoadError.value = true
      ElMessage.error('模型加载超时，请检查网络连接后重试')
    }
  }, 30000) 
  if (scene && model) {
    scene.remove(model)
    model = null
    renderer?.renderLists.dispose()
    if (window.gc) {
      window.gc()
    }
  }
  const loader = new GLTFLoader()
  try {
    loader.load(modelPath, (gltf) => {
      if (modelLoadTimeout) {
        clearTimeout(modelLoadTimeout)
        modelLoadTimeout = null
      }
      modelLoading.value = false
      model = gltf.scene
      model.traverse((obj: THREE.Object3D) => {
        // @ts-ignore
        if (obj.isMesh) {
          console.log(`Mesh名称: ${obj.name}`)
        }
      })
      
      if (scene && model) {
        scene.add(model)
        const box = new THREE.Box3().setFromObject(model)
        modelCenter = new THREE.Vector3()
        box.getCenter(modelCenter)
        
        modelSize = new THREE.Vector3()
        box.getSize(modelSize)
        
        // 将模型中心移到场景原点
        model.position.x = -modelCenter.x
        model.position.y = -modelCenter.y
        model.position.z = -modelCenter.z
        
        if (camera && controls) {
          // 计算模型尺寸
          const maxDim = Math.max(modelSize.x, modelSize.y, modelSize.z)
          const fov = camera.fov * (Math.PI / 180)
          let cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2))) * 1.5
          
          camera.position.set(0, 0, cameraZ)
          controls.target.set(0, 0, 0)
          controls.update()
        }

        initModelInteraction(model)
        animate()
      }
    }, 
    (xhr) => {
      const loadingProgress = Math.floor((xhr.loaded / xhr.total) * 100)
    },
    (error) => {
      if (modelLoadTimeout) {
        clearTimeout(modelLoadTimeout)
        modelLoadTimeout = null
      }
      
      modelLoading.value = false
      modelLoadError.value = true
      ElMessage.error('模型加载失败，请稍后重试')
    })
  } catch (err) {
    modelLoading.value = false
    modelLoadError.value = true
    ElMessage.error('模型加载发生异常，将尝试加载默认模型')
  }
}
import api from '@/api/axios'

const fetchPartData = async (partName: string, kidId: number) => {
  if (!kidId) return null
  try {
    const response = await api.get(`/threeJs/get/${partName}/${kidId}`)
    if (response.data && response.data.code === 1) {
      return response.data.data
    }
    return null
  } catch (error) {
    return null
  }
}

const showModelPart = async (part: string) => {
  const selectedKid = kidsList.value.find(kid => kid.isSelected)
  if (!selectedKid) {
    ElMessage.warning('请先选择一个宝宝')
    return
  }
  const partMapping: Record<string, string> = {
    'head_1': 'head',
    'head_2': 'head',
    'head_3': 'head',
    'head': 'head',
    'face': 'head',
    'brain': 'head',
    
    'left_arm': 'rightArm',     // 模型左臂->右臂数据
    'right_arm': 'leftArm',     // 模型右臂->左臂数据
    'leftArm': 'rightArm',      // 模型左臂->右臂数据
    'rightArm': 'leftArm',      // 模型右臂->左臂数据
    'left_hand': 'rightHand',   // 模型左手->右手数据
    'right_hand': 'leftHand',   // 模型右手->左手数据
    'leftHand': 'rightHand',    // 模型左手->右手数据
    'rightHand': 'leftHand',    // 模型右手->左手数据
    'left_shoulder': 'rightShoulder', // 模型左肩->右肩数据
    'right_shoulder': 'leftShoulder', // 模型右肩->左肩数据
    'leftShoulder': 'rightShoulder',  // 模型左肩->右肩数据
    'rightShoulder': 'leftShoulder',  // 模型右肩->左肩数据
    'body': 'body',
    'chest': 'body',
    'abdomen': 'body',
    'spine': 'body',
    
    'left_leg_1': 'rightLeg',   // 模型左腿->右腿数据
    'left_leg_2': 'rightLeg',   // 模型左腿->右腿数据
    'leftLeg': 'rightLeg',      // 模型左腿->右腿数据
    'right_leg_1': 'leftLeg',   // 模型右腿->左腿数据
    'right_leg_2': 'leftLeg',   // 模型右腿->左腿数据
    'rightLeg': 'leftLeg',      // 模型右腿->左腿数据
    
    'left_foot': 'rightFoot',   // 模型左脚->右脚数据
    'right_foot': 'leftFoot',   // 模型右脚->左脚数据
    'leftFoot': 'rightFoot',    // 模型左脚->右脚数据
    'rightFoot': 'leftFoot',    // 模型右脚->左脚数据
    
    'eye': 'visual',
    'mouth': 'oral',
    'ear': 'ent',
    'respiratory': 'respiratory',
    'endocrine': 'endocrine'
  }

  let normalizedPart = part.toLowerCase()
  let matchedPart = normalizedPart
  
  Object.keys(partMapping).forEach(key => {
    if (normalizedPart.includes(key.toLowerCase())) {
      matchedPart = key
    }
  })
  
  const apiPartName = partMapping[matchedPart] || matchedPart
  
  const partData = await fetchPartData(apiPartName, selectedKid.id)
  console.log('获取到的部位数据:', partData)
  
  const updatedModelPartInfo = { ...modelPartInfo.value }

  if (partData) {
    updatedModelPartInfo[matchedPart] = {
      name: getPartName(matchedPart),
      description: '',
      icon: '',
      data: formatPartData(partData, matchedPart),
      recommendation: partData.recommendation || '暂无建议'
    }
  } else {
    updatedModelPartInfo[matchedPart] = {
      name: getPartName(matchedPart),
      description: '',
      icon: '',
      data: {},
      recommendation: ''
    }
  }

  modelPartInfo.value = updatedModelPartInfo
  currentModelPart.value = matchedPart
  showModelInfo.value = true
  showAiResponse.value = false
  aiResponseContent.value = ''
}

const formatPartData = (data: any, part: string) => {
  if (!data) return {}
  const formattedData: Record<string, string> = {}
  Object.keys(data).forEach(key => {
    if (key !== 'id' && key !== 'kidId' && key !== 'recommendation' && data[key] !== null && data[key] !== undefined && data[key] !== '') {
      const formattedKey = formatKeyName(key)
      formattedData[formattedKey] = data[key]
    }
  })
  return formattedData
}

const formatKeyName = (key: string) => {
  const keyMapping: Record<string, string> = {
    gripStrength: '握力',
    elbowRangeOfMotion: '肘活动度',
    tinelSign: 'Tinel征',
    circumferenceDifference: '周长差异',
    
    rangeOfMotion: '活动范围',
    painIndex: '疼痛指数',
    stability: '稳定性',
    muscleStrength: '肌肉力量',
    
    scoliosisDegree: '脊柱侧弯程度',
    coreStrength: '核心肌力',
    bodyFatPercentage: '体脂百分比',
    flexibility: '柔韧性',
    
    lengthDifference: '长度差异',
    kneeReflex: '膝反射',
    swellingDegree: '肿胀程度',
    
    headacheFrequency: '头痛频率',
    dizziness: '眩晕情况',
    traumaHistory: '外伤史',
    cognitiveTestResult: '认知测试结果',
    
    jointSwelling: '关节肿胀',
    twoPointDiscrimination: '两点辨别觉',
    nailBedCirculation: '甲床循环',
    
    archStatus: '足弓',
    halluxValgusDegree: '足部旋转',
    callusStatus: '踝关节活动度',
    gaitCycleStatus: '平衡能力'
  }
  
  return keyMapping[key] || key
}

const getPartName = (part: string) => {
  const nameMapping: Record<string, string> = {
    head: '头部',
    head_1: '头部',
    head_2: '头部',
    head_3: '头部',
    face: '面部',
    brain: '脑部',
    leftArm: '右臂',
    rightArm: '左臂',
    left_arm: '右臂',
    right_arm: '左臂',
    leftHand: '右手',
    rightHand: '左手',
    left_hand: '右手',
    right_hand: '左手',
    leftShoulder: '右肩',
    rightShoulder: '左肩',
    left_shoulder: '右肩',
    right_shoulder: '左肩',
    chest: '胸部',
    abdomen: '腹部',
    spine: '脊柱',
    leftLeg: '右腿',
    rightLeg: '左腿',
    left_leg_1: '右腿',
    left_leg_2: '右腿',
    right_leg_1: '左腿',
    right_leg_2: '左腿',
    leftFoot: '右脚',
    rightFoot: '左脚',
    left_foot: '右脚',
    right_foot: '左脚',
    eye: '眼部',
    mouth: '口腔',
    ear: '耳部',
    respiratory: '呼吸系统',
    endocrine: '内分泌系统',
    body: '躯干'
  }
  
  return nameMapping[part] || part
}
</script>

<style scoped>
.health-model-view {
  @apply bg-white rounded-lg shadow-md p-6;
  background: linear-gradient(to bottom, #ffffff, #f9fafb);
}

.baby-selection {
  @apply mb-6 transition-all duration-300;
}

.baby-option {
  @apply bg-white shadow-sm hover:shadow-md transition-all duration-300 transform hover:-translate-y-1;
}

.baby-selection h3 {
  @apply text-lg font-semibold text-gray-700 mb-3 pb-2 border-b border-gray-100;
}

.baby-avatar {
  @apply ring-2 ring-offset-2 ring-blue-200 transition-all duration-300;
}

.baby-selection .baby-option:hover .baby-avatar {
  @apply ring-blue-400;
}

.body-model-container {
  @apply grid grid-cols-1 gap-8;
}

.model-viewer {
  @apply bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl overflow-hidden relative shadow-inner border border-gray-100;
  height: 520px;
}

.model-canvas {
  @apply w-full h-full rounded-lg;
}

.model-controls {
  @apply absolute bottom-6 left-1/2 transform -translate-x-1/2;
  backdrop-filter: blur(8px);
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  padding: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.model-controls .el-button {
  @apply transition-all duration-200 hover:bg-blue-50;
  border-radius: 8px;
}

:deep(.el-tabs__item) {
  @apply font-medium px-4 py-3 transition-all duration-200;
}

:deep(.el-tabs__item.is-active) {
  @apply text-blue-600 font-semibold;
}

:deep(.el-tabs__active-bar) {
  @apply bg-blue-500 h-[3px] rounded-t;
}

.body-parts-forms {
  @apply bg-gradient-to-br from-white to-gray-50 p-6 rounded-xl shadow-sm border border-gray-100;
}

.form-section-group {
  @apply bg-white p-5 rounded-xl shadow-sm mb-8 transition-all duration-300 hover:shadow-md;
}

.form-section-group h3 {
  @apply text-base font-semibold mb-4 pb-2 border-b border-gray-100 text-gray-700;
  background: linear-gradient(90deg, #3b82f6, #60a5fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.form-section {
  @apply bg-white p-5 rounded-xl transition-all duration-300 hover:shadow-sm border border-gray-50;
}

.form-section h4 {
  @apply flex items-center text-gray-700 font-semibold mb-4;
}

:deep(.el-form-item__label) {
  @apply font-medium text-gray-600;
}

:deep(.el-input__wrapper) {
  @apply rounded-lg shadow-sm border border-gray-200 transition-all duration-200;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

:deep(.el-input__wrapper:hover) {
  @apply border-blue-300;
  box-shadow: 0 1px 3px rgba(59, 130, 246, 0.3);
}

:deep(.el-input__wrapper.is-focus) {
  @apply border-blue-400;
  box-shadow: 0 1px 4px rgba(59, 130, 246, 0.4);
}

.form-actions {
  @apply mt-8 flex justify-end;
}

.form-actions .el-button {
  @apply px-6 py-3 font-medium rounded-lg shadow-sm transition-all duration-300 hover:shadow;
}

.health-info-panel {
  @apply fixed bottom-8 right-8 bg-white rounded-xl shadow-xl w-80 z-50
          border border-gray-100 overflow-hidden;
  animation: slide-in-right 0.3s ease-out forwards;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.info-header {
  @apply flex justify-between items-center p-4 bg-gradient-to-r from-blue-500 to-blue-600 text-white;
}

.info-title {
  @apply font-bold text-lg;
}

.info-content {
  @apply p-5;
}

.info-item {
  @apply mb-4 last:mb-0 bg-gray-50 p-3 rounded-md transition-all duration-200 hover:bg-blue-50;
}

.info-label {
  @apply text-sm text-gray-600 mb-1 font-medium;
}

.info-value {
  @apply text-base font-medium text-gray-800;
}

.info-footer {
  @apply p-4 bg-gradient-to-r from-green-50 to-blue-50 border-t border-gray-100;
}

.recommendation-title {
  @apply text-sm font-bold text-green-700 mb-2;
}

.recommendation-content {
  @apply text-sm text-green-600 bg-white p-3 rounded-md shadow-sm;
}

.model-info-panel {
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  animation: slide-in-right 0.3s ease-out forwards;
  border: 1px solid rgba(229, 231, 235, 0.8);
}

.model-info-panel .info-header {
  @apply sticky top-0 flex justify-between items-center p-4 
         bg-gradient-to-r from-blue-500 to-blue-600 text-white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.model-info-panel .info-title {
  @apply font-bold text-lg;
}

.model-info-panel .info-content {
  @apply p-5;
}

.model-info-panel .info-item {
  @apply mb-4 last:mb-0 bg-gray-50 p-3 rounded-md transition-all duration-200 hover:shadow-sm hover:bg-blue-50 border border-gray-100;
}

.model-info-panel .info-label {
  @apply text-sm text-gray-600 mb-1 font-medium;
}

.model-info-panel .info-value {
  @apply text-base font-medium text-gray-800;
}

.model-info-panel .info-footer {
  @apply p-4 bg-gradient-to-r from-green-50 to-blue-50 border-t border-gray-100;
}

.model-info-panel .recommendation-title {
  @apply text-sm font-bold text-green-700 mb-2;
}

.model-info-panel .recommendation-content {
  @apply text-sm text-green-600 bg-white p-3 rounded-md shadow-sm;
}

@keyframes slide-in-right {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.ai-response {
  @apply bg-gradient-to-r from-blue-50 to-indigo-50 p-4 rounded-lg border border-blue-100;
}

.response-header {
  @apply flex justify-between items-center mb-3;
}

.response-title {
  @apply font-semibold text-blue-700 text-sm;
}

.response-content {
  @apply text-sm text-blue-900 min-h-[80px] bg-white bg-opacity-50 rounded-md p-3;
}

.response-loading {
  @apply flex space-x-2 items-center justify-center h-12;
}

.dot-loader {
  @apply w-2 h-2 rounded-full bg-blue-500;
  animation: dotPulse 1.5s infinite ease-in-out;
}

.dot-loader:nth-child(2) {
  animation-delay: 0.2s;
}

.dot-loader:nth-child(3) {
  animation-delay: 0.4s;
}

.response-text {
  @apply leading-relaxed;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes dotPulse {
  0%, 100% { 
    transform: scale(0.8);
    opacity: 0.5;
  }
  50% { 
    transform: scale(1.2);
    opacity: 1;
  }
}

.zoom-feedback {
  @apply absolute bottom-[70px] left-1/2 transform -translate-x-1/2;
  @apply bg-black bg-opacity-70 text-white px-4 py-2 rounded-full text-sm z-10;
  animation: fadeInOut 2s ease-in-out;
}

@keyframes fadeInOut {
  0% { opacity: 0; transform: translateY(10px); }
  20% { opacity: 1; transform: translateY(0); }
  80% { opacity: 1; transform: translateY(0); }
  100% { opacity: 0; transform: translateY(-10px); }
}

.model-loading {
  @apply absolute inset-0 flex flex-col items-center justify-center bg-gray-900 bg-opacity-60 text-white;
  backdrop-filter: blur(3px);
  z-index: 5;
}

.loading-spinner {
  @apply w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full;
  animation: spin 1s linear infinite;
  box-shadow: 0 0 15px rgba(59, 130, 246, 0.5);
}

.loading-text {
  @apply mt-4 text-sm font-medium bg-black bg-opacity-50 px-4 py-2 rounded-full;
}

.model-error {
  @apply absolute inset-0 flex flex-col items-center justify-center bg-gray-900 bg-opacity-80 text-white;
  backdrop-filter: blur(4px);
  z-index: 5;
}

.error-icon {
  @apply text-red-500 text-3xl mb-3;
  filter: drop-shadow(0 0 8px rgba(239, 68, 68, 0.5));
}

.error-text {
  @apply mb-4 text-sm font-medium bg-black bg-opacity-50 px-4 py-2 rounded-full;
}

.hover-card {
  @apply bg-white rounded-lg shadow-md border border-gray-200 overflow-hidden;
  width: 280px;
  max-width: 80vw;
  animation: fadeIn 0.2s ease-out;
  pointer-events: none;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.hover-card-content {
  @apply p-4;
}

.hover-card-title {
  @apply font-bold text-blue-600 mb-2 text-sm border-b border-blue-100 pb-2;
  background: linear-gradient(90deg, #3b82f6, #60a5fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hover-card-data {
  @apply text-xs;
}

.hover-data-item {
  @apply mb-2 last:mb-0 p-2 bg-gray-50 rounded-md transition-all duration-200;
}

.hover-data-label {
  @apply font-medium text-gray-600 mr-1;
}

.hover-data-value {
  @apply text-gray-800 font-medium;
}

.hover-card-empty {
  @apply text-xs text-gray-500 italic bg-gray-50 p-3 rounded-md border border-gray-100 flex items-center justify-center;
  height: 60px;
}

.hover-section-title {
  @apply text-xs font-medium text-gray-600 mt-3 mb-2 border-t border-gray-100 pt-2;
}

.hover-recommendation-text {
  @apply text-xs text-green-600 break-words bg-green-50 p-2 rounded-md border border-green-100;
  max-height: 80px;
  overflow: auto;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

:deep(.el-empty) {
  padding: 24px 0;
}

:deep(.el-empty__description) {
  margin-top: 8px;
}

:deep(.el-empty__image) {
  opacity: 0.8;
}
</style> 