<template>
  <div class="live2d-assistant-container" :class="{ 'chat-open': isChatOpen, 'chat-expanded': isExpanded }">
    <div v-if="isChatOpen" class="chat-container">
      <div class="chat-header">
        <div class="chat-title">
          <div class="avatar">
            <img src="@/assets/客服.svg" alt="客服头像" />
          </div>
          <div class="title-text">
            <div class="name">智能助手小蓝</div>
            <div class="status">在线为您服务</div>
          </div>
        </div>
        <div class="chat-mode-switch">
          <button 
            class="mode-btn" 
            :class="{ 'active': chatMode === 'normal' }" 
            @click="setChatMode('normal')"
          >
            普通对话
          </button>
          <button 
            class="mode-btn" 
            :class="{ 'active': chatMode === 'health' }" 
            @click="setChatMode('health')"
          >
            网页跳转
          </button>
        </div>
        <div class="chat-actions">
          <button class="expand-btn" @click.stop="toggleExpand" :title="isExpanded ? '缩小' : '放大'">
            <svg viewBox="0 0 24 24" width="18" height="18">
              <path v-if="isExpanded" d="M5 16h3v3c0 .55.45 1 1 1s1-.45 1-1v-4c0-.55-.45-1-1-1H5c-.55 0-1 .45-1 1s.45 1 1 1zm1-8h4c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1s-1 .45-1 1v3H6c-.55 0-1 .45-1 1s.45 1 1 1zm13 6h-4c-.55 0-1 .45-1 1v4c0 .55.45 1 1 1s1-.45 1-1v-3h3c.55 0 1-.45 1-1s-.45-1-1-1zm-1-6c.55 0 1-.45 1-1s-.45-1-1-1h-3V3c0-.55-.45-1-1-1s-1 .45-1 1v4c0 .55.45 1 1 1h4z" fill="currentColor"/>
              <path v-else d="M4 14h3v3c0 .55.45 1 1 1s1-.45 1-1v-4c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1s.45 1 1 1zm0-4h4c.55 0 1-.45 1-1V5c0-.55-.45-1-1-1s-1 .45-1 1v3H4c-.55 0-1 .45-1 1s.45 1 1 1zm10 4h3c.55 0 1-.45 1-1s-.45-1-1-1h-4c-.55 0-1 .45-1 1v4c0 .55.45 1 1 1s1-.45 1-1v-3zm-1-4h4c.55 0 1-.45 1-1s-.45-1-1-1h-3V5c0-.55-.45-1-1-1s-1 .45-1 1v4c0 .55.45 1 1 1z" fill="currentColor"/>
            </svg>
          </button>
          <button class="minimize-btn" @click="toggleChat">
            <svg viewBox="0 0 24 24" width="18" height="18">
              <rect x="4" y="11" width="16" height="2" rx="1" fill="currentColor"/>
            </svg>
          </button>
          <button class="close-btn" @click="toggleChat">
            <svg viewBox="0 0 24 24" width="18" height="18">
              <path d="M6.7 5.3a1 1 0 0 0-1.4 1.4L10.6 12l-5.3 5.3a1 1 0 0 0 1.4 1.4L12 13.4l5.3 5.3a1 1 0 0 0 1.4-1.4L13.4 12l5.3-5.3a1 1 0 0 0-1.4-1.4L12 10.6 6.7 5.3z" fill="currentColor"/>
            </svg>
          </button>
        </div>
      </div>
      
      <div class="chat-messages" ref="messagesContainer">
        <div 
          v-for="(message, index) in messages" 
          :key="index" 
          class="message-wrapper"
          :class="{ 'user-wrapper': message.isUser, 'assistant-wrapper': !message.isUser }"
        >
          <div v-if="!message.isUser" class="assistant-avatar">
            <img src="@/assets/客服.svg" alt="客服头像" />
          </div>
          <div 
            class="message" 
            :class="{ 'user-message': message.isUser, 'assistant-message': !message.isUser }"
          >
            <div class="message-content" v-if="message.isUser">{{ message.text }}</div>
            <div class="message-content" v-else v-html="message.text"></div>
            
            <div v-if="message.jumpOptions" class="jump-options">
              <div class="jump-options-text">请选择查看内容：</div>
              <div class="jump-actions">
                <button 
                  v-if="message.jumpOptions && message.jumpOptions.productInfo" 
                  class="jump-btn product-btn" 
                  @click="goToDetail('product', message.jumpOptions.productInfo.id)"
                >
                  查看商品
                </button>
                <button 
                  v-if="message.jumpOptions && message.jumpOptions.encyclopediaInfo" 
                  class="jump-btn encyclopedia-btn" 
                  @click="goToDetail('encyclopedia', message.jumpOptions.encyclopediaInfo.id)"
                >
                  查看百科
                </button>
                <button class="jump-btn cancel-btn" @click="dismissJump">
                  不跳转
                </button>
              </div>
            </div>
            
            <div v-if="message.recommendationType" class="recommendation-buttons">
              <div class="recommendation-text">推荐{{ message.recommendationType === 'product' ? '商品' : '百科' }}：{{ message.recommendationName || '没有找到相关推荐' }}</div>
              <div class="recommendation-actions" v-if="message.recommendationName">
                <button class="recommendation-btn" @click="goToDetail(message.recommendationType, message.recommendationId)">
                  查看详情
                </button>
              </div>
            </div>
            
            <div class="message-time">{{ message.time }}</div>
          </div>
          <div v-if="message.isUser" class="user-avatar">
            <div class="avatar-mini">👤</div>
          </div>
        </div>
        <div v-if="isTyping" class="message-wrapper assistant-wrapper">
          <div class="assistant-avatar">
              <img src="@/assets/客服.svg" alt="客服头像" />
          </div>
          <div class="message assistant-message">
            <div class="message-content typing">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="chat-input-container">
        <div class="input-wrapper">
          <input 
            v-model="userMessage" 
            class="chat-input" 
            placeholder="请输入您的问题..." 
            @keyup.enter="sendMessage"
          />
          <button 
            class="mic-btn" 
            @click="toggleListening"
            :class="{ 'listening': isListening }"
            title="点击开始/停止语音输入"
          >
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 1a3 3 0 0 0-3 3v8a3 3 0 0 0 6 0V4a3 3 0 0 0-3-3z"></path>
              <path d="M19 10v2a7 7 0 0 1-14 0v-2"></path>
              <line x1="12" y1="19" x2="12" y2="23"></line>
              <line x1="8" y1="23" x2="16" y2="23"></line>
            </svg>
          </button>
        </div>
        <button class="send-btn" @click="sendMessage">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" fill="currentColor"/>
          </svg>
        </button>
      </div>
      
      <div class="quick-actions">
        <button 
          v-for="(action, index) in quickActions" 
          :key="index" 
          class="quick-action-btn"
          @click="quickReply(action)"
        >
          {{ action }}
        </button>
      </div>
    </div>
    
    <div class="live2d-model-container" @click="toggleChat">
      <canvas ref="live2dCanvas" class="live2d-canvas"></canvas>
      <div v-if="modelError || isLoading" class="model-error">
        <div class="default-avatar">
          <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
          </svg>
        </div>
        <div v-if="isLoading" class="loading-indicator">
          <div class="loading-circle"></div>
          <span class="loading-text">模型加载中...</span>
        </div>
        <span v-if="modelError" class="error-message">{{ modelError }}</span>
        <button v-if="modelError" class="retry-button" @click.stop="retryLoading">重试</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, watch, reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { marked } from 'marked';
