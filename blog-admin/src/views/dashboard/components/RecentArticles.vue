<script setup lang="ts">
import { Clock, View, ChatDotRound } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { formatRelativeDate } from '@/utils/date'
import type { Article } from '@/api/types'

interface Props {
  articles: Article[]
}

const props = defineProps<Props>()
const router = useRouter()

function handleArticleClick(articleId: string) {
  router.push(`/articles/edit/${articleId}`)
}
</script>

<template>
  <div class="list-content">
    <div
      v-for="(article, index) in articles"
      :key="article.id"
      class="list-item"
      :style="{ animationDelay: `${index * 0.05}s` }"
      @click="handleArticleClick(article.id)"
    >
      <div class="item-main">
        <h4 class="item-title">{{ article.title }}</h4>
        <div class="item-meta">
          <span class="meta-item">
            <el-icon :size="14"><Clock /></el-icon>
            <span>{{ formatRelativeDate(article.date) }}</span>
          </span>
          <span class="meta-item">
            <el-icon :size="14"><View /></el-icon>
            <span>{{ article.views }}</span>
          </span>
          <span class="meta-item">
            <el-icon :size="14"><ChatDotRound /></el-icon>
            <span>{{ article.comments }}</span>
          </span>
        </div>
      </div>
      <el-tag
        :type="article.status === 'published' ? 'success' : 'info'"
        size="small"
        effect="light"
        class="status-tag"
      >
        {{ article.status === 'published' ? '已发布' : '草稿' }}
      </el-tag>
    </div>
    <el-empty v-if="articles.length === 0" description="暂无文章" />
  </div>
</template>

<style scoped>
.list-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.list-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px;
  background: #fafaf9;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  animation: fadeInUp 0.4s ease-out backwards;
}

.list-item:hover {
  background: #f5f5f4;
  transform: translateX(4px);
}

.item-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #1c1917;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

.item-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #78716c;
}

.status-tag {
  font-weight: 500;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
