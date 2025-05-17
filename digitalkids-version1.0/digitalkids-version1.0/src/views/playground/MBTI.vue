<template>
  <div class="min-h-screen pb-20">
    <div v-if="currentPage === 'welcome'" class="flex flex-col items-center justify-center min-h-screen px-4 py-12">
      <div class="text-center max-w-3xl mx-auto">
        <div class="floating inline-block">
          <img src="https://img.icons8.com/color/96/000000/brain.png" alt="Brain" class="w-24 h-24">
        </div>
        <h1 class="text-5xl md:text-6xl font-bold text-pink-600 title-font mb-6">MBTI人格测试</h1>
        <p class="text-xl text-gray-700 mb-8">发现真实的自己，了解你的性格类型！</p>
        <div class="bg-white rounded-2xl shadow-lg p-6 mb-8">
          <p class="text-gray-700 mb-4">✨ 这个测试包含50道题目，大约需要10-15分钟完成</p>
          <p class="text-gray-700">🎯 请根据你的第一反应选择最符合你的选项</p>
        </div>
        <button @click="startTest" class="bg-pink-500 hover:bg-pink-600 text-white font-bold py-4 px-8 rounded-full text-xl transition-all transform hover:scale-105 shadow-lg">
          开始测试 <i class="fas fa-arrow-right ml-2"></i>
        </button>
      </div>
    </div>

    <div v-if="currentPage === 'test'" class="min-h-screen px-4 py-12">
      <div class="max-w-3xl mx-auto">
        <div class="flex justify-between items-center mb-8">
          <div class="flex items-center">
            <img src="https://img.icons8.com/color/48/000000/brain.png" alt="Brain" class="w-10 h-10 mr-2">
            <span class="text-xl font-bold text-pink-600 title-font">MBTI测试</span>
          </div>
          <div class="text-gray-600">
            <span>{{ currentQuestion + 1 }}</span>/50
          </div>
        </div>
        
        <div class="progress-bar mb-8">
          <div class="progress-fill" :style="{ width: `${(currentQuestion + 1) / questions.length * 100}%` }"></div>
        </div>
        
        <div class="bg-white rounded-2xl shadow-lg p-6 mb-8">
          <h2 class="text-2xl font-bold text-gray-800 mb-6">{{ questions[currentQuestion].question }}</h2>
          
          <div class="space-y-4">
            <button 
              v-for="(option, index) in questions[currentQuestion].options" 
              :key="index"
              @click="selectOption(index)"
              :class="[
                'option-btn w-full bg-pink-50 hover:bg-pink-100 text-gray-800 py-4 px-6 rounded-xl text-left border border-pink-200 flex items-center',
                answers[currentQuestion] === index ? 'bg-pink-200 border-pink-400' : ''
              ]"
            >
              <span class="bg-pink-500 text-white rounded-full w-8 h-8 flex items-center justify-center mr-4">
                {{ index === 0 ? 'A' : 'B' }}
              </span>
              <span>{{ option }}</span>
            </button>
          </div>
        </div>
        
        <div class="flex justify-between">
          <button 
            @click="prevQuestion" 
            :disabled="currentQuestion === 0"
            :class="[
              'bg-gray-200 hover:bg-gray-300 text-gray-700 font-bold py-3 px-6 rounded-lg transition-all',
              currentQuestion === 0 ? 'opacity-50 cursor-not-allowed' : ''
            ]"
          >
            <i class="fas fa-arrow-left mr-2"></i> 上一题
          </button>
          <button 
            @click="nextQuestion" 
            class="bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-6 rounded-lg transition-all"
          >
            下一题 <i class="fas fa-arrow-right ml-2"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- 结果页面 -->
    <div v-if="currentPage === 'result'" class="min-h-screen px-4 py-12">
      <div class="max-w-4xl mx-auto text-center">
        <div class="bounce inline-block mb-6">
          <img src="https://img.icons8.com/color/96/000000/confetti.png" alt="Confetti" class="w-24 h-24">
        </div>
        <h1 class="text-5xl md:text-6xl font-bold text-pink-600 title-font mb-6">测试完成!</h1>
        <p class="text-xl text-gray-700 mb-8">你的MBTI人格类型是:</p>
        
        <div class="text-6xl font-bold text-pink-700 mb-12 title-font">{{ mbtiType }}</div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-12">
          <div class="result-card bg-white rounded-2xl shadow-lg p-6 h-full">
            <div class="result-card-inner">
              <div class="result-card-front">
                <h3 class="text-2xl font-bold text-pink-600 mb-4">性格特征</h3>
                <div class="text-gray-700 space-y-2" v-html="personalityTraits"></div>
              </div>
              <div class="result-card-back bg-pink-50 rounded-2xl p-6 h-full flex items-center justify-center">
                <p class="text-gray-700">{{ backTraitsText }}</p>
              </div>
            </div>
          </div>
          
          <div class="result-card bg-white rounded-2xl shadow-lg p-6 h-full">
            <div class="result-card-inner">
              <div class="result-card-front">
                <h3 class="text-2xl font-bold text-pink-600 mb-4">适合职业</h3>
                <div class="text-gray-700 space-y-2" v-html="careers"></div>
              </div>
              <div class="result-card-back bg-pink-50 rounded-2xl p-6 h-full flex items-center justify-center">
                <p class="text-gray-700">{{ backCareersText }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-2xl shadow-lg p-6 mb-8">
          <h3 class="text-2xl font-bold text-pink-600 mb-4">详细分析</h3>
          <div class="text-gray-700 text-left space-y-4" v-html="detailedAnalysis"></div>
        </div>
        
        <div class="flex flex-col sm:flex-row justify-center gap-4 mt-8">
          <button @click="retakeTest" class="bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-6 rounded-lg transition-all">
            <i class="fas fa-redo mr-2"></i> 重新测试
          </button>
          <button @click="shareResult" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-3 px-6 rounded-lg transition-all">
            <i class="fas fa-share-alt mr-2"></i> 分享结果
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentPage: 'welcome',
      currentQuestion: 0,
      mbtiType: '',
      personalityTraits: '',
      careers: '',
      detailedAnalysis: '',
      backTraitsText: '',
      backCareersText: '',
      answers: Array(50).fill(null),
      scores: {
        E: 0, I: 0,
        S: 0, N: 0,
        T: 0, F: 0,
        J: 0, P: 0
      },
      questions: [
        {
          question: "在社交场合中，你通常更倾向于：",
          options: [
            "主动与陌生人交谈，认识新朋友",
            "等待别人先与你交谈，或只与熟悉的人交流"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "当你做决定时，更注重：",
          options: [
            "逻辑和客观事实",
            "个人价值观和他人的感受"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢哪种学习方式？",
          options: [
            "通过实践和亲身体验",
            "通过阅读和理论分析"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "在计划旅行时，你更倾向于：",
          options: [
            "提前安排好详细的行程",
            "灵活安排，随性而为"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "当遇到问题时，你通常：",
          options: [
            "立即采取行动解决",
            "先花时间思考各种可能性"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更相信：",
          options: [
            "确凿的证据和具体事实",
            "灵感和直觉"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "在团队中，你更愿意：",
          options: [
            "跟随大多数人的意见",
            "坚持自己的观点"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢的工作环境是：",
          options: [
            "有计划、有组织的",
            "灵活、自由的"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "周末你更愿意：",
          options: [
            "参加社交活动，与人相处",
            "独自或与亲密的人安静度过"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更擅长：",
          options: [
            "处理具体细节",
            "看到全局和大方向"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "当别人向你倾诉问题时，你更倾向于：",
          options: [
            "提供解决方案和建议",
            "给予情感支持和理解"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更倾向于认为自己是：",
          options: [
            "现实主义者",
            "理想主义者"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "在完成任务时，你通常：",
          options: [
            "提前完成，不喜欢拖延",
            "在截止日期前完成"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "你更看重：",
          options: [
            "公平和正义",
            "和谐与同情"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "按部就班地生活",
            "追求变化和新鲜感"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "在聚会中，你通常：",
          options: [
            "是最后一个离开的人",
            "提前离开或希望早点结束"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更相信：",
          options: [
            "经验证明的东西",
            "未来的可能性"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "当批评别人时，你更注重：",
          options: [
            "直接指出问题",
            "委婉表达，考虑对方感受"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "确定性和稳定性",
            "灵活性和自发性"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "在思考时，你更倾向于：",
          options: [
            "大声说出来",
            "先在内心思考清楚"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更关注：",
          options: [
            "当下的现实",
            "未来的可能性"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "在做决定时，你更依赖：",
          options: [
            "客观标准",
            "个人价值观"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "事先计划好的活动",
            "即兴的活动"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "广泛交友",
            "有几个亲密朋友"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更相信：",
          options: [
            "实际经验",
            "理论原则"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更重视：",
          options: [
            "真理和正确性",
            "和谐和善意"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "有明确规则的环境",
            "可以自由发挥的环境"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "在社交场合中，你通常：",
          options: [
            "主动发起谈话",
            "等待别人先开口"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "关注具体事实",
            "寻找隐藏的含义"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更可能：",
          options: [
            "坚持原则，即使不受欢迎",
            "妥协以维持和谐"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "提前做决定",
            "保留选择权"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "表达自己的想法",
            "倾听他人的观点"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更关注：",
          options: [
            "实际和具体的事物",
            "象征和隐喻"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更可能：",
          options: [
            "批评别人的错误",
            "理解别人的处境"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "按计划行事",
            "随遇而安"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "在社交活动中，你通常：",
          options: [
            "感到精力充沛",
            "感到消耗精力"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "关注现实细节",
            "想象各种可能性"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更重视：",
          options: [
            "逻辑一致性",
            "情感和谐"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "有明确目标",
            "保持开放选择"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "主动与人交往",
            "等待别人接近"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更相信：",
          options: [
            "实际经验",
            "直觉预感"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更可能：",
          options: [
            "直言不讳",
            "委婉表达"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "有结构的生活",
            "灵活的生活"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "在社交场合中，你通常：",
          options: [
            "容易结识新朋友",
            "感到不自在"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "关注现实",
            "想象未来"
          ],
          dimension: "SN",
          direction: 1
        },
        {
          question: "你更重视：",
          options: [
            "客观真理",
            "个人价值观"
          ],
          dimension: "TF",
          direction: 1
        },
        {
          question: "你更喜欢：",
          options: [
            "提前规划",
            "随机应变"
          ],
          dimension: "JP",
          direction: 1
        },
        {
          question: "你更倾向于：",
          options: [
            "外向表达",
            "内向思考"
          ],
          dimension: "EI",
          direction: 1
        },
        {
          question: "你更关注：",
          options: [
            "具体事实",
            "整体概念"
          ],
          dimension: "SN",
          direction: 1
        }
      ],
      mbtiDescriptions: {
        "ISTJ": {
          traits: ["• 实际且现实", "• 有责任感", "• 逻辑性强", "• 注重细节"],
          careers: ["• 会计师", "• 审计师", "• 行政人员", "• 军事人员"],
          analysis: "ISTJ型的人是可靠且尽责的，他们重视传统和秩序。作为务实的人，ISTJ喜欢遵循既定的规则和程序，并期望他人也能这样做。他们非常注重细节，擅长处理事实和数据。ISTJ型的人通常安静、严肃，通过认真和可靠赢得成功。"
        },
        "ISFJ": {
          traits: ["• 温暖且关心他人", "• 有责任感", "• 注重细节", "• 传统"],
          careers: ["• 护士", "• 教师", "• 社工", "• 行政助理"],
          analysis: "ISFJ型的人是温暖、友好且负责任的，他们有着强烈的服务意识。他们安静、认真，通过细致和可靠赢得成功。ISFJ型的人非常注重他人的感受，并努力创造和谐的环境。他们重视安全和传统，擅长处理实际细节。"
        },
        "INFJ": {
          traits: ["• 富有同情心和洞察力", "• 理想主义者", "• 有创造力", "• 深思熟虑"],
          careers: ["• 心理咨询师", "• 作家", "• 教师", "• 社会工作者"],
          analysis: "INFJ是16种人格类型中最稀有的类型之一，仅占人口的1-2%。他们通常被称为'倡导者'或'理想主义者'。INFJ型的人有强烈的直觉能力，能够理解复杂的模式和人际关系。他们通常有深刻的价值观，并努力使世界变得更美好。在人际关系中，INFJ型的人忠诚且富有同情心，但可能需要时间向他人敞开心扉。他们重视深度和有意义的联系。"
        },
        "INTJ": {
          traits: ["• 独立思考者", "• 战略思想家", "• 有决心", "• 创新"],
          careers: ["• 科学家", "• 工程师", "• 系统分析师", "• 管理顾问"],
          analysis: "INTJ型的人是原创思想家，有着强烈的动力和创新的想法。他们有着敏锐的洞察力，能够看到可能性，并制定全面的策略来实现目标。INTJ型的人通常独立、果断，对自己的能力和判断充满信心。他们重视知识、能力和效率，擅长长期规划和目标设定。"
        },
        "ISTP": {
          traits: ["• 冷静且灵活", "• 行动导向", "• 逻辑性强", "• 务实"],
          careers: ["• 工程师", "• 技工", "• 运动员", "• 警察"],
          analysis: "ISTP型的人是冷静的观察者，擅长处理危机和实际问题。他们通常是行动导向的，喜欢通过动手实践来学习。ISTP型的人逻辑性强、务实，对机械和工具有着天生的理解力。他们通常安静且保留，喜欢自由和灵活性，不喜欢过多的规则和限制。"
        },
        "ISFP": {
          traits: ["• 温和且敏感", "• 艺术气质", "• 灵活", "• 注重当下"],
          careers: ["• 艺术家", "• 音乐家", "• 设计师", "• 护士"],
          analysis: "ISFP型的人是安静、友好且敏感的，他们有着强烈的艺术感和审美观。他们活在当下，享受生活带来的简单乐趣。ISFP型的人通常谦逊且保留，不喜欢将自己的价值观强加于人。他们重视个人自由，喜欢灵活、自发的生活方式。ISFP型的人擅长处理实际问题和创造美丽的事物。"
        },
        "INFP": {
          traits: ["• 理想主义者", "• 有创造力", "• 忠诚", "• 价值观驱动"],
          careers: ["• 作家", "• 心理咨询师", "• 艺术家", "• 教师"],
          analysis: "INFP型的人是理想主义者，忠于自己的价值观和重要的人。他们希望外部生活与自己的内在价值观一致。INFP型的人好奇且善于观察，能够看到可能性。他们通常是适应性强且灵活的，除非他们的价值观受到威胁。INFP型的人通常安静、保留，喜欢深度、有意义的关系。"
        },
        "INTP": {
          traits: ["• 逻辑性强", "• 创新", "• 独立思考者", "• 理论导向"],
          careers: ["• 科学家", "• 哲学家", "• 程序员", "• 建筑师"],
          analysis: "INTP型的人是创新者，对知识有着永不满足的渴望。他们逻辑性强、精确，擅长理论思考。INTP型的人通常是安静且保留的，喜欢独自工作。他们重视自主性和智力自由，擅长分析复杂问题并提出原创解决方案。INTP型的人喜欢探索新的想法和可能性，有时会忽视实际细节。"
        },
        "ESTP": {
          traits: ["• 行动导向", "• 灵活", "• 现实", "• 善于解决问题"],
          careers: ["• 企业家", "• 销售人员", "• 运动员", "• 急救人员"],
          analysis: "ESTP型的人是行动导向的，享受即时行动和结果。他们灵活且适应性强，喜欢通过实践经验学习。ESTP型的人通常是外向、现实且务实的，擅长处理危机和实际问题。他们喜欢自由和灵活性，不喜欢过多的规则和限制。ESTP型的人通常善于观察，能够快速评估情况并采取行动。"
        },
        "ESFP": {
          traits: ["• 外向且友好", "• 乐观", "• 注重当下", "• 喜欢帮助他人"],
          careers: ["• 演员", "• 主持人", "• 销售人员", "• 儿童护理"],
          analysis: "ESFP型的人是外向、友好且接受的，他们热爱生活、人和物质享受。他们喜欢与他人一起工作，使事情变得有趣。ESFP型的人灵活且自发，适应性强，喜欢通过实践经验学习。他们通常乐观且充满活力，喜欢成为关注的焦点。ESFP型的人擅长处理人际关系和创造愉快的氛围。"
        },
        "ENFP": {
          traits: ["• 热情且富有想象力", "• 有创造力", "• 善于沟通", "• 灵活"],
          careers: ["• 咨询师", "• 教师", "• 演员", "• 记者"],
          analysis: "ENFP型的人是热情且富有想象力的，他们能够看到生活中各种可能性。他们通常是外向、友好且接受的，擅长处理人际关系。ENFP型的人灵活且适应性强，喜欢通过探索新的想法和可能性来学习。他们通常乐观且充满活力，喜欢激励他人。ENFP型的人重视自由和多样性，擅长创造积极的变化。"
        },
        "ENTP": {
          traits: ["• 创新", "• 善于解决问题", "• 独立思考者", "• 善于沟通"],
          careers: ["• 企业家", "• 律师", "• 发明家", "• 营销人员"],
          analysis: "ENTP型的人是创新者，喜欢探索新的想法和可能性。他们通常是外向、友好且接受的，擅长处理人际关系。ENTP型的人逻辑性强、精确，擅长理论思考。他们喜欢智力挑战，擅长辩论和解决问题。ENTP型的人通常灵活且适应性强，喜欢自由和多样性。"
        },
        "ESTJ": {
          traits: ["• 实际且现实", "• 有组织能力", "• 有责任感", "• 传统"],
          careers: ["• 经理", "• 行政人员", "• 警察", "• 军官"],
          analysis: "ESTJ型的人是务实且现实的，他们喜欢组织和执行事情。他们通常是外向、友好且接受的，擅长处理实际细节。ESTJ型的人有责任感，重视传统和秩序。他们喜欢遵循既定的规则和程序，并期望他人也能这样做。ESTJ型的人通常果断且直接，擅长管理和监督工作。"
        },
        "ESFJ": {
          traits: ["• 温暖且关心他人", "• 有责任感", "• 善于合作", "• 传统"],
          careers: ["• 护士", "• 教师", "• 社工", "• 行政助理"],
          analysis: "ESFJ型的人是温暖、友好且负责任的，他们有着强烈的服务意识。他们通常是外向、友好且接受的，擅长处理人际关系。ESFJ型的人有责任感，重视和谐与合作。他们喜欢与他人一起工作，使事情变得顺利。ESFJ型的人通常注重传统和秩序，擅长创造支持性的环境。"
        },
        "ENFJ": {
          traits: ["• 富有同情心", "• 善于沟通", "• 有魅力", "• 理想主义者"],
          careers: ["• 教师", "• 心理咨询师", "• 人力资源", "• 公关"],
          analysis: "ENFJ型的人是温暖、同理心且负责任的，他们有着强烈的帮助他人的愿望。他们通常是外向、友好且接受的，擅长处理人际关系。ENFJ型的人理想主义且忠诚，重视和谐与合作。他们喜欢与他人一起工作，激励他人发挥潜力。ENFJ型的人通常有魅力且善于沟通，擅长创造积极的变化。"
        },
        "ENTJ": {
          traits: ["• 果断", "• 战略思想家", "• 有领导力", "• 逻辑性强"],
          careers: ["• 经理", "• 执行官", "• 律师", "• 企业家"],
          analysis: "ENTJ型的人是果断且自信的，他们喜欢领导和组织活动。他们通常是外向、友好且接受的，擅长处理人际关系。ENTJ型的人逻辑性强、精确，擅长长期规划和目标设定。他们喜欢智力挑战，擅长解决问题和做出决策。ENTJ型的人通常有魅力且善于沟通，擅长激励他人实现目标。"
        }
      }
    };
  },
  methods: {
    startTest() {
      this.currentPage = 'test';
    },
    selectOption(optionIndex) {
      this.answers[this.currentQuestion] = optionIndex;
      
      if (this.currentQuestion < this.questions.length - 1) {
        setTimeout(() => {
          this.nextQuestion();
        }, 300);
      }
    },
    prevQuestion() {
      if (this.currentQuestion > 0) {
        this.currentQuestion--;
      }
    },
    nextQuestion() {
      if (this.currentQuestion < this.questions.length - 1) {
        this.currentQuestion++;
      } else if (this.currentQuestion === this.questions.length - 1 && this.answers[this.currentQuestion] !== null) {
        this.showResult();
      }
    },
    showResult() {
      this.calculateScores();
      

      this.mbtiType = 
        (this.scores.E > this.scores.I ? 'E' : 'I') +
        (this.scores.S > this.scores.N ? 'S' : 'N') +
        (this.scores.T > this.scores.F ? 'T' : 'F') +
        (this.scores.J > this.scores.P ? 'J' : 'P');
      

      this.currentPage = 'result';
      

      const description = this.mbtiDescriptions[this.mbtiType] || {
        traits: ["• 未知特性"],
        careers: ["• 未知职业"],
        analysis: "无法找到该类型的详细分析。"
      };

      if (!description.traits) description.traits = ["• 未知特性"];
      if (!description.careers) description.careers = ["• 未知职业"];
      if (!description.analysis) description.analysis = "无法找到该类型的详细分析。";
      
      this.personalityTraits = description.traits.map(trait => `<p>${trait}</p>`).join('');
      this.careers = description.careers.map(career => `<p>${career}</p>`).join('');
      
      const sentences = description.analysis.split('.');
      this.detailedAnalysis = sentences.map(sentence => 
        sentence.trim() ? `<p>${sentence.trim()}.</p>` : ''
      ).join('').replace(/\.\./g, '.');
      
      this.backTraitsText = sentences[0] ? sentences[0].trim() + "." : "上述职业可能更加适合您~";
      this.backCareersText = sentences[1] ? sentences[1].trim() + "." : "上述职业可能更加适合您~";
    },
    calculateScores() {
      this.questions.forEach((q, index) => {
        if (this.answers[index] !== null) {
          const dimension = q.dimension;
          const direction = q.direction;
          
          if (this.answers[index] === 0) {
            this.scores[dimension[0]] += direction;
          } else {
            this.scores[dimension[1]] += direction;
          }
        }
      });
    },
    retakeTest() {
      this.currentQuestion = 0;
      this.answers = Array(this.questions.length).fill(null);
      this.scores = {
        E: 0, I: 0,
        S: 0, N: 0,
        T: 0, F: 0,
        J: 0, P: 0
      };
      
      this.currentPage = 'test';
    },
    shareResult() {
      const shareText = `我的MBTI人格类型是${this.mbtiType}！你也来测试一下吧！`;
      const shareUrl = window.location.href;
      
      if (navigator.share) {
        navigator.share({
          title: 'MBTI人格测试结果',
          text: shareText,
          url: shareUrl
        }).catch(err => {
          this.fallbackShare(shareText, shareUrl);
        });
      } else {
        this.fallbackShare(shareText, shareUrl);
      }
    },
    fallbackShare(shareText, shareUrl) {
      const textArea = document.createElement('textarea');
      textArea.value = `${shareText}\n${shareUrl}`;
      document.body.appendChild(textArea);
      textArea.select();
      document.execCommand('copy');
      document.body.removeChild(textArea);
      
      alert('结果已复制到剪贴板，可以粘贴到任何地方分享！');
    }
  }
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&family=ZCOOL+QingKe+HuangYou&display=swap');
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css');

body {
  font-family: sans-serif;
  background-color: #fff5f5;
}

.title-font {
  font-family: 'Ma Shan Zheng', cursive;
}

.progress-bar {
  height: 10px;
  border-radius: 5px;
  background-color: #fecaca;
}

.progress-fill {
  height: 100%;
  border-radius: 5px;
  background-color: #f87171;
  transition: width 0.3s ease;
}

.option-btn {
  transition: all 0.3s ease;
}

.option-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.bounce {
  animation: bounce 1s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.floating {
  animation: floating 3s ease-in-out infinite;
}

@keyframes floating {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0px); }
}

.result-card {
  perspective: 1000px;
}

.result-card-inner {
  transition: transform 0.8s;
  transform-style: preserve-3d;
}

.result-card:hover .result-card-inner {
  transform: rotateY(180deg);
}

.result-card-front, .result-card-back {
  backface-visibility: hidden;
}

.result-card-back {
  transform: rotateY(180deg);
}
</style>