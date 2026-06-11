<script setup lang="ts">
import type { Category } from '@/api/types'

interface Props {
  categories: Category[]
}

defineProps<Props>()
</script>

<template>
  <ul class="category-nav">
    <li v-for="category in categories" :key="category.tagCode" class="category-item">
      <RouterLink :to="`/blog?category=${category.tagCode}`">
        {{ category.tagName }}
        <span class="count">({{ category.articleCount }})</span>
      </RouterLink>
    </li>
  </ul>
</template>

<style scoped>
.category-nav {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  line-height: 36px;
  border-bottom: 1px solid var(--color-border);
}

.category-item:last-child {
  border-bottom: none;
}

.category-item a {
  display: block;
  padding: 0 var(--spacing-sm);
  color: var(--color-text-muted);
  text-decoration: none;
  transition: all var(--transition-base);
}

.category-item a:hover {
  color: var(--color-primary);
  background: var(--color-surface-secondary);
}

.count {
  font-size: var(--font-size-xs);
  color: var(--color-text-light);
  margin-left: var(--spacing-xs);
}
</style>
