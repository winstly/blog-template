<script setup lang="ts">
import type { Tag } from '@/api/types'

interface Props {
  tags: Tag[]
}

defineProps<Props>()
</script>

<template>
  <div class="tag-cloud">
    <RouterLink
      v-for="tag in tags"
      :key="tag.id"
      :to="`/blog?tag=${tag.id}`"
      class="tag-item"
    >
      {{ tag.name }}
    </RouterLink>
  </div>
</template>

<style scoped>
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-xs);
}

.tag-item {
  display: inline-block;
  padding: 4px var(--spacing-sm);
  background: var(--color-surface-tertiary);
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
  text-decoration: none;
  transition: all var(--transition-base);
}

.tag-item:hover {
  background: var(--color-primary);
  color: var(--color-text-on-dark);
}
</style>
