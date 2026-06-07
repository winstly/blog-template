<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">分类管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建分类</el-button>
    </div>

    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类名称" class="w-full">
              <el-input
                v-model="filters.name"
                placeholder="搜索分类名称"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="层级" class="w-full">
              <el-select v-model="filters.level" placeholder="全部" clearable class="w-full">
                <el-option label="顶级分类" value="root" />
                <el-option label="子分类" value="child" />
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

    <el-card shadow="never" class="category-tree-card">
      <el-tree
        v-loading="loading"
        :data="treeData"
        node-key="id"
        :props="{ label: 'name', children: 'children' }"
        :expand-on-click-node="false"
        default-expand-all
        draggable
        :allow-drop="allowDrop"
        @node-drop="handleDrop"
      >
        <template #default="{ data, node }">
          <CategoryTreeNode
            :data="data"
            :node="node"
            @add-child="handleAddChild"
            @edit="handleEdit"
            @delete="handleDelete"
          />
        </template>
      </el-tree>

      <el-empty v-if="!loading && treeData.length === 0" description="暂无数据" />
    </el-card>

    <!-- Drawer -->
    <el-drawer
      v-model="dialogVisible"
      :title="dialogTitle"
      direction="rtl"
      size="400px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父分类">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="选择父分类（不选为顶级分类）"
            clearable
            check-strictly
            class="w-full"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999" class="w-full" />
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
import { Plus } from '@element-plus/icons-vue'
import CategoryTreeNode from './components/CategoryTreeNode.vue'
import SearchCard from '@/components/common/SearchCard.vue'
import { categoryService } from '@/api/services/category'
import { buildTree } from '@/utils/tree'
import type { Category } from '@/api/types'
import type { FormInstance, FormRules } from 'element-plus'
import type Node from 'element-plus/es/components/tree/src/model/node'

const loading = ref(false)
const categories = ref<Category[]>([])
const dialogVisible = ref(false)

// Search filters
const filters = reactive({
  name: '',
  level: '' as '' | 'root' | 'child',
})

// 树数据 - 使用 filteredCategories 构建层级结构
const filteredCategories = computed(() => {
  // 无筛选条件时返回全部数据
  if (!filters.name && !filters.level) {
    return categories.value
  }

  const all = categories.value

  // 筛选匹配的节点
  let filtered = all.filter(cat => {
    const nameMatch = !filters.name || cat.name.toLowerCase().includes(filters.name.toLowerCase())
    const levelMatch = !filters.level ||
      (filters.level === 'root' ? !cat.parentId : !!cat.parentId)
    return nameMatch && levelMatch
  })

  // 收集相关节点的 ID（祖先和子节点）
  const relatedIds = new Set<string>()
  filtered.forEach(item => {
    relatedIds.add(item.id)
    // 添加所有祖先
    let current = item
    while (current.parentId) {
      relatedIds.add(current.parentId)
      current = all.find(c => c.id === current.parentId) || current
    }
    // 添加所有子节点
    const addChildIds = (parentId: string) => {
      all.filter(c => c.parentId === parentId).forEach(c => {
        relatedIds.add(c.id)
        addChildIds(c.id)
      })
    }
    addChildIds(item.id)
  })

  return all.filter(c => relatedIds.has(c.id))
})

// treeData 使用 filteredCategories 构建树
const treeData = computed(() => {
  return buildTree(filteredCategories.value, { sortBy: 'sort' })
})

// 构建分类树用于下拉选择
const categoryTree = computed(() => {
  return buildTree(categories.value, { sortBy: 'sort' })
})

// 加载所有分类
async function loadCategories() {
  loading.value = true
  try {
    const all = await categoryService.getAll()
    categories.value = all
  } finally {
    loading.value = false
  }
}

// 搜索 - 只需更新 filters，computed 会自动重新计算
function handleSearch() {
  // 筛选逻辑由 filteredCategories computed 处理
  // 无需手动操作数据
}

