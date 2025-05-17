<template>
  <div class="search-box-container">
    <div class="search-box">
      <el-icon class="search-icon"><Search /></el-icon>
      <input 
        type="text" 
        :placeholder="placeholder"
        class="search-input"
        v-model="searchQuery"
        @input="handleSearch" 
        @keyup.enter="handleEnterSearch"
      >
      <span v-if="!searchQuery" class="enter-hint">按下 Enter 搜索</span>
      <button 
        v-if="searchQuery" 
        class="search-button"
        @click="handleEnterSearch"
        aria-label="搜索"
      >
        <el-icon><ArrowRight /></el-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search, ArrowRight } from '@element-plus/icons-vue'

const props = defineProps({
  placeholder: {
    type: String,
    default: '搜索'
  }
})

const emit = defineEmits(['search'])
const searchQuery = ref('')

const handleSearch = () => {
  emit('search', searchQuery.value)
}

const handleEnterSearch = () => {
  if (searchQuery.value.trim()) {
    emit('search', searchQuery.value)
  }
}
</script>

<style scoped>
.search-box-container {
  @apply w-full max-w-md mx-auto;
}

.search-box {
  @apply relative flex items-center shadow-sm;
}

.search-icon {
  @apply absolute left-3 text-gray-400 w-5 h-5 transition-colors duration-200;
}

.search-input {
  @apply w-full py-2.5 px-4 pl-10 pr-10 bg-white border border-gray-200 rounded-xl text-sm 
         focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 
         transition-all duration-200;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-input:hover {
  @apply border-gray-300;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.search-box:focus-within .search-icon {
  @apply text-blue-500;
}

.enter-hint {
  @apply absolute right-4 text-xs text-gray-400 pointer-events-none select-none transition-opacity duration-200;
  font-style: italic;
}

.search-button {
  @apply absolute right-3 flex items-center justify-center w-7 h-7 text-gray-400 hover:text-blue-500 
         bg-gray-100 hover:bg-blue-50 rounded-full transition-all duration-200;
}

.search-button:focus {
  @apply outline-none ring-2 ring-blue-500/50;
}

@media (max-width: 640px) {
  .search-input {
    @apply py-2 text-xs;
  }
  
  .enter-hint {
    @apply text-xs;
  }
  
  .search-button {
    @apply w-6 h-6;
  }
}
</style> 