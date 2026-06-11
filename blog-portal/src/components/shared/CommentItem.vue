<script setup lang="ts">
import { ref } from 'vue'
import type { Message } from '@/api/types'
import { useNotification } from '@/composables'

interface Props {
  message: Message
  isReply?: boolean
}

const props = defineProps<Props>()

const showReplyForm = ref(false)
const replyContent = ref('')
const { visible, message: notificationMessage, show } = useNotification()

function toggleReply() {
  showReplyForm.value = !showReplyForm.value
}

function submitReply() {
  if (!replyContent.value.trim()) return
  show('回复功能暂未开放')
  replyContent.value = ''
  showReplyForm.value = false
}
</script>

<template>
  <!-- 通知组件 -->
  <Transition name="notification">
    <div v-if="visible" class="notification">
      <i class="fa fa-info-circle"></i>
      {{ notificationMessage }}
    </div>
  </Transition>

  <div :class="['comment-item', { 'is-reply': isReply }]">
    <img :src="message.userAvatarUrl ?? ''" :alt="message.userName" class="avatar" />

    <div class="comment-body">
      <div class="comment-header">
        <span class="author">{{ message.userName }}</span>
        <span v-if="isReply && message.replyTo" class="reply-to">
          <i class="fa fa-reply"></i>
          {{ message.replyTo }}
        </span>
        <span class="time">{{ message.date }}</span>
      </div>

      <div class="comment-content">
        {{ message.content }}
      </div>

      <div class="comment-footer">
        <span class="location">
          <i class="fa fa-map-marker"></i>
          {{ message.location }}
        </span>
        <button class="reply-btn" @click="toggleReply">
          <i class="fa fa-comment-o"></i>
          回复
        </button>
      </div>

      <!-- 回复表单 -->
      <Transition name="expand">
        <div v-if="showReplyForm" class="reply-form">
          <textarea
            v-model="replyContent"
            placeholder="写下你的回复..."
            rows="2"
          ></textarea>
          <div class="reply-actions">
            <button class="cancel-btn" @click="showReplyForm = false">取消</button>
            <button
              class="submit-btn"
              :disabled="!replyContent.trim()"
              @click="submitReply"
            >
              发送
            </button>
          </div>
        </div>
      </Transition>

      <!-- 子回复 -->
      <div v-if="message.replies && message.replies.length > 0" class="replies">
        <CommentItem
          v-for="reply in message.replies"
          :key="reply.id"
          :message="reply"
          is-reply
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid var(--color-border);
}

.comment-item:last-child {
  border-bottom: none;
}

.is-reply {
  padding: 12px 0;
  margin-top: 12px;
  background: var(--color-surface-secondary);
  border-radius: var(--radius-md);
  padding: 12px;
  border-bottom: none;
}

.avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
  border: 2px solid var(--color-border);
}

.is-reply .avatar {
  width: 36px;
  height: 36px;
}

.comment-body {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
  flex-wrap: wrap;
}

.author {
  font-weight: 600;
  color: var(--color-accent-secondary);
  font-size: var(--font-size-sm);
}

.reply-to {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: var(--color-text-light);
}

.reply-to i {
  font-size: 10px;
  transform: scaleX(-1);
}

.time {
  font-size: var(--font-size-xs);
  color: var(--color-text-light);
  margin-left: auto;
}

.comment-content {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  line-height: 1.7;
  word-break: break-word;
}

.comment-footer {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 10px;
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: var(--color-text-light);
}

.location i {
  font-size: var(--font-size-xs);
  color: var(--color-accent-secondary);
}

.reply-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.reply-btn:hover {
  background: var(--color-border);
  color: var(--color-accent-secondary);
}

/* 回复表单 */
.reply-form {
  margin-top: 12px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 12px;
  border: 1px solid var(--color-border-dark);
}

.reply-form textarea {
  width: 100%;
  border: 1px solid var(--color-border-dark);
  border-radius: var(--radius-sm);
  padding: 10px;
  font-size: var(--font-size-xs);
  resize: none;
  font-family: inherit;
  box-sizing: border-box;
  transition: border-color var(--transition-fast);
}

.reply-form textarea:focus {
  outline: none;
  border-color: var(--color-accent-secondary);
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 10px;
}

.cancel-btn,
.submit-btn {
  padding: 6px 14px;
  border-radius: var(--radius-sm);
  font-size: var(--font-size-xs);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.cancel-btn {
  background: var(--color-surface-secondary);
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-muted);
}

.cancel-btn:hover {
  background: var(--color-surface-tertiary);
}

.submit-btn {
  background: var(--gradient-accent);
  border: none;
  color: var(--color-text-on-dark);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 子回复 */
.replies {
  margin-top: 12px;
  padding-left: 12px;
  border-left: 2px solid var(--color-border-dark);
}

/* 展开动画 */
.expand-enter-active,
.expand-leave-active {
  transition: all var(--transition-base) ease;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  max-height: 0;
  margin-top: 0;
}

.expand-enter-to,
.expand-leave-from {
  opacity: 1;
  max-height: 200px;
}

/* 通知样式 */
.notification {
  position: fixed;
  top: var(--spacing-xl);
  left: 50%;
  transform: translateX(-50%);
  background: var(--gradient-accent);
  color: var(--color-text-on-dark);
  padding: var(--spacing-md) var(--spacing-2xl);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-accent);
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: var(--font-size-base);
  z-index: var(--z-notification);
}

.notification i {
  font-size: var(--font-size-lg);
}

.notification-enter-active,
.notification-leave-active {
  transition: all var(--transition-base) ease;
}

.notification-enter-from,
.notification-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(-20px);
}
</style>
