<script setup lang="ts">
import type { Article } from '@/api/types'

interface Props {
  article: Article
}

defineProps<Props>()
</script>

<template>
  <article class="article-card">
    <div v-if="article.isTop" class="top-badge">置顶</div>

    <div class="card-header">
      <h2 class="title">
        <RouterLink :to="article.link">{{ article.title }}</RouterLink>
      </h2>
      <div class="meta">
        <span class="date">
          <i class="fa fa-calendar-o"></i>
          {{ article.date }}
        </span>
      </div>
    </div>

    <div class="card-body">
      <RouterLink :to="article.link" class="cover">
        <img :src="article.cover" :alt="article.title" />
      </RouterLink>
      <p class="summary">{{ article.summary }}</p>
    </div>

    <div class="card-footer">
      <div class="tags">
        <i class="fa fa-tags"></i>
        <RouterLink
          v-for="tag in article.tags"
          :key="tag"
          :to="`/blog?tag=${tag}`"
          class="tag"
        >
          {{ tag }}
        </RouterLink>
      </div>
      <div class="stats">
        <span><i class="fa fa-eye"></i> {{ article.views || 0 }}</span>
        <span><i class="fa fa-comments"></i> {{ article.comments || 0 }}</span>
      </div>
    </div>

    <RouterLink :to="article.link" class="read-more">
      阅读全文 <i class="fa fa-angle-right"></i>
    </RouterLink>
  </article>
</template>

<style scoped>
.article-card {
  background: var(--color-surface);
  border-radius: var(--radius-2xl);
  padding: var(--spacing-2xl);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border-light);
  position: relative;
  transition: all var(--transition-base) ease;
}

.article-card:hover {
  box-shadow: var(--shadow-xl);
  transform: translateY(-2px);
}

.top-badge {
  position: absolute;
  top: var(--spacing-2xl);
  right: var(--spacing-2xl);
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e53 100%);
  color: var(--color-text-on-dark);
  padding: var(--spacing-xs) var(--spacing-md);
  border-radius: var(--radius-xl);
  font-size: var(--font-size-xs);
  font-weight: 500;
  z-index: 1;
}

.card-header {
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--color-border);
}

.title {
  margin: 0 0 10px 0;
  font-size: var(--font-size-3xl);
  font-weight: 600;
  line-height: 1.4;
}

.title a {
  color: var(--color-text-dark);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.title a:hover {
  color: var(--color-primary);
}

.meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.date {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
}

.card-body {
  display: flex;
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
}

.cover {
  flex-shrink: 0;
  width: 200px;
  height: 130px;
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-base);
}

.cover:hover img {
  transform: scale(1.05);
}

.summary {
  flex: 1;
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  line-height: 1.8;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--color-border);
}

.tags {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.tags > i {
  color: var(--color-text-light);
  font-size: var(--font-size-base);
}

.tag {
  background: var(--color-surface-tertiary);
  color: var(--color-text-light);
  padding: var(--spacing-xs) 10px;
  border-radius: var(--radius-xl);
  font-size: var(--font-size-xs);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.tag:hover {
  background: var(--color-primary);
  color: var(--color-text-on-dark);
}

.stats {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
}

.stats span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.read-more {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: var(--spacing-lg);
  color: var(--color-primary);
  font-size: var(--font-size-base);
  font-weight: 500;
  text-decoration: none;
  transition: all var(--transition-fast);
}

.read-more:hover {
  gap: 10px;
}

.read-more i {
  transition: transform var(--transition-fast);
}

.read-more:hover i {
  transform: translateX(3px);
}

@media screen and (max-width: 768px) {
  .article-card {
    padding: 18px;
    padding-top: 48px;
  }

  .top-badge {
    top: var(--spacing-md);
    right: var(--spacing-md);
  }

  .card-body {
    flex-direction: column;
  }

  .cover {
    width: 100%;
    height: 180px;
  }

  .card-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-md);
  }
}
</style>
