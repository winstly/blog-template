<script setup lang="ts">
import { ref } from 'vue'
import { useNotification } from '@/composables'

const content = ref('')
const isSubmitting = ref(false)
const { visible, message: notificationMessage, show } = useNotification()

function handleSubmit() {
  if (!content.value.trim()) return
  isSubmitting.value = true
  setTimeout(() => {
    show('留言功能暂未开放，敬请期待！')
    content.value = ''
    isSubmitting.value = false
  }, 500)
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

  <div class="comment-form">
    <div class="form-header">
      <i class="fa fa-pencil"></i>
      <span>发表评论</span>
    </div>
    <div class="form-body">
      <textarea
        v-model="content"
        placeholder="说点什么吧..."
        rows="4"
      ></textarea>
      <div class="form-footer">
        <button
          class="submit-btn"
          :disabled="isSubmitting || !content.trim()"
          @click="handleSubmit"
        >
          <i class="fa fa-paper-plane"></i>
          {{ isSubmitting ? '提交中...' : '发送评论' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-form {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.form-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: 16px 20px;
  background: var(--color-surface-secondary);
  border-bottom: 1px solid var(--color-border);
  font-weight: 600;
  color: var(--color-text-dark);
  font-size: var(--font-size-base);
}

.form-header i {
  color: var(--color-accent-secondary);
  font-size: var(--font-size-sm);
}

.form-body {
  padding: 16px 20px;
}

.form-body textarea {
  width: 100%;
  padding: 14px;
  border: 1px solid var(--color-border-dark);
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  line-height: 1.7;
  resize: vertical;
  font-family: inherit;
  box-sizing: border-box;
  transition: all var(--transition-fast);
  color: var(--color-text-secondary);
}

.form-body textarea:focus {
  outline: none;
  border-color: var(--color-accent-secondary);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-body textarea::placeholder {
  color: var(--color-text-light);
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.submit-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 24px;
  background: var(--gradient-accent);
  color: var(--color-text-on-dark);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: var(--font-size-sm);
  font-weight: 500;
  transition: all var(--transition-base);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.submit-btn:disabled {
  background: var(--color-border-dark);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
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
