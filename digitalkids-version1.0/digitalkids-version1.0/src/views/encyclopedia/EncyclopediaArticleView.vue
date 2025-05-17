<template>
  <div class="article-detail-container">
    <div v-if="isLoading" class="loading-container">
      <div class="spinner">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
      <p>正在加载文章...</p>
    </div>
    <div v-else-if="error" class="error-container">
      <div class="error-icon">
        <i class="fas fa-exclamation-circle"></i>
      </div>
      <h3>加载失败</h3>
      <p>{{ error }}</p>
      <button class="back-button" @click="goBack">
        <i class="fas fa-arrow-left"></i> 返回列表
      </button>
    </div>
    <div v-else-if="article" class="article-content-wrapper">
      <div class="container">
        <div class="back-nav">
          <button class="back-button" @click="goBack">
            <i class="fas fa-arrow-left"></i> 返回列表
          </button>
        </div>
        <article class="article-body">
          <header class="article-header">
            <h1 class="article-title">{{ article.title }}</h1>
            <div class="article-meta">
              <span class="article-date">
                <i class="far fa-calendar-alt"></i> {{ formatDate(article.createTime) }}
              </span>
              <span class="article-views">
                <i class="fas fa-eye"></i> {{ article.views || 0 }} 阅读
              </span>
            </div>
          </header>
          <div class="article-content" v-html="article.content"></div>
          <div class="article-author">
            <div class="author-info">
              <div class="author-avatar">
                <img src="/src/assets/智塑.png" alt="作者头像">
              </div>
              <div class="author-meta">
                <h4 class="author-name">系统管理员</h4>
                <p class="author-role">官方发布</p>
              </div>
            </div>
          </div>
          <div class="article-actions">
            <button 
              class="action-button like-button" 
              :class="{ 'active': article.likeStatus === 1 }"
              @click="handleLike"
            >
              <i class="fas fa-heart"></i>
              <span class="action-count">{{ article.likeCount || 0 }}</span>
              <span class="action-text">{{ article.likeStatus === 1 ? '已点赞' : '点赞' }}</span>
            </button>
            <button 
              class="action-button favorite-button"
              :class="{ 'active': isFavorite }"
              @click="handleFavorite"
            >
              <i class="fas fa-star"></i>
              <span class="action-text">{{ isFavorite ? '已收藏' : '收藏' }}</span>
            </button>
          </div>
        </article>
        <section class="comments-section">
          <h2 class="comments-title">
            <i class="fas fa-comments"></i> 评论区
            <span class="comments-count">{{ article.commentVos?.length || 0 }}条评论</span>
          </h2>
          <div class="comment-form">
            <div class="comment-avatar">
              <img :src="userAvatar" alt="用户头像">
            </div>
            <div class="comment-input-wrapper">
              <textarea 
                class="comment-input" 
                placeholder="写下你的想法..." 
                v-model="commentContent"
                @focus="showCommentActions = true"
              ></textarea>
              <div class="comment-actions" v-if="showCommentActions">
                <button class="cancel-button" @click="cancelComment">取消</button>
                <button 
                  class="submit-button" 
                  :disabled="!commentContent.trim()" 
                  @click="submitComment(null)"
                >
                  发布评论
                </button>
              </div>
            </div>
          </div>
          <div class="comments-list" v-if="article.commentVos && article.commentVos.length > 0">
            <div 
              v-for="comment in article.commentVos" 
              :key="comment.id" 
              class="comment-item"
            >
              <div class="comment-avatar">
                <img :src="comment.getUserVo?.avatar || defaultAvatar" alt="评论者头像">
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.getUserVo?.name || '匿名用户' }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-footer">
                  <button class="comment-action" @click="replyToComment(comment)">
                    <i class="fas fa-reply"></i> 回复
                  </button>
                  <button 
                    class="comment-action" 
                    :class="{ 'active': comment.likeStatus === 1 }"
                    @click="likeComment(comment)"
                  >
                    <i class="fas fa-heart"></i> 
                    <span>{{ comment.likeCount || 0 }}</span>
                  </button>
                </div>
                <div class="reply-form" v-if="activeReplyId === comment.id">
                  <div class="comment-input-wrapper">
                    <textarea 
                      class="comment-input" 
                      placeholder="回复评论..." 
                      v-model="replyContent"
                    ></textarea>
                    <div class="comment-actions">
                      <button class="cancel-button" @click="cancelReply">取消</button>
                      <button 
                        class="submit-button" 
                        :disabled="!replyContent.trim()" 
                        @click="submitComment(comment.id)"
                      >
                        发布回复
                      </button>
                    </div>
                  </div>
                </div>
                <div 
                  class="sub-comments" 
                  v-if="comment.commentSonVoList && comment.commentSonVoList.length > 0"
                >
                  <div 
                    v-for="(subComment, index) in comment.commentSonVoList" 
                    :key="index" 
                    class="sub-comment-item"
                  >
                    <div class="comment-avatar">
                      <img :src="subComment.getUserVo?.avatar || defaultAvatar" alt="回复者头像">
                    </div>
                    <div class="comment-content">
                      <div class="comment-header">
                        <span class="comment-author">{{ subComment.getUserVo?.name || '匿名用户' }}</span>
                      </div>
                      <div class="comment-text">{{ subComment.content }}</div>
                      <div class="comment-footer">
                        <button 
                          class="comment-action" 
                          :class="{ 'active': subComment.likeStatus === 1 }"
                          @click="likeSubComment(subComment, comment)"
                        >
                          <i class="fas fa-heart"></i> 
                          <span>{{ subComment.likeCount || 0 }}</span>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="no-comments" v-else>
            <div class="no-comments-icon">
              <i class="far fa-comment-dots"></i>
            </div>
            <p>暂无评论，来发表第一条评论吧</p>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { 
  getEncyclopediaDetail, 
  addComment, 
  like,
  addFavorite,
  deleteFavorite,
  type EncyclopediaDetailVo,
  type CommentVo,
  type CommentSonVo 
} from '@/api/encyclopedia';