const router = useRouter();
const markedOptions = {
  gfm: true, 
  breaks: true, 
  silent: true
};
const formatTime = () => {
  return new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
};
const isChatOpen = ref(false);
const isExpanded = ref(false); 
const userMessage = ref('');
const messages = ref<{ 
  text: string; 
  isUser: boolean; 
  time: string;
  recommendationType?: 'product' | 'encyclopedia' | null;
  recommendationId?: number | null;
  recommendationName?: string | null;
  jumpOptions?: {
    productInfo?: { id: number; name: string } | undefined;
    encyclopediaInfo?: { id: number; title: string } | undefined;
  } | undefined;
}[]>([
  { text: '你好！我是小蓝，有什么可以帮助你的吗？', isUser: false, time: formatTime() }
]);
const isTyping = ref(false);
const live2dCanvas = ref<HTMLCanvasElement | null>(null);
const messagesContainer = ref<HTMLElement | null>(null);
const modelError = ref<string>('');
const modelLoaded = ref(false);
const isListening = ref(false);
const recognition = ref<any>(null); 
const chatMode = ref<'normal' | 'health'>('normal'); 
const currentSession = ref<string | null>(null); 
const currentStreamingResponse = ref<string>(''); 
const isLoading = ref(true); 
const loadRetryCount = ref(0);
const maxRetryAttempts = 3; 

const quickActions = [
  '帮助', 
  '健康建议', 
  '联系客服',
  '常见问题'
];

let app: any = null;
let model: any = null;

const checkResourceExists = async (url: string): Promise<boolean> => {
  try {
    const response = await fetch(url, { method: 'HEAD' });
    return response.ok;
  } catch (error) {
    return false;
  }
};

const toggleExpand = () => {
  isExpanded.value = !isExpanded.value;
};

onMounted(async () => {
  loadScripts();
  initSpeechRecognition();
  if (messages.value.length > 0) {
    const welcomeMsg = messages.value[0].text;
    generateAndPlayAudio(welcomeMsg);
  }
});

onUnmounted(() => {
  if (app) {
    app.destroy(true);
    app = null;
  }
  stopListening();
  recognition.value = null;
});

watch(messages, () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
});

const initLive2D = async () => {
  try {
    if (!live2dCanvas.value) return;
    live2dCanvas.value.width = 300;
    live2dCanvas.value.height = 300;
    
    app = new window.PIXI.Application({
      width: 1500,
      height: 1500, 
      view: live2dCanvas.value,
      autoStart: true,
      transparent: true,
      backgroundAlpha: 0,
      antialias: true,
      resolution: 2
    });
    
    model = await window.PIXI.live2d.Live2DModel.from('/Live2D/kei_vowels_pro/kei_vowels_pro.model3.json');
  
    model.scale.set(0.8);
    model.position.set(750, 1100);
    model.anchor.set(0.5, 0.5);
    app.stage.addChild(model);
    
    if (model.internalModel?.motionManager) {
      const motionGroups = model.internalModel.motionManager.definitions;
      if (motionGroups.idle && motionGroups.idle.length > 0) {
        model.motion('idle', 0);
      } else {
        const firstGroup = Object.keys(motionGroups)[0];
        if (firstGroup) {
          model.motion(firstGroup, 0);
        }
      }
    }
    
    modelError.value = '';
    modelLoaded.value = true;
    isLoading.value = false; 
    loadRetryCount.value = 0; 
    
    setInterval(() => {
      if (model && !isChatOpen.value) {
        try {
          model.expression(Math.floor(Math.random() * 4));
        } catch (e) {}
      }
    }, 5000);
    
  } catch (error) {
    loadRetryCount.value++;
    if (loadRetryCount.value < maxRetryAttempts) {
      setTimeout(() => {
        initLive2D();
      }, 2000); 
    } else {
      modelError.value = '模型加载失败，请点击重试';
      isLoading.value = false; 
    }
  }
};

