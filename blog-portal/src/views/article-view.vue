<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import MarkdownRenderer from '@/components/shared/MarkdownRenderer.vue'
import CommentSection from '@/components/shared/CommentSection.vue'
import { mockArticles, mockProfile, mockMessages } from '@/mock'

const route = useRoute()

const article = computed(() => {
  const id = route.params.id as string
  return mockArticles.find((a) => a.id === id) || mockArticles[0]
})

const mockMarkdownContent = `## 前言

这是一篇示例文章，用于展示 Markdown 渲染效果。

## 代码示例

\`\`\`typescript
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const articleId = computed(() => route.params.id)

console.log('Article ID:', articleId.value)
\`\`\`

## Mermaid 图表

\`\`\`mermaid
graph TD
    A[开始] --> B{是否登录?}
    B -->|是| C[显示内容]
    B -->|否| D[跳转登录]
    D --> E[登录成功]
    E --> C
    C --> F[结束]
\`\`\`

## 列表示例

1. 第一项
2. 第二项
3. 第三项

- 无序列表项 1
- 无序列表项 2
- 无序列表项 3

## 引用

> 这是一段引用文字，用于展示引用块的样式效果。

## 总结

本文介绍了如何在 Vue 3 项目中集成 Markdown 渲染功能，并支持 Mermaid 图表渲染。
`

const renderedContent = computed(() => {
  if (!article.value) return ''
  return mockMarkdownContent
})

const shareLinks = [
  { icon: 'fa-thumbs-up', color: '#ff6b6b' },
  { icon: 'fa-weixin', color: '#07C160' },
  { icon: 'fa-weibo', color: '#E6162D' },
  { icon: 'fa-qq', color: '#12B7F5' },
]
</script>

<template>
  <div class="article-page">
    <!-- 返回按钮 -->
    <RouterLink to="/blog" class="back-btn">
      <i class="fa fa-arrow-left"></i>
      <span>返回博客</span>
    </RouterLink>

    <!-- 简洁标题栏 -->
    <div class="article-banner">
      <div class="banner-content">
        <div class="meta-top">
          <span class="date"><i class="fa fa-calendar-o"></i> {{ article?.date }}</span>
          <span class="views"><i class="fa fa-eye"></i> {{ article?.views || 0 }}</span>
        </div>
        <h1>{{ article?.title }}</h1>
        <div class="meta-bottom">
          <span class="author"><i class="fa fa-user"></i> {{ article?.author || mockProfile.nickname }}</span>
          <div class="tags">
            <RouterLink
              v-for="tag in article?.tags"
              :key="tag"
              :to="`/blog?tag=${tag}`"
              class="tag"
            >
              {{ tag }}
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
              <p>本文版权归 <strong>{{ mockProfile.nickname }}</strong> 所有，转载请注明出处。</p>
              <p>本文链接：<RouterLink :to="article?.link || '#'">{{ article?.title }}</RouterLink></p>
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
          <CommentSection :messages="mockMessages" id="comments" />
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>
.article-page {
  min-height: 100vh;
  background: #f5f7fa;
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
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
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
  background: linear-gradient(135deg, var(--color-accent-secondary) 0%, #764ba2 100%);
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
