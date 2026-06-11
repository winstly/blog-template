<script setup lang="ts">
import { ref, onMounted } from 'vue';
import ArticleCard from './ArticleCard.vue';
import { articleService } from '@/api/services';
import type { Article } from '@/api/types';

const articles = ref<Article[]>([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const result = await articleService.getList({ page: 1, pageSize: 6 });
    articles.value = result.list;
  } catch (e) {
    console.error('Failed to fetch articles:', e);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <section id="articles-section" class="articles-section py-24 bg-white">
    <div class="articles-container mx-auto px-4">
      <!-- Section Header -->
      <div
        v-motion
        :initial="{ opacity: 0, y: -50 }"
        :visible="{ opacity: 1, y: 0, transition: { duration: 800 } }"
        class="text-center mb-12"
      >
        <h1 class="section-title text-3xl font-medium text-text-primary relative inline-block">
          热门文章
        </h1>
        <p class="mt-6 text-text-muted leading-relaxed">
          很想给你写封信,告诉你这里的天气.
          <br />昨夜的那场电影,还有我的心情.
        </p>
      </div>

      <!-- Articles Grid -->
      <div v-if="!loading" class="articles-grid grid grid-cols-1 md:grid-cols-3">
        <div
          v-for="(article, index) in articles"
          :key="article.articleCode"
          class="article-col"
        >
          <ArticleCard :article="article" :delay="index * 200" />
        </div>
      </div>
      <div v-else class="text-center py-12">
        <i class="fa fa-spinner fa-spin"></i> 加载中...
      </div>
    </div>
  </section>
</template>

<style scoped>
.articles-section {
  font-family: 'Microsoft YaHei', sans-serif;
}

.articles-container {
  width: 90%;
  position: relative;
  margin: 0 auto;
  padding-top: 0.1px;
}

@media screen and (min-width: 768px) {
  .articles-container {
    width: 750px;
  }
}

@media screen and (min-width: 992px) {
  .articles-container {
    width: 970px;
  }
}

@media screen and (min-width: 1200px) {
  .articles-container {
    width: 1170px;
  }
}

.section-title {
  padding-bottom: 30px;
  position: relative;
  font-weight: 500;
}

.section-title::after {
  position: absolute;
  width: 50px;
  height: 2px;
  content: '';
  left: 50%;
  margin-left: -25px;
  bottom: -1px;
  background: var(--color-primary);
}

.article-col {
  padding: 0 10px;
}
</style>
