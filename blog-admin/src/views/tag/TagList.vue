<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">标签管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建标签</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标签名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索标签名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文章数" class="w-full">
              <el-select v-model="filters.hasArticles" placeholder="全部" clearable class="w-full">
                <el-option label="有文章" value="yes" />
                <el-option label="无文章" value="no" />
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

    <el-card shadow="never">
      <el-table v-loading="loading" :data="pagedTags">
        <el-table-column label="名称" prop="tagName" />
        <el-table-column label="文章数" prop="articleCount" width="100" />
        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.gmtCreate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" @click="handleMerge(row)">合并</el-button>
            <el-popconfirm
              title="确定删除此标签吗？"
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
        :total="filteredTags.length"
        @change="handlePageChange"
      />
    </el-card>

    <!-- Create/Edit Drawer -->
    <el-drawer
      v-model="dialogVisible"
      :title="isEdit ? '编辑标签' : '新建标签'"
      direction="rtl"
      size="400px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入标签名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-drawer>

    <!-- Merge Drawer -->
    <el-drawer v-model="mergeVisible" title="合并标签" direction="rtl" size="400px">
      <el-form label-width="80px">
        <el-form-item label="目标标签">
          <el-select v-model="mergeTarget" placeholder="选择目标标签" class="w-full">
            <el-option
              v-for="tag in tags.filter(t => t.tagCode !== mergeSourceId)"
              :key="tag.tagCode"
              :label="tag.tagName"
              :value="tag.tagCode"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="mergeVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMergeSubmit">确定</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import { tagService } from '@/api/services/tag'
import { formatDate } from '@/utils/date'
import type { Tag } from '@/api/types'
import type { FormInstance, FormRules } from 'element-plus'

const loading = ref(false)
const tags = ref<Tag[]>([])

// Search filters
const filters = reactive({
  name: '',
  hasArticles: '' as '' | 'yes' | 'no',
})

// 使用 computed 进行过滤
const filteredTags = computed(() => {
  return tags.value.filter(tag => {
    const nameMatch = !filters.name || tag.tagName.toLowerCase().includes(filters.name.toLowerCase())
    const articlesMatch = !filters.hasArticles ||
      (filters.hasArticles === 'yes' ? (tag.articleCount || 0) > 0 : (tag.articleCount || 0) === 0)
    return nameMatch && articlesMatch
  })
})

// Pagination
const pagination = reactive({
  page: 1,
  pageSize: 10,
})

const pagedTags = computed(() => {
  const start = (pagination.page - 1) * pagination.pageSize
  const end = start + pagination.pageSize
  return filteredTags.value.slice(start, end)
})

function handlePageChange() {
  // Page change handled by computed property
}
const dialogVisible = ref(false)
const mergeVisible = ref(false)
const isEdit = ref(false)
const currentCode = ref('')
const formRef = ref<FormInstance>()

const form = reactive({
  tagName: '',
})

const rules: FormRules = {
  tagName: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' },
  ],
}

const mergeSourceId = ref('')
const mergeSourceName = ref('')
const mergeTarget = ref('')

async function loadTags() {
  loading.value = true
  try {
    tags.value = await tagService.getAll()
    pagination.page = 1
  } finally {
    loading.value = false
  }
}

// 搜索 - 只需重置页码，computed 会自动重新计算
function handleSearch() {
  pagination.page = 1
}

// 重置搜索 - 清空筛选条件并重置页码
function handleReset() {
  filters.name = ''
  filters.hasArticles = ''
  pagination.page = 1
}

function handleCreate() {
  isEdit.value = false
  currentCode.value = ''
  form.tagName = ''
  dialogVisible.value = true
}

function handleEdit(tag: Tag) {
  isEdit.value = true
  currentCode.value = tag.tagCode
  form.tagName = tag.tagName
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value) {
      await tagService.update(currentCode.value, { tagName: form.tagName })
      ElMessage.success('标签更新成功')
    } else {
      await tagService.create({ tagCode: 'tag_' + Date.now(), tagName: form.tagName })
      ElMessage.success('标签创建成功')
    }

    dialogVisible.value = false
    loadTags()
  } catch (error) {
    const message = error instanceof Error ? error.message : '操作失败'
    ElMessage.error(message)
  }
}

async function handleDelete(tag: Tag) {
  try {
    await tagService.delete(tag.tagCode)
    ElMessage.success('删除成功')
    loadTags()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

function handleMerge(tag: Tag) {
  mergeSourceId.value = tag.tagCode
  mergeSourceName.value = tag.tagName
  mergeTarget.value = ''
  mergeVisible.value = true
}

async function handleMergeSubmit() {
  if (!mergeTarget.value) {
    ElMessage.warning('请选择目标标签')
    return
  }

  try {
    await tagService.move(mergeSourceId.value, mergeTarget.value)
    ElMessage.success('合并成功')
    mergeVisible.value = false
    loadTags()
  } catch (error) {
    console.error('合并失败:', error)
    ElMessage.error('合并失败')
  }
}

onMounted(() => {
  loadTags()
})
</script>
