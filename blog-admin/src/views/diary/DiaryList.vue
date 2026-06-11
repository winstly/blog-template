<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">日记管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">写日记</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年份" class="w-full">
              <el-select v-model="filters.year" placeholder="全部年份" clearable class="w-full" @change="handleSearch">
                <el-option
                  v-for="year in availableYears"
                  :key="year"
                  :label="year + '年'"
                  :value="year"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容关键词" class="w-full">
              <el-input
                v-model="filters.keyword"
                placeholder="搜索日记内容"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template #actions>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </template>
    </SearchCard>

    <!-- Diary List -->
    <el-timeline v-loading="loading">
      <el-timeline-item
        v-for="diary in displayedDiaries"
        :key="diary.id"
        :timestamp="formatDate(diary.date)"
        placement="top"
      >
        <el-card shadow="hover">
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <p class="text-gray-800 whitespace-pre-wrap">{{ diary.content }}</p>
              <div v-if="diary.images?.length" class="flex gap-2 mt-3">
                <el-image
                  v-for="(img, idx) in diary.images"
                  :key="idx"
                  :src="img"
                  class="w-24 h-24 rounded object-cover cursor-pointer"
                  preview-teleported
                  :preview-src-list="diary.images"
                  :initial-index="idx"
                />
              </div>
            </div>
            <div class="flex gap-2 ml-4">
              <el-button link type="primary" @click="handleEdit(diary)">编辑</el-button>
              <el-popconfirm
                title="确定删除这条日记吗？"
                @confirm="handleDelete(diary)"
              >
                <template #reference>
                  <el-button link type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <el-empty v-if="!loading && displayedDiaries.length === 0" description="暂无日记" />

    <!-- 加载更多 -->
    <div v-if="loadingMore" class="text-center py-4 text-gray-500">
      <el-icon class="animate-spin"><Loading /></el-icon> 加载中...
    </div>
    <div v-else-if="noMore && displayedDiaries.length > 0" class="text-center py-4 text-gray-400 text-sm">
      没有更多日记了
    </div>
    <div v-else-if="!noMore && displayedDiaries.length > 0" class="text-center py-4">
      <el-button link type="primary" @click="loadMore">加载更多</el-button>
    </div>

    <!-- Dialog -->
    <el-drawer
      v-model="dialogVisible"
      :title="isEdit ? '编辑日记' : '写日记'"
      direction="rtl"
      size="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            class="w-full"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="记录今天的心情..."
          />
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
            action="#"
            :auto-upload="false"
            list-type="picture-card"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            :file-list="imageFiles"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Loading } from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import { diaryService } from '@/api/services/diary'
import type { DiaryEntry } from '@/api/types'
import type { FormInstance, FormRules, UploadFile } from 'element-plus'

const loading = ref(false)
const loadingMore = ref(false)
const noMore = ref(false)
const diaries = ref<DiaryEntry[]>([])
const displayedDiaries = ref<DiaryEntry[]>([])
const dialogVisible = ref(false)

// Search filters
const filters = reactive({
  year: '',
  keyword: '',
})

// Pagination for waterfall
const pageSize = 10
let currentPage = 1

const isEdit = ref(false)
const currentId = ref('')
const formRef = ref<FormInstance>()
const imageFiles = ref<UploadFile[]>([])

const form = reactive({
  date: '',
  content: '',
  images: [] as string[],
})

const rules: FormRules = {
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { min: 1, max: 2000, message: '长度在 1 到 2000 个字符', trigger: 'blur' },
  ],
}

const availableYears = computed(() => {
  const years = new Set<string>()
  diaries.value.forEach(d => {
    years.add(d.date.slice(0, 4))
  })
  return Array.from(years).sort((a, b) => b.localeCompare(a))
})

// Filtered diaries based on filters
const filteredDiaries = computed(() => {
  return diaries.value.filter(diary => {
    const yearMatch = !filters.year || diary.date.startsWith(filters.year)
    const keywordMatch = !filters.keyword ||
      diary.content.toLowerCase().includes(filters.keyword.toLowerCase())
    return yearMatch && keywordMatch
  })
})

function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    month: 'long',
    day: 'numeric',
    weekday: 'short',
  })
}

async function loadDiaries() {
  loading.value = true
  try {
    const year = filters.year ? parseInt(filters.year) : undefined
    diaries.value = await diaryService.getList({ year })
    // Reset waterfall state
    currentPage = 1
    displayedDiaries.value = filteredDiaries.value.slice(0, pageSize)
    noMore.value = displayedDiaries.value.length >= filteredDiaries.value.length
  } finally {
    loading.value = false
  }
}

async function loadMore() {
  if (loadingMore.value || noMore.value) return

  loadingMore.value = true
  // Simulate loading delay for better UX
  await new Promise(resolve => setTimeout(resolve, 300))

  currentPage++
  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  const newItems = filteredDiaries.value.slice(start, end)

  if (newItems.length > 0) {
    displayedDiaries.value.push(...newItems)
  }

  if (displayedDiaries.value.length >= filteredDiaries.value.length) {
    noMore.value = true
  }

  loadingMore.value = false
}

function handleSearch() {
  // Reset waterfall when search changes
  currentPage = 1
  displayedDiaries.value = filteredDiaries.value.slice(0, pageSize)
  noMore.value = displayedDiaries.value.length >= filteredDiaries.value.length
}

function handleReset() {
  filters.year = ''
  filters.keyword = ''
  loadDiaries()
}

function handleCreate() {
  isEdit.value = false
  currentId.value = ''
  form.date = new Date().toISOString().slice(0, 10)
  form.content = ''
  form.images = []
  imageFiles.value = []
  dialogVisible.value = true
}

function handleEdit(diary: DiaryEntry) {
  isEdit.value = true
  currentId.value = diary.id
  form.date = diary.date
  form.content = diary.content
  form.images = diary.images ? [...diary.images] : []
  imageFiles.value = form.images.map((url, idx) => ({
    name: `image-${idx}.jpg`,
    url,
    uid: idx,
  })) as UploadFile[]
  dialogVisible.value = true
}

function handleImageChange(file: UploadFile) {
  const reader = new FileReader()
  reader.onload = (e) => {
    form.images.push(e.target?.result as string)
  }
  reader.readAsDataURL(file.raw!)
}

function handleImageRemove(file: UploadFile) {
  const idx = imageFiles.value.findIndex(f => f.uid === file.uid)
  if (idx > -1) {
    form.images.splice(idx, 1)
  }
}

async function handleSubmit() {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value) {
      await diaryService.update(currentId.value, { ...form })
      ElMessage.success('日记更新成功')
    } else {
      await diaryService.create({ ...form })
      ElMessage.success('日记创建成功')
    }

    dialogVisible.value = false
    loadDiaries()
  } catch (error) {
    const message = error instanceof Error ? error.message : '操作失败'
    ElMessage.error(message)
  }
}

async function handleDelete(diary: DiaryEntry) {
  try {
    await diaryService.delete(diary.id)
    ElMessage.success('删除成功')
    loadDiaries()
  } catch {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadDiaries()
})
</script>

<style scoped>
:deep(.el-timeline-item__node) {
  background-color: var(--el-color-primary);
}
</style>
