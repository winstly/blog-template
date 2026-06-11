<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import MarkdownRenderer from '@/components/shared/MarkdownRenderer.vue';
import CommentSection from '@/components/shared/CommentSection.vue';
import { articleService } from '@/api/services';
import { messageService } from '@/api/services';
import { useSiteData } from '@/composables';
import type { Article, Message } from '@/api/types';

const route = useRoute();
const { profile, fetchProfile } = useSiteData();

const article = ref<Article | null>(null);
const messages = ref<Message[]>([]);
const loading = ref(true);

onMounted(async () => {
  const code = route.params.id as string;
  try {
    const [articleResult, messagesResult] = await Promise.all([
      articleService.getByCode(code),
      messageService.getList({ page: 1, pageSize: 50 }),
    ]);
    article.value = articleResult;
    messages.value = messagesResult.list;
  } catch (e) {
    console.error('Failed to fetch article:', e);
  } finally {
    loading.value = false;
  }
  fetchProfile();
});

const renderedContent = computed(() => {
  if (!article.value) return '';
  return article.value.body ?? '';
});

const shareLinks = [
  { icon: 'fa-thumbs-up', color: '#ff6b6b' },
  { icon: 'fa-weixin', color: '#07C160' },
  { icon: 'fa-weibo', color: '#E6162D' },
  { icon: 'fa-qq', color: '#12B7F5' },
];
</script>

<template>
  <div class="article-page">
    <template v-if="!loading && article">
      <!-- 返回按钮 -->
      <RouterLink to="/blog" class="back-btn">
        <i class="fa fa-arrow-left"></i>
        <span>返回博客</span>
      </RouterLink>

      <!-- 简洁标题栏 -->
      <div class="article-banner">
        <div class="banner-content">
          <div class="meta-top">
            <span class="date"><i class="fa fa-calendar-o"></i> {{ article.publishedAt ?? article.gmtCreate }}</span>
            <span class="views"><i class="fa fa-eye"></i> {{ article.viewCount }}</span>
          </div>
          <h1>{{ article.title }}</h1>
          <div class="meta-bottom">
            <span class="author"><i class="fa fa-user"></i> {{ profile?.nickname ?? '' }}</span>
            <div class="tags">
              <RouterLink
                v-for="tag in article.tags"
                :key="tag.tagCode"
                :to="`/blog?tag=${tag.tagCode}`"
                class="tag"
              >
                {{ tag.tagName }}
              </RouterLink>
            </div>
          </div>
        </div>
      </div>

      <div class="page-content">
        <div class="container">
          <!-- 文章主体 -->
          <article class="article-main">
            <div class="article-body">
              <MarkdownRenderer :content="renderedContent" />
            </div>

            <!-- 版权声明 -->
            <div class="copyright-notice">
              <div class="notice-icon">
                <i class="fa fa-copyright"></i>
              </div>
              <div class="notice-content">
                <p class="notice-title">版权声明</p>
                <p>本文版权归 <strong>{{ profile?.nickname ?? '' }}</strong> 所有，转载请注明出处。</p>
                <p>本文链接：<RouterLink :to="`/article/${article.articleCode}`">{{ article.title }}</RouterLink></p>
              </div>
            </div>

            <!-- 分享按钮 -->
            <div class="share-section">
              <span class="share-label">分享到：</span>
              <div class="share-buttons">
                <button
                  v-for="share in shareLinks"
                  :key="share.icon"
                  class="share-btn"
                  :style="{ '--share-color': share.color }"
                >
                  <i :class="['fa', share.icon]"></i>
                </button>
              </div>
            </div>

            <!-- 评论区 -->
            <CommentSection :messages="messages" id="comments" />
          </article>
        </div>
      </div>
    </template>

    <div v-if="loading" class="loading-state">
      <i class="fa fa-spinner fa-spin"></i> 加载中...
    </div>

    <div v-if="!loading && !article" class="loading-state">
      <i class="fa fa-exclamation-circle"></i> 文章未找到
    </div>
  </div>
</template>

<style scoped>
.article-page {
  min-height: 100vh;
  background: var(--color-background);
}