const retryLoading = (event: Event) => {
  event.stopPropagation(); 
  loadRetryCount.value = 0;
  modelError.value = '';
  isLoading.value = true;
  
  if (app) {
    app.destroy(true);
    app = null;
  }
  
  setTimeout(() => {
    loadScripts();
  }, 500);
};

const loadScripts = () => {
  const scripts = [
    '/Live2D/live2dcubismcore.min.js',
    '/Live2D/pixi.min.js',
    '/Live2D/cubism4.min.js'
  ];
  
  let loadedCount = 0;
  const scriptPromises = [];
  
  for (const src of scripts) {
    const existingScript = document.querySelector(`script[src="${src}"]`);
    if (existingScript) {
      loadedCount++;
      if (loadedCount === scripts.length) {
        initLive2D();
      }
      continue;
    }
    
    const scriptPromise = new Promise<void>((resolve, reject) => {
      const script = document.createElement('script');
      script.src = src;
      script.async = true;
      
      script.onload = () => {
        loadedCount++;
        resolve();
      };
      
      script.onerror = () => {
        reject(new Error(`脚本 ${src} 加载失败`));
      };
      
      document.head.appendChild(script);
    });
    
    scriptPromises.push(scriptPromise);
  }
  
  if (loadedCount === scripts.length) {
    initLive2D();
    return;
  }
  
  Promise.all(scriptPromises)
    .then(() => {
      initLive2D();
    })
    .catch((error) => {
      loadRetryCount.value++;
      console.error('脚本加载失败:', error);
      
      if (loadRetryCount.value < maxRetryAttempts) {
        console.log(`脚本加载失败，正在进行第${loadRetryCount.value}次重试...`);
        setTimeout(() => {
          loadScripts();
        }, 2000); 
      } else {
        modelError.value = '脚本加载失败，请点击重试';
        isLoading.value = false;
      }
    });
};

const addMessage = (message: {
  text: string;
  isUser: boolean;
  time: string;
  recommendationType?: 'product' | 'encyclopedia' | null;
  recommendationId?: number | null;
  recommendationName?: string | null;
  jumpOptions?: {
    productInfo?: { id: number; name: string } | undefined;
    encyclopediaInfo?: { id: number; title: string } | undefined;
  } | undefined;
}) => {
  messages.value.push(message);
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

const toggleChat = () => {
  isChatOpen.value = !isChatOpen.value;
  
  if (isChatOpen.value) {
    if (messages.value.length === 0) {
      const welcomeMsg = '你好！我是小蓝，有什么可以帮助你的吗？';
      addMessage({
        text: welcomeMsg,
        isUser: false,
        time: formatTime()
      });
      
      generateAndPlayAudio(welcomeMsg);
    }
    
    if (model) {
      try {
        const motions = ['tap_body', 'tap', 'talk', 'greeting'];
        for (const m of motions) {
          try {
            model.motion(m, 0);
            break;
          } catch (e) { }
        }
        
        if (model.expression) model.expression(0);
      } catch (e) { }
    }
  } else if (model) {
    try {
      model.motion('idle', 0);
      if (model.expression) model.expression(1);
    } catch (e) { }
  }
};

const initSpeechRecognition = () => {
  if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
    console.log('您的浏览器不支持语音识别功能');
    return;
  }
  
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
  recognition.value = new SpeechRecognition();
  
  recognition.value.continuous = false; 
  recognition.value.interimResults = true; 
  recognition.value.lang = 'zh-CN'; // 语言为中文
  recognition.value.onresult = (event: any) => {
    const result = event.results[0];
    const transcript = result[0].transcript;
    
    userMessage.value = transcript;
    
    if (result.isFinal && transcript.trim() !== '') {
      if (!isListening.value) return; 
      if (userMessage.value.trim().length > 0) {
        sendMessage();
      }
    }
  };
  
  recognition.value.onend = () => {
    isListening.value = false;
  };
  
  recognition.value.onerror = (event: any) => {
    isListening.value = false;
    if (event.error === 'no-speech') {
      alert('没有检测到语音，请再试一次');
    } else if (event.error === 'not-allowed') {
      alert('请允许使用麦克风权限以便进行语音输入');
    } else if (event.error === 'network') {
      alert('网络错误，请检查您的连接');
    }
  };
};

const startListening = () => {
  if (!recognition.value) {
    initSpeechRecognition();
  }
  
  if (recognition.value && !isListening.value) {
    recognition.value.start();
    isListening.value = true;
  }
};

