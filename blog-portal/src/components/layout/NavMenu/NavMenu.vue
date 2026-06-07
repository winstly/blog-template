<script setup lang="ts">
import { useAppStore } from '@/plugins/stores/app'
import NavMenuButton from './NavMenuButton.vue'
import NavMenuPanel from './NavMenuPanel.vue'

const appStore = useAppStore()
</script>

<template>
  <NavMenuButton
    :is-open="appStore.isMenuOpen"
    @toggle="appStore.toggleMenu"
  />
  <NavMenuPanel
    :is-open="appStore.isMenuOpen"
    @close="appStore.closeMenu"
  />
</template>
