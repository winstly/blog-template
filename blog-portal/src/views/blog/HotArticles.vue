<script setup lang="ts">
import type { Article } from '@/api/types'

interface Props {
  articles: Article[]
}

defineProps<Props>()
</script>

<template>
  <ul class="hot-articles">
    <li v-for="(article, index) in articles" :key="article.id">
      <span :class="['rank', `rank-${index + 1}`]">{{ index + 1 }}</span>
      <RouterLink :to="article.link" class="article-link">
        {{ article.title }}
      </RouterLink>
    </li>
  </ul>
</template>

<style scoped>
.hot-articles {
  list-style: none;
  padding: 0;
  margin: 0;
}

.hot-articles li {
  display: flex;
  align-items: flex-start;
  padding: var(--spacing-xs) 0;
  border-bottom: 1px solid var(--color-border);
  line-height: 1.5;
}

.hot-articles li:last-child {
  border-bottom: none;
}

.rank {
  display: inline-block;
  width: 22px;
  height: 22px;
  line-height: 22px;
  text-align: center;
  border-radius: 50%;
  background: var(--color-surface-tertiary);
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
  margin-right: var(--spacing-sm);
  flex-shrink: 0;
}

.rank-1 {
  background: #e24d46;
  color: var(--color-text-on-dark);
}

.rank-2 {
  background: var(--color-accent);
  color: var(--color-text-on-dark);
}

.rank-3 {
  background: var(--color-primary);
  color: var(--color-text-on-dark);
}

.article-link {
  color: var(--color-text-muted);
  text-decoration: none;
  font-size: var(--font-size-sm);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.article-link:hover {
  color: var(--color-primary);
}
</style>
