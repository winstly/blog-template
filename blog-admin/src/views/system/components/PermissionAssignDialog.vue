<template>
  <el-drawer
    :model-value="visible"
    title="分配权限"
    direction="rtl"
    size="550px"
    @update:model-value="handleVisibleChange"
  >
    <div class="permission-drawer-content">
      <!-- Breadcrumb Navigation -->
      <div class="perm-breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <el-link
              :underline="false"
              :type="currentLevel === 0 ? 'primary' : ''"
              @click="goToLevel(0)"
            >
              全部权限
            </el-link>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentParent">
            <el-link
              :underline="false"
              :type="currentLevel === 1 ? 'primary' : ''"
              @click="goToLevel(1)"
            >
              {{ currentParent.name }}
            </el-link>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- Permission Tree -->
      <el-tree
        ref="treeRef"
        :data="filteredTree"
        show-checkbox
        node-key="id"
        :default-checked-keys="selectedKeys"
        :props="{ label: 'name', children: 'children' }"
        class="permission-tree"
      >
        <template #default="{ data }">
          <div class="perm-tree-node">
            <div class="perm-node-content">
              <div class="perm-type-icon" :class="`type-${data.type}-bg`">
                <el-icon :size="14">
                  <Folder v-if="data.type === 'menu'" />
                  <Key v-else />
                </el-icon>
              </div>
              <span class="perm-node-name">{{ data.name }}</span>
              <el-tag
                :type="data.type === 'menu' ? 'success' : ''"
                size="small"
                effect="plain"
                class="perm-type-tag"
              >
                {{ data.type === 'menu' ? '菜单' : '按钮' }}
              </el-tag>
            </div>
            <el-button
              v-if="data.children?.length && data.type === 'menu'"
              link
              type="primary"
              size="small"
              @click.stop="viewChildren(data)"
            >
              查看
            </el-button>
          </div>
        </template>
      </el-tree>

      <div class="perm-selected-info">
        已选择 {{ selectedCount }} 项权限
      </div>
    </div>
    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSave">保存</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Folder, Key } from '@element-plus/icons-vue'
import type { ElTree } from 'element-plus'
import { usePermissionStore } from '@/plugins/stores/permission'
import type { Permission } from '@/api/types'

interface Props {
  visible: boolean
  roleId: string
  selectedKeys: string[]
}

const props = defineProps<Props>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  save: [roleId: string, keys: string[]]
}>()

const treeRef = ref<InstanceType<typeof ElTree>>()
const permissionStore = usePermissionStore()

// Permission tree data from store
const permissionTree = ref<Permission[]>([])

// Navigation state
const currentLevel = ref(0)
const currentParent = ref<Permission | null>(null)

// Filtered tree based on navigation
const filteredTree = computed(() => {
  let data = permissionTree.value
  if (currentLevel.value === 1 && currentParent.value) {
    data = currentParent.value.children || []
  }
  return data
})

// Selected count (based on initial selectedKeys, updated on save)
const selectedCount = computed(() => {
  return props.selectedKeys.length
})

// View children of a menu permission
function viewChildren(data: Permission) {
  if (data.type === 'menu' && data.children?.length) {
    currentParent.value = data
    currentLevel.value = 1
  }
}

// Navigate to specific level
function goToLevel(level: number) {
  currentLevel.value = level
  if (level === 0) {
    currentParent.value = null
  }
}

// Handle dialog visibility change
function handleVisibleChange(value: boolean) {
  emit('update:visible', value)
}

// Handle cancel
function handleCancel() {
  emit('update:visible', false)
}

// Handle save
function handleSave() {
  const checkedKeys = treeRef.value?.getCheckedKeys() as string[]
  const halfCheckedKeys = treeRef.value?.getHalfCheckedKeys() as string[]
  const allKeys = [...checkedKeys, ...halfCheckedKeys]

  emit('save', props.roleId, allKeys)
  emit('update:visible', false)
}

// Reset navigation when dialog opens
watch(() => props.visible, (newVal) => {
  if (newVal) {
    currentLevel.value = 0
    currentParent.value = null
  }
})

// Load permission tree on mount
permissionTree.value = permissionStore.getPermissionTree()
</script>

<style scoped>
.permission-drawer-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.perm-breadcrumb {
  padding: 0 8px 12px;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 12px;
}

.permission-tree {
  flex: 1;
  overflow-y: auto;
  max-height: 380px;
}

.perm-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 4px 0;
}

.perm-node-content {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.perm-type-icon {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.type-menu-bg {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.type-button-bg {
  background: rgba(103, 194, 58, 0.1);
  color: #3b82f6;
}

.perm-node-name {
  font-size: 14px;
  font-weight: 500;
}

.perm-type-tag {
  margin-left: 4px;
}

.perm-selected-info {
  padding: 12px 8px 0;
  border-top: 1px solid #e4e7ed;
  margin-top: 12px;
  font-size: 13px;
  color: #606266;
}

:deep(.el-tree-node__content) {
  height: auto;
  min-height: 40px;
}
</style>
