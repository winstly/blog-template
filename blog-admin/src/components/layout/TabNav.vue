<script setup lang="ts">
import { ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Close } from '@element-plus/icons-vue'
import { useTabNavStore } from '@/plugins/stores/tabNav'
import type { TabItem } from '@/plugins/stores/tabNav'

const router = useRouter()
const tabStore = useTabNavStore()

// 右键菜单状态
const contextMenu = ref({
  visible: false,
  x: 0,
  y: 0,
  tab: null as TabItem | null,
})

function handleClick(tab: TabItem) {
  tabStore.activateTab(tab.path)
  router.push(tab.path)
}

function handleClose(tab: TabItem) {
  if (tab.fixed) return

  const wasActive = tabStore.isActive(tab.path)
  tabStore.closeTab(tab.path)

  if (wasActive) {
    router.push(tabStore.activeTabPath)
  }
}

function handleContextMenu(event: MouseEvent, tab: TabItem) {
  event.preventDefault()
  contextMenu.value = {
    visible: true,
    x: event.clientX,
    y: event.clientY,
    tab,
  }
}

function closeContextMenu() {
  contextMenu.value.visible = false
}

function handleMenuAction(action: 'closeOther' | 'closeRight' | 'closeAll') {
  const tab = contextMenu.value.tab
  if (!tab) return

  switch (action) {
    case 'closeOther':
      tabStore.closeOtherTabs(tab.path)
      break
    case 'closeRight':
      tabStore.closeRightTabs(tab.path)
      break
    case 'closeAll':
      tabStore.closeAllTabs()
      router.push('/dashboard')
      break
  }
  closeContextMenu()
}

// 点击其他地方关闭菜单
function handleGlobalClick() {
  if (contextMenu.value.visible) {
    closeContextMenu()
  }
}

// 组件卸载时清理
onUnmounted(() => {
  closeContextMenu()
})
</script>

<template>
  <div class="tab-nav" @click="handleGlobalClick">
    <div class="tab-scroll-container">
      <div
        v-for="tab in tabStore.tabs"
        :key="tab.path"
        class="tab-item"
        :class="{
          'is-active': tabStore.isActive(tab.path),
          'is-fixed': tab.fixed,
        }"
        @click="handleClick(tab)"
        @contextmenu="handleContextMenu($event, tab)"
      >
        <span class="tab-title">{{ tab.title }}</span>
        <span
          v-if="!tab.fixed"
          class="tab-close"
          :class="{ 'is-visible': tabStore.isActive(tab.path) }"
          @click.stop="handleClose(tab)"
        >
          <Close class="close-icon" />
        </span>
        <div class="tab-indicator" />
      </div>
    </div>

    <!-- 右键菜单 -->
    <div
      v-if="contextMenu.visible"
      class="context-menu"
      :style="{ left: `${contextMenu.x}px`, top: `${contextMenu.y}px` }"
      @click.stop
    >
      <div class="menu-item" @click="handleMenuAction('closeOther')">
        <span class="menu-icon">📌</span>
        <span>关闭其他</span>
      </div>
      <div class="menu-item" @click="handleMenuAction('closeRight')">
        <span class="menu-icon">➡️</span>
        <span>关闭右侧</span>
      </div>
      <div class="menu-divider" />
      <div class="menu-item menu-item-danger" @click="handleMenuAction('closeAll')">
        <span class="menu-icon">🗑️</span>
        <span>关闭全部</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.tab-nav {
  height: 42px;
  background: var(--color-card-bg);
  border-bottom: 1px solid var(--color-border);
  padding: 0 16px;
  display: flex;
  align-items: flex-end;
}

.tab-scroll-container {
  display: flex;
  gap: 4px;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  padding-bottom: 0;
}

.tab-scroll-container::-webkit-scrollbar {
  display: none;
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  background: transparent;
  border-radius: 8px 8px 0 0;
  cursor: pointer;
  position: relative;
  transition: all 0.2s ease;
  white-space: nowrap;
  user-select: none;
  font-size: 13px;
  color: var(--color-gray-500);
  margin-bottom: 0;
}

.tab-item::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: transparent;
  transition: background 0.2s ease;
}

.tab-item:hover {
  color: var(--color-gray-700);
  background: var(--color-gray-100);
}

.tab-item.is-active {
  color: var(--color-primary);
  background: rgba(59, 130, 246, 0.06);
  font-weight: 500;
}

.tab-item.is-active::before {
  background: var(--color-primary);
}

.tab-item.is-fixed .tab-title::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  background: var(--color-primary);
  border-radius: 50%;
  margin-right: 6px;
  vertical-align: middle;
}

.tab-title {
  line-height: 1;
}

.tab-close {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  opacity: 0;
  transition: all 0.2s ease;
  margin-left: 2px;
}

.tab-item:hover .tab-close,
.tab-close.is-visible {
  opacity: 1;
}

.tab-close:hover {
  background: rgba(0, 0, 0, 0.08);
}

.close-icon {
  width: 12px;
  height: 12px;
  color: currentColor;
}

.tab-close:hover .close-icon {
  color: var(--color-danger);
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%) scaleX(0);
  width: 60%;
  height: 2px;
  background: var(--color-primary);
  border-radius: 2px 2px 0 0;
  transition: transform 0.2s ease;
}

.tab-item.is-active .tab-indicator {
  transform: translateX(-50%) scaleX(1);
}

/* 右键菜单 */
.context-menu {
  position: fixed;
  min-width: 140px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  border: 1px solid var(--color-border);
  padding: 6px;
  z-index: 1000;
  animation: menuFadeIn 0.15s ease-out;
}

@keyframes menuFadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: var(--color-gray-700);
  transition: all 0.2s ease;
}

.menu-item:hover {
  background: var(--color-gray-100);
}

.menu-item-danger {
  color: var(--color-danger);
}

.menu-item-danger:hover {
  background: rgba(239, 68, 68, 0.08);
}

.menu-icon {
  font-size: 14px;
  width: 16px;
  text-align: center;
}

.menu-divider {
  height: 1px;
  background: var(--color-border);
  margin: 6px 0;
}
</style>
