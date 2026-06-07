<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Fold,
  Expand,
  FullScreen,
  ArrowDown,
  User,
  Setting,
  SwitchButton,
  Bell,
  Search,
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/plugins/stores/auth'

const props = defineProps<{
  collapse: boolean
}>()

const emit = defineEmits<{
  'update:collapse': [value: boolean]
  'trigger-search': []
  'show-notifications': []
}>()

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const isCollapse = computed({
  get: () => props.collapse,
  set: (value) => emit('update:collapse', value),
})

const user = computed(() => authStore.user)
const currentRoute = computed(() => route)

const pageTitle = computed(() => {
  const titles: Record<string, string> = {
    '/dashboard': '仪表盘',
    '/articles': '文章管理',
    '/articles/create': '新建文章',
    '/categories': '分类管理',
    '/tags': '标签管理',
    '/diaries': '日记管理',
    '/messages': '留言管理',
    '/links': '友链管理',
  }
  return titles[route.path] || 'Blog Admin'
})

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}

function toggleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

async function handleCommand(command: string) {
  switch (command) {
    case 'profile':
      ElMessage.info('个人资料功能开发中')
      break
    case 'settings':
      ElMessage.info('系统设置功能开发中')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          confirmButtonClass: 'el-button--danger',
        })
        authStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch {
        // User cancelled
      }
      break
  }
}
</script>

<template>
  <header class="navbar">
    <!-- Left Section -->
    <div class="navbar-left">
      <button
        class="collapse-btn"
        :class="{ collapsed: isCollapse }"
        :aria-label="isCollapse ? '展开侧边栏' : '收起侧边栏'"
        :aria-expanded="!isCollapse"
        @click="toggleCollapse"
        @keydown.enter="toggleCollapse"
        @keydown.space.prevent="toggleCollapse"
      >
        <el-icon :size="18" aria-hidden="true">
          <Fold v-if="!isCollapse" />
          <Expand v-else />
        </el-icon>
      </button>

      <div class="breadcrumb-section">
        <h1 class="page-title">{{ pageTitle }}</h1>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentRoute.path !== '/dashboard'">
            {{ pageTitle }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <!-- Right Section -->
    <div class="navbar-right">
      <!-- Search Trigger -->
      <button
        class="action-btn"
        aria-label="搜索"
        @keydown.enter="$emit('trigger-search')"
        @keydown.space.prevent="$emit('trigger-search')"
      >
        <el-icon :size="18" aria-hidden="true"><Search /></el-icon>
      </button>

      <!-- Notifications -->
      <button
        class="action-btn notification-btn"
        aria-label="通知 (3条未读)"
        aria-haspopup="true"
        @keydown.enter="$emit('show-notifications')"
        @keydown.space.prevent="$emit('show-notifications')"
      >
        <el-icon :size="18" aria-hidden="true"><Bell /></el-icon>
        <span class="notification-badge" aria-label="3条未读通知">3</span>
      </button>

      <!-- Fullscreen -->
      <button
        class="action-btn"
        aria-label="切换全屏"
        @click="toggleFullscreen"
        @keydown.enter="toggleFullscreen"
        @keydown.space.prevent="toggleFullscreen"
      >
        <el-icon :size="18" aria-hidden="true"><FullScreen /></el-icon>
      </button>

      <div class="divider" />

      <!-- User Dropdown -->
      <el-dropdown
        trigger="click"
        aria-label="用户菜单"
        @command="handleCommand"
      >
        <div
          class="user-trigger"
          role="button"
          tabindex="0"
          aria-haspopup="true"
          aria-expanded="false"
          @keydown.enter="$el.querySelector('.el-dropdown__trigger')?.click()"
          @keydown.space.prevent="$el.querySelector('.el-dropdown__trigger')?.click()"
        >
          <el-avatar
            :size="34"
            :src="user?.avatar"
            class="user-avatar"
            :alt="`${user?.nickname || 'Admin'} 的头像`"
          >
            {{ user?.nickname?.charAt(0).toUpperCase() || 'U' }}
          </el-avatar>
          <div class="user-info">
            <span class="user-name">{{ user?.nickname || 'Admin' }}</span>
            <span class="user-role">管理员</span>
          </div>
          <el-icon class="dropdown-icon" aria-hidden="true"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown">
            <div class="dropdown-header">
              <el-avatar :size="40" :src="user?.avatar">
                {{ user?.nickname?.charAt(0).toUpperCase() || 'U' }}
              </el-avatar>
              <div class="dropdown-user-info">
                <span class="dropdown-user-name">{{ user?.nickname || 'Admin' }}</span>
                <span class="dropdown-user-email">{{ user?.email || 'admin@example.com' }}</span>
              </div>
            </div>
            <el-dropdown-item command="profile" divided>
              <el-icon><User /></el-icon>
              <span>个人资料</span>
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </el-dropdown-item>
            <el-dropdown-item command="logout" class="logout-item">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<style scoped>
.navbar {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--color-border);
}