// 重置搜索 - 清空筛选条件，computed 会自动恢复全部数据
function handleReset() {
  filters.name = ''
  filters.level = ''
  // 无需重新加载，computed 会自动恢复显示全部数据
}

const isEdit = ref(false)
const currentId = ref('')

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑分类'
  return currentId.value ? '添加子分类' : '新建分类'
})

const formRef = ref<FormInstance>()
const form = ref({
  name: '',
  parentId: undefined as string | undefined,
  sort: 0,
})

const rules: FormRules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' },
  ],
}

function handleCreate() {
  isEdit.value = false
  currentId.value = ''
  form.value = {
    name: '',
    parentId: undefined,
    sort: 0,
  }
  dialogVisible.value = true
}

function handleAddChild(parent: Category) {
  isEdit.value = false
  currentId.value = parent.id
  form.value = {
    name: '',
    parentId: parent.id,
    sort: 0,
  }
  dialogVisible.value = true
}

function handleEdit(category: Category) {
  isEdit.value = true
  currentId.value = category.id
  form.value = {
    name: category.name,
    parentId: category.parentId,
    sort: category.sort || 0,
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return

  // 表单验证
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value) {
      await categoryService.update(currentId.value, {
        name: form.value.name,
        parentId: form.value.parentId,
        sort: form.value.sort,
      })
      ElMessage.success('分类更新成功')
    } else {
      await categoryService.create({
        name: form.value.name,
        parentId: form.value.parentId,
        sort: form.value.sort,
      })
      ElMessage.success('分类创建成功')
    }

    dialogVisible.value = false
    await loadCategories()
  } catch (error) {
    const message = error instanceof Error ? error.message : '操作失败'
    ElMessage.error(message)
  }
}

async function handleDelete(category: Category) {
  // 检查是否有子节点
  const hasChildren = categories.value.some(c => c.parentId === category.id)
  if (hasChildren) {
    ElMessage.error('请先删除子分类')
    return
  }

  try {
    await categoryService.delete(category.id)
    ElMessage.success('删除成功')
    await loadCategories()
  } catch (error) {
    const message = error instanceof Error ? error.message : '删除失败'
    ElMessage.error(message)
  }
}

// 拖拽控制
function allowDrop(
  _draggingNode: Node,
  dropNode: Node,
  type: 'prev' | 'inner' | 'next'
): boolean {
  if (type === 'inner') {
    // 不允许拖入自己的子节点
    const isDescendant = (parent: Category, childId: string): boolean => {
      if (parent.id === childId) return true
      if (parent.children) {
        return parent.children.some(child => isDescendant(child, childId))
      }
      return false
    }

    const dragData = _draggingNode.data as Category
    const dropData = dropNode.data as Category

    if (isDescendant(dragData, dropData.id)) {
      return false
    }
  }
  return true
}

// 处理拖拽完成
async function handleDrop(
  draggingNode: Node,
  dropNode: Node,
  type: 'before' | 'after' | 'inner'
) {
  const dragData = draggingNode.data as Category
  const dropData = dropNode.data as Category

  let newParentId: string | undefined
  if (type === 'inner') {
    newParentId = dropData.id
  } else {
    newParentId = dropData.parentId
  }

  try {
    await categoryService.update(dragData.id, {
      parentId: newParentId,
    })
    ElMessage.success('移动成功')
    await loadCategories()
  } catch (error) {
    const message = error instanceof Error ? error.message : '移动失败'
    ElMessage.error(message)
    await loadCategories()
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-tree-card {
  padding: 16px;
}

:deep(.el-tree-node__content) {
  height: auto;
  padding: 8px 0;
}

:deep(.el-tree-node__content:hover) .node-actions {
  opacity: 1;
}

:deep(.el-tree-node__content:hover) {
  background: var(--color-gray-100);
}

:deep(.el-tree-node.is-drop-inner > .el-tree-node__content) {
  background: rgba(59, 130, 246, 0.08);
  border: 1px dashed var(--color-primary);
}
</style>