const defaultAvatar = '/public/images/default-avatar.jpg';
const userAvatar = computed(() => {
  return authStore.user?.avatar || defaultAvatar;
});
const authorAvatar = ref(defaultAvatar);
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const articleId = computed(() => Number(route.params.id));
const article = ref<EncyclopediaDetailVo | null>(null);
const isLoading = ref(true);
const error = ref('');
const commentContent = ref('');
const showCommentActions = ref(false);
const activeReplyId = ref<number | null>(null);
const replyContent = ref('');
const isFavorite = ref(false);
const favoriteId = ref<number | null>(null);

const formatDate = (dateString: string) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\//g, '-');
};

const loadArticle = async () => {
  isLoading.value = true;
  error.value = '';
  try {
    const favoriteInfo = localStorage.getItem(`article_favorite_${articleId.value}`);
    if (favoriteInfo) {
      const { id, status } = JSON.parse(favoriteInfo);
      favoriteId.value = id;
      isFavorite.value = status === 1;
    }
    const response = await getEncyclopediaDetail(articleId.value);
    if (response.code === 1 && response.data) {
      article.value = response.data;
    } else {
      error.value = response.msg || '获取文章详情失败';
    }
  } catch (err: any) {
    error.value = err.message || '加载文章时发生错误，请稍后再试';
  } finally {
    isLoading.value = false;
  }
};

const goBack = () => {
  router.back();
};

const handleLike = async () => {
  if (!article.value) return;
  try {
    const response = await like(1, article.value.id);
    if (response.code === 1 && response.data) {
      article.value.likeStatus = response.data.likeStatus;
      article.value.likeCount = response.data.entityLikeCount;
      if (response.data.likeStatus === 1) {
        showLikeAnimation();
      }
    }
  } catch (err) {
  }
};

const handleFavorite = async () => {
  if (!article.value) return;
  try {
    const currentUser = authStore.user;
    if (!currentUser || !currentUser.id) {
      alert('请先登录后再收藏文章');
      router.push('/login');
      return;
    }
    const userId = currentUser.id;
    if (isFavorite.value && favoriteId.value) {
      const response = await deleteFavorite(favoriteId.value);
      if (response.code === 1) {
        isFavorite.value = false;
        favoriteId.value = null;
        localStorage.removeItem(`article_favorite_${articleId.value}`);
      }
    } else {
      const response = await addFavorite({
        entityType: 1,
        entityId: article.value.id,
        userId: userId
      });
      if (response.code === 1 && response.data) {
        isFavorite.value = true;
        favoriteId.value = response.data;
        showFavoriteAnimation();
        localStorage.setItem(`article_favorite_${articleId.value}`, JSON.stringify({
          id: response.data,
          status: 1
        }));
      }
    }
  } catch (err) {
  }
};

