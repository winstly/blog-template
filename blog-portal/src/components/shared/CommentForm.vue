<script setup lang="ts">
import { ref } from 'vue'

const content = ref('')
const isSubmitting = ref(false)

function handleSubmit() {
  if (!content.value.trim()) return
  isSubmitting.value = true
  setTimeout(() => {
    alert('留言功能暂未开放，敬请期待！')
    content.value = ''
    isSubmitting.value = false
  }, 500)
}
</script>

<template>
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
  background: linear-gradient(135deg, var(--color-accent-secondary) 0%, #764ba2 100%);
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
</style>
