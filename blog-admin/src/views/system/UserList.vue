<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">用户管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建用户</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="用户名" class="w-full">
              <el-input
                v-model="filters.username"
                placeholder="搜索用户名"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="昵称" class="w-full">
              <el-input
                v-model="filters.nickname"
                placeholder="搜索昵称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
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

    <el-card shadow="never">
      <el-table v-loading="loading" :data="userList" row-key="id">
        <el-table-column label="头像" width="70">
          <template #default="{ row }">
            <el-avatar :size="36" :src="row.avatar">
              {{ row.nickname.charAt(0).toUpperCase() }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="username" min-width="120" />
        <el-table-column label="昵称" prop="nickname" min-width="120" />
        <el-table-column label="邮箱" prop="email" min-width="180" />
        <el-table-column label="角色" prop="role" min-width="120">
          <template #default="{ row }">
            <el-tag v-for="role in row.roles" :key="role" size="small" class="mr-1">
              {{ role }}
            </el-tag>
          </template>
        </el-table-column>
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
            <el-button link type="primary" @click="handleResetPassword(row)">重置密码</el-button>
            <el-popconfirm title="确定删除此用户吗？" @confirm="handleDelete(row)">
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

    <!-- User Dialog -->
    <el-drawer v-model="dialog.visible" :title="dialog.isEdit ? '编辑用户' : '新建用户'" direction="rtl" size="450px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="dialog.isEdit" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!dialog.isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-select v-model="form.roles" multiple placeholder="请选择角色" class="w-full">
            <el-option
              v-for="role in roleOptions"
              :key="role.id"
              :label="role.name"
              :value="role.name"
            />
          </el-select>
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

    <!-- Reset Password Drawer -->
    <el-drawer v-model="pwdDialog.visible" title="重置密码" direction="rtl" size="400px">
      <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
        <el-form-item label="新密码" prop="password">
          <el-input v-model="pwdForm.password" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handlePwdSubmit">确定</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { SystemUser, RoleOption, UserStatus } from '@/api/types'

const loading = ref(false)
const userList = ref<SystemUser[]>([])
const roleOptions = ref<RoleOption[]>([
  { id: '1', name: '管理员' },
  { id: '2', name: '编辑' },
  { id: '3', name: '作者' },
])

const filters = reactive({
  username: '',
  nickname: '',
  status: '' as '' | UserStatus,
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
})

// User Dialog
const dialog = reactive({
  visible: false,
  isEdit: false,
  currentId: '',
})

const formRef = ref<FormInstance>()
const form = reactive({
  username: '',
  nickname: '',
  email: '',
  password: '',
  roles: [] as string[],
  status: 'enabled' as UserStatus,
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  roles: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

// Password Dialog
const pwdDialog = reactive({
  visible: false,
  userId: '',
})

const pwdFormRef = ref<FormInstance>()
const pwdForm = reactive({
  password: '',
  confirmPassword: '',
})

const pwdRules: FormRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== pwdForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

// Mock data
let mockUsers: SystemUser[] = [
  {
    id: '1',
    username: 'admin',
    nickname: '管理员',
    email: 'admin@example.com',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
    roles: ['管理员'],
    status: 'enabled',
    createTime: '2024-01-01 00:00:00',
  },
  {
    id: '2',
    username: 'editor',
    nickname: '编辑',
    email: 'editor@example.com',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=editor',
    roles: ['编辑'],
    status: 'enabled',
    createTime: '2024-01-02 00:00:00',
  },
]

async function loadData() {
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 300))
    let result = [...mockUsers]
    if (filters.username) {
      result = result.filter(u => u.username.includes(filters.username))
    }
    if (filters.nickname) {
      result = result.filter(u => u.nickname.includes(filters.nickname))
    }
    if (filters.status) {
      result = result.filter(u => u.status === filters.status)
    }
    userList.value = result
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
  filters.username = ''
  filters.nickname = ''
  filters.status = ''
  pagination.page = 1
  loadData()
}

function handleCreate() {
  dialog.isEdit = false
  dialog.currentId = ''
  form.username = ''
  form.nickname = ''
  form.email = ''
  form.password = ''
  form.roles = []
  form.status = 'enabled'
  dialog.visible = true
}

function handleEdit(row: SystemUser) {
  dialog.isEdit = true
  dialog.currentId = row.id
  form.username = row.username
  form.nickname = row.nickname
  form.email = row.email
  form.password = ''
  form.roles = [...row.roles]
  form.status = row.status
  dialog.visible = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    if (dialog.isEdit) {
      const index = mockUsers.findIndex(u => u.id === dialog.currentId)
      if (index !== -1) {
        mockUsers[index] = {
          ...mockUsers[index],
          nickname: form.nickname,
          email: form.email,
          roles: form.roles,
          status: form.status,
        }
        ElMessage.success('用户更新成功')
      }
    } else {
      mockUsers.push({
        id: Date.now().toString(),
        username: form.username,
        nickname: form.nickname,
        email: form.email,
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${form.username}`,
        roles: form.roles,
        status: form.status,
        createTime: new Date().toLocaleString('zh-CN'),
      })
      ElMessage.success('用户创建成功')
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

async function handleDelete(row: SystemUser) {
  if (row.username === 'admin') {
    ElMessage.error('不能删除管理员账号')
    return
  }
  mockUsers = mockUsers.filter(u => u.id !== row.id)
  ElMessage.success('删除成功')
  loadData()
}

function handleResetPassword(row: SystemUser) {
  pwdDialog.userId = row.id
  pwdForm.password = ''
  pwdForm.confirmPassword = ''
  pwdDialog.visible = true
}

async function handlePwdSubmit() {
  if (!pwdFormRef.value) return
  try {
    await pwdFormRef.value.validate()
    ElMessage.success('密码重置成功')
    pwdDialog.visible = false
  } catch {
    // validation failed
  }
}

onMounted(() => {
  loadData()
})
</script>
