<template>
  <div class="edit-page">
    <!-- Header -->
    <header class="edit-header">
      <div class="header-left">
        <el-button link @click="$router.back()">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <h2 class="page-title">{{ pageTitle }}</h2>
      </div>
      <div class="header-actions">
        <!-- 查看态：只显示编辑按钮 -->
        <template v-if="isEdit && !isEditing">
          <el-button type="primary" @click="enterEditMode">
            <el-icon><Edit /></el-icon>
            编辑文章
          </el-button>
        </template>
        <!-- 编辑态：显示保存按钮 -->
        <template v-else>
          <el-button v-if="isEdit" @click="cancelEdit">取消</el-button>
          <el-button @click="handleSave('draft')">
            <el-icon><Document /></el-icon>
            保存草稿
          </el-button>
          <el-button type="primary" @click="handleSave('published')">
            <el-icon><Promotion /></el-icon>
            {{ isEdit ? '更新发布' : '立即发布' }}
          </el-button>
        </template>
      </div>
    </header>

    <!-- Main Content -->
    <div class="edit-layout">
      <!-- Left: Basic Info -->
      <aside class="edit-sidebar">
        <el-scrollbar>
          <ArticleEditForm
            v-model="form"
            :category-tree="categoryTree"
            :tags="tags"
            :disabled="isEdit && !isEditing"
          />
        </el-scrollbar>
      </aside>

      <!-- Right: Markdown Editor -->
      <main class="edit-main">
        <div class="editor-tabs">
          <div
            class="tab-item"
            :class="{ active: activeTab === 'edit' }"
            @click="activeTab = 'edit'"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </div>
          <div
            class="tab-item"
            :class="{ active: activeTab === 'preview' }"
            @click="activeTab = 'preview'"
          >
            <el-icon><View /></el-icon>
            预览
          </div>
          <div
            v-if="isEditing || !isEdit"
            class="tab-item"
            :class="{ active: activeTab === 'split' }"
            @click="activeTab = 'split'"
          >
            <el-icon><Grid /></el-icon>
            分屏
          </div>
        </div>

        <div class="editor-container" :class="activeTab">
          <!-- Editor Area -->
          <div v-show="activeTab !== 'preview'" class="editor-area">
            <div ref="editorRef" class="milkdown-editor"></div>
            <textarea
              v-model="form.content"
              class="fallback-editor"
              :disabled="isEdit && !isEditing"
              placeholder="使用 Markdown 格式编写文章内容...

支持：
# 一级标题
## 二级标题
**粗体**
*斜体*
[链接](url)
![图片](url)
```代码块```"
            ></textarea>
          </div>

          <!-- Preview Area -->
          <div v-show="activeTab !== 'edit'" class="preview-area">
            <ArticlePreview :content="form.content" />
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Edit, View, Grid, Document, Promotion } from '@element-plus/icons-vue'
import { articleService } from '@/api/services/article'
import { categoryService } from '@/api/services/category'
import { tagService } from '@/api/services/tag'
import { buildTree } from '@/utils/tree'
import ArticleEditForm from './components/ArticleEditForm.vue'
import ArticlePreview from './components/ArticlePreview.vue'
import type { Category, Tag } from '@/api/types'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => route.params.id as string)

// 编辑模式：新建文章默认可编辑，编辑文章默认查看态
const isEditing = ref(!isEdit.value)
const originalForm = ref<string>('') // 保存原始数据用于取消编辑时恢复

const categories = ref<Category[]>([])
const categoryTree = ref<Category[]>([])
const tags = ref<Tag[]>([])
const activeTab = ref<'edit' | 'preview' | 'split'>('split')

// 页面标题
const pageTitle = computed(() => {
  if (!isEdit.value) return '写文章'
  return isEditing.value ? '编辑文章' : '查看文章'
})

const form = ref({
  title: '',
  summary: '',
  content: '',
  category: '',
  tags: [] as string[],
  cover: '',
  status: 'draft' as 'draft' | 'published',
  date: new Date().toISOString(),
})

async function loadCategories() {
  categories.value = await categoryService.getAll()
  categoryTree.value = buildTree(categories.value)
}