const stopListening = () => {
  if (recognition.value && isListening.value) {
    recognition.value.stop();
    isListening.value = false;
  }
};

const toggleListening = () => {
  if (isListening.value) {
    stopListening();
  } else {
    startListening();
  }
};

const setChatMode = (mode: 'normal' | 'health') => {
  chatMode.value = mode;
};

// 发送问题到智能体
const sendQuestionToAI = async (question: string) => {
  try {
    currentStreamingResponse.value = '';
    addMessage({
      text: '',
      isUser: false,
      time: formatTime()
    });
    isTyping.value = true;
    const token = localStorage.getItem('token');
    // 设置请求头
    const headers = {
      'Content-Type': 'application/json'
    };
    if (token) {
      // @ts-ignore
      headers.Authorization = `Bearer ${token}`;
    } else {
      console.warn('未找到token');
    }
    if(chatMode.value === 'normal'){
    const response = await axios.post('/api/question', {
      question: question
    }, { headers });
    if (response.data.code === 1 && response.data.data) {
      currentSession.value = response.data.data;
      if (currentSession.value) {
        let requestId = '';
        const idMatch = currentSession.value.match(/ID：([a-f0-9-]+)/i);
        if (idMatch && idMatch[1]) {
          requestId = idMatch[1];
        } else {
          isTyping.value = false;
          const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
          if (lastIndex >= 0) {
            messages.value[lastIndex].text = "抱歉，处理请求时出现问题，请稍后再试。";
            generateAndPlayAudio("抱歉，处理请求时出现问题，请稍后再试。");
          }
          return;
        }
        const answer = await pollForAnswer(requestId, headers);
        if (answer) {
          await simulateStreamingResponse(answer);
        } else {
          const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
          if (lastIndex >= 0) {
            messages.value[lastIndex].text = "抱歉，无法获取回答，请稍后再试。";
            generateAndPlayAudio("抱歉，无法获取回答，请稍后再试。");
          }
        }
        isTyping.value = false;
      } else {
        isTyping.value = false;
        const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
        if (lastIndex >= 0) {
          messages.value[lastIndex].text = "抱歉，会话创建失败，请稍后再试。";
          generateAndPlayAudio("抱歉，会话创建失败，请稍后再试。");
        }
      }
    } else {
      isTyping.value = false;
      const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
      if (lastIndex >= 0) {
        messages.value[lastIndex].text = "抱歉，我暂时无法回答您的问题，请稍后再试。";
        generateAndPlayAudio("抱歉，我暂时无法回答您的问题，请稍后再试。");
      }
    }
  }else if(chatMode.value === 'health'){
    await getHealthRecommendation(question);
  }
  }catch (error) {
    isTyping.value = false;
    const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
    if (lastIndex >= 0) {
      messages.value[lastIndex].text = "抱歉，我暂时无法回答您的问题，请稍后再试。";
      generateAndPlayAudio("抱歉，我暂时无法回答您的问题，请稍后再试。");
    }
  }
};

// 轮询获取回答函数
const pollForAnswer = async (requestId: string, headers: Record<string, string>): Promise<string | null> => {
  const maxAttempts = 30; 
  const interval = 1000; 
  let attempts = 0;
  
  const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
  if (lastIndex >= 0) {
    messages.value[lastIndex].text = "正在思考中，请稍候...";
  }
  
  while (attempts < maxAttempts) {
    try {
      attempts++;
      const statusResponse = await axios.get(`/api/status/${requestId}`, { headers });
      if (statusResponse.data.code === 1 && statusResponse.data.data) {
        let response = statusResponse.data.data;
        response = response.trim();
        return response;
      }
      if (statusResponse.data.code === 0 && statusResponse.data.msg === "问题结果没有得到，请继续轮询") {
        if (lastIndex >= 0 && attempts % 3 === 0) { 
          const dots = '.'.repeat((attempts / 3) % 4);
          messages.value[lastIndex].text = `正在思考中，请稍候${dots}`;
        }
        await new Promise(resolve => setTimeout(resolve, interval));
        continue;
      }
      console.error('轮询获取回答失败:', statusResponse.data);
      return null;
    } catch (error) {
      console.error('轮询请求异常:', error);
      await new Promise(resolve => setTimeout(resolve, interval));
    }
  }
  return null;
};

const stripHtmlTags = (html: string) => {
  return html.replace(/<[^>]*>/g, '');
};

const removeQuotes = (text: string): string => {
  text = text.trim();
  return text.replace(/^(&quot;|"|\\"|'|&apos;|\\')/, '')
            .replace(/(&quot;|"|\\"|'|&apos;|\\')$/, '');
};

const renderMarkdown = (mdText: string): string => {
  try {
    const processedText = mdText.replace(/\\n/g, '\n');
    return marked.parse(processedText, markedOptions) as string;
  } catch (error) {
    console.error('Markdown渲染错误', error);
    return mdText;
  }
};

