<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">权限管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建权限</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="权限名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索权限名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="权限编码" class="w-full">
              <el-input
                v-model="filters.code"
                placeholder="搜索权限编码"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="类型" class="w-full">
              <el-select v-model="filters.type" placeholder="全部" clearable class="w-full">
                <el-option label="菜单" value="menu" />
                <el-option label="按钮" value="button" />
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

    <el-card shadow="never" class="permission-tree-card">
      <el-tree
        v-loading="loading"
        :data="treeData"
        node-key="id"
        lazy
        :load="loadNode"
        :props="{ label: 'name', children: 'children', isLeaf: 'isLeaf' }"
        draggable
        :allow-drop="allowDrop"
        :expand-on-click-node="false"
        @node-drop="handleDrop"
      >
        <template #default="{ data }">
          <div class="custom-tree-node" :class="`type-${data.type}`">
            <div class="node-content">
              <el-icon class="node-icon">
                <Folder v-if="data.type === 'menu'" />
                <Key v-else />
              </el-icon>
              <span class="node-label">{{ data.name }}</span>
              <code class="permission-code">{{ data.code }}</code>
              <el-tag
                :type="data.type === 'menu' ? 'success' : 'primary'"
                size="small"
                effect="plain"
              >
                {{ data.type === 'menu' ? '菜单' : '按钮' }}
              </el-tag>
              <el-tag v-if="data.isSystem" type="danger" size="small" effect="dark">系统</el-tag>
            </div>
            <div class="node-actions">
              <el-button
                v-if="data.type === 'menu'"
                link
                type="primary"
                size="small"
                :icon="Plus"
                @click.stop="handleAddChild(data)"
              >
                子权限
              </el-button>
              <el-button
                link
                type="primary"
                size="small"
                :icon="Edit"
                :disabled="data.isSystem"
                @click.stop="handleEdit(data)"
              >
                编辑
              </el-button>
              <el-popconfirm
                title="确定删除此权限吗？"
                :content="data.hasChildren ? '该权限下有子权限，将一并删除' : ''"
                @confirm="handleDelete(data)"
              >
                <template #reference>
                  <el-button
                    link
                    type="danger"
                    size="small"
                    :icon="Delete"
                    :disabled="data.isSystem"
                    @click.stop
                  >
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </template>
      </el-tree>

      <el-empty v-if="!loading && isSearchMode && treeData.length === 0" description="暂无数据" />
    </el-card>

    <!-- Dialog -->
    <el-drawer
      v-model="dialog.visible"
      :title="dialogTitle"
      direction="rtl"
      size="450px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item v-if="form.parentData" label="父权限">
          <div class="parent-info">
            <span>{{ form.parentData.name }}</span>
            <code>{{ form.parentData.code }}</code>
          </div>
        </el-form-item>

        <el-form-item label="权限类型" prop="type">
          <el-radio-group v-model="form.type" :disabled="dialog.isEdit">
            <el-radio value="menu">菜单权限</el-radio>
            <el-radio value="button">按钮权限</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="!form.parentData && !dialog.isEdit" label="父权限">
          <el-tree-select
            v-model="form.parentId"
            :data="parentTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="选择父权限（不选为顶级权限）"
            clearable
            check-strictly
            class="w-full"
          />
          <p class="form-tip">选择父权限将创建为其子权限</p>
        </el-form-item>

        <el-form-item label="权限名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>

        <el-form-item label="权限编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入权限编码（如：article:create）"
            :disabled="dialog.isEdit"
            @input="form.code = form.code.toLowerCase()"
          >
            <template v-if="form.prefix" #prepend>{{ form.prefix }}</template>
          </el-input>
          <p class="form-tip">编码格式：module:action，用于程序识别</p>
        </el-form-item>

        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="2"
            placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Plus,
  Edit,
  Delete,
  Folder,
  Key,
} from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import { buildTree } from '@/utils/tree'
import type { FormInstance, FormRules } from 'element-plus'
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { Permission, PermissionType } from '@/api/types'

const loading = ref(false)
const treeData = ref<Permission[]>([])
const isSearchMode = ref(false)

const filters = reactive({
  name: '',
  code: '',
  type: '' as '' | PermissionType,
})

// 懒加载节点数据
async function loadNode(node: any, resolve: (data: Permission[]) => void) {
  if (node.level === 0) {
    const roots = await loadRootPermissions()
    return resolve(roots)
  }
  const children = await loadChildren(node.data.id)
  resolve(children)
}

