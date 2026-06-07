<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  Odometer,
  Document,
  Folder,
  CollectionTag,
  Calendar,
  ChatDotRound,
  Link,
  Notebook,
  Collection,
  User,
  UserFilled,
  Lock,
} from '@element-plus/icons-vue'
import { useTabNavStore } from '@/plugins/stores/tabNav'

const props = defineProps<{
  isCollapse: boolean
}>()

const route = useRoute()
const tabNavStore = useTabNavStore()

const activeRoute = computed(() => route.path)

// 菜单分组
const menuGroups = [
  {
    title: '内容管理',
    items: [
      { path: '/articles', title: '文章管理', icon: Document },
      { path: '/categories', title: '分类管理', icon: Folder },
      { path: '/tags', title: '标签管理', icon: CollectionTag },
      { path: '/diaries', title: '日记管理', icon: Calendar },
      { path: '/messages', title: '留言管理', icon: ChatDotRound },
      { path: '/links', title: '友链管理', icon: Link },
    ],
  },
  {
    title: '系统管理',
    items: [
      { path: '/dicts', title: '字典管理', icon: Collection },
      { path: '/users', title: '用户管理', icon: User },
      { path: '/roles', title: '角色管理', icon: UserFilled },
      { path: '/permissions', title: '权限管理', icon: Lock },
    ],
  },
]

// 扁平化菜单用于查找
const allMenuItems = menuGroups.flatMap(g => g.items)

function handleSelect(index: string) {
  const item = allMenuItems.find(item => item.path === index)
  if (item) {
    tabNavStore.addTab({
      path: index,
      title: item.title,
      fixed: false,
    })
  }
}
</script>

<template>
  <aside class="sidebar-container" role="complementary" aria-label="侧边导航栏">
    <!-- Logo Section -->
    <div class="logo-section">
      <router-link to="/" class="logo-link" aria-label="返回首页">
        <div class="logo-icon" aria-hidden="true">
          <Notebook class="icon-svg" />
        </div>
        <span v-if="!isCollapse" class="logo-text">Blog Admin</span>
      </router-link>
    </div>

    <!-- Dashboard -->
    <div class="dashboard-section">
      <el-menu
        :default-active="activeRoute"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="transparent"
        text-color="#a8a29e"
        active-text-color="#3b82f6"
        class="main-menu"
        @select="handleSelect"
      >
        <el-menu-item
          index="/dashboard"
          class="menu-item dashboard-item"
          @click="tabNavStore.addTab({ path: '/dashboard', title: '仪表盘', fixed: true })"
        >
          <el-icon class="menu-icon">
            <Odometer />
          </el-icon>
          <template #title>
            <span class="menu-title">仪表盘</span>
          </template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- Navigation Menu Groups -->
    <nav class="nav-menu" role="navigation" aria-label="主导航菜单">
      <el-menu
        :default-active="activeRoute"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="transparent"
        text-color="#a8a29e"
        active-text-color="#3b82f6"
        class="main-menu"
        @select="handleSelect"
      >
        <template v-for="group in menuGroups" :key="group.title">
          <!-- Group Title -->
          <h3 v-if="!isCollapse" class="menu-group-title" :id="`group-${group.title}`">
            {{ group.title }}
          </h3>
          <el-divider v-if="!isCollapse" class="group-divider" role="separator" />

          <!-- Group Items -->
          <el-menu-item
            v-for="item in group.items"
            :key="item.path"
            :index="item.path"
            class="menu-item"
          >
            <el-icon class="menu-icon">
              <component :is="item.icon" />
            </el-icon>
            <template #title>
              <span class="menu-title">{{ item.title }}</span>
            </template>
          </el-menu-item>
        </template>
      </el-menu>
    </nav>

    <!-- Footer Section -->
    <footer class="sidebar-footer">
      <div v-if="!isCollapse" class="footer-content">
        <div class="footer-line" aria-hidden="true" />
        <p class="footer-text">Blog Admin v1.0</p>
      </div>
    </footer>
  </aside>
</template>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, var(--color-sidebar-bg) 0%, var(--color-sidebar-surface) 100%);
}

/* Logo Section */
.logo-section {
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.logo-link {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: var(--color-sidebar-text-hover);
  transition: opacity 0.2s ease;
}

.logo-link:hover {
  opacity: 0.9;
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.15) 0%, rgba(59, 130, 246, 0.05) 100%);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-svg {
  width: 20px;
  height: 20px;
  color: var(--color-sidebar-active);
}

.logo-text {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 600;
  letter-spacing: 0.02em;
  color: var(--color-sidebar-text-hover);
  white-space: nowrap;
}

/* Dashboard Section */
.dashboard-section {
  padding: 12px 10px 0;
}

.dashboard-item {
  background: rgba(59, 130, 246, 0.08) !important;
  border: 1px solid rgba(59, 130, 246, 0.15) !important;
}

.dashboard-item:hover {
  background: rgba(59, 130, 246, 0.12) !important;
}

/* Navigation Menu */
.nav-menu {
  flex: 1;
  padding: 16px 10px 12px;
  overflow-y: auto;
  overflow-x: hidden;
}

.main-menu {
  border: none !important;
}

/* Menu Group Title */
.menu-group-title {
  padding: 8px 14px 4px;
  font-size: 11px;
  font-weight: 600;
  color: var(--color-gray-500);
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.group-divider {
  margin: 4px 14px 8px;
  border-color: rgba(255, 255, 255, 0.06);
}

/* Menu Item */
:deep(.el-menu-item) {
  height: 44px;
  margin: 3px 0;
  padding: 0 14px !important;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

:deep(.el-menu-item::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 0;
  background: var(--color-sidebar-active);
  border-radius: 0 2px 2px 0;
  transition: height 0.25s ease;
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.04) !important;
  color: var(--color-sidebar-text-hover) !important;
}

:deep(.el-menu-item.is-active) {
  background: rgba(59, 130, 246, 0.1) !important;
  color: var(--color-sidebar-active) !important;
}

:deep(.el-menu-item.is-active::before) {
  height: 20px;
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 0 !important;
  transition: transform 0.2s ease;
}

:deep(.el-menu-item:hover .el-icon) {
  transform: scale(1.1);
}

:deep(.el-menu-item.is-active .el-icon) {
  color: var(--color-sidebar-active);
}

.menu-title {
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.01em;
}

/* Collapsed State */
:deep(.el-menu--collapse) {
  width: 64px;
}

:deep(.el-menu--collapse .el-menu-item) {
  padding: 0 !important;
  justify-content: center;
}

:deep(.el-menu--collapse .el-tooltip__trigger) {
  display: flex !important;
  align-items: center;
  justify-content: center;
  padding: 0 !important;
}

/* Footer Section */
.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.footer-content {
  text-align: center;
}

.footer-line {
  width: 30px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(168, 162, 158, 0.3), transparent);
  margin: 0 auto 10px;
}

.footer-text {
  font-size: 11px;
  color: var(--color-gray-500);
  letter-spacing: 0.05em;
  margin: 0;
}

/* Custom Scrollbar */
.nav-menu::-webkit-scrollbar {
  width: 3px;
}

.nav-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
}

.nav-menu::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.15);
}
</style>
