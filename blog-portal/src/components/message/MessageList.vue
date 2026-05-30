<script setup lang="ts">
import type { Message } from '@/types'
import CommentItem from '@/components/shared/CommentItem.vue'

interface Props {
  messages: Message[]
}

defineProps<Props>()
</script>

<template>
  <div class="message-list">
    <div class="list-header">
      <i class="fa fa-comments-o"></i>
      <span>留言列表</span>
    </div>
    <CommentItem
      v-for="message in messages"
      :key="message.id"
      :message="message"
    />
    <div v-if="messages.length === 0" class="empty">
      <i class="fa fa-commenting-o"></i>
      <p>暂无留言，快来抢沙发吧~</p>
    </div>
  </div>
</template>

<style scoped>
.message-list {
  margin-top: 20px;
  background: var(--color-surface);
  padding: 24px;
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border-light);
}

.list-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--color-border);
  color: var(--color-text-dark);
  font-weight: 600;
}

.list-header i {
  color: #f093fb;
}

.empty {
  text-align: center;
  padding: 40px 20px;
  color: var(--color-text-light);
}

.empty i {
  font-size: 48px;
  margin-bottom: 16px;
  display: block;
}

.empty p {
  margin: 0;
  font-size: var(--font-size-sm);
}
</style>