async function loadTags() {
  tags.value = await tagService.getAll()
}

async function loadArticle() {
  if (!isEdit.value) return
  const article = await articleService.getById(articleId.value)
  if (article) {
    form.value = {
      title: article.title,
      summary: article.summary,
      content: article.content,
      category: article.category,
      tags: [...article.tags],
      cover: article.cover || '',
      status: article.status,
      date: article.date,
    }
    // 保存原始数据用于取消编辑时恢复
    originalForm.value = JSON.stringify(form.value)
    // 编辑模式默认查看态，预览 tab
    activeTab.value = 'preview'
  }
}

function enterEditMode() {
  isEditing.value = true
  activeTab.value = 'split'
}

async function cancelEdit() {
  // 检查是否有修改
  if (JSON.stringify(form.value) !== originalForm.value) {
    try {
      await ElMessageBox.confirm('当前有未保存的修改，确定要取消编辑吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
    } catch {
      return
    }
  }
  // 恢复原始数据
  if (originalForm.value) {
    form.value = JSON.parse(originalForm.value)
  }
  isEditing.value = false
  activeTab.value = 'preview'
}

async function handleSave(status: 'draft' | 'published') {
  // Validation
  if (!form.value.title.trim()) {
    ElMessage.error('请输入文章标题')
    return
  }
  if (!form.value.category) {
    ElMessage.error('请选择文章分类')
    return
  }
  if (!form.value.content.trim()) {
    ElMessage.error('请输入文章内容')
    return
  }

  try {
    const now = new Date().toISOString()
    const data = {
      ...form.value,
      status,
      date: isEdit.value ? form.value.date : now,
      views: 0,
      comments: 0,
      isTop: false,
    }

    if (isEdit.value) {
      await articleService.update(articleId.value, data)
      ElMessage.success('文章更新成功')
      // 更新原始数据，退出编辑态
      originalForm.value = JSON.stringify(form.value)
      isEditing.value = false
      activeTab.value = 'preview'
    } else {
      await articleService.create(data)
      ElMessage.success('文章发布成功')
      router.push('/articles')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  }
}

onMounted(() => {
  loadCategories()
  loadTags()
  loadArticle()
})
</script>

<style scoped>
.edit-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-content-bg);
}

.edit-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: white;
  border-bottom: 1px solid var(--color-border);
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* Layout */
.edit-layout {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* Sidebar */
.edit-sidebar {
  width: 360px;
  background: white;
  border-right: 1px solid var(--color-border);
  flex-shrink: 0;
}

/* Main Editor */
.edit-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* Tabs */
.editor-tabs {
  display: flex;
  gap: 4px;
  padding: 8px 16px;
  background: white;
  border-bottom: 1px solid var(--color-border);
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 13px;
  color: var(--color-gray-500);
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s;
}

.tab-item:hover {
  background: var(--color-gray-100);
  color: var(--color-gray-700);
}

.tab-item.active {
  background: rgba(59, 130, 246, 0.1);
  color: var(--color-primary);
  font-weight: 500;
}

/* Editor Container */
.editor-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.editor-container.edit .editor-area {
  width: 100%;
}

.editor-container.edit .preview-area {
  display: none;
}

.editor-container.preview .editor-area {
  display: none;
}

.editor-container.preview .preview-area {
  width: 100%;
}

.editor-container.split .editor-area {
  width: 50%;
  border-right: 1px solid var(--color-border);
}

.editor-container.split .preview-area {
  width: 50%;
}

/* Editor Area */
.editor-area {
  background: white;
  overflow: hidden;
}

.fallback-editor {
  width: 100%;
  height: 100%;
  padding: 20px;
  border: none;
  outline: none;
  font-family: 'Fira Code', 'SF Mono', Monaco, monospace;
  font-size: 14px;
  line-height: 1.8;
  resize: none;
  color: var(--color-gray-900);
  background: white;
}

.fallback-editor::placeholder {
  color: var(--color-gray-400);
}

.fallback-editor:disabled {
  background: var(--color-gray-50);
  cursor: not-allowed;
}

/* Preview Area */
.preview-area {
  background: white;
  overflow-y: auto;
}
</style>
