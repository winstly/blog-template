<script setup lang="ts">
import { mockProfile } from '@/mock'

interface Props {
  title: string
  subtitle?: string
  color?: string
}

const props = withDefaults(defineProps<Props>(), {
  subtitle: '',
  color: '#667eea',
})
</script>

<template>
  <div class="page-banner" :style="{ '--accent-color': color }">
    <div class="banner-content">
      <h1>{{ title }}</h1>
      <p v-if="subtitle">{{ subtitle }}</p>
      <p v-else>{{ mockProfile.signature }}</p>
    </div>
  </div>
</template>

<style scoped>
.page-banner {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.banner-content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px 20px;
}

.banner-content h1 {
  margin: 0 0 6px;
  font-size: var(--font-size-2xl);
  font-weight: 600;
  color: var(--color-text-dark);
  display: flex;
  align-items: center;
  gap: 10px;
}

.banner-content h1::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 24px;
  background: var(--accent-color);
  border-radius: 2px;
}

.banner-content p {
  margin: 0;
  font-size: var(--font-size-sm);
  color: var(--color-text-light);
  padding-left: 14px;
}

@media screen and (max-width: 768px) {
  .banner-content {
    padding: 18px 15px;
  }

  .banner-content h1 {
    font-size: var(--font-size-xl);
  }

  .banner-content h1::before {
    height: 20px;
  }

  .banner-content p {
    font-size: var(--font-size-xs);
  }
}
</style>
