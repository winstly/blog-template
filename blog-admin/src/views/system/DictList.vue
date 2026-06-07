<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">字典管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建场景码</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="字段名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索字段名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业务编码" class="w-full">
              <el-input
                v-model="filters.code"
                placeholder="搜索业务编码"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="级别" class="w-full">
              <el-select v-model="filters.level" placeholder="全部" clearable class="w-full">
                <el-option label="场景码" :value="1" />
                <el-option label="分组码" :value="2" />
                <el-option label="业务码" :value="3" />
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

    <el-card shadow="never" class="dict-tree-card">
      <el-tree
        v-loading="loading"
        :data="treeData"
        node-key="id"
        lazy
        :load="loadNode"
        :props="{ label: 'fieldName', children: 'children', isLeaf: 'isLeaf' }"
        draggable
        :allow-drop="allowDrop"
        :expand-on-click-node="false"
        @node-drop="handleDrop"
      >
        <template #default="{ data }">
          <div class="custom-tree-node" :class="`level-${data.level}`">
            <div class="node-content">
              <el-icon class="node-icon">
                <OfficeBuilding v-if="data.level === 1" />
                <FolderOpened v-else-if="data.level === 2" />
                <Document v-else />
              </el-icon>
              <span class="node-label">{{ data.fieldName }}</span>
              <code class="biz-code">{{ getFullCode(data) }}</code>
              <el-tag v-if="data.level === 3" size="small" type="info" effect="plain">
                {{ data.fieldValue }}
              </el-tag>
              <el-tag
                :type="data.status === 'enabled' ? 'success' : 'info'"
                size="small"
              >
                {{ data.status === 'enabled' ? '启用' : '禁用' }}
              </el-tag>
            </div>
            <div class="node-actions">
              <el-button
                v-if="data.level < 3"
                link
                type="primary"
                size="small"
                :icon="Plus"
                @click.stop="handleAddChild(data)"
              >
                {{ data.level === 1 ? '分组' : '业务' }}
              </el-button>
              <el-button
                link
                type="primary"
                size="small"
                :icon="Edit"
                @click.stop="handleEdit(data)"
              >
                编辑
              </el-button>
              <el-popconfirm
                title="确定删除吗？"
                :content="data.hasChildren ? '该节点下有子节点，将一并删除' : ''"
                @confirm="handleDelete(data)"
              >
                <template #reference>
                  <el-button
                    link
                    type="danger"
                    size="small"
                    :icon="Delete"
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

    <!-- Drawer -->
    <el-drawer
      v-model="dialog.visible"
      :title="dialogTitle"
      direction="rtl"
      size="450px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item v-if="form.parentData" label="父级">
          <div class="parent-info">
            <span>{{ form.parentData.fieldName }}</span>
            <code>{{ getFullCode(form.parentData) }}</code>
          </div>
        </el-form-item>

        <el-form-item label="级别">
          <el-tag :type="levelTypeMap[form.level]" size="large">
            {{ levelNameMap[form.level] }}
          </el-tag>
        </el-form-item>

        <el-form-item :label="codeLabel" prop="code">
          <el-input
            v-model="form.code"
            :placeholder="codePlaceholder"
            :disabled="dialog.isEdit"
            @input="form.code = form.code.toUpperCase()"
          >
            <template v-if="form.prefix" #prepend>{{ form.prefix }}</template>
          </el-input>
          <p class="form-tip">{{ codeTip }}</p>
        </el-form-item>

        <el-form-item label="字段名称" prop="fieldName">
          <el-input v-model="form.fieldName" placeholder="请输入字段名称" />
        </el-form-item>

        <el-form-item v-if="form.level === 3" label="字段值" prop="fieldValue">
          <el-input v-model="form.fieldValue" placeholder="请输入字段值" />
        </el-form-item>

        <el-form-item label="排序">
          <el-input-number v-model="form.order" :min="0" :max="999" class="w-full" />
        </el-form-item>

        <el-form-item label="状态">
          <el-switch
            v-model="form.status"
            active-value="enabled"
            inactive-value="disabled"
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
  OfficeBuilding,
  FolderOpened,
  Document,
} from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import type { FormInstance, FormRules } from 'element-plus'
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DictItem, DictStatus } from '@/api/types'

