<script setup lang="ts">
import type { Category, Tag, Article } from '@/types'
import CategoryNav from './CategoryNav.vue'
import TagCloud from './TagCloud.vue'
import HotArticles from './HotArticles.vue'

interface Props {
  categories: Category[]
  tags: Tag[]
  hotArticles: Article[]
}

defineProps<Props>()
</script>

<template>
  <aside class="blog-sidebar">
    <div class="sidebar-item">
      <h3 class="sidebar-title">
        <i class="fa fa-folder-o"></i> 分类
      </h3>
      <CategoryNav :categories="categories" />
    </div>
    <div class="sidebar-item">
      <h3 class="sidebar-title">
        <i class="fa fa-tags"></i> 标签
      </h3>
      <TagCloud :tags="tags" />
    </div>
    <div class="sidebar-item">
      <h3 class="sidebar-title">
        <i class="fa fa-fire"></i> 热门文章
      </h3>
      <HotArticles :articles="hotArticles" />
    </div>
  </aside>
</template>

<style scoped>
.blog-sidebar {
  position: sticky;
  top: 80px;
}

.sidebar-item {
  background: var(--color-surface);
  margin-bottom: 20px;
  padding: 20px;
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border-light);
}

.sidebar-title {
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--color-text-dark);
  margin: 0 0 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--color-border);
}

.sidebar-title i {
  margin-right: 8px;
  color: #11998e;
}
</style>
