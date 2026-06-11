<script setup lang="ts">
import type { Article } from '@/api/types'
import ArticleCard from './ArticleCard.vue'

interface Props {
  articles: Article[]
}

defineProps<Props>()
</script>

<template>
  <div class="article-list">
    <ArticleCard
      v-for="article in articles"
      :key="article.articleCode"
      :article="article"
    />
  </div>
</template>

<style scoped>
.article-list {
  position: relative;
}
</style>