// 通过TTS接口生成语音并播放
const generateAndPlayAudio = (text: string) => {
  let cleanText = stripHtmlTags(text);
  cleanText = removeQuotes(cleanText);
  axios.get(`http://127.0.0.1:2020/dealAudio?file_name=test.mp3&voice=xiaoyi&text=${encodeURIComponent(cleanText)}`)
    .then(response => {
      const audioUrl = response.data + "?v=" + new Date().getTime();
      if (model) {
        try {
          if (model.speak) {
            model.speak(audioUrl, {
              volume: 1,
              expression: 0,
              resetExpression: true,
              crossOrigin: 'anonymous'
            });
          } else {
            const audio = new Audio(audioUrl);
            audio.play();
            if (model.motion) model.motion('idle', 0);
            if (model.expression) model.expression(0);
          }
        } catch (error) {
          const audio = new Audio(audioUrl);
          audio.play();
        }
      } else {
        const audio = new Audio(audioUrl);
        audio.play();
      }
    })
    .catch(error => {
      if (model?.expression) {
        try {
          model.expression(3);
        } catch (e) {}
      }
    });
};

const simulateStreamingResponse = async (fullResponse: string) => {
  const processedResponse = fullResponse.replace(/\\n/g, '\n');
  const allChars = processedResponse.split('');
  generateAndPlayAudio(processedResponse);
  let currentText = '';
  for (let i = 0; i < allChars.length; i++) {
    currentText += allChars[i];
    currentStreamingResponse.value = currentText;
    const htmlContent = renderMarkdown(currentText);
    const lastIndex = messages.value.map(m => !m.isUser).lastIndexOf(true);
    if (lastIndex >= 0) {
      messages.value[lastIndex].text = htmlContent;
    }
    nextTick(() => {
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
      }
    });
    await new Promise(resolve => setTimeout(resolve, 100));
  }
};

// 获取健康推荐
const getHealthRecommendation = async (question: string) => {
  try {
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json'
    };
    if (token) {
      // @ts-ignore
      headers.Authorization = `Bearer ${token}`;
    } else {
      console.warn('未找到token');
    }
    const response = await axios.post('/api/talk', {
      question: question,
      pageReq: {
        pageSize: 1,
        pageNum: 1
      }
    }, { headers });
    if (response.data.code === 1 && response.data.data) {
      const { productId, parentingEncyclopediaId } = response.data.data;
      let jumpText = "根据您的问题，我找到了相关内容，您想要查看：";
      addMessage({
        text: jumpText,
        isUser: false,
        time: formatTime(),
      });
      generateAndPlayAudio(jumpText);
      let optionsText = '';
      let hasOptions = false;
      let productInfo: { id: number; name: string } | undefined = undefined;
      let encyclopediaInfo: { id: number; title: string } | undefined = undefined;
      if (productId) {
        hasOptions = true;
        try {
          const productHeaders = {
            'Content-Type': 'application/json'
          };
          if (token) {
            // @ts-ignore
            productHeaders.Authorization = `Bearer ${token}`;
          }
          const productResponse = await axios.get(`/api/product/${productId}`, { headers: productHeaders });
          console.log('商品详情:', productResponse.data);
          if (productResponse.data.code === 0 && productResponse.data.data) {
            productInfo = {
              id: productId,
              name: productResponse.data.data.name
            };
            console.log('设置商品信息:', productInfo);
            optionsText += `商品：${productInfo.name}`;
          } else {
            console.warn('商品API返回了未预期的格式:', productResponse.data);
            productInfo = {
              id: productId,
              name: '商品详情'
            };
            optionsText += `商品详情`;
          }
        } catch (error) {
          console.error('获取商品信息失败', error);
          productInfo = {
            id: productId,
            name: '商品详情'
          };
          optionsText += `商品详情`;
        }
      }
      if (parentingEncyclopediaId) {
        hasOptions = true;
        try {
          const encyclopediaHeaders = {
            'Content-Type': 'application/json'
          };
          if (token) {
            // @ts-ignore
            encyclopediaHeaders.Authorization = `Bearer ${token}`;
          }
          
          const encyclopediaResponse = await axios.get(`/api/parentingEncyclopedia/${parentingEncyclopediaId}`, { headers: encyclopediaHeaders });
          if (encyclopediaResponse.data.code === 0 && encyclopediaResponse.data.data) {
            encyclopediaInfo = {
              id: parentingEncyclopediaId,
              title: encyclopediaResponse.data.data.title
            };
            optionsText += optionsText ? `，或百科：${encyclopediaInfo.title}` : `百科：${encyclopediaInfo.title}`;
          } else {
            encyclopediaInfo = {
              id: parentingEncyclopediaId,
              title: '百科文章'
            };
            optionsText += optionsText ? `，或百科文章` : `百科文章`;
          }
        } catch (error) {
          console.error('获取百科信息失败', error);
          encyclopediaInfo = {
            id: parentingEncyclopediaId,
            title: '百科文章'
          };
          optionsText += optionsText ? `，或百科文章` : `百科文章`;
        }
      }
      
      if (hasOptions) {
        const jumpMessage = {
          text: optionsText,
          isUser: false,
          time: formatTime(),
          jumpOptions: {
            productInfo,
            encyclopediaInfo
          }
        };
        messages.value.push(jumpMessage);
        nextTick(() => {
          if (messagesContainer.value) {
            messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
          }
        });
        generateAndPlayAudio(optionsText);
      } else {
        const noRecommendText = "抱歉，我没有找到相关的内容可以跳转。";
        addMessage({
          text: noRecommendText,
          isUser: false,
          time: formatTime()
        });
        generateAndPlayAudio(noRecommendText);
      }
    } else {
      const errorText = "抱歉，查询相关内容时出现问题，请稍后再试。";
      addMessage({
        text: errorText,
        isUser: false,
        time: formatTime()
      });
      generateAndPlayAudio(errorText);
    }
  } catch (error) {
    const errorText = "抱歉，网络请求失败，请检查您的网络连接。";
    addMessage({
      text: errorText,
      isUser: false,
      time: formatTime()
    });
    generateAndPlayAudio(errorText);
  }
};