const loading = ref(false)
const treeData = ref<DictItem[]>([])
const isSearchMode = ref(false)

const filters = reactive({
  name: '',
  code: '',
  level: null as number | null,
})

const levelNameMap = { 1: '场景码', 2: '分组码', 3: '业务码' }
const levelTypeMap = { 1: 'primary', 2: 'success', 3: 'warning' }

// 懒加载节点数据
async function loadNode(node: any, resolve: (data: DictItem[]) => void) {
  if (node.level === 0) {
    const roots = await loadRootDicts()
    return resolve(roots)
  }
  const children = await loadChildren(node.data)
  resolve(children)
}

async function loadRootDicts(): Promise<DictItem[]> {
  loading.value = true
  await new Promise(r => setTimeout(r, 200))
  try {
    return mockDicts
      .filter(d => d.level === 1)
      .map(d => ({
        ...d,
        hasChildren: hasChildren(d),
        isLeaf: !hasChildren(d),
      }))
      .sort((a, b) => a.order - b.order)
  } finally {
    loading.value = false
  }
}

async function loadChildren(parent: DictItem): Promise<DictItem[]> {
  await new Promise(r => setTimeout(r, 100))

  if (parent.level === 1) {
    return mockDicts
      .filter(d => d.level === 2 && d.sceneCode === parent.sceneCode)
      .map(d => ({
        ...d,
        hasChildren: hasChildren(d),
        isLeaf: !hasChildren(d),
      }))
      .sort((a, b) => a.order - b.order)
  }

  if (parent.level === 2) {
    return mockDicts
      .filter(d =>
        d.level === 3 &&
        d.sceneCode === parent.sceneCode &&
        d.groupCode === parent.groupCode
      )
      .map(d => ({ ...d, isLeaf: true, hasChildren: false }))
      .sort((a, b) => a.order - b.order)
  }

  return []
}

async function loadAllDicts(): Promise<DictItem[]> {
  loading.value = true
  await new Promise(r => setTimeout(r, 200))
  loading.value = false
  return mockDicts.map(d => ({ ...d }))
}

function hasChildren(item: DictItem, source = mockDicts): boolean {
  if (item.level === 3) return false
  if (item.level === 1) {
    return source.some(d => d.level === 2 && d.sceneCode === item.sceneCode)
  }
  return source.some(d =>
    d.level === 3 &&
    d.sceneCode === item.sceneCode &&
    d.groupCode === item.groupCode
  )
}

async function handleSearch() {
  // 条件为空时，加载所有数据
  if (!filters.name && !filters.code && !filters.level) {
    return handleReset()
  }

  isSearchMode.value = true
  const all = await loadAllDicts()

  let filtered = all.filter(item => {
    const nameMatch = !filters.name || item.fieldName.toLowerCase().includes(filters.name.toLowerCase())
    const codeMatch = !filters.code || getFullCode(item).toLowerCase().includes(filters.code.toLowerCase())
    const levelMatch = !filters.level || item.level === filters.level
    return nameMatch && codeMatch && levelMatch
  })

  // 构建完整树
  treeData.value = buildTreeFromFlat(filtered.map(d => ({
    ...d,
    isLeaf: !hasChildren(d, all),
    hasChildren: hasChildren(d, all),
  })))
}

function buildTreeFromFlat(items: DictItem[]): DictItem[] {
  const map = new Map<string, DictItem>()
  const roots: DictItem[] = []

  items.forEach(item => {
    map.set(item.id, { ...item, children: [] })
  })

  items.forEach(item => {
    const node = map.get(item.id)!
    if (item.level === 1) {
      roots.push(node)
    } else if (item.level === 2) {
      const parent = map.get(items.find(d => d.level === 1 && d.sceneCode === item.sceneCode)?.id || '')
      if (parent) {
        parent.children!.push(node)
      } else {
        roots.push(node)
      }
    } else if (item.level === 3) {
      const parent = map.get(items.find(d =>
        d.level === 2 &&
        d.sceneCode === item.sceneCode &&
        d.groupCode === item.groupCode
      )?.id || '')
      if (parent) {
        parent.children!.push(node)
      } else {
        roots.push(node)
      }
    }
  })

  return roots.sort((a, b) => a.order - b.order)
}

