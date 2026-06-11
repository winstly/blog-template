<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">友链管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">添加友链</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="网站名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索网站名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" class="w-full">
              <el-select v-model="filters.active" placeholder="全部" clearable class="w-full">
                <el-option label="启用" :value="true" />
                <el-option label="禁用" :value="false" />
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
      <el-table v-loading="loading" :data="pagedLinks" row-key="id">
        <el-table-column label="排序" width="80">
          <template #default="{ row }">
            <span class="text-gray-400">{{ row.order }}</span>
          </template>
        </el-table-column>
        <el-table-column label="网站" min-width="250">
          <template #default="{ row }">
            <div class="flex items-center gap-3">
              <el-avatar :size="40" :src="row.logo" shape="square">
                {{ row.name.charAt(0).toUpperCase() }}
              </el-avatar>
              <div>
                <p class="font-medium text-gray-800">{{ row.name }}</p>
                <a
                  :href="row.url"
                  target="_blank"
                  class="text-sm text-blue-500 hover:underline"
                >
                  {{ row.url }}
                </a>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="描述" prop="description" min-width="200">
          <template #default="{ row }">
            <span class="text-gray-600 text-sm">{{ row.description || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.active"
              @change="(val: boolean) => handleStatusChange(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row, $index }">
            <el-button
              link
              :disabled="$index === 0"
              @click="handleMove(row, -1)"
            >
              <el-icon><ArrowUp /></el-icon>
            </el-button>
            <el-button
              link
              :disabled="$index === links.length - 1"
              @click="handleMove(row, 1)"
            >
              <el-icon><ArrowDown /></el-icon>
            </el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              title="确定删除此友链吗？"
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
        :total="filteredLinks.length"
        @change="handlePageChange"
      />
    </el-card>

    <!-- Drawer -->
    <el-drawer
      v-model="dialogVisible"
      :title="isEdit ? '编辑友链' : '添加友链'"
      direction="rtl"
      size="450px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="网站名称" />
        </el-form-item>
        <el-form-item label="链接" prop="url">
          <el-input v-model="form.url" placeholder="https://example.com" />
        </el-form-item>
        <el-form-item label="Logo">
          <el-input v-model="form.logo" placeholder="Logo URL（可选）" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="网站描述（可选）"
          />
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="form.active" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import { linkService } from '@/api/services/link'
import type { FriendLink } from '@/api/types'
import type { FormInstance, FormRules } from 'element-plus'

const loading = ref(false)
const links = ref<FriendLink[]>([])

// Search filters
const filters = reactive({
  name: '',
  active: undefined as boolean | undefined,
})

// 使用 computed 进行过滤
const filteredLinks = computed(() => {
  return links.value.filter(link => {
    const nameMatch = !filters.name || link.name.toLowerCase().includes(filters.name.toLowerCase())
    const statusMatch = filters.active === undefined || link.active === filters.active
    return nameMatch && statusMatch
  })
})

// Pagination
const pagination = reactive({
  page: 1,
  pageSize: 10,
})

const pagedLinks = computed(() => {
  const start = (pagination.page - 1) * pagination.pageSize
  const end = start + pagination.pageSize
  return filteredLinks.value.slice(start, end)
})

function handlePageChange() {
  // Page change handled by computed property
}
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentId = ref('')
const formRef = ref<FormInstance>()

const form = reactive({
  name: '',
  url: '',
  logo: '',
  description: '',
  active: true,
})

const rules: FormRules = {
  name: [
    { required: true, message: '请输入网站名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' },
  ],
  url: [
    { required: true, message: '请输入链接地址', trigger: 'blur' },
    { type: 'url', message: '请输入有效的 URL', trigger: 'blur' },
  ],
}

async function loadLinks() {
  loading.value = true
  try {
    links.value = await linkService.getAll()
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
  filters.active = undefined
  pagination.page = 1
}

function handleCreate() {
  isEdit.value = false
  currentId.value = ''
  form.name = ''
  form.url = ''
  form.logo = ''
  form.description = ''
  form.active = true
  dialogVisible.value = true
}

function handleEdit(link: FriendLink) {
  isEdit.value = true
  currentId.value = link.id
  form.name = link.name
  form.url = link.url
  form.logo = link.logo || ''
  form.description = link.description || ''
  form.active = link.active
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value) {
      await linkService.update(currentId.value, { ...form })
      ElMessage.success('友链更新成功')
    } else {
      await linkService.create({ ...form })
      ElMessage.success('友链添加成功')
    }

    dialogVisible.value = false
    loadLinks()
  } catch (error) {
    const message = error instanceof Error ? error.message : '操作失败'
    ElMessage.error(message)
  }
}

async function handleStatusChange(link: FriendLink, active: boolean) {
  try {
    await linkService.update(link.id, { active })
    ElMessage.success(active ? '已启用' : '已禁用')
  } catch {
    link.active = !active
    ElMessage.error('操作失败')
  }
}

async function handleMove(link: FriendLink, direction: -1 | 1) {
  try {
    await linkService.reorderSingle(link.id, direction)
    ElMessage.success('排序已更新')
    loadLinks()
  } catch {
    ElMessage.error('排序失败')
  }
}

async function handleDelete(link: FriendLink) {
  try {
    await linkService.delete(link.id)
    ElMessage.success('删除成功')
    loadLinks()
  } catch {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadLinks()
})
</script>