const dismissJump = () => {
  const index = [...messages.value].reverse().findIndex(m => m.jumpOptions);
  if (index >= 0) {
    const realIndex = messages.value.length - 1 - index;
    addMessage({
      text: "好的，我会继续为您提供其他帮助。",
      isUser: false,
      time: formatTime()
    });
    generateAndPlayAudio("好的，我会继续为您提供其他帮助。");
  }
};

const goToDetail = (type: string, id: number | null | undefined) => {
  if (!id) return;
  const token = localStorage.getItem('token');
  if (type === 'product') {
    router.push({
      path: `/mall/product/${id}`,
      query: token ? { token } : {}
    });
  } else if (type === 'encyclopedia') {
    router.push({
      path: `/encyclopedia/article/${id}`,
      query: token ? { token } : {}
    });
  }
};

// 发送消息
const sendMessage = async () => {
  if (!userMessage.value.trim()) return;
  addMessage({
    text: userMessage.value,
    isUser: true,
    time: formatTime()
  });
  if (isListening.value) {
    stopListening();
  }
  if (checkForNavigationCommand(userMessage.value)) {
    userMessage.value = '';
    return;
  }
  isTyping.value = true;
  await sendQuestionToAI(userMessage.value);
  userMessage.value = '';
};

const quickReply = (action: string) => {
  addMessage({
    text: action,
    isUser: true,
    time: formatTime()
  });
  sendQuestionToAI(action);
};

const checkForNavigationCommand = (text: string) => {
  if (text.includes('跳转')) {
    const navigationTargets = [
      { name: '首页', path: '/' },
      { name: '百科', path: '/encyclopedia' },
      { name: '健康', path: '/health' },
      { name: '商城', path: '/shop' },
      { name: '我的', path: '/user' }
    ];
    
    for (const target of navigationTargets) {
      if (text.includes(target.name)) {
        setTimeout(() => {
          router.push(target.path);
        }, 2000);
        const jumpText = `好的，已为您跳转到${target.name}`;
        addMessage({
          text: jumpText,
          isUser: false,
          time: formatTime()
        });
        generateAndPlayAudio(jumpText);
        return true;
      }
    }
  }
  return false;
};
</script>

<style scoped>
.live2d-assistant-container {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 10000;
  display: flex;
  flex-direction: row-reverse;
  align-items: flex-end;
  font-family: 'HKPUGS-Medium', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  gap: 16px;
}

.live2d-model-container {
  width: 300px;
  height: 300px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  background-color: transparent;
  overflow: visible;
  border-radius: 0;
  box-shadow: none;
}

.live2d-model-container:hover {
  transform: scale(1.05);
}

.live2d-canvas {
  width: 100%;
  height: 100%;
  transform: scale(1.2);
}

.model-error {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(110, 142, 251, 0.7);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  text-align: center;
  padding: 10px;
  border-radius: 50%;
  box-sizing: border-box;
  backdrop-filter: blur(5px);
  box-shadow: 0 4px 15px rgba(110, 142, 251, 0.3);
}

.default-avatar {
  width: 100px;
  height: 100px;
  margin-bottom: 10px;
  animation: gentle-pulse 3s infinite ease-in-out;
}

.error-message {
  font-size: 12px;
  max-width: 150px;
  line-height: 1.4;
  margin-bottom: 10px;
}

.retry-button {
  background-color: white;
  color: #6e8efb;
  border: none;
  border-radius: 15px;
  padding: 5px 15px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
  margin-top: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.retry-button:hover {
  background-color: #f0f2ff;
  transform: translateY(-2px);
}

.loading-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.loading-circle {
  width: 30px;
  height: 30px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top: 3px solid white;
  border-radius: 50%;
  animation: spin 1.5s linear infinite;
  margin-bottom: 10px;
}

.loading-text {
  font-size: 12px;
  color: white;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes gentle-pulse {
  0%, 100% { opacity: 0.9; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.05); }
}

.chat-container {
  width: 350px;
  height: 500px;
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin-bottom: 15px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  animation: slide-up 0.3s ease;
  transition: all 0.4s cubic-bezier(0.165, 0.84, 0.44, 1);
  position: relative;
}

.chat-expanded .chat-container {
  width: 500px;
  height: 600px;
}

.chat-open .live2d-model-container {
  margin-bottom: 10px;
}

.chat-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 10% 10%, rgba(255, 107, 107, 0.02), transparent 40%),
              radial-gradient(circle at 90% 90%, rgba(74, 144, 242, 0.02), transparent 40%);
  z-index: 0;
  animation: gradient-shift 15s ease infinite;
  pointer-events: none;
}

@keyframes gradient-shift {
  0% { 
    background-position: 0% 0%, 100% 100%; 
    opacity: 0.5;
  }
  50% { 
    background-position: 100% 0%, 0% 100%; 
    opacity: 0.8;
  }
  100% { 
    background-position: 0% 0%, 100% 100%; 
    opacity: 0.5;
  }
}