async function handleReset() {
  filters.name = ''
  filters.code = ''
  filters.level = null
  isSearchMode.value = true
  // 加载所有数据
  const all = await loadAllDicts()
  treeData.value = buildTreeFromFlat(all.map(d => ({
    ...d,
    isLeaf: !hasChildren(d, all),
    hasChildren: hasChildren(d, all),
  })))
}

// Dialog
const dialog = reactive({
  visible: false,
  isEdit: false,
  currentId: '',
})

const formRef = ref<FormInstance>()
const form = reactive({
  level: 1 as 1 | 2 | 3,
  parentData: null as DictItem | null,
  prefix: '',
  code: '',
  fieldName: '',
  fieldValue: '',
  order: 0,
  status: 'enabled' as DictStatus,
})

const dialogTitle = computed(() =>
  (dialog.isEdit ? '编辑' : '新建') + levelNameMap[form.level]
)

const codeLabel = computed(() =>
  form.level === 1 ? '场景码' : form.level === 2 ? '分组码' : '业务码'
)

const codePlaceholder = computed(() => {
  if (form.level === 1) return '如：BLOG'
  if (form.level === 2) return '如：ARTICLE'
  return '如：STATUS'
})

const codeTip = computed(() => {
  if (form.level === 1) return '场景码作为顶级分类'
  if (form.level === 2) return '分组码属于场景下'
  return '业务码属于分组下'
})

const rules: FormRules = {
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z][A-Z0-9_]*$/, message: '编码以大写字母开头', trigger: 'blur' },
  ],
  fieldName: [{ required: true, message: '请输入字段名称', trigger: 'blur' }],
  fieldValue: [{ required: form.level === 3, message: '请输入字段值', trigger: 'blur' }],
}

function handleCreate() {
  dialog.isEdit = false
  dialog.currentId = ''
  form.level = 1
  form.parentData = null
  form.prefix = ''
  form.code = ''
  form.fieldName = ''
  form.fieldValue = ''
  form.order = treeData.value.length
  form.status = 'enabled'
  dialog.visible = true
}

function handleAddChild(parent: DictItem) {
  dialog.isEdit = false
  dialog.currentId = ''
  form.level = (parent.level + 1) as 1 | 2 | 3
  form.parentData = parent
  form.prefix = getFullCode(parent) + '.'
  form.code = ''
  form.fieldName = ''
  form.fieldValue = ''
  form.order = 0
  form.status = 'enabled'
  dialog.visible = true
}

