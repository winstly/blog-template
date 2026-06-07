<script setup lang="ts">
import { ref, nextTick } from 'vue'
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
import TabNav from './TabNav.vue'

const isCollapse = ref(false)
const mainContentRef = ref<HTMLElement>()

// Skip to main content functionality
function skipToMainContent() {
  nextTick(() => {
    const mainContent = mainContentRef.value
    if (mainContent) {
      mainContent.focus()
      mainContent.scrollIntoView({ behavior: 'smooth' })
    }
  })
}

// Handle keyboard events for skip link
function handleSkipKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter' || event.key === ' ') {
    event.preventDefault()
    skipToMainContent()
  }
}
</script>

<template>
  <el-container class="app-layout">
    <!-- Skip Link for Accessibility -->
    <a
      href="#main-content"
      class="skip-link"
      @click.prevent="skipToMainContent"
      @keydown="handleSkipKeydown"
    >
      跳转到主内容
    </a>

    <!-- Sidebar -->
    <el-aside
      :width="isCollapse ? '64px' : '220px'"
      class="sidebar"
      role="complementary"
      aria-label="侧边栏"
    >
      <Sidebar :is-collapse="isCollapse" />
    </el-aside>

    <!-- Main Container -->
    <el-container class="main-wrapper">
      <!-- Navbar -->
      <el-header class="header" role="banner">
        <Navbar v-model:collapse="isCollapse" />
      </el-header>

      <!-- Tab Navigation -->
      <TabNav />

      <!-- Main Content -->
      <el-main
        id="main-content"
        ref="mainContentRef"
        class="main-content"
        role="main"
        aria-label="主内容区域"
        tabindex="-1"
      >
        <div class="content-wrapper">
          <router-view v-slot="{ Component }">
            <transition
              name="fade-slide"
              mode="out-in"
            >
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.app-layout {
  height: 100vh;
  background: var(--color-content-bg);
}

/* Skip Link for Keyboard Navigation */
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  background: var(--color-primary);
  color: var(--color-gray-50);
  padding: 8px 16px;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  z-index: 10000;
  border-radius: 0 0 8px 0;
  transition: top 0.2s ease;
}

.skip-link:focus {
  top: 0;
  outline: 2px solid var(--color-gray-50);
  outline-offset: -2px;
}

/* Focus visible styles for keyboard navigation */
:deep(*:focus-visible) {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

:deep(button:focus-visible),
:deep(a:focus-visible),
:deep([role="button"]:focus-visible) {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
  border-radius: 4px;
}

/* Sidebar */
.sidebar {
  background: linear-gradient(180deg, var(--color-sidebar-bg) 0%, var(--color-sidebar-surface) 100%);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.08);
  z-index: 100;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Main Wrapper */
.main-wrapper {
  display: flex;
  flex-direction: column;
  background: var(--color-content-bg);
  min-width: 0;
}

/* Header */
.header {
  height: 64px;
  padding: 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  flex-shrink: 0;
}

/* Main Content */
.main-content {
  flex: 1;
  padding: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  overflow-x: hidden;
}

/* Page Transition */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(8px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* Custom Scrollbar */
.content-wrapper::-webkit-scrollbar {
  width: 5px;
}

.content-wrapper::-webkit-scrollbar-track {
  background: transparent;
}

.content-wrapper::-webkit-scrollbar-thumb {
  background: var(--color-gray-300);
  border-radius: 3px;
}

.content-wrapper::-webkit-scrollbar-thumb:hover {
  background: var(--color-gray-400);
}
</style>
