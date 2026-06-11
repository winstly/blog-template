<template>
  <div class="space-y-4">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">文章管理</h2>
      <el-button type="primary" :icon="Plus" @click="$router.push('/articles/create')">
        写文章
      </el-button>
    </div>

    <!-- Filters -->
    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="关键词" class="w-full">
              <el-input
                v-model="filters.keyword"
                placeholder="搜索标题"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类" class="w-full">
              <el-tree-select
                v-model="filters.category"
                :data="categoryTree"
                :props="{ label: 'tagName', value: 'tagCode' }"
                placeholder="选择分类"
                clearable
                check-strictly
                class="w-full"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" class="w-full">
              <el-select v-model="filters.status" placeholder="全部" clearable class="w-full">
                <el-option label="已发布" value="PUBLISHED" />
                <el-option label="草稿" value="DRAFT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template #actions>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </template>
    </SearchCard>

    <!-- Table -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="articles"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.coverUrl"
              :src="row.coverUrl"
              class="w-16 h-10 rounded object-cover"
              preview-teleported
            />
            <div v-else class="w-16 h-10 rounded bg-gray-100 flex items-center justify-center text-gray-400 text-xs">
              无封面
            </div>
          </template>
        </el-table-column>
        <el-table-column label="标题" min-width="180">
          <template #default="{ row }">
            <p class="font-medium text-gray-800 truncate" :title="row.title">{{ row.title }}</p>
          </template>
        </el-table-column>
        <el-table-column label="摘要" min-width="200">
          <template #default="{ row }">
            <p class="text-sm text-gray-500 line-clamp-2">{{ row.summary || '暂无摘要' }}</p>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.category?.tagName || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标签" min-width="150">
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.tags.slice(0, 3)"
              :key="tag.tagCode"
              size="small"
              class="mr-1"
            >
              {{ tag.tagName }}
            </el-tag>
            <span v-if="row.tags.length > 3" class="text-gray-400 text-xs">+{{ row.tags.length - 3 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.publishStatus === 'PUBLISHED' ? 'success' : 'info'" size="small">
              {{ row.publishStatus === 'PUBLISHED' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="浏览" prop="viewCount" width="80" />
        <el-table-column label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.gmtCreate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              title="确定要删除这篇文章吗？"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <Pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        @change="loadArticles"
      >
        <template #batch-actions>
          <el-button
            v-if="selectedArticles.length > 0"
            type="danger"
            size="small"
            @click="handleBatchDelete"
          >
            批量删除 ({{ selectedArticles.length }})
          </el-button>
        </template>
      </Pagination>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { articleService } from '@/api/services/article'
import { categoryService } from '@/api/services/category'
import SearchCard from '@/components/common/SearchCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import { formatDate } from '@/utils/date'
import type { Article, Category } from '@/api/types'

const router = useRouter()

const loading = ref(false)
const articles = ref<Article[]>([])
const categories = ref<Category[]>([])
const categoryTree = ref<Category[]>([])
const selectedArticles = ref<Article[]>([])

const filters = reactive({
  keyword: '',
  category: '',
  status: '' as '' | 'DRAFT' | 'PUBLISHED',
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
})

async function loadCategories() {
  categories.value = await categoryService.getAll()
  categoryTree.value = categories.value
}

async function loadArticles() {
  loading.value = true
  try {
    const result = await articleService.getList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: filters.keyword || undefined,
      category: filters.category || undefined,
      status: filters.status || undefined,
    })
    articles.value = result.list
    pagination.total = result.pagination.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  loadArticles()
}

function handleReset() {
  filters.keyword = ''
  filters.category = ''
  filters.status = ''
  pagination.page = 1
  loadArticles()
}

function handleSelectionChange(selection: Article[]) {
  selectedArticles.value = selection
}

function handleEdit(article: Article) {
  router.push(`/articles/edit/${article.articleCode}`)
}

async function handleDelete(article: Article) {
  try {
    await articleService.delete(article.articleCode)
    ElMessage.success('删除成功')
    loadArticles()
  } catch {
    ElMessage.error('删除失败')
  }
}

async function handleBatchDelete() {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedArticles.value.length} 篇文章吗？`,
      '确认删除',
      { type: 'warning' }
    )
    await articleService.batchDelete(selectedArticles.value.map(a => a.articleCode))
    ElMessage.success('批量删除成功')
    loadArticles()
  } catch (error) {
    // 用户取消操作或删除失败
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

onMounted(() => {
  loadCategories()
  loadArticles()
})
</script>

<style scoped>
.el-form-item {
  margin-bottom: 0;
}
</style>