function handleEdit(data: DictItem) {
  dialog.isEdit = true
  dialog.currentId = data.id
  form.level = data.level
  form.parentData = null
  form.prefix = ''
  form.code = data.level === 1 ? data.sceneCode : data.level === 2 ? data.groupCode! : data.bizCode!
  form.fieldName = data.fieldName
  form.fieldValue = data.fieldValue || ''
  form.order = data.order
  form.status = data.status
  dialog.visible = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()

    if (dialog.isEdit) {
      const item = mockDicts.find(d => d.id === dialog.currentId)
      if (item) {
        item.fieldName = form.fieldName
        item.fieldValue = form.fieldValue
        item.order = form.order
        item.status = form.status
      }
      ElMessage.success('更新成功')
    } else {
      const newItem: DictItem = {
        id: Date.now().toString(),
        level: form.level,
        sceneCode: form.level === 1 ? form.code : form.parentData!.sceneCode,
        groupCode: form.level === 2 ? form.code : form.level === 3 ? form.parentData!.groupCode : undefined,
        bizCode: form.level === 3 ? form.code : undefined,
        fieldName: form.fieldName,
        fieldValue: form.fieldValue,
        order: form.order,
        status: form.status,
      }
      mockDicts.push(newItem)
      ElMessage.success('创建成功')
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

async function handleDelete(data: DictItem) {
  const idsToDelete = new Set<string>([data.id])

  if (data.level === 1) {
    mockDicts.filter(d => d.level > 1 && d.sceneCode === data.sceneCode)
      .forEach(d => idsToDelete.add(d.id))
  } else if (data.level === 2) {
    mockDicts.filter(d => d.level === 3 && d.sceneCode === data.sceneCode && d.groupCode === data.groupCode)
      .forEach(d => idsToDelete.add(d.id))
  }

  mockDicts = mockDicts.filter(d => !idsToDelete.has(d.id))
  ElMessage.success('删除成功')
  refreshTree()
}

async function refreshTree() {
  // 统一切换到搜索模式并重新加载数据
  // 因为懒加载模式的刷新机制不稳定
  isSearchMode.value = true
  const all = await loadAllDicts()
  treeData.value = buildTreeFromFlat(all.map(d => ({
    ...d,
    isLeaf: !hasChildren(d, all),
    hasChildren: hasChildren(d, all),
  })))
}

function allowDrop(_draggingNode: Node, _dropNode: Node, type: 'prev' | 'inner' | 'next'): boolean {
  return type !== 'inner'
}

function handleDrop(_draggingNode: Node, _dropNode: Node, _type: 'before' | 'after' | 'inner') {
  ElMessage.success('排序已更新')
}

function getFullCode(item: DictItem): string {
  if (item.level === 1) return item.sceneCode
  if (item.level === 2) return `${item.sceneCode}.${item.groupCode}`
  return `${item.sceneCode}.${item.groupCode}.${item.bizCode}`
}

// Mock 数据 - 扁平存储
let mockDicts: DictItem[] = [
  { id: '1', level: 1, sceneCode: 'BLOG', fieldName: '博客系统', order: 1, status: 'enabled' },
  { id: '2', level: 1, sceneCode: 'SYSTEM', fieldName: '系统管理', order: 2, status: 'enabled' },
  { id: '1-1', level: 2, sceneCode: 'BLOG', groupCode: 'ARTICLE', fieldName: '文章管理', order: 1, status: 'enabled' },
  { id: '1-2', level: 2, sceneCode: 'BLOG', groupCode: 'USER', fieldName: '用户管理', order: 2, status: 'enabled' },
  { id: '2-1', level: 2, sceneCode: 'SYSTEM', groupCode: 'CONFIG', fieldName: '系统配置', order: 1, status: 'enabled' },
  { id: '1-1-1', level: 3, sceneCode: 'BLOG', groupCode: 'ARTICLE', bizCode: 'STATUS', fieldName: '文章状态', fieldValue: 'status', order: 1, status: 'enabled' },
  { id: '1-1-2', level: 3, sceneCode: 'BLOG', groupCode: 'ARTICLE', bizCode: 'TYPE', fieldName: '文章类型', fieldValue: 'type', order: 2, status: 'enabled' },
  { id: '1-2-1', level: 3, sceneCode: 'BLOG', groupCode: 'USER', bizCode: 'STATUS', fieldName: '用户状态', fieldValue: 'status', order: 1, status: 'enabled' },
  { id: '1-2-2', level: 3, sceneCode: 'BLOG', groupCode: 'USER', bizCode: 'ROLE', fieldName: '用户角色', fieldValue: 'role', order: 2, status: 'enabled' },
  { id: '2-1-1', level: 3, sceneCode: 'SYSTEM', groupCode: 'CONFIG', bizCode: 'SITE', fieldName: '站点配置', fieldValue: 'site', order: 1, status: 'enabled' },
]
</script>

<style scoped>
.dict-tree-card {
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

.node-label {
  font-size: 14px;
  color: var(--color-gray-900);
  font-weight: 500;
}

.biz-code {
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