const submitComment = async (targetId: number | null) => {
  if (!article.value) return;
  const content = targetId ? replyContent.value : commentContent.value;
  if (!content.trim()) return;
  try {
    let finalTargetId = 0;
    if (targetId) {
      const commentToReply = article.value.commentVos.find(comment => comment.id === targetId);
      if (commentToReply && commentToReply.getUserVo) {
        finalTargetId = commentToReply.getUserVo.id;
      }
    }
    const response = await addComment({
      entityType: 1,
      entityId: article.value.id,
      targetId: finalTargetId,
      content: content.trim()
    });
    if (response.code === 1) {
      await loadArticle();
      if (targetId) {
        replyContent.value = '';
        activeReplyId.value = null;
      } else {
        commentContent.value = '';
        showCommentActions.value = false;
      }
    }
  } catch (err) {
  }
};

const replyToComment = (comment: CommentVo) => {
  activeReplyId.value = comment.id;
  replyContent.value = '';
};

const cancelComment = () => {
  commentContent.value = '';
  showCommentActions.value = false;
};

const cancelReply = () => {
  replyContent.value = '';
  activeReplyId.value = null;
};

const likeComment = async (comment: CommentVo) => {
  try {
    const response = await like(3, comment.id, comment.getUserVo.id);
    if (response.code === 1 && response.data) {
      comment.likeStatus = response.data.likeStatus;
      comment.likeCount = response.data.entityLikeCount;
    }
  } catch (err) {
  }
};

const likeSubComment = async (subComment: CommentSonVo, parentComment: CommentVo) => {
  try {
    const response = await like(3, subComment.id, subComment.getUserVo.id);
    if (response.code === 1 && response.data) {
      subComment.likeStatus = response.data.likeStatus;
      subComment.likeCount = response.data.entityLikeCount;
    }
  } catch (err) {
  }
};

const showLikeAnimation = () => {
  const likeButton = document.querySelector('.like-button');
  if (likeButton) {
    likeButton.classList.add('animate');
    setTimeout(() => {
      likeButton.classList.remove('animate');
    }, 1000);
  }
};

const showFavoriteAnimation = () => {
  const favoriteButton = document.querySelector('.favorite-button');
  if (favoriteButton) {
    favoriteButton.classList.add('animate');
    setTimeout(() => {
      favoriteButton.classList.remove('animate');
    }, 1000);
  }
};

onMounted(() => {
  loadArticle();
});
</script>

<style scoped>
.article-detail-container {
  background-color: #f9fafb;
  min-height: 100vh;
  padding-bottom: 3rem;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
}

