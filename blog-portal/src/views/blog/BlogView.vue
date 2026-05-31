<script setup lang="ts">
import PageBanner from '@/components/shared/PageBanner.vue'
import ArticleList from './ArticleList.vue'
import BlogSidebar from './BlogSidebar.vue'
import { mockArticles, mockCategories, mockTags, mockHotArticles } from '@/api/mock'
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
        <div class="blog-layout">
          <main class="col-content">
            <ArticleList :articles="mockArticles" />
          </main>
          <BlogSidebar
            :categories="mockCategories"
            :tags="mockTags"
            :hot-articles="mockHotArticles"
          />
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
