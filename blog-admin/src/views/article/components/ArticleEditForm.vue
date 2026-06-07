<template>
  <div class="sidebar-content">
    <h3 class="section-title">基本信息</h3>

    <!-- Title -->
    <el-form-item label="文章标题" required class="form-item">
      <el-input
        v-model="form.title"
        placeholder="请输入文章标题"
        size="large"
        maxlength="100"
        show-word-limit
        :disabled="disabled"
      />
    </el-form-item>

    <!-- Summary -->
    <el-form-item label="文章摘要" class="form-item">
      <el-input
        v-model="form.summary"
        type="textarea"
        :rows="3"
        placeholder="请输入文章摘要（不填将自动提取正文前200字）"
        maxlength="500"
        show-word-limit
        :disabled="disabled"
      />
    </el-form-item>

    <!-- Category -->
    <el-form-item label="所属分类" required class="form-item">
      <el-tree-select
        v-model="form.category"
        :data="categoryTree"
        :props="{ label: 'name', value: 'name' }"
        placeholder="选择分类"
        check-strictly
        class="w-full"
        :disabled="disabled"
      />
    </el-form-item>

    <!-- Tags -->
    <el-form-item label="文章标签" class="form-item">
      <el-select
        v-model="form.tags"
        multiple
        filterable
        allow-create
        placeholder="选择或输入标签"
        class="w-full"
        :disabled="disabled"
      >
        <el-option
          v-for="tag in tags"
          :key="tag.id"
          :label="tag.name"
          :value="tag.name"
        />
      </el-select>
    </el-form-item>

    <!-- Cover -->
    <el-form-item label="封面图片" class="form-item">
      <el-upload
        v-if="!disabled"
        class="cover-uploader"
        action="#"
        :auto-upload="false"
        :show-file-list="false"
        :on-change="handleCoverChange"
      >
        <img v-if="form.cover" :src="form.cover" class="cover-preview" />
        <div v-else class="cover-placeholder">
          <el-icon :size="24"><Plus /></el-icon>
          <span>点击上传封面</span>
        </div>
      </el-upload>
      <!-- 查看态只显示图片 -->
      <div v-else class="cover-display">
        <img v-if="form.cover" :src="form.cover" class="cover-preview" />
        <div v-else class="cover-placeholder">
          <span>暂无封面</span>
        </div>
      </div>
      <el-button
        v-if="form.cover && !disabled"
        link
        type="danger"
        size="small"
        @click="form.cover = ''"
      >
        删除封面
      </el-button>
    </el-form-item>

    <!-- Status Preview -->
    <div class="status-card">
      <h4 class="status-title">发布状态</h4>
      <div class="status-item">
        <span class="status-label">当前状态</span>
        <el-tag :type="form.status === 'published' ? 'success' : 'info'">
          {{ form.status === 'published' ? '已发布' : '草稿' }}
        </el-tag>
      </div>
      <div class="status-item">
        <span class="status-label">创建时间</span>
        <span class="status-value">{{ formatDate(form.date) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { Category, Tag } from '@/api/types'
import type { UploadFile } from 'element-plus'

interface FormData {
  title: string
  summary: string
  content: string
  category: string
  tags: string[]
  cover: string
  status: 'draft' | 'published'
  date: string
}

const props = defineProps<{
  modelValue: FormData
  categoryTree: Category[]
  tags: Tag[]
  disabled?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: FormData]
}>()

const form = computed({
  get: () => props.modelValue,
  set: (val: FormData) => emit('update:modelValue', val),
})

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleString('zh-CN')
}

function handleCoverChange(file: UploadFile) {
  const reader = new FileReader()
  reader.onload = (e) => {
    form.value.cover = e.target?.result as string
  }
  reader.readAsDataURL(file.raw!)
}
</script>

<style scoped>
.sidebar-content {
  padding: 20px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0 0 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-light);
}

.form-item {
  margin-bottom: 20px;
}

.form-item :deep(.el-form-item__label) {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-gray-600);
  padding-bottom: 8px;
}

/* Cover Upload */
.cover-uploader {
  border: 2px dashed var(--color-border);
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.2s;
}

.cover-uploader:hover {
  border-color: var(--color-primary);
}

.cover-placeholder {
  height: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--color-gray-400);
  font-size: 13px;
}

.cover-preview {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
}

.cover-display {
  border: 1px solid var(--color-border);
  border-radius: 8px;
  overflow: hidden;
}

.cover-display .cover-placeholder {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-gray-400);
  font-size: 13px;
  background: var(--color-gray-50);
}

/* Status Card */
.status-card {
  background: var(--color-gray-50);
  border-radius: 8px;
  padding: 16px;
  margin-top: 24px;
}

.status-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0 0 12px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.status-label {
  font-size: 12px;
  color: var(--color-gray-500);
}

.status-value {
  font-size: 12px;
  color: var(--color-gray-600);
}
</style>
