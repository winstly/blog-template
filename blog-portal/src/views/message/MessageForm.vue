<script setup lang="ts">
import { ref } from 'vue';
import { messageService } from '@/api/services';

const content = ref('');
const userName = ref('');
const isSubmitting = ref(false);

const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref<'success' | 'error'>('success');

async function handleSubmit() {
  if (!content.value.trim()) return;
  isSubmitting.value = true;
  try {
    await messageService.create({
      userName: userName.value.trim() || '匿名用户',
      remark: content.value.trim(),
    });
    notificationMessage.value = '留言成功！';
    notificationType.value = 'success';
    showNotification.value = true;
    content.value = '';
  } catch (e) {
    notificationMessage.value = (e as Error).message || '留言失败，请稍后重试';
    notificationType.value = 'error';
    showNotification.value = true;
  } finally {
    isSubmitting.value = false;
    setTimeout(() => {
      showNotification.value = false;
    }, 3000);
  }
}
</script>

<template>
  <div class="message-form-wrap">
    <Transition name="notification">
      <div v-if="showNotification" :class="['notification', notificationType]">
        <i :class="['fa', notificationType === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle']"></i>
        {{ notificationMessage }}
      </div>
    </Transition>
    <form class="message-form" @submit.prevent="handleSubmit">
      <div class="form-header">
        <i class="fa fa-pencil"></i>
        <span>写下你的留言</span>
      </div>
      <div class="form-item">
        <input
          v-model="userName"
          placeholder="你的昵称（可选）"
          class="form-input"
        />
      </div>
      <div class="form-item">
        <textarea
          v-model="content"
          placeholder="说点什么吧..."
          class="form-textarea"
          rows="4"
        ></textarea>
      </div>
      <div class="form-item">
        <button
          type="submit"
          class="submit-btn"
          :disabled="isSubmitting || !content.trim()"
        >
          <i class="fa fa-paper-plane"></i>
          {{ isSubmitting ? '提交中...' : '提交留言' }}
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.message-form-wrap {
  background: var(--color-surface);
  padding: var(--spacing-2xl);
  margin-top: var(--spacing-xl);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border-light);
}

.form-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 2px solid var(--color-border);
  color: var(--color-text-dark);
  font-weight: 600;
}

.form-header i {
  color: var(--color-accent-gradient-start);
}

.form-item {
  margin-bottom: var(--spacing-lg);
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-input {
  width: 100%;
  padding: var(--spacing-md) var(--spacing-lg);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-xl);
  font-size: var(--font-size-base);
  font-family: inherit;
  box-sizing: border-box;
  transition: all var(--transition-base);
  color: var(--color-text-secondary);
}

.form-input:focus {
  outline: none;
  border-color: var(--color-accent-gradient-start);
  box-shadow: 0 0 0 3px rgba(240, 147, 251, 0.1);
}

.form-input::placeholder {
  color: var(--color-text-placeholder);
}

.form-textarea {
  width: 100%;
  padding: var(--spacing-lg);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-xl);
  font-size: var(--font-size-md);
  line-height: 1.8;
  resize: vertical;
  font-family: inherit;
  box-sizing: border-box;
  transition: all var(--transition-base);
  color: var(--color-text-secondary);
}

.form-textarea:focus {
  outline: none;
  border-color: var(--color-accent-gradient-start);
  box-shadow: 0 0 0 3px rgba(240, 147, 251, 0.1);
}

.form-textarea::placeholder {
  color: var(--color-text-placeholder);
}

.submit-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-md) 28px;
  background: var(--gradient-primary);
  color: var(--color-text-on-dark);
  border: none;
  border-radius: var(--radius-full);
  cursor: pointer;
  font-size: var(--font-size-base);
  font-weight: 500;
  transition: all var(--transition-base);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(240, 147, 251, 0.4);
}

.submit-btn:disabled {
  background: var(--color-surface-tertiary);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.notification {
  position: fixed;
  top: var(--spacing-xl);
  left: 50%;
  transform: translateX(-50%);
  padding: var(--spacing-md) var(--spacing-2xl);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-accent);
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: var(--font-size-base);
  z-index: var(--z-notification);
}

.notification.success {
  background: var(--gradient-accent);
  color: var(--color-text-on-dark);
}

.notification.error {
  background: var(--color-error-bg);
  color: var(--color-error-text);
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
