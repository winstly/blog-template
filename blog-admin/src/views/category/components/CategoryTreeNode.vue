<template>
  <div class="custom-tree-node">
    <div class="node-content">
      <el-icon class="node-icon">
        <Folder v-if="!node.isLeaf || data.hasChildren" />
        <Document v-else />
      </el-icon>
      <span class="node-label">{{ data.tagName }}</span>
      <el-tag size="small" type="info" class="node-count">
        {{ data.articleCount || 0 }} 篇文章
      </el-tag>
    </div>
    <div class="node-actions">
      <el-button
        link
        type="primary"
        size="small"
        :icon="Plus"
        @click.stop="handleAddChild"
      >
        添加子分类
      </el-button>
      <el-button
        link
        type="primary"
        size="small"
        :icon="Edit"
        @click.stop="handleEdit"
      >
        编辑
      </el-button>
      <el-popconfirm
        title="确定删除此分类吗？"
        @confirm="handleDelete"
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

<script setup lang="ts">
import { Plus, Edit, Delete, Folder, Document } from '@element-plus/icons-vue'
import type { Category } from '@/api/types'
import type Node from 'element-plus/es/components/tree/src/model/node'

interface Props {
  data: Category & { hasChildren?: boolean; isLeaf?: boolean }
  node: Node
}

const props = defineProps<Props>()

const emit = defineEmits<{
  'add-child': [data: Category]
  'edit': [data: Category]
  'delete': [data: Category, node: Node]
}>()

function handleAddChild() {
  emit('add-child', props.data)
}

function handleEdit() {
  emit('edit', props.data)
}

function handleDelete() {
  emit('delete', props.data, props.node)
}
</script>

<style scoped>
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
  color: var(--color-primary);
}

.node-label {
  font-size: 14px;
  color: var(--color-gray-900);
  font-weight: 500;
}

.node-count {
  margin-left: 8px;
}

.node-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

/* Note: The hover effect is applied from the parent component */
</style>
