<template>
  <div class="space-y-4">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">角色管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建角色</el-button>
    </div>

    <!-- Search -->
    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索角色名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" class="w-full">
              <el-select v-model="filters.status" placeholder="全部" clearable class="w-full">
                <el-option label="启用" value="enabled" />
                <el-option label="禁用" value="disabled" />
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
      <el-table v-loading="loading" :data="roleList" row-key="id">
        <el-table-column label="角色名称" prop="name" min-width="150">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <el-tag :type="row.isSystem ? 'danger' : 'primary'" size="small">
                {{ row.isSystem ? '系统' : '自定义' }}
              </el-tag>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="角色编码" prop="code" min-width="150">
          <template #default="{ row }">
            <code class="code-text">{{ row.code }}</code>
          </template>
        </el-table-column>
        <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip />
        <el-table-column label="用户数" prop="userCount" width="90" align="center" />
        <el-table-column label="状态" prop="status" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'info'" size="small">
              {{ row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handleAssignPermission(row)">分配权限</el-button>
            <el-popconfirm
              v-if="!row.isSystem"
              title="确定删除此角色吗？"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        @change="loadData"
      />
    </el-card>

    <!-- Role Dialog -->
    <el-drawer v-model="dialog.visible" :title="dialog.isEdit ? '编辑角色' : '新建角色'" direction="rtl" size="450px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入角色编码（英文）"
            :disabled="dialog.isEdit"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="form.status"
            active-value="enabled"
            inactive-value="disabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-drawer>

    <!-- Permission Assign Dialog -->
    <PermissionAssignDialog
      v-model:visible="permDialog.visible"
      :role-id="permDialog.roleId"
      :selected-keys="permDialog.selectedKeys"
      @save="handleSavePermissions"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import PermissionAssignDialog from './components/PermissionAssignDialog.vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Role, RoleStatus } from '@/api/types'

const loading = ref(false)
const roleList = ref<Role[]>([])

const filters = reactive({
  name: '',
  status: '' as '' | RoleStatus,
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
})

// Dialog
const dialog = reactive({
  visible: false,
  isEdit: false,
  currentId: '',
})

const formRef = ref<FormInstance>()
const form = reactive({
  name: '',
  code: '',
  description: '',
  status: 'enabled' as RoleStatus,
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '编码只能包含字母、数字和下划线', trigger: 'blur' },
  ],
}

// Permission Dialog
const permDialog = reactive({
  visible: false,
  roleId: '',
  selectedKeys: [] as string[],
})

// Mock data
let mockRoles: Role[] = [
  {
    id: '1',
    name: '管理员',
    code: 'admin',
    description: '系统管理员，拥有所有权限',
    status: 'enabled',
    isSystem: true,
    userCount: 1,
    createTime: '2024-01-01 00:00:00',
    permissions: ['article:view', 'article:create', 'article:edit', 'article:delete', 'category:manage', 'tag:manage'],
  },
  {
    id: '2',
    name: '编辑',
    code: 'editor',
    description: '内容编辑，可管理文章和分类',
    status: 'enabled',
    isSystem: true,
    userCount: 1,
    createTime: '2024-01-01 00:00:00',
    permissions: ['article:view', 'article:create', 'article:edit', 'category:manage'],
  },
  {
    id: '3',
    name: '作者',
    code: 'author',
    description: '普通作者，只能管理自己的文章',
    status: 'enabled',
    isSystem: true,
    userCount: 0,
    createTime: '2024-01-01 00:00:00',
    permissions: ['article:view', 'article:create'],
  },
]

async function loadData() {
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 300))
    let result = [...mockRoles]
    if (filters.name) {
      result = result.filter(r => r.name.includes(filters.name))
    }
    if (filters.status) {
      result = result.filter(r => r.status === filters.status)
    }
    roleList.value = result
    pagination.total = result.length
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  filters.name = ''
  filters.status = ''
  pagination.page = 1
  loadData()
}

function handleCreate() {
  dialog.isEdit = false
  dialog.currentId = ''
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = 'enabled'
  dialog.visible = true
}

function handleEdit(row: Role) {
  if (row.isSystem) {
    ElMessage.warning('系统角色不能编辑')
    return
  }
  dialog.isEdit = true
  dialog.currentId = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description
  form.status = row.status
  dialog.visible = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    if (dialog.isEdit) {
      const index = mockRoles.findIndex(r => r.id === dialog.currentId)
      if (index !== -1) {
        mockRoles[index] = {
          ...mockRoles[index],
          name: form.name,
          description: form.description,
          status: form.status,
        }
        ElMessage.success('角色更新成功')
      }
    } else {
      mockRoles.push({
        id: Date.now().toString(),
        name: form.name,
        code: form.code,
        description: form.description,
        status: form.status,
        isSystem: false,
        userCount: 0,
        createTime: new Date().toLocaleString('zh-CN'),
        permissions: [],
      })
      ElMessage.success('角色创建成功')
    }
    dialog.visible = false
    loadData()
  } catch (error) {
    if (error !== false) {
      const message = error instanceof Error ? error.message : '操作失败'
      ElMessage.error(message)
    }
  }
}

async function handleDelete(row: Role) {
  if (row.isSystem) {
    ElMessage.warning('系统角色不能删除')
    return
  }
  if (row.userCount > 0) {
    ElMessage.error('该角色下还有用户，不能删除')
    return
  }
  mockRoles = mockRoles.filter(r => r.id !== row.id)
  ElMessage.success('删除成功')
  loadData()
}

// Permission assignment
function handleAssignPermission(row: Role) {
  permDialog.roleId = row.id
  permDialog.selectedKeys = [...row.permissions]
  permDialog.visible = true
}

function handleSavePermissions(roleId: string, keys: string[]) {
  const index = mockRoles.findIndex(r => r.id === roleId)
  if (index !== -1) {
    mockRoles[index].permissions = keys
    ElMessage.success('权限分配成功')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.code-text {
  background: var(--color-gray-100);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  color: var(--color-gray-600);
}
</style>