async function loadRootPermissions(): Promise<Permission[]> {
  loading.value = true
  await new Promise(r => setTimeout(r, 200))
  try {
    return mockPermissions
      .filter(p => !p.parentId)
      .map(p => ({
        ...p,
        hasChildren: hasChildren(p),
        isLeaf: !hasChildren(p),
      }))
  } finally {
    loading.value = false
  }
}

async function loadChildren(parentId: string): Promise<Permission[]> {
  await new Promise(r => setTimeout(r, 100))
  return mockPermissions
    .filter(p => p.parentId === parentId)
    .map(p => ({
      ...p,
      hasChildren: hasChildren(p),
      isLeaf: !hasChildren(p),
    }))
}

async function loadAllPermissions(): Promise<Permission[]> {
  loading.value = true
  await new Promise(r => setTimeout(r, 200))
  loading.value = false
  return mockPermissions.map(p => ({ ...p }))
}

function hasChildren(item: Permission, source = mockPermissions): boolean {
  return source.some(p => p.parentId === item.id)
}

async function handleSearch() {
  // 条件为空时，加载所有数据
  if (!filters.name && !filters.code && !filters.type) {
    return handleReset()
  }

  isSearchMode.value = true
  const all = await loadAllPermissions()

  let filtered = all.filter(item => {
    const nameMatch = !filters.name || item.name.toLowerCase().includes(filters.name.toLowerCase())
    const codeMatch = !filters.code || item.code.toLowerCase().includes(filters.code.toLowerCase())
    const typeMatch = !filters.type || item.type === filters.type
    return nameMatch && codeMatch && typeMatch
  })

  // 构建完整树
  treeData.value = buildTree(filtered.map(p => ({
    ...p,
    isLeaf: !hasChildren(p, all),
    hasChildren: hasChildren(p, all),
  })))
}

async function handleReset() {
  filters.name = ''
  filters.code = ''
  filters.type = ''
  isSearchMode.value = true
  // 加载所有数据
  const all = await loadAllPermissions()
  treeData.value = buildTree(all.map(p => ({
    ...p,
    isLeaf: !hasChildren(p, all),
    hasChildren: hasChildren(p, all),
  })))
}

// 父权限选择树（只显示菜单类型）
const parentTreeData = computed(() => {
  const filterMenuOnly = (items: Permission[]): Permission[] => {
    return items
      .filter(item => item.type === 'menu')
      .map(item => ({
        ...item,
        children: item.children ? filterMenuOnly(item.children) : undefined,
      }))
  }
  return filterMenuOnly(mockPermissions)
})

// Dialog
const dialog = reactive({
  visible: false,
  isEdit: false,
  currentId: '',
})

const formRef = ref<FormInstance>()
const form = reactive({
  type: 'menu' as PermissionType,
  parentId: undefined as string | undefined,
  parentData: null as Permission | null,
  prefix: '',
  name: '',
  code: '',
  description: '',
})

const dialogTitle = computed(() => {
  if (dialog.isEdit) return '编辑权限'
  return form.parentData ? '新建子权限' : '新建权限'
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { pattern: /^[a-z]+(:[a-z]+)*$/, message: '编码格式为：module:action，小写字母', trigger: 'blur' },
  ],
}

function handleCreate() {
  dialog.isEdit = false
  dialog.currentId = ''
  form.type = 'menu'
  form.parentId = undefined
  form.parentData = null
  form.prefix = ''
  form.name = ''
  form.code = ''
  form.description = ''
  dialog.visible = true
}

function handleAddChild(parent: Permission) {
  dialog.isEdit = false
  dialog.currentId = ''
  form.type = 'button'
  form.parentId = parent.id
  form.parentData = parent
  form.prefix = parent.code + ':'
  form.name = ''
  form.code = ''
  form.description = ''
  dialog.visible = true
}

function handleEdit(data: Permission) {
  if (data.isSystem) {
    ElMessage.warning('系统权限不能编辑')
    return
  }
  dialog.isEdit = true
  dialog.currentId = data.id
  form.type = data.type
  form.parentData = null
  form.prefix = ''
  form.name = data.name
  form.code = data.code
  form.description = data.description || ''
  dialog.visible = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()

    if (dialog.isEdit) {
      const item = mockPermissions.find(p => p.id === dialog.currentId)
      if (item) {
        item.name = form.name
        item.description = form.description
      }
      ElMessage.success('权限更新成功')
    } else {
      const newPermission: Permission = {
        id: Date.now().toString(),
        name: form.name,
        code: form.prefix + form.code,
        type: form.type,
        description: form.description,
        parentId: form.parentId,
        isSystem: false,
      }
      mockPermissions.push(newPermission)
      ElMessage.success('权限创建成功')
    }

    dialog.visible = false
    refreshTree()
  } catch (error) {
    if (error !== false) {
      const message = error instanceof Error ? error.message : '操作失败'
      ElMessage.error(message)
    }
  }
}

