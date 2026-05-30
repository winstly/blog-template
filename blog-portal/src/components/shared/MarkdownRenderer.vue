<script setup lang="ts">
import { ref, watch, nextTick, onMounted, shallowRef } from 'vue'
import type MarkdownIt from 'markdown-it'

interface Props {
  content: string
}

const props = defineProps<Props>()

const renderedContent = ref('')
const isLoaded = ref(false)
const md = shallowRef<MarkdownIt | null>(null)

// Lazy load markdown-it
async function loadMarkdownIt() {
  const [MarkdownItModule, hljsModule] = await Promise.all([
    import('markdown-it'),
    import('highlight.js'),
  ])

  const MarkdownIt = MarkdownItModule.default
  const hljs = hljsModule.default

  const instance = new MarkdownIt({
    html: true,
    linkify: true,
    typographer: true,
    highlight: function (str: string, lang: string): string {
      if (lang && hljs.getLanguage(lang)) {
        try {
          return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang, ignoreIllegals: true }).value}</code></pre>`
        } catch {
          // fall through
        }
      }
      return `<pre class="hljs"><code>${escapeHtml(str)}</code></pre>`
    },
  })

  md.value = instance
  isLoaded.value = true
}

function escapeHtml(str: string): string {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')
}

function extractMermaidBlocks(html: string): { html: string; blocks: { id: string; code: string }[] } {
  const blocks: { id: string; code: string }[] = []
  let counter = 0

  const result = html.replace(/<pre><code class="language-mermaid">([\s\S]*?)<\/code><\/pre>/g, (_, code) => {
    const id = `mermaid-${counter++}`
    const decodedCode = code
      .replace(/&lt;/g, '<')
      .replace(/&gt;/g, '>')
      .replace(/&amp;/g, '&')
      .replace(/&quot;/g, '"')
      .replace(/&#39;/g, "'")
    blocks.push({ id, code: decodedCode.trim() })
    return `<div id="${id}" class="mermaid-placeholder">Loading diagram...</div>`
  })

  return { html: result, blocks }
}

async function renderMermaidBlocks(blocks: { id: string; code: string }[]) {
  // Lazy load mermaid only when needed
  const mermaidModule = await import('mermaid')
  const mermaid = mermaidModule.default

  mermaid.initialize({
    startOnLoad: false,
    theme: 'dark',
    securityLevel: 'loose',
  })

  for (const block of blocks) {
    try {
      const { svg } = await mermaid.render(`${block.id}-svg`, block.code)
      const el = document.getElementById(block.id)
      if (el) {
        el.innerHTML = svg
      }
    } catch (error) {
      const errorInfo = {
        diagramId: block.id,
        code: block.code,
        timestamp: new Date().toISOString(),
        error: error instanceof Error ? error.message : String(error),
      }
      console.error('[MermaidRenderError]', errorInfo)
      const el = document.getElementById(block.id)
      if (el) {
        el.innerHTML = `<pre class="mermaid-error">${block.code}</pre>`
      }
    }
  }
}

async function render() {
  if (!props.content || !md.value) {
    renderedContent.value = ''
    return
  }

  let html = md.value.render(props.content)

  const { html: processedHtml, blocks } = extractMermaidBlocks(html)

  // Lazy load DOMPurify only when needed
  const DOMPurifyModule = await import('dompurify')
  const DOMPurify = DOMPurifyModule.default

  renderedContent.value = DOMPurify.sanitize(processedHtml, {
    ADD_TAGS: ['mermaid'],
    ADD_ATTR: ['class', 'id'],
  })

  await nextTick()

  if (blocks.length > 0) {
    await renderMermaidBlocks(blocks)
  }
}

// Load markdown-it on mount
onMounted(() => {
  loadMarkdownIt()
})

// Re-render when content changes or markdown-it loads
watch([() => props.content, isLoaded], () => {
  if (isLoaded.value) {
    render()
  }
}, { immediate: true })
</script>

<template>
  <div v-if="!isLoaded" class="markdown-loading">
    <span>加载中...</span>
  </div>
  <div v-else class="markdown-body" v-html="renderedContent"></div>
</template>

<style scoped>
.markdown-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-3xl);
  color: var(--color-text-light);
  font-size: var(--font-size-base);
}
</style>

<style>
.markdown-body {
  line-height: 1.9;
  color: var(--color-text-secondary);
}

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  margin: 28px 0 var(--spacing-lg);
  color: var(--color-text-dark);
  font-weight: 600;
}

.markdown-body h1 {
  font-size: var(--font-size-4xl);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--color-border);
}

.markdown-body h2 {
  font-size: var(--font-size-3xl);
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--color-border);
}

.markdown-body h3 {
  font-size: var(--font-size-2xl);
}

.markdown-body h4 {
  font-size: var(--font-size-xl);
}

.markdown-body p {
  margin: var(--spacing-lg) 0;
}

.markdown-body a {
  color: var(--color-link);
  text-decoration: none;
  border-bottom: 1px dashed var(--color-link);
  transition: all var(--transition-fast);
}

.markdown-body a:hover {
  color: var(--color-link-hover);
  border-bottom-style: solid;
}

.markdown-body img {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-md);
  margin: var(--spacing-lg) 0;
  box-shadow: var(--shadow-lg);
}

.markdown-body ul,
.markdown-body ol {
  padding-left: var(--spacing-3xl);
  margin: var(--spacing-lg) 0;
}

.markdown-body li {
  margin: var(--spacing-sm) 0;
  line-height: 1.8;
}

.markdown-body blockquote {
  margin: var(--spacing-xl) 0;
  padding: var(--spacing-lg) var(--spacing-xl);
  border-left: 4px solid var(--color-accent-secondary);
  background: linear-gradient(135deg, var(--color-surface-secondary) 0%, var(--color-surface-tertiary) 100%);
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
  color: var(--color-text-light);
}

.markdown-body blockquote p {
  margin: 0;
}

.markdown-body pre {
  margin: var(--spacing-xl) 0;
  padding: var(--spacing-xl);
  background: var(--color-code-bg);
  border-radius: var(--radius-xl);
  overflow-x: auto;
}

.markdown-body code {
  font-family: var(--font-family-mono);
  font-size: var(--font-size-base);
}

.markdown-body pre code {
  color: var(--color-code-text);
  background: transparent;
}

.markdown-body :not(pre) > code {
  padding: 3px var(--spacing-sm);
  background: linear-gradient(135deg, #f0f4ff 0%, #e8f0fe 100%);
  border-radius: var(--radius-sm);
  color: var(--color-accent-secondary);
  font-size: var(--font-size-base);
}

.markdown-body table {
  width: 100%;
  border-collapse: collapse;
  margin: var(--spacing-xl) 0;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.markdown-body th,
.markdown-body td {
  border: 1px solid var(--color-border-dark);
  padding: var(--spacing-md) var(--spacing-lg);
  text-align: left;
}

.markdown-body th {
  background: var(--color-surface-secondary);
  font-weight: 600;
  color: var(--color-text-dark);
}

.markdown-body tr:nth-child(even) {
  background: var(--color-surface-secondary);
}

.markdown-body hr {
  border: none;
  border-top: 2px solid var(--color-border);
  margin: 32px 0;
}

.markdown-body .mermaid-placeholder {
  text-align: center;
  padding: var(--spacing-2xl);
  background: var(--color-surface-secondary);
  border-radius: var(--radius-xl);
  color: var(--color-text-light);
  margin: var(--spacing-xl) 0;
}

.markdown-body .mermaid-error {
  background: #fff5f5;
  border: 1px solid #fed7d7;
  padding: var(--spacing-md);
  border-radius: var(--radius-md);
  color: var(--color-error);
}

/* Code highlighting - Catppuccin Mocha Theme */
.hljs {
  background: var(--color-code-bg);
  color: var(--color-code-text);
}

.hljs-comment,
.hljs-quote {
  color: var(--color-code-comment);
  font-style: italic;
}

.hljs-doctag,
.hljs-keyword,
.hljs-formula {
  color: var(--color-code-keyword);
}

.hljs-section,
.hljs-name,
.hljs-selector-tag,
.hljs-deletion,
.hljs-subst {
  color: #f38ba8;
}

.hljs-literal {
  color: #fab387;
}

.hljs-string,
.hljs-regexp,
.hljs-addition,
.hljs-attribute,
.hljs-meta .hljs-string {
  color: var(--color-code-string);
}

.hljs-attr,
.hljs-variable,
.hljs-template-variable,
.hljs-type,
.hljs-selector-class,
.hljs-selector-attr,
.hljs-selector-pseudo,
.hljs-number {
  color: var(--color-code-number);
}

.hljs-symbol,
.hljs-bullet,
.hljs-link,
.hljs-meta,
.hljs-selector-id,
.hljs-title {
  color: var(--color-code-function);
}

.hljs-built_in,
.hljs-title.class_,
.hljs-class .hljs-title {
  color: var(--color-code-number);
}
</style>