@keyframes slide-up {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.chat-header {
  background: linear-gradient(135deg, #FF7B7B, #FF9E9E);
  color: white;
  padding: 14px 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  position: relative;
  z-index: 1;
}

.chat-title {
  display: flex;
  align-items: center;
}

.avatar {
  width: 36px;
  height: 36px;
  background-color: transparent;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  overflow: visible;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.avatar-icon, .avatar-mini {
  display: block;
  overflow: visible;
}

.title-text {
  display: flex;
  flex-direction: column;
}

.name {
  font-weight: 600;
  font-size: 18px;
  letter-spacing: 0.4px;
}

.status {
  font-size: 13px;
  opacity: 0.9;
}

.chat-mode-switch {
  display: flex;
  gap: 8px;
  margin-right: 10px;
  position: relative;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 2px;
}

.mode-btn {
  background: transparent;
  color: white;
  border: none;
  border-radius: 14px;
  padding: 7px 12px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  font-weight: 500;
  letter-spacing: 0.3px;
  position: relative;
  z-index: 2;
}

.mode-btn.active {
  color: #FF6B6B;
  font-weight: bold;
}

.mode-btn.active::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 14px;
  z-index: -1;
  animation: btn-select 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

@keyframes btn-select {
  from { transform: scale(0.7); opacity: 0.5; }
  to { transform: scale(1); opacity: 1; }
}

.mode-btn:hover:not(.active) {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.chat-actions {
  display: flex;
  gap: 10px;
}

.chat-actions button {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  overflow: hidden;
  position: relative;
}

.chat-actions button::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: scale(0);
  opacity: 0;
  transition: all 0.4s ease-out;
}

.chat-actions button:active::after {
  transform: scale(2);
  opacity: 0;
  transition: 0s;
}

.chat-actions button:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.chat-actions button svg {
  transition: transform 0.2s ease;
}

.chat-actions button:active svg {
  transform: scale(0.85);
}

.chat-messages {
  flex: 1;
  padding: 18px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background-color: #FAFAFF;
  scroll-behavior: smooth;
  position: relative;
  z-index: 1;
}

.message-wrapper {
  display: flex;
  align-items: flex-end;
  margin-bottom: 4px;
  animation: message-appear 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  transform-origin: bottom;
}

@keyframes message-appear {
  from { opacity: 0; transform: translateY(10px) scale(0.9); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

.user-wrapper {
  justify-content: flex-end;
}

.assistant-wrapper {
  justify-content: flex-start;
}

.user-avatar, .assistant-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
  margin-right: 8px;
  overflow: visible;
  filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.1));
  animation: avatar-pop 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) 0.2s backwards;
}

@keyframes avatar-pop {
  from { transform: scale(0); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.avatar-mini {
  font-size: 14px;
}

.user-avatar {
  margin-left: 8px;
  background-color: #e9eef8;
}

.assistant-avatar {
  margin-right: 8px;
}

.message {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  font-size: 14px;
  position: relative;
  word-break: break-word;
  line-height: 1.5;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  letter-spacing: 0.3px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.message:hover {
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.06);
  transform: translateY(-1px);
}

.message-content {
  margin-bottom: 5px;
}

.message-time {
  font-size: 10px;
  color: rgba(0, 0, 0, 0.4);
  text-align: right;
}

.user-message {
  background: linear-gradient(135deg, #54C8E8, #4A90F2);
  color: white;
  border-bottom-right-radius: 4px;
}

.user-message .message-time {
  color: rgba(255, 255, 255, 0.8);
}

.assistant-message {
  background-color: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.08);
}

.chat-input-container {
  padding: 16px;
  display: flex;
  align-items: center;
  background-color: white;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 1;
}

.input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background-color: #f8faff;
  border: 1px solid rgba(74, 144, 242, 0.1);
  border-radius: 24px;
  padding: 4px 6px 4px 18px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.input-wrapper:focus-within {
  border-color: rgba(74, 144, 242, 0.5);
  box-shadow: 0 0 0 3px rgba(74, 144, 242, 0.15);
  background-color: white;
  animation: input-focus 0.3s ease;
}

@keyframes input-focus {
  0% { transform: scale(0.98); }
  50% { transform: scale(1.01); }
  100% { transform: scale(1); }
}

.chat-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 10px 0;
  font-size: 15px;
  outline: none;
  color: #333;
  font-family: inherit;
}

.chat-input::placeholder {
  color: #b0b9c5;
}

.input-wrapper:focus-within .chat-input::placeholder {
  color: #cbd5e0;
}

.mic-btn {
  background-color: transparent;
  color: #a0aec0;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  margin-left: 4px;
  position: relative;
  overflow: hidden;
}

.mic-btn::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(74, 144, 242, 0.1);
  border-radius: 50%;
  transform: scale(0);
  opacity: 0;
  transition: all 0.4s ease-out;
}

.mic-btn:active::after {
  transform: scale(2);
  opacity: 0;
  transition: 0s;
}

.mic-btn:hover {
  color: #4A90F2;
  background-color: rgba(74, 144, 242, 0.08);
  transform: translateY(-2px) scale(1.05);
}

.mic-btn.listening {
  background-color: #FF6B6B;
  color: white;
  animation: mic-pulse 1.5s infinite;
}

.send-btn {
  background: linear-gradient(135deg, #54C8E8, #4A90F2);
  color: white;
  border: none;
  border-radius: 50%;
  width: 44px;
  height: 44px;
  margin-left: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 3px 8px rgba(74, 144, 242, 0.3);
  position: relative;
  overflow: hidden;
}

.send-btn::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  transform: scale(0);
  opacity: 0;
  transition: all 0.4s ease-out;
}

.send-btn:active::after {
  transform: scale(2);
  opacity: 0;
  transition: 0s;
}

.send-btn svg {
  transition: transform 0.2s ease;
}

.send-btn:active svg {
  transform: scale(0.85);
}

.send-btn:hover {
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 4px 12px rgba(74, 144, 242, 0.4);
}

.send-btn:active {
  transform: translateY(0) scale(0.95);
  box-shadow: 0 2px 6px rgba(74, 144, 242, 0.3);
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px 14px;
  background-color: white;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 1;
}

.quick-action-btn {
  background: linear-gradient(135deg, #FFEFD0, #FFDA8A);
  color: #83580B;
  border: none;
  border-radius: 16px;
  padding: 8px 14px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 2px 4px rgba(255, 203, 107, 0.3);
  letter-spacing: 0.3px;
  position: relative;
  overflow: hidden;
}

.quick-action-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 150%;
  height: 150%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3), transparent 70%);
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  transition: all 0.5s ease-out;
  border-radius: 50%;
}