/* Left Section */
.navbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: transparent;
  color: var(--color-gray-500);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.collapse-btn:hover {
  background: var(--color-gray-100);
  color: var(--color-gray-700);
}

.collapse-btn.collapsed {
  transform: rotate(180deg);
}

.breadcrumb-section {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.page-title {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0;
}

/* Right Section */
.navbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-btn {
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: transparent;
  color: var(--color-gray-500);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.action-btn:hover {
  background: var(--color-gray-100);
  color: var(--color-gray-700);
}

.notification-btn {
  position: relative;
}

.notification-badge {
  position: absolute;
  top: 6px;
  right: 6px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  background: var(--color-danger);
  color: white;
  font-size: 10px;
  font-weight: 600;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid white;
}

.divider {
  width: 1px;
  height: 24px;
  background: var(--color-border);
  margin: 0 4px;
}

/* User Dropdown */
.user-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 10px 6px 6px;
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.2s ease;
}

.user-trigger:hover {
  background: var(--color-gray-100);
}

.user-avatar {
  border: 2px solid var(--color-border);
  transition: border-color 0.2s ease;
}

.user-trigger:hover .user-avatar {
  border-color: var(--color-gray-300);
}

.user-info {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-gray-900);
}

.user-role {
  font-size: 11px;
  color: var(--color-gray-400);
}

.dropdown-icon {
  color: var(--color-gray-400);
  font-size: 12px;
  transition: transform 0.2s ease;
}

.user-trigger:hover .dropdown-icon {
  transform: rotate(180deg);
}

/* Dropdown Menu */
:deep(.user-dropdown) {
  min-width: 220px;
  padding: 8px !important;
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  margin: -4px -4px 4px;
  background: linear-gradient(135deg, var(--color-gray-50) 0%, var(--color-gray-100) 100%);
  border-radius: 10px;
}

.dropdown-user-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dropdown-user-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-gray-900);
}

.dropdown-user-email {
  font-size: 12px;
  color: var(--color-gray-400);
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px !important;
  border-radius: 8px !important;
  font-size: 13px;
  color: var(--color-gray-700);
}

:deep(.el-dropdown-menu__item .el-icon) {
  font-size: 16px;
  color: var(--color-gray-400);
}

:deep(.el-dropdown-menu__item:hover) {
  background: var(--color-gray-100) !important;
  color: var(--color-gray-900) !important;
}

:deep(.el-dropdown-menu__item:hover .el-icon) {
  color: var(--color-primary);
}

:deep(.el-dropdown-menu__item.logout-item) {
  color: var(--color-danger);
}

:deep(.el-dropdown-menu__item.logout-item .el-icon) {
  color: var(--color-danger);
}

:deep(.el-dropdown-menu__item.logout-item:hover) {
  background: var(--color-danger) !important;
  background: rgba(239, 68, 68, 0.08) !important;
}
</style>