.spinner {
  font-size: 2.5rem;
  color: #41B883;
  margin-bottom: 1rem;
  animation: spin 1.5s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-container p {
  font-size: 1.125rem;
  color: #6b7280;
}

.error-container {
  text-align: center;
  padding: 3rem 0;
  height: 60vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.error-icon {
  font-size: 3.5rem;
  color: #ef4444;
  margin-bottom: 1.5rem;
}

.error-container h3 {
  font-size: 1.75rem;
  color: #1f2937;
  margin-bottom: 0.75rem;
}

.error-container p {
  font-size: 1.125rem;
  color: #6b7280;
  max-width: 500px;
  margin: 0 auto 2rem;
}

.back-nav {
  padding: 1.5rem 0;
}

.back-button {
  background: transparent;
  border: none;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #6b7280;
  font-size: 1rem;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.back-button:hover {
  color: #41B883;
  background: rgba(65, 184, 131, 0.1);
}

.article-body {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  padding: 3rem;
}

.article-header {
  margin-bottom: 2.5rem;
  border-bottom: 1px solid #f3f4f6;
  padding-bottom: 1.5rem;
}

.article-title {
  font-size: 2.25rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 1rem;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.article-date,
.article-views {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  color: #6b7280;
}

.article-content {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #374151;
  margin-bottom: 3rem;
}

.article-author {
  margin: 3rem 0;
  padding: 1.5rem;
  background: #f9fafb;
  border-radius: 10px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-meta {
  flex: 1;
}

.author-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 0.25rem;
}

.author-role {
  font-size: 0.9rem;
  color: #6b7280;
}

.article-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding-top: 1.5rem;
  border-top: 1px solid #f3f4f6;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  padding: 0.75rem 1.25rem;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 30px;
  color: #6b7280;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-button:hover {
  background: #f9fafb;
  border-color: #d1d5db;
}

.action-button.active {
  background: rgba(65, 184, 131, 0.1);
  border-color: #41B883;
  color: #41B883;
}

.action-count {
  font-size: 0.95rem;
}

.action-button.animate {
  animation: pulse 1s ease;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.15); }
  100% { transform: scale(1); }
}

.comments-section {
  margin-top: 3rem;
}

.comments-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.comments-count {
  font-size: 1rem;
  font-weight: 400;
  color: #6b7280;
  margin-left: 0.5rem;
}

.comment-form {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.comment-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.comment-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-input-wrapper {
  flex: 1;
}

.comment-input {
  width: 100%;
  min-height: 100px;
  padding: 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  font-size: 1rem;
  resize: vertical;
  outline: none;
  transition: border-color 0.2s ease;
}

.comment-input:focus {
  border-color: #41B883;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 0.75rem;
}

.cancel-button,
.submit-button {
  padding: 0.65rem 1.25rem;
  border-radius: 6px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.cancel-button {
  background: transparent;
  border: 1px solid #e5e7eb;
  color: #6b7280;
}

.cancel-button:hover {
  background: #f9fafb;
  border-color: #d1d5db;
}

.submit-button {
  background: #41B883;
  border: 1px solid #41B883;
  color: white;
}

.submit-button:hover {
  background: #3aa876;
}

.submit-button:disabled {
  background: #9ca3af;
  border-color: #9ca3af;
  cursor: not-allowed;
}

.comments-list {
  margin-top: 1.5rem;
}

.comment-item {
  display: flex;
  gap: 1rem;
  padding: 1.5rem;
  background: white;
  border-radius: 10px;
  margin-bottom: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: 600;
  color: #111827;
  font-size: 1.05rem;
}

.comment-text {
  color: #374151;
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 0.75rem;
}

.comment-footer {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-top: 0.75rem;
}

.comment-action {
  background: transparent;
  border: none;
  color: #6b7280;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.35rem;
  cursor: pointer;
  transition: color 0.2s ease;
}

.comment-action:hover {
  color: #41B883;
}

.comment-action.active {
  color: #41B883;
}

.reply-form {
  margin: 1rem 0;
  padding-left: 0.5rem;
  border-left: 2px solid #f3f4f6;
}

.sub-comments {
  margin-top: 1rem;
  padding-left: 1rem;
  border-left: 2px solid #f3f4f6;
}

.sub-comment-item {
  display: flex;
  gap: 0.75rem;
  padding: 1rem 0;
  border-bottom: 1px solid #f3f4f6;
}

.sub-comment-item:last-child {
  border-bottom: none;
}

.sub-comment-item .comment-avatar {
  width: 36px;
  height: 36px;
}

.no-comments {
  text-align: center;
  padding: 3rem 0;
  background: white;
  border-radius: 10px;
  margin-top: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.no-comments-icon {
  font-size: 3rem;
  color: #d1d5db;
  margin-bottom: 1rem;
}

.no-comments p {
  font-size: 1.05rem;
  color: #6b7280;
}

@media (max-width: 768px) {
  .article-body {
    padding: 1.5rem;
  }
  
  .article-title {
    font-size: 1.75rem;
  }
  
  .article-content {
    font-size: 1rem;
  }
  
  .article-actions {
    flex-wrap: wrap;
  }
  
  .action-button {
    flex: 1;
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .article-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .comment-form {
    flex-direction: column;
    padding: 1rem;
  }
  
  .comment-avatar {
    align-self: center;
  }
  
  .article-actions {
    flex-direction: column;
  }
  
  .comment-item {
    padding: 1rem;
  }
}
</style> 