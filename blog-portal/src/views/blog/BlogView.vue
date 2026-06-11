<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import PageBanner from '@/components/shared/PageBanner.vue';
import ArticleList from './ArticleList.vue';
import BlogSidebar from './BlogSidebar.vue';
import { articleService } from '@/api/services';
import { categoryService } from '@/api/services';
import { tagService } from '@/api/services';
import type { Article, Category, Tag } from '@/api/types';

const articles = ref<Article[]>([]);
const categories = ref<Category[]>([]);
const tags = ref<Tag[]>([]);
const loading = ref(true);

const hotArticles = computed(() => {
  return [...articles.value]
    .sort((a, b) => b.viewCount - a.viewCount)
    .slice(0, 5);
});

onMounted(async () => {
  try {
    const [articlesResult, categoriesResult, tagsResult] = await Promise.all([
      articleService.getList({ page: 1, pageSize: 20 }),
      categoryService.getList(),
      tagService.getList(),
    ]);
    articles.value = articlesResult.list;
    categories.value = categoriesResult;
    tags.value = tagsResult;
  } catch (e) {
    console.error('Failed to fetch blog data:', e);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="blog-page">
    <PageBanner
      title="博客"
      subtitle="记录技术与生活"
      color="#11998e"
    />
    <div class="doc-container">
      <div class="container-fixed">
        <div v-if="!loading" class="blog-layout">
          <main class="col-content">
            <ArticleList :articles="articles" />
          </main>
          <BlogSidebar
            :categories="categories"
            :tags="tags"
            :hot-articles="hotArticles"
          />
        </div>
        <div v-else class="text-center py-12">
          <i class="fa fa-spinner fa-spin"></i> 加载中...
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.blog-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.doc-container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}

.container-fixed {
  width: 100%;
  padding: 20px 15px;
}

.blog-layout {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -15px;
}

.col-content {
  flex: 0 0 calc(100% - 320px);
  max-width: calc(100% - 320px);
  padding: 0 15px;
}

.blog-layout > :last-child {
  flex: 0 0 320px;
  max-width: 320px;
  padding: 0 15px;
}

@media screen and (max-width: 992px) {
  .col-content {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .blog-layout > :last-child {
    display: none;
  }
}
</style>
