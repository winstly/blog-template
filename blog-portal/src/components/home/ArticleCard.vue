<script setup lang="ts">
import { Link } from '@lucide/vue'
import type { Article } from '@/types'

interface Props {
  article: Article
  delay?: number
}

const props = withDefaults(defineProps<Props>(), {
  delay: 0,
})
</script>

<template>
  <div
    v-motion
    :initial="{ opacity: 0, y: 50 }"
    :visible="{ opacity: 1, y: 0, transition: { duration: 800, delay: props.delay } }"
    class="article-card"
  >
    <!-- Cover Image -->
    <div class="news-head relative overflow-hidden">
      <img
        :src="article.cover"
        :alt="article.title"
      />
      <a :href="article.link" class="link-icon">
        <Link class="w-5 h-5" />
      </a>
    </div>

    <!-- Content -->
    <div class="news-content">
      <h4 class="mb-2">
        <a :href="article.link" class="title-link">
          {{ article.title }}
        </a>
      </h4>
      <div class="date">
        {{ article.date }}
      </div>
      <p class="summary">
        {{ article.summary }}
      </p>
      <a :href="article.link" class="read-more">
        阅读更多
      </a>
    </div>
  </div>
</template>

<style scoped>
.article-card {
  margin-top: 50px;
  background: var(--color-background);
}

.news-head img {
  width: 100%;
  height: 244px;
  object-fit: cover;
  transition: all var(--transition-slow) ease;
}

.news-head:hover img {
  transform: scale(1.2);
}

.news-head::before {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  background: rgba(255, 255, 255, 0.51);
  content: '';
  opacity: 0;
  visibility: hidden;
  transition: all var(--transition-slow) ease;
  transform: translateY(-100%);
  z-index: 8;
}

.news-head:hover::before {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.link-icon {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 40px;
  height: 40px;
  background: var(--color-dark-bg);
  color: var(--color-text-on-dark);
  opacity: 0;
  visibility: hidden;
  text-align: center;
  margin: -20px 0 0 -20px;
  line-height: 40px;
  z-index: 9;
  transition: all var(--transition-slow) ease;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0;
}

.news-head:hover .link-icon {
  opacity: 1;
  visibility: visible;
}

.news-content {
  padding: var(--spacing-3xl) var(--spacing-lg);
}

.news-content h4 {
  margin-bottom: 10px;
}

.title-link {
  color: var(--color-text-primary);
  transition: color var(--transition-base);
}

.title-link:hover {
  color: var(--color-primary);
}

.news-content .date {
  color: var(--color-text-light);
  font-size: var(--font-size-xs);
  margin-bottom: var(--spacing-lg);
}

.news-content .summary {
  color: var(--color-text-muted);
  height: 55px;
  overflow: hidden;
  line-height: 1.5;
}

.news-content .read-more {
  display: inline-block;
  margin-top: var(--spacing-lg);
  padding: 0;
  background: transparent;
  border: none;
  color: var(--color-accent);
  transition: all var(--transition-slow) ease;
  cursor: pointer;
}

.news-content .read-more:hover {
  color: var(--color-text-secondary);
}
</style>