async function handleDelete(data: Permission) {
  if (data.isSystem) {
    ElMessage.warning('系统权限不能删除')
    return
  }

  // 递归删除子权限
  const idsToDelete = new Set<string>()
  const collectChildren = (parentId: string) => {
    mockPermissions.filter(p => p.parentId === parentId).forEach(p => {
      idsToDelete.add(p.id)
      collectChildren(p.id)
    })
  }

  idsToDelete.add(data.id)
  collectChildren(data.id)

  mockPermissions = mockPermissions.filter(p => !idsToDelete.has(p.id))
  ElMessage.success('删除成功')
  refreshTree()
}

async function refreshTree() {
  // 统一切换到搜索模式并重新加载数据
  // 因为懒加载模式的刷新机制不稳定
  isSearchMode.value = true
  const all = await loadAllPermissions()
  treeData.value = buildTree(all.map(p => ({
    ...p,
    isLeaf: !hasChildren(p, all),
    hasChildren: hasChildren(p, all),
  })))
}

function allowDrop(_draggingNode: Node, _dropNode: Node, type: 'prev' | 'inner' | 'next'): boolean {
  return type !== 'inner'
}

function handleDrop(_draggingNode: Node, _dropNode: Node, _type: 'before' | 'after' | 'inner') {
  ElMessage.success('排序已更新')
}

// Mock 数据
let mockPermissions: Permission[] = [
  {
    id: '1',
    name: '文章管理',
    code: 'article',
    type: 'menu',
    description: '文章相关功能',
    isSystem: true,
  },
  {
    id: '1-1',
    name: '查看文章',
    code: 'article:view',
    type: 'button',
    parentId: '1',
  },
  {
    id: '1-2',
    name: '创建文章',
    code: 'article:create',
    type: 'button',
    parentId: '1',
  },
  {
    id: '1-3',
    name: '编辑文章',
    code: 'article:edit',
    type: 'button',
    parentId: '1',
  },
  {
    id: '1-4',
    name: '删除文章',
    code: 'article:delete',
    type: 'button',
    parentId: '1',
  },
  {
    id: '2',
    name: '用户管理',
    code: 'user',
    type: 'menu',
    description: '用户相关功能',
    isSystem: true,
  },
  {
    id: '2-1',
    name: '查看用户',
    code: 'user:view',
    type: 'button',
    parentId: '2',
  },
  {
    id: '2-2',
    name: '创建用户',
    code: 'user:create',
    type: 'button',
    parentId: '2',
  },
  {
    id: '3',
    name: '系统设置',
    code: 'system',
    type: 'menu',
    description: '系统相关功能',
    isSystem: true,
  },
  {
    id: '3-1',
    name: '字典管理',
    code: 'system:dict',
    type: 'button',
    parentId: '3',
  },
  {
    id: '3-2',
    name: '权限管理',
    code: 'system:permission',
    type: 'button',
    parentId: '3',
  },
]
</script>

<style scoped>
.permission-tree-card {
  padding: 16px;
  min-height: 400px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-right: 8px;
  gap: 16px;
}

.node-content {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.node-icon {
  font-size: 18px;
  color: #409eff;
}

.type-button .node-icon {
  color: #3b82f6;
}

.node-label {
  font-size: 14px;
  color: var(--color-gray-900);
  font-weight: 500;
}

.permission-code {
  background: var(--color-gray-100);
  padding: 2px 8px;
  border-radius: 4px;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  color: var(--color-gray-600);
}

.node-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

:deep(.el-tree-node__content:hover) .node-actions {
  opacity: 1;
}

:deep(.el-tree-node__content) {
  height: auto;
  padding: 8px 0;
}

:deep(.el-tree-node__content:hover) {
  background: var(--color-gray-100);
}

.parent-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--color-gray-100);
  border-radius: 6px;
}

.parent-info code {
  font-size: 12px;
  color: var(--color-gray-600);
}

.form-tip {
  font-size: 12px;
  color: var(--color-gray-400);
  margin-top: 4px;
}
</style>
