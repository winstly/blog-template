<script setup lang="ts">
import type { Message } from '@/types'
import CommentItem from './CommentItem.vue'
import CommentForm from './CommentForm.vue'

interface Props {
  messages: Message[]
  showForm?: boolean
}

withDefaults(defineProps<Props>(), {
  showForm: true,
})
</script>

<template>
  <div class="comment-section">
    <CommentForm v-if="showForm" />

    <div class="comments-wrapper">
      <div class="comments-header">
        <i class="fa fa-comments-o"></i>
        <span>评论列表</span>
        <span class="count">{{ messages.length }}</span>
      </div>

      <div class="comments-list">
        <CommentItem
          v-for="message in messages"
          :key="message.id"
          :message="message"
        />

        <div v-if="messages.length === 0" class="empty-state">
          <i class="fa fa-commenting-o"></i>
          <p>暂无评论，快来抢沙发吧~</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-section {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.comments-wrapper {
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.comments-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg) var(--spacing-xl);
  background: var(--color-surface-secondary);
  border-bottom: 1px solid var(--color-border);
  font-weight: 600;
  color: var(--color-text-dark);
  font-size: var(--font-size-md);
}

.comments-header i {
  color: var(--color-accent-secondary);
  font-size: var(--font-size-base);
}

.count {
  margin-left: auto;
  background: var(--color-accent-secondary);
  color: var(--color-text-on-dark);
  font-size: var(--font-size-xs);
  padding: 2px var(--spacing-sm);
  border-radius: 10px;
  font-weight: 500;
}

.comments-list {
  padding: var(--spacing-sm) var(--spacing-xl);
}

.empty-state {
  text-align: center;
  padding: 40px var(--spacing-xl);
  color: var(--color-text-light);
}

.empty-state i {
  font-size: 40px;
  margin-bottom: var(--spacing-md);
  display: block;
  color: var(--color-border-dark);
}

.empty-state p {
  margin: 0;
  font-size: var(--font-size-base);
}
</style>