.quick-action-btn:active::after {
  transform: translate(-50%, -50%) scale(1);
  opacity: 0;
  transition: 0s;
}

.quick-action-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 4px 8px rgba(255, 203, 107, 0.4);
}

.quick-action-btn:active {
  transform: translateY(0) scale(0.95);
}

.typing {
  display: flex;
  align-items: center;
  height: 20px;
}

.dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #FF6B6B;
  border-radius: 50%;
  margin: 0 2px;
  animation: typing 1.5s infinite ease-in-out;
}

.dot:nth-child(1) { animation-delay: 0s; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-6px); }
}

@keyframes mic-pulse {
  0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 107, 107, 0.5); }
  70% { transform: scale(1.05); box-shadow: 0 0 0 10px rgba(255, 107, 107, 0); }
  100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 107, 107, 0); }
}

.recommendation-buttons {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.recommendation-text {
  font-size: 12px;
  color: #333;
  margin-bottom: 5px;
}

.recommendation-actions {
  display: flex;
  gap: 8px;
}

.recommendation-btn {
  background: linear-gradient(135deg, #54C8E8, #4A90F2);
  color: white;
  border: none;
  border-radius: 12px;
  padding: 5px 10px;
  font-size: 11px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 2px 4px rgba(74, 144, 242, 0.3);
  position: relative;
  overflow: hidden;
}

.recommendation-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3), transparent 70%);
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  transition: all 0.5s ease-out;
  border-radius: 50%;
}

.recommendation-btn:active::after {
  transform: translate(-50%, -50%) scale(1);
  opacity: 0;
  transition: 0s;
}

.recommendation-btn:hover {
  background: linear-gradient(135deg, #4ABADB, #3A80E2);
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 4px 8px rgba(74, 144, 242, 0.4);
}

.recommendation-btn:active {
  transform: translateY(0) scale(0.95);
}

.jump-options {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  width: 100%;
}

.jump-options-text {
  font-size: 12px;
  margin-bottom: 6px;
  color: #666;
  white-space: normal;
  word-break: break-all;
}

.jump-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 6px;
  width: 100%;
}

.jump-btn {
  border: none;
  border-radius: 12px;
  padding: 8px 14px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
  overflow: hidden;
  margin: 2px;
  flex: 1;
  min-width: 80px;
  max-width: 160px;
}

.jump-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 150%;
  height: 150%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3), transparent 70%);
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  transition: all 0.5s ease-out;
  border-radius: 50%;
}

.jump-btn:active::after {
  transform: translate(-50%, -50%) scale(1);
  opacity: 0;
  transition: 0s;
}

.jump-btn:hover {
  transform: translateY(-2px);
}

.jump-btn:active {
  transform: translateY(0) scale(0.95);
}

.product-btn {
  background: linear-gradient(135deg, #54C8E8, #4A90F2);
  color: white;
  box-shadow: 0 2px 4px rgba(74, 144, 242, 0.3);
}

.product-btn:hover {
  box-shadow: 0 4px 8px rgba(74, 144, 242, 0.4);
}

.encyclopedia-btn {
  background: linear-gradient(135deg, #6EE7B7, #3B82F6);
  color: white;
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.3);
}

.encyclopedia-btn:hover {
  box-shadow: 0 4px 8px rgba(59, 130, 246, 0.4);
}

.cancel-btn {
  background: linear-gradient(135deg, #E5E7EB, #D1D5DB);
  color: #4B5563;
  box-shadow: 0 2px 4px rgba(75, 85, 99, 0.2);
}

.cancel-btn:hover {
  box-shadow: 0 4px 8px rgba(75, 85, 99, 0.3);
}
</style>

<script lang="ts">
declare global {
  interface Window {
    PIXI: any;
    SpeechRecognition?: any;
    webkitSpeechRecognition?: any;
  }
}
</script> 