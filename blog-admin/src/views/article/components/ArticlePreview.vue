<template>
  <div class="preview-content prose" v-html="sanitizedContent"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

const props = defineProps<{
  content: string
}>()

const sanitizedContent = computed(() => {
  if (!props.content) return '<p class="empty-hint">开始编写内容...</p>'
  const rawHtml = marked.parse(props.content) as string
  // Security: Sanitize HTML to prevent XSS attacks
  return DOMPurify.sanitize(rawHtml)
})
</script>

<style scoped>
.preview-content {
  padding: 24px 32px;
  max-width: 800px;
  margin: 0 auto;
}

.empty-hint {
  color: var(--color-gray-400);
  text-align: center;
  padding: 40px;
}

/* Prose Styles */
:deep(.prose) {
  line-height: 1.8;
}

:deep(.prose h1) {
  font-size: 28px;
  font-weight: 600;
  margin: 24px 0 16px;
  color: var(--color-gray-900);
}

:deep(.prose h2) {
  font-size: 22px;
  font-weight: 600;
  margin: 24px 0 12px;
  color: var(--color-gray-800);
  border-bottom: 1px solid var(--color-border-light);
  padding-bottom: 8px;
}

:deep(.prose h3) {
  font-size: 18px;
  font-weight: 600;
  margin: 20px 0 10px;
  color: var(--color-gray-700);
}

:deep(.prose p) {
  margin: 12px 0;
  color: var(--color-gray-600);
}

:deep(.prose code) {
  background: var(--color-gray-100);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Fira Code', monospace;
  font-size: 0.9em;
  color: var(--color-danger);
}

:deep(.prose pre) {
  background: var(--color-gray-900);
  color: var(--color-gray-100);
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 16px 0;
}

:deep(.prose pre code) {
  background: transparent;
  color: inherit;
  padding: 0;
}

:deep(.prose blockquote) {
  border-left: 4px solid var(--color-primary);
  padding-left: 16px;
  margin: 16px 0;
  color: var(--color-gray-500);
  font-style: italic;
}

:deep(.prose ul, .prose ol) {
  margin: 12px 0;
  padding-left: 24px;
}

:deep(.prose li) {
  margin: 6px 0;
}

:deep(.prose a) {
  color: var(--color-primary);
  text-decoration: none;
}

:deep(.prose a:hover) {
  text-decoration: underline;
}

:deep(.prose img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 16px 0;
}

:deep(.prose table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}

:deep(.prose th, .prose td) {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
}

:deep(.prose th) {
  background: var(--color-gray-100);
  font-weight: 600;
}

:deep(.prose hr) {
  border: none;
  border-top: 1px solid var(--color-border);
  margin: 24px 0;
}
</style>
