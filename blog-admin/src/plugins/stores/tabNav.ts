import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface TabItem {
  path: string
  title: string
  fixed?: boolean
}

export const useTabNavStore = defineStore('tabNav', () => {
  // State
  const tabs = ref<TabItem[]>([
    { path: '/dashboard', title: '首页', fixed: true },
  ])
  const activeTab = ref('/dashboard')

  // Getters
  const activeTabPath = computed(() => activeTab.value)
  const tabList = computed(() => tabs.value)

  // Actions
  function addTab(tab: TabItem) {
    // Check if tab already exists
    const existingTab = tabs.value.find(t => t.path === tab.path)
    if (existingTab) {
      // Just activate the existing tab
      activeTab.value = tab.path
      return
    }

    // Add new tab
    tabs.value.push({
      path: tab.path,
      title: tab.title,
      fixed: tab.fixed || false,
    })
    activeTab.value = tab.path
  }

  function closeTab(path: string) {
    // Cannot close fixed tabs
    const tab = tabs.value.find(t => t.path === path)
    if (tab?.fixed) {
      return
    }

    const index = tabs.value.findIndex(t => t.path === path)
    if (index === -1) return

    // Remove the tab
    tabs.value.splice(index, 1)

    // If closing active tab, activate another
    if (activeTab.value === path) {
      // Try to activate the tab to the right first
      if (index < tabs.value.length) {
        activeTab.value = tabs.value[index].path
      }
      // Otherwise activate the tab to the left
      else if (index > 0) {
        activeTab.value = tabs.value[index - 1].path
      }
      // Fallback to dashboard
      else {
        activeTab.value = '/dashboard'
      }
    }
  }

  function activateTab(path: string) {
    const tab = tabs.value.find(t => t.path === path)
    if (tab) {
      activeTab.value = path
    }
  }

  function isActive(path: string): boolean {
    return activeTab.value === path
  }

  function hasTab(path: string): boolean {
    return tabs.value.some(t => t.path === path)
  }

  function closeOtherTabs(keepPath: string) {
    // 保留固定标签和当前标签
    tabs.value = tabs.value.filter(t => t.fixed || t.path === keepPath)
    activeTab.value = keepPath
  }

  function closeRightTabs(currentPath: string) {
    const currentIndex = tabs.value.findIndex(t => t.path === currentPath)
    if (currentIndex === -1) return

    // 删除当前标签右侧的所有非固定标签
    tabs.value = tabs.value.filter((t, index) => t.fixed || index <= currentIndex)

    // 如果当前激活的标签被关闭了，激活当前标签
    if (!tabs.value.some(t => t.path === activeTab.value)) {
      activeTab.value = currentPath
    }
  }

  function closeAllTabs() {
    // 只保留固定标签
    tabs.value = tabs.value.filter(t => t.fixed)
    // 激活第一个固定标签
    activeTab.value = tabs.value[0]?.path || '/dashboard'
  }

  return {
    tabs,
    activeTab,
    activeTabPath,
    tabList,
    addTab,
    closeTab,
    activateTab,
    isActive,
    hasTab,
    closeOtherTabs,
    closeRightTabs,
    closeAllTabs,
  }
})