/* 返回按钮 */
.back-btn {
  position: fixed;
  top: var(--spacing-xl);
  left: var(--spacing-xl);
  z-index: 50;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px var(--spacing-lg);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  text-decoration: none;
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  font-weight: 500;
  box-shadow: var(--shadow-lg);
  transition: all var(--transition-base) ease;
}

.back-btn:hover {
  box-shadow: var(--shadow-xl);
  transform: translateX(-2px);
  color: var(--color-accent-secondary);
}

.back-btn i {
  font-size: var(--font-size-xs);
}

/* 简洁标题栏 */
.article-banner {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.banner-content {
  max-width: 800px;
  margin: 0 auto;
  padding: var(--spacing-2xl) var(--spacing-xl);
}

.meta-top {
  display: flex;
  gap: var(--spacing-lg);
  margin-bottom: 10px;
}

.meta-top span {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: var(--font-size-sm);
  color: var(--color-text-light);
}

.meta-top i {
  font-size: var(--font-size-xs);
}

.banner-content h1 {
  margin: 0 0 var(--spacing-md);
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--color-text-dark);
  line-height: 1.4;
}

.meta-bottom {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.author {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: var(--font-size-base);
  color: var(--color-accent-secondary);
}

.author i {
  font-size: var(--font-size-xs);
}

.tags {
  display: flex;
  gap: 6px;
}

.tag {
  background: var(--color-surface-tertiary);
  color: var(--color-text-light);
  padding: 3px 10px;
  border-radius: 10px;
  font-size: var(--font-size-xs);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.tag:hover {
  background: var(--color-accent-secondary);
  color: var(--color-text-on-dark);
}

/* 页面内容 */
.page-content {
  padding: var(--spacing-xl) 15px 60px;
}

.container {
  max-width: 800px;
  margin: 0 auto;
}

/* 文章主体 */
.article-main {
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  padding: 28px;
  box-shadow: var(--shadow-sm);
}

.article-body {
  color: var(--color-text-secondary);
  line-height: 1.9;
}

/* 版权声明 */
.copyright-notice {
  display: flex;
  gap: 14px;
  background: var(--color-surface-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  margin: var(--spacing-2xl) 0;
  border-left: 3px solid var(--color-accent-secondary);
}

.notice-icon {
  width: 36px;
  height: 36px;
  background: var(--gradient-accent);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.notice-icon i {
  color: var(--color-text-on-dark);
  font-size: var(--font-size-base);
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-weight: 600;
  color: var(--color-text-dark);
  margin: 0 0 6px;
  font-size: var(--font-size-base);
}

.notice-content p {
  margin: 0;
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
  line-height: 1.7;
}

.notice-content a {
  color: var(--color-accent-secondary);
  text-decoration: none;
}

.notice-content a:hover {
  text-decoration: underline;
}

/* 分享按钮 */
.share-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
  padding: var(--spacing-xl) 0;
  border-top: 1px solid var(--color-border);
  border-bottom: 1px solid var(--color-border);
  margin-bottom: var(--spacing-2xl);
}

.share-label {
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
}

.share-buttons {
  display: flex;
  gap: 10px;
}

.share-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: var(--color-surface-secondary);
  color: var(--color-text-light);
  cursor: pointer;
  transition: all var(--transition-base);
  display: flex;
  align-items: center;
  justify-content: center;
}

.share-btn:hover {
  background: var(--share-color);
  color: var(--color-text-on-dark);
  transform: translateY(-2px);
}

.share-btn i {
  font-size: var(--font-size-base);
}

/* Loading / empty state */
.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-height: 50vh;
  color: var(--color-text-light);
  font-size: var(--font-size-lg);
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .back-btn {
    top: 15px;
    left: 15px;
    padding: var(--spacing-sm) var(--spacing-md);
    font-size: var(--font-size-sm);
  }

  .back-btn span {
    display: none;
  }

  .banner-content h1 {
    font-size: var(--font-size-2xl);
  }

  .meta-bottom {
    flex-wrap: wrap;
    gap: 10px;
  }

  .article-main {
    padding: var(--spacing-xl);
    border-radius: 10px;
  }

  .copyright-notice {
    flex-direction: column;
    text-align: center;
    align-items: center;
  }
}
</style>
