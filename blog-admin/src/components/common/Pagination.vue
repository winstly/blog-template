<template>
  <div class="pagination-wrapper">
    <!-- Batch Actions -->
    <div class="batch-actions">
      <slot name="batch-actions" />
    </div>

    <!-- Pagination Controls -->
    <div class="pagination-controls">
      <!-- Total Info -->
      <div class="total-info">
        共 <span class="total-count">{{ total }}</span> 条记录
        <span class="page-info" v-if="totalPages > 1">
          ，第 <span class="current-page">{{ currentPage }}</span> / {{ totalPages }} 页
        </span>
      </div>

      <!-- Page Size Selector -->
      <div class="page-size-selector">
        <span class="selector-label">每页</span>
        <el-select
          :model-value="pageSize"
          size="small"
          class="size-select"
          @change="handleSizeChange"
        >
          <el-option
            v-for="size in pageSizes"
            :key="size"
            :label="`${size} 条`"
            :value="size"
          />
        </el-select>
      </div>

      <!-- Navigation Buttons -->
      <div class="nav-buttons">
        <button
          class="nav-btn"
          :disabled="currentPage <= 1"
          @click="handlePageChange(currentPage - 1)"
        >
          <el-icon><ArrowLeft /></el-icon>
        </button>

        <!-- Page Numbers -->
        <div class="page-numbers">
          <template v-for="page in displayedPages" :key="page">
            <button
              v-if="page !== '...'"
              class="page-number"
              :class="{ active: page === currentPage }"
              @click="handlePageChange(page as number)"
            >
              {{ page }}
            </button>
            <span v-else class="ellipsis">...</span>
          </template>
        </div>

        <button
          class="nav-btn"
          :disabled="currentPage >= totalPages"
          @click="handlePageChange(currentPage + 1)"
        >
          <el-icon><ArrowRight /></el-icon>
        </button>
      </div>

      <!-- Jump To Page -->
      <div class="jump-to-page">
        <span class="jump-label">跳至</span>
        <el-input-number
          v-model="jumpPage"
          :min="1"
          :max="totalPages"
          :controls="false"
          size="small"
          class="jump-input"
          @keyup.enter="handleJump"
        />
        <span class="jump-label">页</span>
        <el-button size="small" type="primary" plain @click="handleJump">
          跳转
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

interface Props {
  currentPage: number
  pageSize: number
  total: number
  pageSizes?: number[]
}

const props = withDefaults(defineProps<Props>(), {
  pageSizes: () => [10, 20, 50, 100],
})

const emit = defineEmits<{
  'update:currentPage': [page: number]
  'update:pageSize': [size: number]
  change: [page: number, pageSize: number]
}>()

const jumpPage = ref(1)

const totalPages = computed(() => Math.ceil(props.total / props.pageSize) || 1)

// Calculate displayed page numbers
const displayedPages = computed(() => {
  const total = totalPages.value
  const current = props.currentPage
  const pages: (number | string)[] = []

  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
  } else {
    if (current <= 3) {
      pages.push(1, 2, 3, 4, 5, '...', total)
    } else if (current >= total - 2) {
      pages.push(1, '...', total - 4, total - 3, total - 2, total - 1, total)
    } else {
      pages.push(1, '...', current - 1, current, current + 1, '...', total)
    }
  }

  return pages
})

watch(() => props.currentPage, (newPage) => {
  jumpPage.value = newPage
}, { immediate: true })

function handlePageChange(page: number) {
  if (page < 1 || page > totalPages.value || page === props.currentPage) {
    return
  }
  emit('update:currentPage', page)
  emit('change', page, props.pageSize)
}

function handleSizeChange(size: number) {
  emit('update:pageSize', size)
  emit('update:currentPage', 1)
  emit('change', 1, size)
}

function handleJump() {
  const page = Math.max(1, Math.min(jumpPage.value || 1, totalPages.value))
  handlePageChange(page)
}
</script>

<style scoped>
.pagination-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: white;
  border-top: 1px solid var(--color-border-light);
  gap: 16px;
  flex-wrap: wrap;
}

.batch-actions {
  flex-shrink: 0;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: flex-end;
  flex: 1;
}

/* Total Info */
.total-info {
  font-size: 13px;
  color: var(--color-gray-500);
}

.total-count,
.current-page {
  color: var(--color-primary);
  font-weight: 600;
}

.page-info {
  margin-left: 4px;
}

/* Page Size Selector */
.page-size-selector {
  display: flex;
  align-items: center;
  gap: 8px;
}

.selector-label {
  font-size: 13px;
  color: var(--color-gray-500);
}

.size-select {
  width: 90px;
}

/* Navigation Buttons */
.nav-buttons {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-border);
  background: white;
  border-radius: 6px;
  cursor: pointer;
  color: var(--color-gray-600);
  transition: all 0.2s ease;
}

.nav-btn:hover:not(:disabled) {
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.nav-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

/* Page Numbers */
.page-numbers {
  display: flex;
  gap: 4px;
}

.page-number {
  min-width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: var(--color-gray-600);
  transition: all 0.2s ease;
}

.page-number:hover {
  background: var(--color-gray-100);
}

.page-number.active {
  background: rgba(59, 130, 246, 0.1);
  border-color: var(--color-primary);
  color: var(--color-primary);
  font-weight: 600;
}

.ellipsis {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-gray-400);
  font-size: 13px;
}

/* Jump To Page */
.jump-to-page {
  display: flex;
  align-items: center;
  gap: 8px;
}

.jump-label {
  font-size: 13px;
  color: var(--color-gray-500);
}

.jump-input {
  width: 60px;
}

/* Responsive */
@media (max-width: 768px) {
  .pagination-wrapper {
    flex-direction: column;
    align-items: stretch;
  }

  .pagination-controls {
    justify-content: center;
  }

  .jump-to-page {
    display: none;
  }
}
</style>
